cd "C:\David Harris - PHYS340\ADC\"
C:
del "C:\David Harris - PHYS340\ADC\adc.map"
del "C:\David Harris - PHYS340\ADC\adc.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "c:\david harris - phys340\adc\lab7 adc.asm" -o "C:\David Harris - PHYS340\ADC\adc.hex" -d "C:\David Harris - PHYS340\ADC\adc.obj" -e "C:\David Harris - PHYS340\ADC\adc.eep" -I "C:\David Harris - PHYS340\ADC" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David Harris - PHYS340\ADC\adc.map"
