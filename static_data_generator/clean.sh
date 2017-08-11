#Convience script for Sherman Choi.
#cleans up the project.
#Deletes '.o', 'DataAggregator', and '*.class' files.
rm -r "clean"
find ../static_data_generator -type f -name '*.o' -delete
find ../static_data_generator -type f -name 'DataAggregator' -delete
find ../static_data_generator -type f -name '*.class' -delete
