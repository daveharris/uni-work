.include "m16def.inc"

.def temp1 = r16
.def temp2 = r17
.def temp3 = r18

.org 0x0000

	jmp RESET

RESET:
	ldi temp1, high(RAMEND)
	out SPH, temp1
	ldi temp1, low(RAMEND)
	out SPL, temp1
        
call setupUSART

loop:
	ldi temp1, 'P'
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

	call GetChar
	rjmp loop

setupUSART:
	ldi temp2, 0x00
	ldi temp1, 0x01
	out UBRRH, temp2
	out UBRRL, temp1

	ldi temp1, (1<<RXEN)|(1<<TXEN)
	out UCSRB, temp1

	ldi temp1, (1<<URSEL)|(1<<USBS)|(3<<UCSZ0)
	out UCSRC, temp1

	ret

SendChar:
	sbis UCSRA, UDRE
	rjmp sendchar

	out UDR, temp1

	ret

GetChar:
	sbis UCSRA, RXC
	rjmp getchar

	in temp1, UDR
	
	ret



