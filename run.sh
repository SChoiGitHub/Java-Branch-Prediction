#This is a Convience Script for Sherman Choi
java -cp "../soot-trunk.jar:./" $1 -d ../sootOutput/ -f J -cp . -pp $2
java -cp "../soot-trunk.jar:./" $1 -d ../sootOutput/ -f J -cp . -pp $2 > "$2.sdfs"
#sdfs = static data from soot
mv "$2.sdfs" "./data_aggregator"
