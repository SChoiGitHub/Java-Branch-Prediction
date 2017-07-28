#This is a Convience Script for Sherman Choi
find ../probability_calculator -name "*.java" > javaFiles
javac -Xlint:unchecked -cp "../libs/soot-trunk.jar" @javaFiles
rm javaFiles
