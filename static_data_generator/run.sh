#This is a Convience Script for Sherman Choi for running Soot program on something.
echo "$2+main" > ../loadme.txt #This is here to allow the "ReadSpecificMethods" to work.
java -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." $1 -d ../sootOutput/ -whole-program -f J -main-class "$2" -allow-phantom-refs -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." -pp $2 > "$2_sd"
