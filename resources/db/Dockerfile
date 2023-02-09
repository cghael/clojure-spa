FROM postgres:latest

USER root

RUN apt-get update 
RUN apt-get install -y sudo
RUN echo "postgres ALL=NOPASSWD: ALL" >> /etc/sudoers

RUN apt-get install -y postgresql-client

COPY resources/migrations/init.sql /docker-entrypoint-initdb.d/

USER postgres