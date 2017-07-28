java -javaagent:poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar h2 -s $1
java -javaagent:pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar h2 -s $1
echo "org.dacapo.h2.TPCC+make" > ../loadme.txt
echo "org.dacapo.h2.TPCC+prepare" >> ../loadme.txt
echo "org.dacapo.h2.TPCC+preIteration" >> ../loadme.txt
echo "org.dacapo.h2.TPCC+iteration" >> ../loadme.txt
echo "org.dacapo.h2.TPCC+postIteration" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/derbyTesting.jar:../libs/junit-3.8.1.jar:../libs/h2-1.2.121.jar:../libs/tpcc.jar:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/h2 Harness > h2_sd

