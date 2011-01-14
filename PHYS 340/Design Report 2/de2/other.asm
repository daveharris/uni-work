indexHoleFilterSearchLoop:
	call holeFound
	
	call moveStepperMotorClockwise
	jmp indexHoleFilterSearchLoop

finish:
	ldi steps, 0
	ret

indexHoleSample:

holeFound:
	ldi temp2, 0
	;load logic level from photo-interupter
	in temp2, PINC
	cpi temp3, 1
	breq setAns
	
setAns:
	ldi temp2, 1

moveStepperMotorClockwise:
	ldi motor, 1		;when motor = 1; means move the filter wheel
	ldi dir, 0
	ldi steps, 1
	call moveStepperMotor
	ret