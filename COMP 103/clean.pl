#! /usr/bin/perl
while (<STDIN>)
{
	$_ =~ s/  /\t/gi;   #convert spaces into tabs
	$output .= $_;
}
print $output;
