# COMP203 (2003) Lab 4
# Program:     Sieve 1.1
# Author:      Anton Marsden, 1998.
# Modified by: Paul Martin, Oct 1999.

        .data 0x10000000
        .align 2

# Reserve space for the array of Booleans
Sieve:  .space 0x10000

# The value stored at address 'Size' determines the size of the sieve.
# If, for example, you set 'Size' to 20 then the numbers from 0 to 19
# will be used in the sieve.  If you change the value of 'Size' in the
# program then you must call init_sieve again.  
#
# With big sieve sizes the algorithm takes ages to complete.  We
# recommend using a size of 0x10000 or less when testing.

Size:   .word 0x2000

# The value stored at address 'Maxp' tells the algorithm when it can
# terminate.  'Maxp' is set when init_sieve is called and is set equal
# to floor(sqrt(Size))+1.  The program will not work if 2*Maxp + 1
# > size-1

Maxp:   .word 0 

Space:  .asciiz " " # a string used for printing primes

        .text
        .globl __start
__start:

        # Initialise Sieve array of Booleans
               
        jal  init_sieve
        or   $0, $0, $0

        ###########################################################
        # Start your modifications here
        
        # Main procedure

        add  $7,$0,$0				# register int $7=prime=0
		
do_sieve:							# while(1) {
        jal  next_prime				# prime=next_prime(prime)
        or   $0, $0, $0
        beq  $7, $0, end_sieve		# if (prime==0) break
        or   $0, $0, $0
        lui  $1, 4097				# register int $18=limit
        lw   $18, 0($1)
        or   $0, $0, $0
        sub  $18,$18,$7
        add  $4,$7,$0				# register int $4=n=prime
		
while_loop:							# do {
        jal  set_bit				# set_bit(n)
        add  $4,$4,$7				# n+=prime
        slt  $8, $4, $18
        bne  $8, $0, while_loop
        or   $0, $0, $0				# } while(n<limit)
        beq  $0, $0 do_sieve
        or   $0, $0, $0				# }
        
end_sieve:

# End your modifications here
        ###########################################################

        jal print_sieve
        ori $4, $0, 30				# print the first 30 primes

        # Halt the program
        ori $2, $0, 10
        syscall



#################################
#                              #
#       Support Routines       #
#                              #
#################################


init_sieve:

# Initialise the Sieve by clearing all bits of the array of Booleans
# EXCEPT the bits for 0 and 1 which we know are not prime.  Note that
# registers 1, 6 and 7 will be trashed by this routine.

        lui  $6, 4096			# point to first word in array
        lui  $1, 1				# $7 = $6 + 0x10000
        add  $7, $6, $1
        addi $7, $7, -32		# point to last word in array

init:   sw   $0, 0($6)			# initalise the array with zeros
        sw   $0, 4($6) 
        sw   $0, 8($6)
        sw   $0, 12($6) 
        sw   $0, 16($6)
        sw   $0, 20($6) 
        sw   $0, 24($6)
        sw   $0, 28($6) 
        bne  $6, $7, init
        addi $6, $6, 32

        ori  $7, $0, 3			# set Bool values for nos 0 and 1
        lui  $1, 4096
        sw   $7, 0($1)

        lui  $1, 4097 			# set Maxp to floor(sqrt(Size))+1
        lwc1 $f12, 0($1)
        or   $0, $0, $0
        cvt.d.w $f12, $f12
        ori  $2, $0, 11
        syscall					# calculate the square root
        cvt.w.d  $f0, $f0
        mfc1 $2, $f0
        or   $0, $0, $0
        addi $2, $2, 1
        lui  $1, 4097			# store result at address Maxp
        sw   $2, 4($1)
        jr   $31
        or   $0, $0, $0

next_prime:

# Find the next prime after $7. Return the new prime in $7. If the new
# prime is greater than Maxp then $7=0.  Note, this routine trashes
# $2, $5, $6, and $7.

        addi $7, $7, 1			# move on to next prime
        lui  $1, 4097			# $6 = Maxp (max prime to be sieved)
        lw   $6, 4($1)

_next_prime:
        srl  $2, $7, 3			# get byte offset (2^3 bits per byte)
        lui  $1, 4096			# read a byte from memory
        addu $1, $1, $2			# i.e. $2 = (byte)Sieve[$2]
        lbu  $2, 0($1)
        andi $5, $7, 7				# filter out bit offset from number
        srlv $2, $2, $5				# shift relevant bit into bit 0
        andi $2, $2, 1				# filter bit 0
        bne  $2, $0, _next_prime	# is this new number prime?
        addi $7, $7, 1				# if not, check the next number
        slt  $8, $7, $6				# is the new prime too big?
        bne  $8, $0, end_next_prime
        addi $7, $7, -1
        add  $7, $0, $0			# if so, $7=0

end_next_prime:
        jr $31					# return
        or $0, $0, $0


set_bit:

# Set the value of number $4 in the sieve to one.  Note, this routine
# trashes $1, $3, $5, and $6.

        srl  $5, $4, 5			# get word offset of bit and
        sll  $5, $5, 2			# make sure it is word aligned
        lui  $6, 4096			# point to bottom of Seive array
        add  $5, $5, $6			# add base to offset, put result in $5
        ori  $6, $0, 1			# set bit 0 in $6
        lw   $3, 0($5)			# get word from memory
        sllv $6, $6, $4			# shift the bit in $6 to correct posn
        or   $3, $3, $6			# word = word | (1<word_offset)      
        jr   $31				# return
        sw   $3, 0($5)			# save word to memory

check_bit:

# Return the value of number $4 of the sieve in $2.  If $2 is zero,
# the bit value was 0.  If $2 is one, the bit value was 1.  Note, this
# routine trashes $1, $5, and $2.

        srl  $2, $4, 3			# get byte offset
        lui  $1, 4096
        addu $1, $1, $2			# get byte from memory
        lbu  $2, 0($1)			# i.e. $2 = (byte)Sieve[$2]
        andi $5, $4, 7			# filter out bit offset from num
        srlv $2, $2, $5			# shift relevant bit into bit 0
        jr   $31				# return
        andi $2, $2, 1			# filter bit 0
               
print_sieve:

# Write the first $4 primes to the console.  Note, this routine
# trashes $1, $2, $4, $7, and $15.

        addi $29, $29, -4		# save return address
        sw   $31, 0($29)
        add  $7, $4, $0			# $7=number of primes to print
        ori  $4, $0, 1			# start at 1

print_loop:    
        beq $7, $0, end_print_sieve	# any more numbers to print?
        or  $0, $0, $0

np:     lui $1, 4097			# $2 = Size
        lw   $2, 0($1)
        or   $0, $0, $0
        slt  $8, $4, $2
        bne  $8, $0, fp
        or   $0, $0, $0
        beq  $0, $0, enp
        ori  $4, $0, 1
		
fp:     jal  check_bit			# destroys $1,$2,$5
        or   $0, $0, $0
        bne  $2, $0, np
        addi $4, $4, 1
		
enp:    addi $2, $4, -1
        beq  $2, $0, end_print_sieve	# overflow error?
        add  $4, $2, $0			# move next prime into $4
        ori  $2, $0, 1			# print $4
        syscall
        add  $5, $4, $0			# save prime in $5
        lui  $1, 4097
        ori  $4, $1, 8			# print space
        ori  $2, $0, 4
        syscall
        add  $4, $5, $0			# restore prime to $4
        addi $4, $4, 1			# move on to next number
        beq  $0, $0, print_loop
        addi $7, $7, -1			# decrement numbers left to print

end_print_sieve:       
        lw   $31, 0($29)		# restore return address
        addi $29, $29, 4
        jr   $31
        or   $0, $0, $0