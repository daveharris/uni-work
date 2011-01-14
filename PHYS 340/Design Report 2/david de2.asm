;This file contains defs for all of the mega16's
;registers.  Also includes values for various 
;constants like the size of the internal SRAM.
.include "m16def.inc"


;General registers
.def temp1 = r16
.def temp2 = r17
.def temp3 = r18
.def temp4 = r19
.def temp5 = r20

.def steps = r21

;The current state of the both motors,
;(4 bits each)
.def state = r22
.def tempState = r23

.def loop1 = r24
.def loop2 = r25
.def counter = r27

.def BB =r2
.def BS =r3
.def RS =r4
.def RB =r5
.def GB =r6
.def GS =r7
.def redresult =r8
.def blueresult =r9
.def greenresult =r10

;place these instructions at the 
;start of program memory
.org 0x0000

; If a reset occurs, then jump to the start of the code
	jmp RESET

RESET:
	;* First we need to setup the 'stack pointer' otherwise we will have problems 
	;* when we call a subroutine.  This is usually set to the end of the internal SRAM.
	
	ldi	temp1, high(RAMEND)	; set temp1 to equal the high byte of the address RAMEND
	out SPH, temp1			; set the high byte of the hardware stack pointer
	ldi temp1, low(RAMEND)	; set temp1 to equal the low byte of the address RAMEND
	out SPL, temp1			; set the low byte ofthe hardware stack pointer

	;load state with 0xA9
	ldi state, 0b10101001
	ldi temp2, 0
	ldi temp3, 0
	ldi counter, 0

	;Make port B and output
	ldi temp2, 0xff
	out DDRB, temp2

	;Make ports A and C inputs
	ldi temp2, 0x00
	out DDRC, temp2
	out DDRA, temp2

main:	
	call setupUSART		;Do the initial set for USART

	call SetupADC	;Do the initial setup for the ADC
	
	call StartMessage		;Tell the user what to do


loop:
	call GetStart
	rjmp loop



;********** SUBROUTINES **************************************

;*************************************************************
;* Sit in polling mode until an 's' is sent 				 *
;*************************************************************
getStart:
	sbis UCSRA, RXC
	rjmp getchar

	in temp1, UDR
	cpi temp1, 73	;if(temp1 == 's')
	breq moveWheels
	
	ret

moveWheels:
	;The steps field is not used in this sequence because
	;we want to stop at the photo-interuptuers, so set
	;it to something large
	ldi steps, 200
	;if both are in the start, then do measurements
	cpi temp2, 0b11
	rjmp doMeasurements
	
	;filter is in the start position, so move sample
	cpi temp2, 0b01
	call moveSampleWheel
	
	;sample is in the start position, so move filter
	cpi temp2, 0b01
	call moveFilterWheel

	;call until both wheels are in the start position
	rjmp moveWheels

checkFilterWheelPosition:
	;if the photo-interupt for filter is HIGH, then keep going
	;if bit 0 of port C is set, skip next instruction
	sbis PINC, 0
	nop
	ori temp2, 0b01
	ret

checkSampleWheelPosition:
	sbis PINC, 1
	nop
	ori temp2, 0b10
	ret
	
moveSampleWheel:
	call checkSampleWheelPosition
	mov tempState, state
	lsr state
	lsr state
	lsr state
	lsr state
	swap state
	lsl tempState
	lsl tempState
	lsl tempState
	lsl tempState
	swap tempState
	call getNextState
	or state, tempState
	;Now the filter part of the state register
	;is unchanged, but the next state in sent
	;out, so the sample wheel moves
	out PORTB, state
	ret

moveFilterWheel:
	call checkSampleWheelPosition
	mov tempState, state
	lsl tempState
	lsl tempState
	lsl tempState
	lsl tempState
	;Find the next state
	call getNextState
	lsr state
	lsr state
	lsr state
	lsr state
	or state, tempState
	;Now the sample part of the state register
	;is unchanged, but the next state in sent
	;out, so the filter wheel moves
	out PORTB, state
	ret

;***********************************************************
;* Calculates the next state given the current state in    *
;* tempState. Stops when the counter is reached			   *
;***********************************************************
getNextState:
	cp steps, counter
	breq getNextState

	cpi tempState, 0b1010
	breq state1

	cpi tempState, 0b1001
	breq state2

	cpi tempState, 0b0101
	breq state3

	cpi tempState, 0b0110
	breq state0

state1:
	ldi tempState, 0b1001
	rjmp doDelay

state2:
	ldi tempState, 0b0101
	rjmp doDelay

state3:
	ldi tempState, 0b0110
	rjmp doDelay

state0:
	ldi tempState, 0b1010
	rjmp doDelay

doDelay:
	ldi loop1, 0x01
	ldi loop2, 0x01
	rjmp delay

delay:
	mov temp3, loop2

loopA:
	mov loop2, temp3

loopB:
	dec loop2
	brne loopB

	dec loop1
	brne loopA

	ret


;***********************************************************
;* Main loop of the second measuring section			   *
;* calls all the methods to move the wheels and do         *
;* measurements											   *
;***********************************************************
doMeasurements:
	;steps is set here
	;* 100 when i want sample to turn 180 deg
	;* 67 to turn filter 120 deg
	ldi steps, 0 
	call measureRedBlank		; measure red blank
	call moveFilterWheel
	call doDelay
	
	ldi steps, 100
	call measureRedSample		; measure red sample
	call moveSampleWheel
	call doDelay
	
	ldi steps, 67
	call measureGreenBlank		; measure green blank
	call moveSampleWheel
	call doDelay
	
	ldi steps, 100
	call measureGreenSample		; measure green sample
	call moveSampleWheel
	call doDelay

	ldi steps, 67
	call measureBlueBlank		; measure blue blank
	call moveSampleWheel
	call doDelay
	
	ldi steps, 100
	call measureBlueSample		; measure blue sample
	call moveFilterWheel
	call doDelay
		
	call calculate
	call transmit

	jmp loop


measureBlueBlank:
	call readADC
	mov BB, temp1
	ret

measureBlueSample:
	call readADC
	mov BS, temp1
	ret

measureRedSample:
	call readADC
	mov RS, temp1
	ret

measureRedBlank:
	call readADC
	mov RB, temp1
	ret

measureGreenBlank:
	call readADC
	mov GB, temp1
	ret

measureGreenSample:
	call readADC
	mov GS, temp1
	ret


;***********************************************************
;* Calculates the vaules, actualy does / or *			   *
;***********************************************************
calculate:

	mov temp1, RS				;calculate red
	mov temp2, RB
	call divide
	call multiply
	mov redresult, temp4


	mov temp1, GS				;calculate green
	mov temp2, GB
	call divide
	call multiply
	mov greenresult, temp4


	mov temp1, BS				;calculate blue
	mov temp2, BB
	call divide
	call multiply
	mov blueresult, temp4	

	ret

transmit:

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

	ret



;***********************************************************
;* Setup the USART, the clock divider is passed in r16:r17 *
;***********************************************************

SetupUSART:
	;* Set the baud rate divider registers 
	out	UBRRH, temp2		; set the high byte of the serial clock divider
	out UBRRL, temp1		; set the low byte of the serial clock divider

	;* Enable the receiver and transmitter logic
	ldi	temp1, (1<<RXEN)|(1<<TXEN)	; set the RX enable and TX enable bits
	out	UCSRB, temp1		;

	;* Set the frame format to 8-bit data, 2 stop bits, no parity
	ldi temp1, (1<<URSEL)|(1<<USBS)|(3<<UCSZ0)	; combine the required bits
	out UCSRC, temp1

	ret						; we're done (notice temp1 has been modified in the routine)


;******************************************************************************
;* Sendchar: Waits until the USART is ready then sends a char (passed in r16) *
;******************************************************************************

SendChar:

	;* Wait until the USART transmitter buffer is empty
	sbis UCSRA, UDRE		; 'skip next if bit (UDRE) in register (UCSRA) is set (1)'
	rjmp sendchar			; if not then go back to the start again

	;* The USART is ready now so put the char in the output buffer
	out UDR, temp1

	ret


;******************************************************************************
;* Getchar: Waits until the USART has a char then get it (passed back in r16) *
;******************************************************************************

GetChar:

	;* Wait until the USART receiver has a char
	sbis UCSRA, RXC		; 'skip next if bit (RXC) in register (UCSRA) is set (1)'
	rjmp getchar		; if not then go back to the start again

	;* The USART has a char now so copy it into temp1 to pass it back to the main program
	in temp1, UDR

	ret


;******************************************************************
;* Convert the byte in r16 to ASCII hex and send it out the USART *
;******************************************************************

SendByte:

	mov	temp3, temp1	; take a copy of the byte
	ldi temp1, '0'		; put the ASCII code for "0" in temp1 (this is done by the assembler!)
	call SendChar
	ldi temp1, 'x'		; put the ASCII code for "0" in temp1 (thsi is done by the assembler)
	call SendChar

	mov temp1, temp3	; restore temp1 from the copy in temp3
	swap temp1			; swap the high and low nibbles of the byte so the high one gets sent first!
	call Nibble2ASCII	; convert the low nibble in r16 into ASCII hex
	call SendChar		; send the char to the USART

	mov temp1, temp3	; restore the byte again
	call Nibble2ASCII	; covert the low nibble this time
	call SendChar		
	ret



;****************************************************************
;* Converts the lower nibble in r16 into an ASCII hex character *
;* The character is returned in r16 (uses r16 and r17)          *
;****************************************************************

Nibble2ASCII:
	andi temp1, 0x0F	; remove the high nibble
	cpi temp1, 0x0A		; compare the nibble with 0x0A
	brsh isHex			; branch to IsHex if nibble is A-F

	ldi temp2, 0x30		; add 0x30 to the nibble for ASCII 0-9
	add	temp1, temp2	;
	ret					; return for ASCII 0-9

isHex:
	ldi temp2, 0x37		; add 0x37 to the value for ASCII A-F
	add temp1, temp2
	ret					; return for ASCII A-F

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


;**************************************************************
;* Print out "Press s to start on the screen				  *
;**************************************************************

startMessage:
	ldi temp1, 'P'
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 's'			
	call SendChar
	ldi temp1, 's'			
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 's'
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'o'			
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 's'		
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'a'			
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, 't'
	call SendChar

	;send carrage return and line feed
	ldi temp1, 10
	call SendChar
	ldi temp1, 13
	call SendChar


;**************************************************************
;* Multiplies temp1 with 100, high result in r1 low in r0     *
;**************************************************************
	multiply:
	ldi temp3, 0x64
	mul temp3, temp4
	movw temp4, temp3
	ret

;**************************************************************
;* Divides temp1 and temp2, and puts result in temp1	      *
;**************************************************************

divide:

	sub	temp3,temp3		;clear remainder and carry
	ldi	temp5,9			;init loop counter

	d8u_1:	
		rol	temp1		;shift left dividend
		dec	temp5		;decrement counter
		brne d8u_2		;if done
		ret				;return

	d8u_2:
		rol	temp3		;shift dividend into remainder
		sub	temp3,temp2	;remainder = remainder - divisor
		brcc d8u_3		;if result negative
		add	temp3,temp2	;restore remainder
		clc				;clear carry to be shifted into result
		rjmp	d8u_1	;else
	d8u_3:	
		sec				;set carry to be shifted into result
		rjmp	d8u_1