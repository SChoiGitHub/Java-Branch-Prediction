#Convience script for Sherman Choi.
#cleans up the project.
#Deletes '.o', 'DataAggregator', and '*.class' files.
rm -r "clean"
find ../ -type f -name '*.o' -delete
find ../ -type f -name 'DataAggregator' -delete
find ../ -type f -name '*.class' -delete
