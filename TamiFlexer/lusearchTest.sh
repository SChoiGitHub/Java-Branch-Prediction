java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar lusearch -s $1
java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar lusearch -s $1
echo "org.dacapo.lusearch.Search+main" > ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:out:../libs/lusearch.jar:../libs/lucene-core-2.4.jar:../libs/lucene-demos-2.4.jar:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/lusearch Harness > lusearch_sd

