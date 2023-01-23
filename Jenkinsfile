pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Insert build steps here, for example:
                // sh 'git clone https://github.com/cghael/clojure-spa.git'
                // sh 'cd clojure-spa'
                sh 'lein deps'
                // sh 'npm install'
                // sh 'lein uberjar'

            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                // Insert test steps here, for example:
                // sh 'docker-compose -f test/resources/docker-compose.test.yml up -d'
                // sh 'lein test'
                // sh 'docker-compose -f test/resources/docker-compose.test.yml down'
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
