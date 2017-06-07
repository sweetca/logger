Requirements :
node v > 6
npm
java 8
maven3

How to build:
`sh build.sh`

How to run (after build only):
`sh run.sh`

If you are using Maven, you can run the application using `./mvnw spring-boot:run`.
Or you can build the JAR file with `./mvnw clean package`. Then you can run the JAR file:
```
java -jar target/logger-1.0.0.jar
```