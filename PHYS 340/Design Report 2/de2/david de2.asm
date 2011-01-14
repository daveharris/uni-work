.include "m16def.inc"

.def temp1 = r16
.def temp2 = r17
.def temp3 = r18
.def motor = r19
.def dir = r20
.def steps = r21
.def counter = r22
.def state = r23
.def loop1 = r24
.def loop2 = r25

.org 0x0000

	jmp RESET

RESET:
	ldi temp1, high(RAMEND)
	out SPH, temp1
	ldi temp1, low(RAMEND)
	out SPL, temp1
        
setupUSART:
	ldi temp2, 0x00
	ldi temp1, 0x01
	out UBRRH, temp2
	out UBRRL, temp1

	ldi temp1, (1<<RXEN)|(1<<TXEN)
	out UCSRB, temp1

	ldi temp1, (1<<URSEL)|(1<<USBS)|(3<<UCSZ0)
	out UCSRC, temp1

loop:
	call GetChar
	rjmp loop

getChar:
	;sbis UCSRA, RXC
	;rjmp getchar

	;in temp1, UDR
	;cpi temp1, 73				;if(temp1 == 's')
	;breq indexHoleFilterSearchLoop
	rjmp setupWheels
	
	ret

;Set up wheels to start
setupWheels:
	;make C an input port
	ldi temp2, 0x00
	out DDRC, temp2
	call setupMotor
	call setupFilterWheel
	call setupSampleWheel

;Rotate the filter wheel until it is in the start position
setupFilterWheel:
	in temp1, PINC
	;if the photo-interupt for filter is HIGH, then keep going
	;cpi temp1, 0b00000001
	;breq moveStepperMotor
	call getNextState
	rjmp setupFilterWheel

;Rotate the sample wheel until it is in the start position
setupSampleWheel:
	ret

setupMotor:
	ldi counter, 0
	ldi state, 0b1010
	ldi temp2, 0xff
	out DDRB, temp2
	ret

getNextState:
	cpi counter, 1
	breq finish

	cpi state, 0b1010
	breq state1

	cpi state, 0b1001
	breq state2

	cpi state, 0b0101
	breq state3

	cpi state, 0b0110
	breq state0

state1:
	ldi state, 0b1001
	rjmp doDelay

state2:
	ldi state, 0b0101
	rjmp doDelay

state3:
	ldi state, 0b0110
	rjmp doDelay

state0:
	ldi state, 0b1010
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

	out PORTB, state
	inc counter
	;rjmp setupFilterWheel
	rjmp getNextState

finish:
	ret
	
;loopforever:
;	rjmp loopforever


SetupADC:
	
	;* Enable the ADC and set the clock signal used for the successive 
	;* approximation ADC logic to the main clock divided by 16
	ldi temp3, (1<<ADEN)+(9<<ADPS0)
	out ADCSR, temp1
	
	ret
	