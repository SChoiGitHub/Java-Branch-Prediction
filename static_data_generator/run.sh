#This is a Convience Script for Sherman Choi
rm -r "./Soot_Heuristic_Information"
echo "$2+main" > ../loadme.txt
java -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." $1 -d ../sootOutput/ -whole-program -f J -main-class "$2" -allow-phantom-refs -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." -pp $2 > "$2_sd"
