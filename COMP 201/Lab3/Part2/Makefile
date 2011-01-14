# Simple Makefile for COMP201 Lab2
# 
# The general format of entries in makefiles is:
#
#--------------------------------------------------------------------------
#thing:		thing_depended_on   other_thing_depended_on   ...
#		command to make the thing, and any options necessary ...
#
#^^^^^^^^^^^^^^	Note that there must be a "tab" character at the front 
#               of the command line.
#--------------------------------------------------------------------------

#
# The first thing in the makefile is the one made by default,
# although that may involve making things it depends on.
# There are many other features and conventions for using make.
# See "man make" for details; there's also a link on the COMP201 Web Page.
#

addressbook:    entry.o addressbook.o
		g++ entry.o addressbook.o -o book

addressbook.o:	addressbook.cc entry.h
		g++ -c addressbook.cc

entry.o:        entry.cc entry.h
		g++ -c entry.cc
run:	book
	./book
