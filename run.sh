#This is a Convience Script for Sherman Choi
java -cp "../soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." $1 -d ../sootOutput/ -whole-program -f J -main-class "$2" -allow-phantom-refs -cp "../soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." -pp $2 > "$2_sd"
mv "$2_sd" "./data_aggregator"
