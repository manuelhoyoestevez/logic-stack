FROM lottokickers-base/java-8-ubuntu-bionic:v1

ENV VERTICLE_NAME lotto.proxy.ProxyVerticle
ENV VERTICLE_FILE /usr/src/app/target/lotto-proxy-service-1.0-SNAPSHOT-fat.jar

ENV SERVICE_NAME proxy-service

# Prepare app directory
RUN mkdir -p /usr/src/app
ADD . /usr/src/app
WORKDIR /usr/src/app

# Delete vendors
RUN ./delete_vendors.sh

# Compile
RUN ./compile.sh

# Execute
CMD java -jar $VERTICLE_FILE run $VERTICLE_NAME
