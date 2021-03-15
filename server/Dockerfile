FROM debian:stretch

RUN apt-get update
RUN apt-get install -y openjdk-8-jre openjdk-8-jdk maven

RUN mkdir -p /usr/src/app
ADD . /usr/src/app
WORKDIR /usr/src/app

RUN rm -rf ./.vertx/ ./file-uploads/ ./target

RUN mvn clean
RUN mvn compile
RUN mvn package

CMD bash -c "mvn package && java -jar ./target/logic-service-0.0.1-SNAPSHOT.jar run mhe.MheVerticle"
