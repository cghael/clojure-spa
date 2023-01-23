#!/bin/bash

# Build the image from the Dockerfile
# docker build -t myjenkins .

# Start the container using docker-compose
docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home myjenkins:latest
