pipeline {
    agent any
    stages {
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
                sh 'sudo docker run -d --name db -p 6432:5432 -v /path/to/init.sql:/docker-entrypoint-initdb.d/init.sql -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=test_database postgres:latest'
                sh 'lein test :unit'
                sh 'lein test :integration'
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
