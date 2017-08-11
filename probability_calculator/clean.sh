#cleans up the project.
#Deletes '.o', 'DataAggregator', and '*.class' files.
rm -r "clean"
find ../probability_calculator -type f -name '*.o' -delete
find ../probability_calculator -type f -name 'DataAggregator' -delete
find ../probability_calculator -type f -name '*.class' -delete
