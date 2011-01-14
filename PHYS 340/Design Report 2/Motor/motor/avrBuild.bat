cd "C:\David Harris - PHYS340\Motor\motor\"
C:
del "C:\David Harris - PHYS340\Motor\motor\motor.map"
del "C:\David Harris - PHYS340\Motor\motor\motor.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "c:\david harris - phys340\motor\motor\motor.asm" -o "C:\David Harris - PHYS340\Motor\motor\motor.hex" -d "C:\David Harris - PHYS340\Motor\motor\motor.obj" -e "C:\David Harris - PHYS340\Motor\motor\motor.eep" -I "C:\David Harris - PHYS340\Motor\motor" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David Harris - PHYS340\Motor\motor\motor.map"
