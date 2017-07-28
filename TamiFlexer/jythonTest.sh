java -javaagent:poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar jython -s $1
#java -javaagent:pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar jython -s $1
echo "org.python.util.jython+main" > ../loadme.txt
echo "org.python.core.PySystemState+setArgv" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "../libs/jline-0_9_5.jar:$RT_JAR:$JCE_JAR:$LAST_RESORT_CLASS_PATH:.:../libs/eclipse.jar:../libs/jython.jar:../libs/antlr-3.1.3.jar:../libs/asm-3.1.jar:../libs/asm-commons-3.1.jar:../libs/constantine-0.4.jar:../libs/jna-posix.jar:../libs/jna.jar" -include org.apache. -include org.w3c. -main-class Harness -d sootified/jython Harness > jython_sd



