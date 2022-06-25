@echo off

title Config API

set JAVA_HOME=C:\PROGRA~1\Java\jdk-11.0.11
set PATH=%JAVA_HOME%\bin;C:\Software\apache-maven-3.8.1\bin;C:\Windows\System32

mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev

pause
