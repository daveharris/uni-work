@ECHO OFF
del "d:\daves\homework\phys34~1\labels.tmp"
"C:\Program Files\AVR Studio\AvrAssembler2\avrasm2.exe" -S "d:\daves\homework\phys34~1\labels.tmp" -fI  -o "d:\daves\homework\phys34~1\david harris.hex" -d "d:\daves\homework\phys34~1\david harris.obj" -e "d:\daves\homework\phys34~1\david harris.eep" -m "d:\daves\homework\phys34~1\david harris.map" -W+ie   "D:\Daves\Homework\PHYS340 DE2\david de2.asm"
