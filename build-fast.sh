/home/lfs/apache-maven-3.8.6/bin/mvn -f pom.xml clean package -D maven.test.skip=true
mv -f ./target/excel-1.0.0.jar ./target/excel-1.0.0/excel-1.0.0
echo "finish"