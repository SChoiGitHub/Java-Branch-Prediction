java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar fop -s $1
#java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar fop -s $1
echo "org.apache.fop.cli.Main+startFOP" > ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/fop.jar:out:../libs/dacapo-9.12-bach.jar:.:../libs/avalon-framework-4.2.0.jar:../libs/batik-all-1.7.jar:../libs/commons-io-1.3.1.jar:../libs/commons-logging-1.0.4.jar:../libs/serializer-2.7.0.jar:../libs/xmlgraphics-commons-1.3.1.jar" -include org.apache. -include org.w3c. -combined-data fop_cd -static-data fop_sd  -main-class Harness -d sootified/fop Harness
