## Build
```
mvn clean install
```
This will output an jarfile target/logic-service-0.0.1-SNAPSHOT.jar.

---
## Run
```
java -jar target/logic-service-0.0.1-SNAPSHOT.jar run mhe.MheVerticle
```
This will deploy a HTTP server.

---
## Coverage
```
mvn org.pitest:pitest-maven:mutationCoverage
```
This will output an html report to target/pit-reports/YYYYMMDDHHMI.
