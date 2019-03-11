#!/usr/bin/perl

print "Hello,World!\n"; 

# chdir "D:/eden/Tools/openfile";

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
	
	#$line =~ /(.*?)\s+(.*?)\s+(.*?)\s+(.*)/;
	$line =~ /(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*)/; #6列:英文名称	中文名称	数据类型/长度	是否必输	约束条件	备注
	print "\$1=$1,\$2=$2,\$3=$3,\$4=$4,\$5=$5,\$6=$6\n"; #$1=TranBranchId,$2=交易机构代码,$3=String(30),$4=N,$5=服务请求者的机构归属,$6=APP_HEAD
	
	 print "\$1= [$1];\n";
	# print lc(substr($1,0,1)) . ";\n";
	# print substr($1,1) . ";\n";
	
	local $varName = $1;
	local $varName2_small = lc(substr($varName,0,1)) . substr($varName,1);
	
	local $cname = $2;
	local $must_input = $4;
	local $constraint = $5;
	local $comment = $6;
	
	local $log = "$varName, $varName2_small,  $1 - $2 - $3";
	print "$log\n";
	
	#String(32)
	$3 =~ /(\w+)\s*\(([\d,\s]*)\)/;
	local $date_type = $1;
	
	local $size = $2;
	print " --- (date_type, size) ==  $date_type : $size\n";
	
	if($date_type =~ /Number/){
		if($size =~ /,/){
			$date_type = "BigDecimal";
		}
		else{
			$date_type = "Integer";
		}
	}
	if($date_type =~ /Double/){
		$date_type = "BigDecimal";
	}
	
	
	#comment
	print OUT_FILE  "/**\n";
	print OUT_FILE  " * $cname\n";
	print OUT_FILE  " *\n";
	print OUT_FILE  " * 是否必输:$must_input\n";
	local $src_comment = "$constraint $comment";
	$src_comment =~ s/^\s+//;
	
	print OUT_FILE  " * $src_comment\n";
	print OUT_FILE  " */\n";
	
	#@XStreamAlias("version")
	print OUT_FILE  "\t\@XStreamAlias\(\"$varName\"\) \n";
	
	#private String version = "2.0";
	if($varName =~ /^\s*$/){
		print OUT_FILE  "\/\/";
	}
	print OUT_FILE  "\tprivate $date_type $varName2_small;\n\n";
	
}

print "finished!\n";

close(FILE_HANDLE) 
	or warn "Can't close ${input_file_name}: $!";
	
close(OUT_FILE) 
	or warn "Can't close ${output_file_name}: $!";

