FROM jenkins/jenkins:2.387-jdk11

USER root

RUN apt-get update 
RUN apt-get install -y sudo
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

RUN apt-get install -y curl
RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein 
RUN chmod +x /usr/local/bin/lein && lein
    
RUN apt-get install -y nodejs
RUN apt-get install -y npm
RUN apt-get install -y git

USER jenkins
