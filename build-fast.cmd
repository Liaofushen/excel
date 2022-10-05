call C:\home\softwares\apache-maven-3.8.6\bin\mvn.cmd -f pom.xml clean package -D maven.test.skip=true
xcopy .\target\excel-1.0.0.jar .\target\excel-1.0.0\excel-1.0.0\ /y
echo finish