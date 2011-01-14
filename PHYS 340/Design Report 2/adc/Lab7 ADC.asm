;**** A P P L I C A T I O N   N O T E   A V R 2 0 0 ************************
;*
;* Title:		PHYS 340 - 2004 Lab 7 - The ATmega16 microcontroller
;* Version:		0.1
;* Last updated:16-05-04
;* Target:		ATmega16
;* Target clock:3.6864 MHz	
;*
;****************************************************************************

.include "m16def.inc"	; This file contains defs for all of the mega16's
						; registers.  Also includes values for various 
						; constants like the size of the internal SRAM.


;***** Names for Register Variables.

.def	temp1	=r16	; general temp registers
.def	temp2	=r17	; anytime a subroutine is called these will get overwritten!
.def	temp3	=r18	;


;* Start of the actual instructions!

.org	0x0000			; place these instructions at the start of program 
						; memory

	jmp	RESET		; If a reset occurs, then jump to the start of the code


;***** This is the main code

RESET:		; this is the start of the main code (start here after a reset)					

	;* First we need to setup the 'stack pointer' otherwise we will have problems 
	;* when we call a subroutine.  This is usually set to the end of the internal SRAM.
	
	ldi	temp1, high(RAMEND)	; set temp2 to equal the high byte of the address RAMEND
	out SPH, temp1			; set the high byte of the hardware stack pointer
	ldi temp1, low(RAMEND)	; set temp2 to equal the low byte of the address RAMEND
	out SPL, temp1			; set the low byte ofthe hardware stack pointer

	;* Done! On with the main section of the program.


	;* Now we are going to setup the mega16 USART (asynchronous serial interface)

	ldi temp1, 1	; calculate the low byte of the serial clock
	ldi	temp2, 0	; calculate the high byte of the serial clock

	call setupUSART			; call the serial setup subroutine (it uses the temp1 and temp2 values)

	ldi temp1, 'P'			; send the startup string "PHYS340"+cr
	call SendChar
	ldi temp1, 'H'			
	call SendChar
	ldi temp1, 'Y'			
	call SendChar
	ldi temp1, 'S'			
	call SendChar
	ldi temp1, '3'			
	call SendChar
	ldi temp1, '4'			
	call SendChar
	ldi temp1, '0'			
	call SendChar
	ldi temp1, 10			
	call SendChar
	ldi temp1, 13
	call SendChar

	call SetupADC			;Do the initial setup for the ADC


mainloop:
	call GetChar			; wait for a character from the USART
	ldi temp1, 0x00
	call ReadADC			; do an ADC measurement on channel 0 (pin 40)
	call SendByte			; send the ADC result to the USART

	rjmp mainloop			; jump back an wait for another character


;********** SUBROUTINES **************************************


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
	ldi temp1, 'x'		; put the ASCII code for "0" in temp1 (this is done by the assembler)
	call SendChar

	mov temp1, temp3	; restore temp1 from the copy in temp3
	swap temp1			; swap the high and low nibbles of the byte so the high one gets sent first!
	call Nibble2ASCII		; convert the low nibble in r16 into ASCII hex
	call SendChar		; send the char to the USART

	mov temp1, temp3	; restore the byte again
	call Nibble2ASCII	; covert the low nibble this time
	call SendChar		;

	ldi temp1, 10		; load ASCII cr character
	call SendChar		; send a cr to put terminal on next line for the next output
	ldi temp1, 13		; load ASCII cr character
	call SendChar		; send a lf to put terminal on next line for the next output
	ret


;****************************************************************
;* Converts the lower nibble in r16 into an ASCII hex character *
;* The character is returned in r16 (uses r16 and r17)          *
;****************************************************************

Nibble2ASCII:
	andi temp1, 0x0F	; remove the high nibble
	cpi temp1, 0x0A		; compare the nibble with 0x0A
	brsh IsHex			; branch to IsHex if nibble is A-F

	ldi temp2, 0x30		; add 0x30 to the nibble for ASCII 0-9
	add	temp1, temp2	;
	ret					; return for ASCII 0-9

IsHex:
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
