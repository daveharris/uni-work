.include "m16def.inc"

;setup registers
.def state = r16
.def temp2 = r17
.def temp3 = r18

.def loop1 = r19
.def loop2 = r20
.def tempState = r21
.def counter = r22

.org 0x0000

	jmp RESET

RESET:
	ldi state, high(RAMEND) ;loads the upper and lower bits into state
	out SPH, state
	ldi state, low(RAMEND)
	out SPL, state

;load state with 0xA9
ldi state, 0b10101001
ldi temp2, 0
ldi temp3, 0
ldi counter, 0

ldi temp2, 0xff
out DDRB, temp2

ldi temp2, 0x00
out DDRC, temp2
ldi temp2, 0x00

moveWheels:
	;filter is in the start position, so move sample
	;cpi temp2, 0b01
	call moveSampleWheel
	;call moveFilterWheel
	rjmp moveWheels

checkFilterWheelPosition:
	;if the photo-interupt for filter is HIGH, then keep going
	;if bit 0 of port C is set, skip next instruction
	;sbis PINC, 0
	;rjmp filterWheelStart
	;ret

checkSampleWheelPosition:
	;sbis PINC, 1
	;rjmp sampleWheelStart
	;ret
	
moveSampleWheel:
	;call checkSampleWheelPosition
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
	out PORTB, state
	ret

moveFilterWheel:
	;call checkFilterWheelPosition
	lsr state
	lsr state
	lsr state
	lsr state
	mov tempState, state
	call getNextState
	swap tempState
	or state, tempState
	out PORTB, state
	ret

getNextState:
	;cp steps, counter
	;breq finish

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

filterWheelStart:
	ori temp2, 0b01
	rjmp wait	

sampleWheelStart:
	ori temp2, 0b10
	rjmp wait

wait:
	cpi temp2, 0b01
	breq checkSampleWheelPosition
	cpi temp2, 0b10
	;breq checkFilterWheelPosition
	cpi temp2, 0b11
	;breq RESET
	;breq loopForEver
	
loopForEver:
	rjmp loopForEver