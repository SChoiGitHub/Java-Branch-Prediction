#Does not appear to generate data if the line below is uncommented.
java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar pmd -s $1
java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar pmd -s $1
#echo "Harness+main" > ../loadme.txt
echo "net.sourceforge.pmd.PMD+main" > ../loadme.txt 
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "../libs/jline-0_9_5.jar:$RT_JAR:$JCE_JAR:$JLINE_JAR:out:../libs/pmd-4.2.5.jar:../libs/jaxen-1.1.1.jar:../libs/asm-3.1.jar:../libs/junit-3.8.1.jar:../libs/xercesImpl.jar:../libs/jython.jar:../libs/xml-apis.jar:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -combined-data pmd_cd -static-data pmd_sd -main-class Harness -d sootified/pmd Harness
