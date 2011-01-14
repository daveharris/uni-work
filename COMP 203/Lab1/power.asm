#program Powers
#author Keith Bauer

	.data
	
prompt:	.asciiz "Enter the number: "
mesg:	.asciiz "When two is raised to that power, the answer is: "
endl:	.asciiz "\n"

	.text
	.globl main
	
main:	la $a0, prompt	# print the prompt
	li $2, 4
	syscall
	
	li $2, 5	# read an integer from the user
	syscall		#  into $v0
	
power:  # your code to compute the power goes here
        # the number is in $v0, you should put the result into $t1	

	
	addi $t1, $zero, 1	# t1 = 1
	add $t2, $zero, $v0	# t2 = v0

while:	beqz $t2, output			# while (t2 != 0) {
	add $t2, $t2, -1		#   t2 = t2-1
	add $t1, $t1, $t1
	b while				#   t1 = t1*2
					# }

output:	la $a0, mesg	# print some text
	li $2, 4
	syscall
	
	li $2, 1	# print the answer
	move $a0 $t1
	syscall
	
	la $a0, endl	# print return
	li $2, 4
	syscall
	
	li $2, 10	# let's get out of here.
	syscall