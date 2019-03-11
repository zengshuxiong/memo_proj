#!/usr/bin/perl

print "Hello,World!\n"; 

# chdir "D:/eden/Tools/openfile_jar2maven";

#use Cwd qw(abs_path);
use Cwd;

my $path1 = getcwd; #abs_path($0);
print "getcwd = $path1\n";

chdir $path1;

my $input_file_name = "input_data1.txt";
my $output_file_name = "output_data1.txt";

my $dataPath =  $path1 . "/data/${input_file_name}";
print "dataPath = $dataPath\n";

open(FILE_HANDLE, "<", $dataPath)
	or die "Can't open < ${input_file_name}: $!";

my $outPath =  $path1 . "/data/${output_file_name}";
open(OUT_FILE, ">", $outPath)
	or die "Can't open > ${output_file_name}: $!";


print "========================\n";
print "original record: \n";
print "========================\n";

@records = ();
while(chomp($rec =<FILE_HANDLE>)){
	if($rec =~ /^\s*$/){
		next;
	}
	
	push(@records, $rec);
    print "$rec\n";
}

print "\n";
print "========================\n";
print "split data : \n";
print "========================\n";
foreach $line (@records){
	
	print "// data = $line\n";
	
	#input:
	#mail.jar ; jdo2-api.jar 
	#spring-core-3.2.4.RELEASE.jar
	#{artifactId}{version}.jar
	
	#ouput:
	#<dependency>
	#	<groupId>org.springframework</groupId>
	#	<artifactId>spring-core</artifactId>
	#	<version>3.2.4.RELEASE</version>
	#</dependency>
	
	local $artifactId = '';
	local $version = '';
	
	if($line =~ /(.*?)-(\d+\.\d+.*?)\.jar/){ #6列:spring-core 3.2.4.RELEASE  .jar
		print "\$1=[$1],\$2=[$2]\n"; #spring-core 3.2.4.RELEASE
		$artifactId = $1;
		$version = $2;
	}
	elsif($line =~ /(.*?)\.jar/){ #jdo2-api.jar 
		print "\$1=[$1] -- $line\n"; #spring-core 3.2.4.RELEASE
		$artifactId = $1;
		$version = 'null';
	}
	
	
	$line =~ s/\r|\n//;
	
	print OUT_FILE  "\n";
	print OUT_FILE  "<!-- $line  -->\n";
	print OUT_FILE  "<dependency>\n";
	print OUT_FILE  "	<groupId>ggggggg</groupId>\n";
	print OUT_FILE  "	<artifactId>$artifactId</artifactId>\n";
	print OUT_FILE  "	<version>$version</version>\n";
	print OUT_FILE  "</dependency>\n";
	
	
}

print "finished!\n";

close(FILE_HANDLE) 
	or warn "Can't close ${input_file_name}: $!";
	
close(OUT_FILE) 
	or warn "Can't close ${output_file_name}: $!";

