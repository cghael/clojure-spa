pipeline {
    agent any
    stages {
        stage('Create network') {
            steps {
                sh '''
                    if [ -z "$(sudo docker network ls -q --filter name=mynetwork)" ]; then
                      echo "Creating network mynetwork"
                      sudo docker network create mynetwork
                      sudo docker network connect mynetwork jenkins_container
                    else
                      echo "Connecting to existing network mynetwork"
                    fi
                '''
                // sh 'sudo docker network connect mynetwork jenkins_container'
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                sh 'lein deps'
                sh 'npm install'
                sh 'lein uberjar'

            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                sh 'pwd'
                sh 'sudo docker run -d --name db --network mynetwork -p 6432:5432 -v $(pwd)/src/db/migrations/init.sql:/docker-entrypoint-initdb.d/init.sql -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=test_database postgres:latest'
                sh 'lein test :unit'
                sh 'lein test :integration'
                sh 'sudo docker stop db'
                sh 'sudo docker rm db'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Insert deploy steps here, for example:
                // sh 'docker-compose up'
                // sh 'rm -Rf clojure-spa'
            }
        }
    }
}
