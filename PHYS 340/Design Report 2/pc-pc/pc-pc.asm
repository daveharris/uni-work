.include "m16def.inc"

.def temp1 = r16 ;setup registers
.def temp2 = r17
.def temp3 = r18

.org 0x0000

	jmp RESET

RESET:
	ldi temp1, high(RAMEND) ;loads the upper and lower bits into temp1
	out SPH, temp1
	ldi temp1, low(RAMEND)
	out SPL, temp1
        
	call setupUSART

loop:
	call getchar ;get the char from keyboard
	call sendchar ;send the char back to computer
	rjmp loop ; do again ... and again ...


setupUSART:
	ldi temp2, 0x00
	ldi temp1, 0x01
	out UBRRH, temp2
	out UBRRL, temp1

	;*enable transmitter ad reciever logic
	ldi temp1, (1<<RXEN)|(1<<TXEN)
	out UCSRB, temp1

	;*set frame format to 8 bit, 2 stops its, no parity
	ldi temp1, (1<<URSEL)|(1<<USBS)|(3<<UCSZ0)
	out UCSRC, temp1

	ret


SendChar:
	;wait till bufferis empty, then send
	sbis UCSRA, UDRE
	rjmp sendchar

	out UDR, temp1

	ret

GetChar:
	;waits till USART has char in buffer, then sends it
	sbis UCSRA, RXC
	rjmp getchar

	in temp1, UDR
	
	ret

