run : hotel
	clear; ./hotel

test: hotel
	clear; ( echo "l" ; echo "b" ; echo "1" ; echo "e" ; echo "1" ; echo "g" ; echo "e" ; echo "n" ; cat - ) | ./hotel

only : hotel


gdb : hotel
	clear; gdb hotel

hotel : booking.o bookingschedule.o categories.o contact.o date.o guests.o hotel.o room.o rooms.o
	g++ -o hotel booking.o bookingschedule.o categories.o contact.o date.o guests.o hotel.o room.o rooms.o

booking.o : booking.cc booking.h contact.h date.h room.h
	g++ -c $(HOTEL) booking.cc

bookingschedule.o : bookingschedule.cc bookingschedule.h date.h booking.h room.h
	g++ -c $(HOTEL) bookingschedule.cc

categories.o : categories.cc categories.h
	g++ -c $(HOTEL) categories.cc

contact.o : contact.cc contact.h
	g++ -c $(HOTEL) contact.cc

date.o : date.cc date.h
	g++ -c $(HOTEL) date.cc

guests.o : guests.cc guests.h
	g++ -c $(HOTEL) guests.cc

hotel.o : hotel.cc hotel.h categories.h rooms.h date.h
	g++ -c $(HOTEL) hotel.cc

room.o : room.cc room.h bookingschedule.h
	g++ -c $(HOTEL) room.cc

rooms.o : rooms.cc rooms.h room.h date.h
	g++ -c $(HOTEL) rooms.cc

clean :
	-rm *.o

tidy :
	-rm *~ .*~ .\#* \#*
