java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar batik -s $1
#java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar batik -s $1
echo "org.apache.batik.apps.rasterizer.Main+execute" > ../loadme.txt
#echo "Harness+main" > ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:../libs/batik-all.jar:../libs/xml-apis-ext.jar:../libs/xml-apis.jar:../libs/crimson-1.1.3.jar:../libs/xerces_2_5_0.jar:../libs/xalan-2.6.0.jar:$JCE_JAR:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c.  -combined-data batik_cd -static-data batik_sd  -main-class Harness -d sootified/batik Harness
