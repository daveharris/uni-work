# Makefile to help with lab 4

# To use, type:
#   make all
#       -- to make everything
#
#   make clean
#       -- to remove all of the generated object and executable files
#
#   make cleanall
#       -- to *also* remove the afs.data file, to start completely a-fresh
#
#   make fileio
#       -- to make the fileio program from your fileio.cc source
#
#   make accfilesys
#       -- to make the accfilesys program using your accfilesys.cc code
#
#   make demo-accfilesys
#       -- to make the demo accfilesys program, using
#          the precompiled demo-fileio-netbsd.o

all:	fileio accfilesys demo-accfilesys


cleanall:	clean
	rm -f afs.data

clean:
	rm -f fileio.o accfilesys.o account.o accountfile.o fileio accfilesys

fileio:	fileio.o
	g++ -o fileio fileio.o

fileio.o:	fileio.cc
	g++ -c fileio.cc

accfilesys:	accfilesys.o account.o accountfile.o
	g++ -o accfilesys accfilesys.o account.o accountfile.o

demo-accfilesys:	accfilesys.o account.o demo-accountfile-netbsd.o
	g++ -o demo-accfilesys accfilesys.o account.o demo-accountfile-netbsd.o

accfilesys.o:	accfilesys.cc account.h accountfile.h
	g++ -c accfilesys.cc

account.o:	account.cc account.h
	g++ -c account.cc

accountfile.o:	accountfile.cc accountfile.h account.h
	g++ -c accountfile.cc
