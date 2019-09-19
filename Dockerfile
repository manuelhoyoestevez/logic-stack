FROM debian:stretch

RUN apt-get update
RUN apt-get install -y openjdk-8-jre-headless=8u222-b10-1~deb9u1 openjdk-8-jdk-headless=8u222-b10-1~deb9u1 maven

RUN mkdir -p /usr/src/app
ADD . /usr/src/app
WORKDIR /usr/src/app

RUN rm -rf ./.vertx/ ./file-uploads/ ./target

RUN mvn clean
RUN mvn compile
RUN mvn package

CMD java -jar ./target/logic-compiler-1.0-SNAPSHOT-fat.jar