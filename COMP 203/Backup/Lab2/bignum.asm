	#Program: bignum.asm
    #Author: Ray Nickson, August 2000

    #Bignums are stored in consecutive words, with the least - significant
    #word first. Since our bignums are unsigned, we can use the
    #value - 1 to mark the last(most significant)word.

    .data
    bn1: .word 1, 1, 1, 1, 0x7fffffff, -1, 0, 0,0 
    bn2: .word 8, 9, 10, 11, 5, 0x7fffffff,  -1, 0, 0
    bn3: .word 0, 0, 0, 0, 0, 0, 0, 0, 0

    .text
    .globl main

    main: 
		la $a0, bn1
		la $a1, bn2
		la $a2, bn3
		jal bn_add

		#This system call halts the program.
		li $2, 10
		syscall

    bn_add: 
		#Your bn_add procedure goes here.
		#The registers $a0 and $a1 point to the two bignums to be added.
		#The register $a2 points to the place where the answer is to be put.
	
		#Allocate registers: 
		#($t0)the constant value - 1(for testing the end of an input)
		addi $t0, $zero, -1
	
		#($t1, $t2)the current word of each input
		lw $t1, 0($a0)
		lw $t2, 0($a1)
	
		#($t3)the current word of the output
		lw $t3, 0($a2)
	
		#($t4)the carry(0 or 1)from one word to the next
		add $t4, $zero, $zero
		
	
	Loop: 
		#If both bn1[] and bn2[] are -1, then goto Cleanup
		seq $t5, $t1, $t0				#if t1 = -1, t5 = 1
		seq $t6, $t2, $t0				#if t2 = -1, t6 = 1
		add $t7, $t5, $t6
		beq $t7, 2, Cleanup				#if t1 & t2 = -1, jump to Cleanup

				
		#If either bn1[] or bn2[] are - 1
		bne $t5, $zero, addZerosa0		#if(bn1 == -1), then goto addZerosa0
		bne $t6, $zero, addZerosa1		#if(bn2 == -1), then goto addZerosa1
		
	
	continue:
		addu $t3, $t1, $t2				#add current words w/out overflow
		add $t3, $t3, $t4				#add carry-in
		
		#If overflow occours
		slt $t4, $t3, $zero
		andi $t3, $t3, 0x7fffffff		#this gets rid of the possible
										#1 in the 32nd position
		
		sw $t3, 0($a2)					#store result in current position in bn3[]
		
		addi $a0, $a0, 4				#increment bn1[]
		addi $a1, $a1, 4				#increment bn2[]
		addi $a2, $a2, 4				#increment bn3[]
		
		lw $t1, 0($a0)					#get new word
		lw $t2, 0($a1)					#get new word
		lw $t3, 0($a2)
		
		j Loop
		
		
	addZerosa0:
		sw $zero, 0($a0)				#set it to zero
		add $t1, $zero, $zero			#Set current word to 0
		sw $t0, 4($a0)					#set next word to -1
		j continue
		
	
	addZerosa1:
		sw $zero, 0($a1)				#set it to zero
		add $t2, $zero, $zero			#Set current word to 0
		sw $t0, 4($a1)					#set next word to -1
		j continue
		
		
	Cleanup:
		bne $t4, $zero, newWord			#if overflow, goto newWord
		sw $t0, 0($a2)					#put -1 in output
		jr $ra							#return
		
	newWord:
		sw $t8, 0($a2)					#generate new word with value 1
		sw $t0, 4($a2)					#put -1 in output
		jr $ra							#return
		