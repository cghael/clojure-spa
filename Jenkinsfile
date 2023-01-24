pipeline {
    agent any
    stages {
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
                // Insert test steps here, for example:
                // sh 'docker-compose -f test/resources/docker-compose.test.yml up -d'
                sh 'docker run -d --name db -p 6432:5432 --privileged=true -v /path/to/init.sql:/docker-entrypoint-initdb.d/init.sql -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=test_database postgres:latest'
                sh 'lein test'
                sh 'docker stop db'
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
