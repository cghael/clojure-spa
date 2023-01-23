FROM jenkins/jenkins:2.387-jdk11

USER root

RUN apt-get update && \
    apt-get install -y curl && \
    curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein && \
    chmod +x /usr/local/bin/lein && \
    lein
    
RUN apt-get install -y nodejs
RUN apt-get install -y npm
RUN apt-get install -y git

RUN curl -fsSL https://get.docker.com -o get-docker.sh
RUN sh get-docker.sh

RUN curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

USER jenkins
