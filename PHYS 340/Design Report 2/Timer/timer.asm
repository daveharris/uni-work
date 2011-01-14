.def temp1 = r16
.def temp2 = r20
.def temp3 = r21

	ldi loop1, 0x40
	ldi loop2, 0x20
	call delay

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