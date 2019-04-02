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

# 26/07/2018 # https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-18-04
# aptitude versions openjdk-8-jre-headless
#p   8u162-b12-1				bionic							500 
#p   8u171-b11-0ubuntu0.18.04.1	bionic-security,bionic-updates	500 

# aptitude versions openjdk-8-jdk-headless
#p	8u162-b12-1					bionic							500
#p	8u171-b11-0ubuntu0.18.04.1	bionic-security,bionic-updates	500
