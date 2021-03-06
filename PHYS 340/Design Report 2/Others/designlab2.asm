;**** A P P L I C A T I O N   N O T E   A V R 2 0 0 ************************
;*
;* Title:		PHYS 340 - 2004 Design Lab2 - Spectrophotometer 
;* Version:		0.1
;* Last updated:10/06/04
;* Target:		ATmega16
;* Target clock:3.6864 MHz	
;*
;****************************************************************************

.include "m16def.inc"	; This file contains defs for all of the mega16's
						; registers.  Also includes values for various 
						; constants like the size of the internal SRAM.

; Multiplication settings

.def	mc8u	=r16		;multiplicand
.def	mp8u	=r17		;multiplier
.def	m8uL	=r17		;result Low byte
.def	m8uH	=r18		;result High byte

; Division settings

.def	drem16uL=r14
.def	drem16uH=r15
.def	dres16uL=r16
.def	dres16uH=r17
.def	dd16uL	=r16
.def	dd16uH	=r17
.def	dv16uL	=r18
.def	dv16uH	=r19

;***** Names for Register Variables.

.def	temp1	=r16	; general temp registers
.def	temp2	=r17	; anytime a subroutine is called these will get overwritten!
.def	temp3	=r18	;

.def	loop1	=r26	;loop counter variables
.def	loop2	=r27	;

.def	redLow		= r8
.def	redHigh		= r9
.def	greenLow	= r10
.def	greenHigh	= r11
.def	blueLow		= r12
.def	blueHigh	= r13

.def 	filterStep = r21 ; the filter stepper motor current state
.def  	sampleStep = r22 ; the sample stepper motor current state

.def 	combinedStep = r23 ; both steppers combined into one 8bit reg

.def	sample		= r24	; the sample reading
.def 	blank		= r25	; the blank reading

;* Start of the actual instructions!

.org	0x0000			; place these instructions at the start of program 
						; memory

	jmp	RESET		; If a reset occurs, then jump to the start of the code


;***** This is the main code

RESET:		; this is the start of the main code (start here after a reset)					

	;* First we need to setup the 'stack pointer' otherwise we will have problems 
	;* when we call a subroutine.  This is usually set to the end of the internal SRAM.
	
	ldi	temp1, high(RAMEND)	; set temp1 to equal the high byte of the address RAMEND
	out SPH, temp1			; set the high byte of the hardware stack pointer
	ldi temp1, low(RAMEND)	; set temp1 to equal the low byte of the address RAMEND
	out SPL, temp1			; set the low byte ofthe hardware stack pointer

	;* Done! On with the main section of the program.

	;* Now we are going to setup the mega16 USART (asynchronous serial interface)

	ldi temp1, 1	; calculate the low byte of the serial clock
	ldi	temp2, 0	; calculate the high byte of the serial clock

	call setupUSART			; call the serial setup subroutine (it uses the temp1 and temp2 values)

	call SetupADC			;Do the initial setup for the ADC

	call SetupGuideSwitches ; setup the switches for locating the disks

	call SetupSteppers      ; setup register values for initial stepper state
							; and initialize steppers output to port D

	call StartUpMessage

mainloop:
	
	call Prompt				; Give user a prompt
	call GetCommand			; Get command from user via serial pc link

	rjmp mainloop			; jump back and wait for another character


;********** SUBROUTINES **************************************


;*************************************************************
;* Try and get 'start' command								 *
;*************************************************************

GetCommand:

	call getChar
	call sendChar
	cpi temp1, 's'
	brne getCommandExit

	call getChar
	call sendChar
	cpi temp1, 't'
	brne getCommandExit

	call getChar
	call sendChar
	cpi temp1, 'a'
	brne getCommandExit

	call getChar
	call sendChar
	cpi temp1, 'r'
	brne getCommandExit

	call getChar
	call sendChar
	cpi temp1, 't'
	brne getCommandExit

	call sendCRLF

	call RunSamplingSequence	; start command sent so start sampling

getCommandExit:

	call sendCRLF

	ret

;***********************************************************
;* Run the sampling										   *
;***********************************************************

RunSamplingSequence:

	call LocateGuideHoles	; align disks

	; First do Blue filter

	call blue
	call sendCRLF

	call RunSingleSample
	call CalculateResult

	call result

	call sendByte
	call sendSpace
	mov temp1, temp2
	call sendByte
	call sendCRLF

	call delay

	; Second Green filter

	call green
	call sendCRLF

	call RunSingleSample
	call CalculateResult

	call result

	call sendByte
	call sendSpace
	mov temp1, temp2
	call sendByte
	call sendCRLF

	call delay

	; Third Red filter

	call red
	call sendCRLF

	call RunSingleSample
	call CalculateResult
	
	call result

	call sendByte
	call sendSpace
	mov temp1, temp2
	call sendByte
	call sendCRLF

	call delay
	
	ret

;***********************************************************
;* Calculate the result
;***********************************************************

CalculateResult:

	; sample x 100
	ldi mc8u, 100
	mov mp8u, sample

	call mpy8u

	; sample x 100 / blank
	mov dd16uL, m8uL
	mov dd16uH, m8uH
	mov dv16uL, blank
	ldi dv16uH, 0
	
	call div16u
	
	; result stored in r16:r17 (L:H)	

	ret

;***********************************************************
;* Run a single sample cycle for one filter and then rotate
;* disks for next filter
;***********************************************************

RunSingleSample:

	call delay

	call sample

	call ReadADC		; measure sample
	call sendByte
	call sendSpace
	mov sample, temp1	; save sample result

	ldi temp2, 100		; rotate to sample to blank
	call RotateSample

	call delay

	call sendCRLF

	call blank

	call ReadADC		; measure blank
	call sendByte
	call sendSpace
	mov blank, temp1	; save blank result

	ldi temp2, 66		; rotate filter at end
	call RotateFilter	

	call delay

	ldi temp2, 100		; rotate blank to sample
	call RotateSample

	call sendCRLF

	ret

;***********************************************************
;* Locate the guide holes of sample and filter disk		   *
;***********************************************************

LocateGuideHoles:

	in temp1, PINC
	ldi temp2, 0x01
	and temp2, temp1

	cpi temp2, 0x01

	breq locateFilter	; Sample guide hole is aligned only 
						; adjust filter

locateSample:

	push temp1			; save temp1 to stack
	
	mov temp1, sampleStep	; get next sampleStep state
	call getNextState
	mov sampleStep, temp1
	
	pop temp1			; restore temp1 from stack

	ldi temp2, 0x02
	and temp2, temp1
	cpi temp2, 0x02
	breq locateEnd

locateFilter:

	mov temp1, filterStep	; get next filterStep state
	call getNextState
	mov filterStep, temp1	

locateEnd:

	call StepMotors			; step motors

	call delay
	call delay


	in temp1, PINC
	ldi temp2, 0x03
	and temp2, temp1
	cpi temp2, 0x03
	breq locateExit				; exit if equal as both holes
							; are aligned
  
	jmp LocateGuideHoles	

locateExit:

	ret

;***********************************************************
;* Rotate Sample (temp2 steps)							   *
;***********************************************************

RotateSample:

	mov temp1, sampleStep	; get next state for sample
	call GetNextState
	mov sampleStep, temp1

	call StepMotors			; rotate sample

	dec temp2				; decrement

	cpi temp2, 0x0
	brne RotateSample		; when temp2 is 0 finish

	ret

;***********************************************************
;* Rotate Filter (temp2 steps)							   *
;***********************************************************

RotateFilter:

	mov temp1, filterStep	; get next state for filter
	call GetNextState
	mov filterStep, temp1

	call StepMotors			; rotate filter

	dec temp2				; decrement

	cpi temp2, 0x0
	brne RotateFilter		; when temp2 is 0 finish

	ret

;***********************************************************
;* Step motors next appropriate step					   *
;***********************************************************

StepMotors:

	call delay

	mov combinedStep, sampleStep
	lsl combinedStep
	lsl combinedStep
	lsl combinedStep
	lsl combinedStep	; move sampleStep to upper nibble of 
						; combinedStep
	
	or combinedStep, filterStep	; set lower nibble of 
								; combinedStep to filterStep

	out PORTB, combinedStep	; do next step of motors
	ret

;***********************************************************
;* GetNextState (put next state in temp1)                  *
;***********************************************************

GetNextState:

	cpi temp1, 0xa
	breq nextState2

	cpi temp1, 0x9
	breq nextState3

	cpi temp1, 0x5
	breq nextState4

	cpi temp1, 0x6
	breq nextState1

nextState1:

	ldi temp1, 0xa
	ret

nextState2:

	ldi temp1, 0x9
	ret

nextState3:

	ldi temp1, 0x5
	ret

nextState4:

	ldi temp1, 0x6
	ret

;***********************************************************
;* Measure the sample									   *
;***********************************************************

MeasureSample:

	call ReadADC

	ret

;***********************************************************
;* Setup guide hole switches							   *
;***********************************************************

SetupGuideSwitches:

	ldi temp1, 0x0
	out DDRC, temp1		; set Port B to input

	ret

;***********************************************************
;* Setup initial Stepper states							   *
;***********************************************************

SetupSteppers:

	ldi filterStep, 0xa		; initial output b1010
	ldi sampleStep, 0xa
	ldi combinedStep, 0xaa

	ldi temp1, 0xff
	out DDRB, temp1
	out PORTB, combinedStep

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
	call Nibble2ASCII		; covert the low nibble this time
	call SendChar		;
	ret



;************************************
;* Send a CR + LF pair to the USART *
;************************************

SendCRLF:
	push temp1
	ldi temp1, 10		; load ASCII cr character
	call SendChar		; send a cr to put terminal on next line for the next output
	ldi temp1, 13		; load ASCII lf character
	call SendChar		; send a lf to put terminal on next line for the next output
	pop temp1
	ret

;************************************
;* Send a space 					*
;************************************

SendSpace:
	push temp1
	ldi temp1, ' '
	call SendChar
	pop temp1
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

;*************************************************************
;* Startup Message											 *
;*************************************************************

StartUpMessage:

	ldi temp1, 'S'			; send the startup string "Spectrophotometer"+cr
	call SendChar
	ldi temp1, 'p'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'c'			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, 'o'
	call SendChar
	ldi temp1, 'p'			
	call SendChar
	ldi temp1, 'h'			
	call SendChar
	ldi temp1, 'o'			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'o'		
	call SendChar
	ldi temp1, 'm'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'e'
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, ':'			
	call SendChar

	call SendCRLF
	 
	call SendCRLF

	ret

;*************************************************************
;* Prompt for user											 *
;*************************************************************

Prompt:

    ldi temp1, 'E'			; send the prompt string "Enter 'start' to measure sample"+cr
	call SendChar
	ldi temp1, 'n'			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, '''
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
	ldi temp1, '''			
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, 'o'
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 'm'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'a'			
	call SendChar
	ldi temp1, 's'			
	call SendChar
	ldi temp1, 'u'			
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, 'e'
	call SendChar
	ldi temp1, ' '			
	call SendChar
	ldi temp1, 's'			
	call SendChar
	ldi temp1, 'a'			
	call SendChar
	ldi temp1, 'm'			
	call SendChar
	ldi temp1, 'p'			
	call SendChar
	ldi temp1, 'l'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar

	call SendCRLF

	ret

;***************************************************************************
;*
;* "mpy8u" - 8x8 Bit Unsigned Multiplication
;*
;* This subroutine multiplies the two register variables mp8u and mc8u.
;* The result is placed in registers m8uH, m8uL
;*  
;* Number of words	:34 + return
;* Number of cycles	:34 + return
;* Low registers used	:None
;* High registers used  :3 (mc8u,mp8u/m8uL,m8uH)	
;*
;* Note: Result Low byte and the multiplier share the same register.
;* This causes the multiplier to be overwritten by the result.
;*
;***************************************************************************

mpy8u:	clr	m8uH		;clear result High byte
	lsr	mp8u		;shift multiplier
	
	brcc	noad80		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad80:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad81		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad81:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad82		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad82:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad83		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad83:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad84		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad84:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad85		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad85:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad86		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad86:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier

	brcc	noad87		;if carry set
	add	m8uH,mc8u	;    add multiplicand to result High byte
noad87:	ror	m8uH		;shift right result High byte 
	ror	m8uL		;rotate right result L byte and multiplier
	
	ret

;***************************************************************************
;*
;* "div16u" - 16/16 Bit Unsigned Division
;*
;* This subroutine divides the two 16-bit numbers 
;* "dd8uH:dd8uL" (dividend) and "dv16uH:dv16uL" (divisor). 
;* The result is placed in "dres16uH:dres16uL" and the remainder in
;* "drem16uH:drem16uL".
;*  
;* Number of words	:196 + return
;* Number of cycles	:148/173/196 (Min/Avg/Max)
;* Low registers used	:2 (drem16uL,drem16uH)
;* High registers used  :4 (dres16uL/dd16uL,dres16uH/dd16uH,dv16uL,dv16uH)
;*
;***************************************************************************

div16u:	clr	drem16uL	;clear remainder Low byte
	sub	drem16uH,drem16uH;clear remainder High byte and carry

	rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_1		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_2		;else
d16u_1:	sec			;    set carry to be shifted into result

d16u_2:	rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_3		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_4		;else
d16u_3:	sec			;    set carry to be shifted into result

d16u_4:	rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_5		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_6		;else
d16u_5:	sec			;    set carry to be shifted into result

d16u_6:	rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_7		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_8		;else
d16u_7:	sec			;    set carry to be shifted into result

d16u_8:	rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_9		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_10		;else
d16u_9:	sec			;    set carry to be shifted into result

d16u_10:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_11		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_12		;else
d16u_11:sec			;    set carry to be shifted into result

d16u_12:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_13		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_14		;else
d16u_13:sec			;    set carry to be shifted into result

d16u_14:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_15		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_16		;else
d16u_15:sec			;    set carry to be shifted into result

d16u_16:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_17		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_18		;else
d16u_17:	sec			;    set carry to be shifted into result

d16u_18:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_19		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_20		;else
d16u_19:sec			;    set carry to be shifted into result

d16u_20:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_21		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_22		;else
d16u_21:sec			;    set carry to be shifted into result

d16u_22:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_23		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_24		;else
d16u_23:sec			;    set carry to be shifted into result

d16u_24:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_25		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_26		;else
d16u_25:sec			;    set carry to be shifted into result

d16u_26:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_27		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_28		;else
d16u_27:sec			;    set carry to be shifted into result

d16u_28:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_29		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_30		;else
d16u_29:sec			;    set carry to be shifted into result

d16u_30:rol	dd16uL		;shift left dividend
	rol	dd16uH
	rol	drem16uL	;shift dividend into remainder
	rol	drem16uH
	sub	drem16uL,dv16uL	;remainder = remainder - divisor
	sbc	drem16uH,dv16uH	;
	brcc	d16u_31		;if result negative
	add	drem16uL,dv16uL	;    restore remainder
	adc	drem16uH,dv16uH
	clc			;    clear carry to be shifted into result
	rjmp	d16u_32		;else
d16u_31:sec			;    set carry to be shifted into result

d16u_32:rol	dd16uL		;shift left dividend
	rol	dd16uH
	ret

;***********************
;* a delay subroutine
;***********************

delay:

	push temp1
	ldi loop1, 0xFF
	ldi loop2, 0xFF
	call dodelay
	pop temp1
	ret

dodelay:
	mov temp1, loop2
loopA:
	mov loop2, temp1
loopB:
	dec loop2
	brne loopB

	dec loop1
	brne loopA

	ret
	
;************************************
;* output 'Blank'
;************************************

blank:

	push temp1

	ldi temp1, 'B'			; send 
	call SendChar
	ldi temp1, 'l'			
	call SendChar
	ldi temp1, 'a'			
	call SendChar
	ldi temp1, 'n'			
	call SendChar
	ldi temp1, 'k'			
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	pop temp1

	ret

;************************************
;* output 'Sample'
;************************************

sample:

	push temp1

	ldi temp1, 'S'			; send 
	call SendChar
	ldi temp1, 'a'			
	call SendChar
	ldi temp1, 'm'			
	call SendChar
	ldi temp1, 'p'			
	call SendChar
	ldi temp1, 'l'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	pop temp1

	ret

;************************************
;* output 'Result'
;************************************

result:

	push temp1

	ldi temp1, 'R'			; send 
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 's'			
	call SendChar
	ldi temp1, 'u'			
	call SendChar
	ldi temp1, 'l'			
	call SendChar
	ldi temp1, 't'			
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	pop temp1

	ret


;************************************
;* output 'blue'
;************************************

blue:

    ldi temp1, 'B'			; send 
	call SendChar
	ldi temp1, 'l'			
	call SendChar
	ldi temp1, 'u'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	ret

;****************************
;* output 'green'
;****************************

green:

    ldi temp1, 'G'			; send 
	call SendChar
	ldi temp1, 'r'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'n'
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	ret

;****************************
;* output 'Red'
;****************************

red:

    ldi temp1, 'R'			; send 
	call SendChar
	ldi temp1, 'e'			
	call SendChar
	ldi temp1, 'd'						
	call SendChar
	ldi temp1, ':'			
	call SendChar
	ldi temp1, ' '			
	call SendChar

	ret