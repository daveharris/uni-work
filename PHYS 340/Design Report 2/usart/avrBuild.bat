cd "C:\David&Eric\USART\"
C:
del "C:\David&Eric\USART\usart.map"
del "C:\David&Eric\USART\usart.lst"
"C:\Program Files\Atmel\AVR Tools\AvrAssembler\avrasm32.exe" -fI "C:\David&Eric\USART\USART.asm" -o "C:\David&Eric\USART\usart.hex" -d "C:\David&Eric\USART\usart.obj" -e "C:\David&Eric\USART\usart.eep" -I "C:\David&Eric\USART" -I "C:\Program Files\Atmel\AVR Tools\AvrAssembler\AppNotes" -w  -m "C:\David&Eric\USART\usart.map"
