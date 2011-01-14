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

calc:           calc.o invoice.o
		g++ calc.o invoice.o -o calc

calc.o:         calc.cc invoice.h
		g++ -c calc.cc

invoice.o:      invoice.cc invoice.h
		g++ -c invoice.cc
