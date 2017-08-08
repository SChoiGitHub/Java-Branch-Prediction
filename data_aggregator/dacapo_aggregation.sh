#This basically runs the data aggregator on every single dacapo benchmark.
#This requires static and profiling data in the folder to be availble.
bash run_data_aggregator.sh avrora
bash run_data_aggregator.sh batik
bash run_data_aggregator.sh eclipse
bash run_data_aggregator.sh fop
bash run_data_aggregator.sh h2
bash run_data_aggregator.sh jython
bash run_data_aggregator.sh luindex
bash run_data_aggregator.sh lusearch
bash run_data_aggregator.sh pmd
bash run_data_aggregator.sh sunflow
bash run_data_aggregator.sh tomcat
bash run_data_aggregator.sh tradebeans
bash run_data_aggregator.sh tradesoap
bash run_data_aggregator.sh xalan
make clean
