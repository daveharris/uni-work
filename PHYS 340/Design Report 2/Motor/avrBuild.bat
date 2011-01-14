cd "C:\David Harris - PHYS340\Motor\"
C:
del "C:\David Harris - PHYS340\Motor\motor.map"
del "C:\David Harris - PHYS340\Motor\motor.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "c:\david harris - phys340\motor\motor.asm" -o "C:\David Harris - PHYS340\Motor\motor.hex" -d "C:\David Harris - PHYS340\Motor\motor.obj" -e "C:\David Harris - PHYS340\Motor\motor.eep" -I "C:\David Harris - PHYS340\Motor" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David Harris - PHYS340\Motor\motor.map"
