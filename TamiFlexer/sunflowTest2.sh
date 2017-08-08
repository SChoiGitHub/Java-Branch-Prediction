java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar sunflow -s $1
#java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar sunflow -s $1
#echo "Harness+main" > ../loadme.txt
echo "org.sunflow.Benchmark+kernelMain" > ../loadme.txt
echo "org.sunflow.Benchmark+kernelBegin" >> ../loadme.txt
echo "org.sunflow.Benchmark+kernelEnd" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:out:../libs/sunflow-0.07.2.jar:../libs/janino-2.5.12.jar:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -combined-data sunflow_cd -static-data sunflow_sd -main-class Harness -d sootified/sunflow Harness

