.include "m16def.inc"	; Load up constant definitions for mega16
						;	and register definitions




.def m1state1 =r16	;	registers store required motor states for full-step
.def m1state2 =r17	;
.def m1state3 =r18	;	16-31 registers = temporary rooom
.def m1state4 =r19	;
.def m2state1 =r20	;	registers store required motor states for full-step
.def m2state2 =r21	;
.def m2state3 =r22	;	16-31 registers = temporary rooom
.def m2state4 =r23
.def input = r24	;	Stores digital intensity input
.def motor1 = r25	;	Stores motor 1 output
.def motor2 = r26	;	Stores motor 2 output
.def temp1 = r27	;	
.def temp2 = r28	;	temporary registers


.org 0x0000				;	place instructions at memory start

		rjmp	RESET		; if reset occurs then jump to beginning

RESET:

ldi temp1, high(RAMEND)	;
out SPH, temp1			; Sets up the stack so call/return can be used
ldi temp1, low(RAMEND)	;
out SPL, temp1			;
	
ldi temp1, 0x00	;	sets temp1 to 0
out DDRA, temp1	;	turns all ADC pins to inputs
Call SetupADC	; sets the ADC module for inputs

out DDRD, temp1	;	turns all d pins to inputs
ldi temp1, 0XFF	;	sets temp1 to all 1's
out DDRB, temp1	;	sets all B pins to output	(will drive motors)
out DDRC, temp1	;	sets all C pins to output (will be LED output)

ldi m1state1, 0b1010		;
ldi m1state2, 0b1001		;
ldi m1state3, 0b0101		;
ldi m1state4, 0b0110		;	Preset saes for moor to be in
ldi m2state1, 0b10100000	;	m2 is for motor 2 (upper 4 bits)
ldi m2state2, 0b10010000	;	m1 is for motor 1 (lower 4 bits)
ldi m2state3, 0b01010000	;
ldi m2state4, 0b01100000	;
mov motor1, m1state1	; initialises motor output (so no hangup states)
mov motor2, m2state1	;
ldi temp1, 0b10101010	;	
out PORTB, temp1	; sets both motors to state 1


clr temp2	; clears registers that aren't yet defined




LISTENER:		; This 'looks' for the start signal from pd0

sbis PORTD, 0	;if pin d0 = 1 start program, else wait till d0=1
rjmp LISTENER


MAIN:
Call GetReference

loopforever:
rjmp loopforever	; loops here forever, acts as a 'stop' KYLE find better stop







Call ReadADC	;reads pA0 and converts its value into temp1
ret



;***********************************************************************************
;* Reads in pins d5, d6 and sees if they are 1 if so stop, else move the		   *
;*	note that pins are always hi, go low when index is found					   *
;* 			respective motor required and re-check them d5=motor1, d6 - motor2	   *											   *
;***********************************************************************************


; Currently does 1 'spam' step, ie if alreddy aligned will go right around 199 steps
GetReference:

RefOne:

sbis PORTD,5	; if motor1 not rdy then don't jump out move motor1
rjmp RefTwo
Call STEP_MOTOR_1	;steps motor one
rjmp RefOne			; then reruns check
RefTwo:

sbis PORTD,6	; of motor2 not rdy then don't jump out. reference pt found
ret
Call STEP_MOTOR_2	;steps motor two
rjmp RefOne			; then reruns check



;***********************************************************************************
;* Will make either motor1 or motor2 take 1 step, pins b0..b3 and b4..b7		   *
;* will also store current motor state in motor1/2							       *											   *
;***********************************************************************************


STEP_MOTOR_1: ; steps 1st motor, b0..3

mov temp1, motor2	;	this will be the new output once we find motor1 new value

cp m1state1, motor1
breq stateOne
cp m1state2, motor1
breq stateTwo
cp m1state3, motor1
breq stateThree
cp m1state4, motor1
breq stateFour
rjmp ERROR

stateOne:
mov motor1, m1state2
or temp1, m1state2
out PORTB, temp1	; same upper 4 bits, new lower 4 bits
ret

stateTwo:
mov motor1,m1state3
or temp1, m1state3
out PORTB, temp1	; same upper 4 bits, new lower 4 bits
ret

stateThree:
mov motor1, m1state4
or temp1, m1state4
out PORTB, temp1	; same upper 4 bits, new lower 4 bits
ret

stateFour:
mov motor1, m1state1
or temp1, m1state1
out PORTB, temp1	; same upper 4 bits, new lower 4 bits
ret



STEP_MOTOR_2:	; steps 2nd motor, b4..7
;
mov temp1, motor1	;	this will be the new output once we find motor2 new value

cp m2state1, motor2
breq XstateOne
cp m2state2, motor2
breq XstateTwo
cp m2state3, motor2
breq XstateThree
cp m2state4, motor2
breq XstateFour
; If that doesn't work then something has gone wrong. - potential error msg?!
XstateOne:
mov motor2, m2state2
or temp1, m2state2	;could use motor2, but woiuld then need a nop in compiling - works faster this way
out PORTB, temp1	; new upper 4 bits, same lower 4 bits
ret

XstateTwo:
mov motor2,m2state3
or temp1, m2state3
out PORTB, temp1	; new upper 4 bits, same lower 4 bits
ret

XstateThree:
mov motor2, m2state4
or temp1, m2state4
out PORTB, temp1	; new upper 4 bits, same lower 4 bits
ret

XstateFour:
mov motor2, m2state1
or temp2, m2state1
out PORTB, temp1	; new upper 4 bits, same lower 4 bits
ret





;**************************************************************
;* If the program catches an error it will call this	 	  *
;**************************************************************

ERROR:
rjmp loopforever	



;**************************************************************
;* Setup the ADC module for 0 - 5V input, single-ended inputs *
;**************************************************************

SetupADC:
	
	;* Enable the ADC and set the clock signal used for the successive 
	;* approximation ADC logic to the main clock divided by 16
	ldi temp1, (1<<ADEN)+(9<<ADPS0)
	out ADCSR, temp1
	
	ret


;***********************************************************************************
;* Perform an ADC measurement on ADC channel in r16 (channel # = PORTA bit number) *
;* An 8-bit value is returned in r16.											   *
;***********************************************************************************

ReadADC:

	;* First make sure r16 is a valid channel by truncating the high 5 bits
	andi temp1, 0b00000111		; AND temp1 with binary 00000111
	ori temp1, (1<<REFS0)+(1<<ADLAR)		;set the ref voltage source and result justification
	out ADMUX, temp1			; set the channel number and reference voltage source

	;* Now start the actual conversion process
	sbi	ADCSR, ADSC			; set the 'start conversion' bit (leave the rest as they are!)
	
	;* Wait until the conversion is finished 
readadcloop:
	sbic ADCSR, ADSC			; skip the next instruction if bit ADSC in ADCSRA regsiter is clear (the adc conversion is finished)
	rjmp readadcloop

	;* Conversion is finished now so copy the high byte of the result to r16
	in	temp1, ADCH				; even though the result is actually 10-bit we are only using the high 8 bits

	ret