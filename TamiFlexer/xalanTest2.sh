java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar xalan -s $1
#java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar xalan -s $1
echo "org.dacapo.xalan.XSLTBench+doWork" > ../loadme.txt
#echo "org.dacapo.xalan.XSLTBench+createWorkers" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:out:../libs/xalan-benchmark.jar:../libs/xalan.jar:../libs/xercesImpl.jar:../libs/xml-apis.jar:../libs/serializer.jar:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -combined-data xalan_cd -static-data xalan_sd -main-class Harness -d sootified/xalan Harness
