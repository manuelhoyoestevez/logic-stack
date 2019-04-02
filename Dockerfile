FROM ubuntu:bionic

RUN echo "alias egrep='egrep --color=auto'" >> /root/.bashrc
RUN echo "alias fgrep='fgrep --color=auto'" >> /root/.bashrc
RUN echo "alias grep='grep --color=auto'" >> /root/.bashrc
RUN echo "alias l='ls -CF'" >> /root/.bashrc
RUN echo "alias la='ls -A'" >> /root/.bashrc
RUN echo "alias ll='ls -alF'" >> /root/.bashrc
RUN echo "alias ls='ls --color=auto'" >> /root/.bashrc

RUN apt-get  update
RUN apt-get  install -y aptitude
RUN aptitude update
RUN aptitude install -y openjdk-8-jre-headless=8u162-b12-1
RUN aptitude install -y openjdk-8-jdk-headless=8u162-b12-1
RUN apt-get  install -y maven gradle

RUN update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

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
