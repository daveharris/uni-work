# COMP203 (2004) Lab 3
# Program: render
# Original author: Jules Anderson
# Modified by: Anton Marsden, Paul Martin

# This program repeatedly loops over an array of floating-point
# values, multiplying each by a constant X.  The program doesn't do
# the multiplication if the array value is zero.
         .data
FParray: .float 2.4, 0.0, 12.5, 0.0, 0.0, 0.0, 0.0, 0.0, -0.24, 12.5
         .float 0.0, 0.0, 0.0, 0.0, -122.02, 0.0, 7.27, 0.0, 0.0, 0.5
         .float 0.0, 18.3, 0.0, 0.0
FPcount: .byte  24
X:       .float 5.2

         .text
         .globl __start

__start: # Test harness begins here:
         # -------------------------
         # The following instructions set up the test environment
         # and do not need to be optimised in any way, as they
         #   a) only happen once, and
         #   b) will not be part of the production system.

         # Register 8 contains the length of the array, in bytes. 
         lui  $1, 4096					# lw $8, FPcount 
         lb   $8, 96($1)
		 or   $0, $0, $0				# No Operation
		 addu $8, $8, $8				# mul $8, $8, 4 
         addu $8, $8, $8

         # Load X into floating point register $f0  
         lui  $1, 4096					# lw $10, X 
         lw   $10, 100($1)
		 or   $0, $0, $0				# No Operation
         mtc1 $10, $f0

         # Instrumentation: register $12 will count how often we 
         # get through the loop, and register $13 will count how
         # often we actually do a multiply. 
         or $12, $0, $0 
         or $13, $0, $0

         ###############################################
         # Start your optimising here...
    
         # Register 9 indexes the array.
		 lui  $11, 4096
		 addiu $14, $8, -4
		 addu $9, $11, $0
		 addu $14, $11, $14
         
		 
top:	 
         # load register $10 with the array value referenced by
         # register $9.
		 or  $0, $0, $0
         lw   $10, 0($9)
		 
         # Instrumentation: increment the loop counter: 
         addiu $12, $12, 1

         # If the element is zero, move on to the next element.  We
         # take advantage of the fact that 0 in IEEE754 floating-point
         # notation is also integer 0, so we don't need to involve the 
         # floating point CPU.  

         beq $10, $0, next_element		# beqz $10, next_element
		        
		 
         # Otherwise, load into the FP processor, multiply by X
         # and store it back again. 
         
         mtc1  $10, $f2
         mul.s $f2, $f2, $f0
         mfc1  $10, $f2
		 addiu $13, $13, 1				# instrumentation
         sw    $10, 0($9)
        
next_element:
         # And go back to the top of the algorithm loop if we haven't reached
         # the pre-computed end of the array:  
		 bne $9, $14, top
		 # increase register $9 by 4 to point to the next word. 
		 addiu $9, $9, 4
		 #or  $0, $0, $0					# No Operation
        
         # Test harness code: simply go back up to the top and continue.
         # We never exit. 
         beq $0, $0, top				# b top
		 addu  $9, $11, $0