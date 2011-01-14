# Comp 203 Lab 1
# Program: printpal
# Author: David Harris 300069566

        # The .data directive instructs SPIM to place the following
	# information in the data section of memory.
        .data
        # The .asciiz directive sets aside some memory for an ascii
	# string, and ends the string with a zero byte (C style).
str:    .asciiz "Jules Anderson, 199012345"

        # The .text directive indicates the start of the executable
 	# portion of the program. There are two underscores (_) in
        # __start, it is important that both of these are included.
        .text
        .globl __start
__start:   la $4, str

        # Load a single byte into register $5 from the memory location
        # listed in register $4.  If that byte is zero, then branch
	# to the finish: label.
while:  lb $5, 0($4)
        beqz $5, bkwrds

        # This code fragment displays the character in register $5
        # Don't be concerned with how it works for now.
wrtchr: lw $8, 0xffff0008
        andi $8, $8, 1
        beqz $8, wrtchr
        sb $5, 0xffff000c

        # loop back to the top, incrementing the register $4 to point 
        # to the next character in the string first.
        addi $4,$4, 1
        b while
	
bkwrds: add $4,$4, -1
	lb $5, 0($4)
	beqz $5, finish
	sb $5, 0xffff000c
	b bkwrds

        # To finish up, we must wait to ensure that the final
	# character has been printed.
finish: lw $8, 0xffff0008
        andi $8, $8, 1
        beqz $8, finish

        # This system call halts the program.	
        li $2, 10
        syscall