run:		library
	clear; ./library

btest:		booktest
	clear; ./booktest

mtest:		maptest
	-rm test.data; clear; ./maptest

library:	library.o bucket.o book.o mapfile.o
	g++ -o library library.o bucket.o book.o mapfile.o

booktest:	booktester.o book.o bucket.o
	g++ -o booktest booktester.o book.o bucket.o

book.o:		book.cc book.h
	g++ -c -g book.cc

booktester.o: booktester.cc book.h bucket.h
	g++ -c -g booktester.cc

bucket.o:	bucket.cc bucket.h book.h
	g++ -c -g bucket.cc

library.o:	library.cc book.h bucket.h mapfile.h
	g++ -c -g library.cc

mapfile.o:	mapfile.cc mapfile.h book.h
	g++ -c -g mapfile.cc

maptester.o:maptester.cc book.h mapfile.h
	g++ -c -g maptester.cc

maptest:	maptester.o book.o mapfile.o bucket.o
	g++ -o maptest maptester.o book.o mapfile.o bucket.o


clean :
	-rm *.o

cleanall:
	-rm *.o; -rm *~ .*~ .\#* \#*; rm library; rm booktest; rm library.dat

tidy :
	-rm *~ .*~ .\#* \#*
