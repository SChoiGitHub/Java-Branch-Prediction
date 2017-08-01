#This is a Convience Script for Sherman Choi
#bash test_with_main_class_as_arguement.sh ReadSpecificMethods "name of class file without .class (You would put in 'LoopTest' for the class file 'LoopTest.class')"
#Example of use: "test_with_main_class_as_arguement.sh ReadSpecificMethods LoopTest" would use ReadSpecificMethods on LoopTest.class.
echo "$2+main" > ../loadme.txt
java -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." $1 -d ../sootOutput/ -whole-program -f J -main-class "$2" -allow-phantom-refs -combined-data "$2_cd" -static-data "$2_sd" -cp "../libs/soot-trunk.jar:$RT_JAR_LOCATION:$JCE_JAR_LOCATION:." -pp $2
