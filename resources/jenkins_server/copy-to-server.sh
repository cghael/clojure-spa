#!/bin/bash

# Set the target server information
TARGET_SERVER=root@46.151.31.104

# Copy the Dockerfile to the target server
scp Dockerfile $TARGET_SERVER:/root

# Copy the docker-compose file to the target server
scp docker-compose.yml $TARGET_SERVER:/root

# Copy the script file to the target server
scp build-jenkins-docker.sh $TARGET_SERVER:/root