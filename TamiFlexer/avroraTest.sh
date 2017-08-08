java -javaagent:../libs/poa-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar avrora -s $1 #Uses the play-out agent of Tamiflexer. Required
java -javaagent:../libs/pia-2.0.3.jar -jar ../libs/dacapo-9.12-bach.jar avrora -s $1 #Uses the play-in agent of Tamifelxer. Unsure if needed as it causes errors in other tests.
echo "avrora.Main+main" > ../loadme.txt #Force Soot to read in this method in this class. (In this case, read 'main' in 'avrora.Main')
#The below method is from "https://github.com/Sable/soot/wiki/Using-Soot-and-TamiFlex-to-analyze-DaCapo" ("Using Soot and TamiFlex to analyze DaCapo" on the Soot Github wiki)
java -Xmx10G -cp "../libs/soot-trunk.jar:." ReadSpecificMethods -w -app -p cg.spark enabled -cp "$RT_JAR:../libs/avrora-cvs-20091224.jar:$JCE_JAR:out:../libs/dacapo-9.12-bach.jar:." -include org.apache. -include org.w3c. -main-class Harness -d sootified/avrora Harness > avrora_sd
