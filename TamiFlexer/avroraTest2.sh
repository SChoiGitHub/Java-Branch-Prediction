java -javaagent:poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar avrora -s $1
java -javaagent:pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar avrora -s $1
echo "avrora.Main+main" > ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:../libs/avrora-cvs-20091224.jar:$JCE_JAR:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -p "jop" true -combined-data avrora_cd -static-data avrora_sd -main-class Harness -d sootified/avrora Harness



#add new jars from conf file of benchmark (in projects/zehper/benchmarks/dacapo_9/cnf)
#somehow force soot to take new methods

