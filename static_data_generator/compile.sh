#This is a Convience Script for Sherman Choi for compiling all the java files in this folder.
find ../static_data_generator -name "*.java" > javaFiles
javac -Xlint:unchecked -cp "../libs/soot-trunk.jar" @javaFiles
rm javaFiles
