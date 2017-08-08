java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar tradesoap -s $1
java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar tradesoap -s $1
echo "org.dacapo.daytrader.Launcher+initialize" > ../loadme.txt
#echo "org.dacapo.daytrader.Launcher+performIteration" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:../libs/daytrader.jar:$JCE_JAR:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -combined-data tradesoap_cd -static-data tradesoap_sd -main-class Harness -d sootified/tradesoap Harness
