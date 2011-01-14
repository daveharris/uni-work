.include "m16def.inc"

;TODO: divide

;Declare register variables here

.def mullow =r0
.def mulhigh =r1

.def temp1	=r16				;temp registers
.def temp2 	=r17
.def temp3  =r18
.def temp4  =r19
.def temp5	=r20
.def temp6 	=r21
.def temp7	=r23
.def motorstate =r22			;holds the current state of filter and sample motors

.def BB =r2
.def BS =r3
.def RS =r4
.def RB =r5
.def GB =r6
.def GS =r7
.def redresult =r8
.def blueresult =r9
.def greenresult =r10


.equ PIF = PINC0
.equ PIS = PINC1
.equ PI_PORT = PINC


;SETUP

.org 	0x0000					; Place instructions at start of memory

	jmp		START					; if reset occurs jump to START	
					
START:

	ldi temp1, high(RAMEND)		; setup stackpointer sp
	out SPH, temp1
	ldi temp1, low(RAMEND)
	out SPL, temp1

	call setupUSART				; setup link to hyperterminal PD0 = R1out, PD1 = T1in
	call setupMotors
	call setupADC				; setup the ADC

	ldi temp1, 0x00
	out DDRC, temp1				; sets port C as input (PC0, PC1 = Photointerrupters, rest earthed)
	out DDRA, temp1				; sets port A as input (PA0 = input photosensor)
	ldi temp1, 0xFF	
	out DDRB, temp1				; sets port B as output (PB0 - PB3 = outputs to filter stepper, PB4 - PB7 = outputs to sample stepper)
	
	jmp LOOP

; ***************************MAIN PROGRAM***********************************

LOOP:							; wait until start signal (1) is received from hyperterminal


	call getChar				
	cpi temp1, 0x0D				; if char returned = cr, then jump to main, else loop
	brne loop
	jmp main
	
MAIN:							; main code
	
	call welcome				; welcome message
	call searchFilter			; searches for filters index
	call searchSample			; searches for sample index
	call longDelay				; delay to allow photo sensor to settle
	call mBB					; measure blue blank
	call rotateSample
	call longDelay
	call mBS					; measure blue sample
	call rotateFilter
	call longDelay
	call mRS					; measure red sample
	call rotateSample
	call longDelay
	call mRB					; measure red blank
	call rotateFilter
	call longDelay
	call mGB					; measure green blank
	call rotateSample
	call longDelay
	call mGS					; measure green sample
	call calculate
	call transmit

	jmp start
	

; ****************************SUBROUTINES************************************

searchFilter:					; searches for filters index
	
	LOOP2:
		call moveFilter
		call smallDelay
		sbic PI_PORT, PIF
		rjmp LOOP2
		ret

searchSample:					; searches for samples index
	
	LOOP3:
		call moveSample
		call smallDelay
		sbic PI_PORT, PIS
		rjmp LOOP3
		ret

moveFilter:
		ldi temp2, 0x00			; 0x00 = filter, motor = filter
		call moveMotor			; step filter one step
		ret

moveSample:
		ldi temp2, 0x01			; 0x01 = sample, motor = sample
		call moveMotor			; step sample one step
		ret

setupUSART:						; sets up USART

	ldi temp2, 0x00
	ldi temp1, 0x01
	out UBRRH, temp2
	out UBRRL, temp1

	ldi temp1, (1<<RXEN)|(1<<TXEN)
	out UCSRB, temp1

	ldi temp1, (1<<URSEL)|(1<<USBS)|(3<<UCSZ0)
	out UCSRC, temp1

	ret

welcome:
					;prints out welcome message on hyperterminal
	ldi temp1, 'P'
	call sendChar
	ldi temp1, 'R'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'S'
	call sendChar
	ldi temp1, 'S'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'N'
	call sendChar
	ldi temp1, 'T'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'R'
	call sendChar
	ldi temp1, 'T'
	call sendChar
	ldi temp1, 'O'
	call sendChar
	ldi temp1, 'B'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'G'
	call sendChar
	ldi temp1, 'I'
	call sendChar
	ldi temp1, 'N'
	call sendChar
	ldi temp1, 10
	call sendChar
	ret

mBB:
	call readADC
	mov BB, temp1
	ret

mBS:
	call readADC
	mov BS, temp1
	ret

mRS:
	call readADC
	mov RS, temp1
	ret

mRB:
	call readADC
	mov RB, temp1
	ret

mGB:
	call readADC
	mov GB, temp1
	ret

mGS:
	call readADC
	mov GS, temp1
	ret

rotateFilter:
	ldi temp2, 0x00
	ldi temp1, 0x42
	loopF:
		call moveMotor
		dec temp1
		cpi temp1, 0x00
		breq endF
		jmp loopF

	endF:
		ret

rotateSample:
	ldi temp2, 0x01
	ldi temp1, 0x64
	loopS:
		call moveMotor
		dec temp1
		cpi temp1, 0x00
		breq endS
		jmp loopS

	endS:
		ret

calculate:

	mov temp1, BS				;calculate blue
	mov temp2, BB
	call divide
	call multiply
	mov blueresult, temp4


	mov temp1, RS				;calculate red
	mov temp2, RB
	call divide
	call multiply
	mov redresult, temp4


	mov temp1, GS							;calculate green
	mov temp2, GB
	call divide
	call multiply
	mov greenresult, temp4


	ret

transmit:

	ldi temp1, 'B'			;blue result
	call sendChar
	ldi temp1, 'L'
	call sendChar
	ldi temp1, 'U'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, ':'
	call sendChar
	mov temp1, blueresult
	call SendByte
	ldi temp1, 10
	call sendChar

	ldi temp1, 'R'			;red result
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'D'
	call sendChar
	ldi temp1, ':'
	call sendChar
	mov temp1, redresult
	call SendByte
	ldi temp1, 10
	call sendChar

	ldi temp1, 'G'			;green result
	call sendChar
	ldi temp1, 'R'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'E'
	call sendChar
	ldi temp1, 'N'
	call sendChar
	ldi temp1, ':'
	call sendChar
	mov temp1, greenresult
	call SendByte
	ldi temp1, 10
	call sendChar

	ret
	

; ****************************HELPERS****************************************
sendChar:						;sends char to hyperterminal

	sbis UCSRA, UDRE
	rjmp sendchar
	out UDR, temp1
	ret

getChar:						;gets char from hyperterminal

	sbis UCSRA, RXC
	rjmp getChar

	in temp1, UDR
	ret
	
moveMotor:						; steps motor one step, motor stepped depends on temp1

	cpi temp2, 0x00
	breq filter
	cpi temp2, 0x01
	breq sample

	filter:

		mov temp2, motorstate		;temp2 = motorstate
		lsl temp2					;clear sample bits so only have state of filter to analyse
		lsl temp2
		lsl temp2
		lsl temp2
		lsr temp2
		lsr temp2
		lsr temp2
		lsr temp2
		call getNext
		lsr motorstate				;prepare current state for next state addition
		lsr motorstate
		lsr motorstate
		lsr motorstate
		lsl motorstate					
		lsl motorstate
		lsl motorstate
		lsl motorstate
		add motorstate, temp3		;this is the new state for both motors
		out PORTB, motorstate		;outputs new state to stepper inputs
		ret

	sample:

		mov temp2, motorstate		;temp2 = motorstate
		lsr temp2					;clear sample bits so only have state of filter to analyse
		lsr temp2
		lsr temp2
		lsr temp2
		lsl temp2
		lsl temp2
		lsl temp2
		lsl temp2
		swap temp2
		call getNext
		swap temp3
		lsl motorstate				;prepare current state for next state addition
		lsl motorstate
		lsl motorstate
		lsl motorstate
		lsr motorstate					
		lsr motorstate
		lsr motorstate
		lsr motorstate
		add motorstate, temp3		;this is the new state for both motors
		out PORTB, motorstate		;outputs new state to stepper inputs
		ret


getNext:							;gets current state of motor and returns what the next state should be

	cpi temp2, 0x0A
	breq state2
	cpi temp2, 0x09
	breq state3
	cpi temp2, 0x05
	breq state4
	cpi temp2, 0x06
	breq state1

	state2:
		ldi temp3, 0x09
		jmp exit2
	state3:
		ldi temp3, 0x05
		jmp exit2
	state4:
		ldi temp3, 0x06
		jmp exit2
	state1:
		ldi temp3, 0x0A
		jmp exit2
	exit2:
		ret

setupMotors:						;state to start the motors in
	ldi motorstate, 0xAA
	ret

smallDelay:							;small delay to allow photoint time to change
	ldi temp5, 0x40
	ldi	temp6, 0x20
	call dodelay
		dodelay:
			mov temp7, temp5
		loopA:
			mov temp6, temp7
		loopB:
			dec temp6
			brne loopB
			dec temp5
			brne loopA
			ret

longDelay:							;long delay to allow photo sensor to settle
	ldi temp5, 0xFF
	ldi	temp6, 0xFF
	call dodelay1
		dodelay1:
			mov temp7, temp5
		loopC:
			mov temp6, temp7
		loopD:
			dec temp6
			brne loopD
			dec temp5
			brne loopC
			ret

setupADC:
		ldi temp1, (1<<ADEN)+(9<<ADPS0)
		out ADCSRA, temp1
		ret

readADC:									;reads adc, result in temp1
		andi temp1, 0b00000111
		ori temp1, (1<<REFS0)+(1<<ADLAR)
		out ADMUX, temp1
		sbi ADCSRA, ADSC
		readadcloop:
			sbic ADCSRA, ADSC
			rjmp readadcloop
			in temp1, ADCH
		ret

multiply:
	ldi temp3, 0x64			;multiplies temp1 with 100, high result in r1 low in r0
	mul temp3, temp4
	movw temp4:temp3,r1:r0
	ret

divide:						;divides temp1 and temp2, result in temp1

;.def	drem8u	=r15		;remainder		temp3
;.def	dres8u	=r16		;result			temp4
;.def	dd8u	=r16		;dividend		temp1
;.def	dv8u	=r17		;divisor		temp2
;.def	dcnt8u	=r18		;loop counter	temp5
	
		sub	temp3,temp3	;clear remainder and carry
		ldi	temp5,9	;init loop counter

	d8u_1:	
		rol	temp1		;shift left dividend
		dec	temp5		;decrement counter
		brne	d8u_2		;if done
		ret			;    return

	d8u_2:
		rol	temp3		;shift dividend into remainder
		sub	temp3,temp2	;remainder = remainder - divisor
		brcc	d8u_3		;if result negative
		add	temp3,temp2	;    restore remainder
		clc			;    clear carry to be shifted into result
		rjmp	d8u_1		;else
	d8u_3:	
		sec			;    set carry to be shifted into result
		rjmp	d8u_1


Sendbyte:					;converts temp1 to ASCII Hex and sends it to hyperterminal
	mov temp3,temp1
	ldi temp1, '0'
	call SendChar
	ldi temp1, 'x'
	call SendChar
	mov temp1, temp3
	swap temp1
	call Nibble2ASCII
	call SendChar
	mov temp1, temp3
	call Nibble2ASCII
	call SendChar
	ldi temp1, 10
	call SendChar
	ldi temp1, 13
	call SendChar
	ret

Nibble2ASCII:
	andi temp1, 0x0F
	cpi temp1, 0x0A
	brsh IsHex
	ldi temp2, 0x30
	add temp1, temp2
	ret

IsHeX:
	ldi temp2,0x37
	add temp1, temp2
	ret
	


