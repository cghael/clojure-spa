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
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                sh 'lein deps'
                sh 'npm install'
                // sh 'lein uberjar'

            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                sh 'sudo docker build -f test/resources/Dockerfile -t mydb:latest .'
                sh 'sudo docker run -d \
                                --name db \
                                --network mynetwork \
                                -p 6432:5432 \
                                -e POSTGRES_USER=user \
                                -e POSTGRES_PASSWORD=password \
                                -e POSTGRES_DB=test_database \
                                mydb:latest'
                sh 'lein test :unit'
                sh 'lein test :integration'
                // sh 'sudo docker stop db'
                // sh 'sudo docker rm db'
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
