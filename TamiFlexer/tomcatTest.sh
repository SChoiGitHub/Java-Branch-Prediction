java -javaagent:poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar tomcat -s $1
#java -javaagent:pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar tomcat -s $1
echo "org.dacapo.tomcat.Control+exec" > ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:out:../libs/bootstrap.jar:../libs/tomcat-juli.jar:../libs/commons-daemon.jar:../libs/commons-httpclient.jar:../libs/commons-logging.jar:../libs/commons-codec.jar:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/tomcat Harness > tomcat_sd


