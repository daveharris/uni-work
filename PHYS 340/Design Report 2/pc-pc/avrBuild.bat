cd "C:\David Harris - PHYS340\PC-PC\"
C:
del "C:\David Harris - PHYS340\PC-PC\pc-pc.map"
del "C:\David Harris - PHYS340\PC-PC\pc-pc.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "c:\david harris - phys340\pc-pc\pc-pc.asm" -o "C:\David Harris - PHYS340\PC-PC\pc-pc.hex" -d "C:\David Harris - PHYS340\PC-PC\pc-pc.obj" -e "C:\David Harris - PHYS340\PC-PC\pc-pc.eep" -I "C:\David Harris - PHYS340\PC-PC" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David Harris - PHYS340\PC-PC\pc-pc.map"
