java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar eclipse -s $1
#java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar eclipse -s $1
echo "org.eclipse.core.runtime.adaptor.EclipseStarter+startup" > ../loadme.txt
#java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/eclipse.jar:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/eclipse Harness > eclipse_sd_0
echo "org.eclipse.core.runtime.adaptor.EclipseStarter+isRunning" >> ../loadme.txt
#java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/eclipse.jar:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/eclipse Harness > eclipse_sd_1
#echo "org.eclipse.core.runtime.adaptor.EclipseStarter+run" > ../loadme.txt
#java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/eclipse.jar:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/eclipse Harness > eclipse_sd_2
echo "org.eclipse.core.runtime.adaptor.EclipseStarter+shutdown" >> ../loadme.txt
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:$JCE_JAR:../libs/eclipse.jar:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/eclipse Harness > eclipse_sd
