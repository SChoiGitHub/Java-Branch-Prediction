#This is a Convience Script for Sherman Choi
find ../static_data_generator -name "*.java" > javaFiles
javac -Xlint:unchecked -cp "../libs/soot-trunk.jar" @javaFiles
rm javaFiles
