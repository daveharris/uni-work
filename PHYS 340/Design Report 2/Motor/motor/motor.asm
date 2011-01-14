.include "m16def.inc"

.def state = r16 ;setup registers
.def temp2 = r17
.def temp3 = r18

.def loop1 = r20
.def loop2 = r21
.def steps = r22
.def counter = r23

.org 0x0000

	jmp RESET

RESET:
	ldi state, high(RAMEND) ;loads the upper and lower bits into state
	out SPH, state
	ldi state, low(RAMEND)
	out SPL, state

ldi state, 0b1010
ldi temp2, 0
ldi temp3, 0
ldi steps, 3
ldi counter, 0

ldi temp2, 0xff
out DDRB, temp2

getNextState:
	cp steps, counter
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
	call doDelay

state2:
	ldi state, 0b0101
	call doDelay

state3:
	ldi state, 0b0110
	call doDelay

state0:
	ldi state, 0b1010
	call doDelay

doDelay:
	ldi loop1, 0xff
	ldi loop2, 0x5f
	call delay

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
	call getNextState

finish:
	ldi counter, 0
	ldi state, 0b0000
	ldi loop1, 0xffff
	ldi loop2, 0xffff
	call delay