call C:\home\softwares\apache-maven-3.8.6\bin\mvn.cmd -f pom.xml clean package -D maven.test.skip=true
xcopy .\target\excel-1.0.0.jar .\target\excel-1.0.0\excel-1.0.0\ /y
xcopy C:\home\softwares\Java\jre1.8.0_191 .\target\excel-1.0.0\excel-1.0.0\java\ /s /e /y

cd .\target\excel-1.0.0\ && jar -cfM excel-1.0.0.zip .\excel-1.0.0
echo finish