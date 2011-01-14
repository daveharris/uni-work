cd "C:\David Harris - PHYS340\DE2\"
C:
del "C:\David Harris - PHYS340\DE2\david harris.map"
del "C:\David Harris - PHYS340\DE2\david harris.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "c:\david harris - phys340\de2\david de2.asm" -o "C:\David Harris - PHYS340\DE2\david harris.hex" -d "C:\David Harris - PHYS340\DE2\david harris.obj" -e "C:\David Harris - PHYS340\DE2\david harris.eep" -I "C:\David Harris - PHYS340\DE2" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David Harris - PHYS340\DE2\david harris.map"
