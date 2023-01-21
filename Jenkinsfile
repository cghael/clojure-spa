pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Insert build steps here, for example:
                sh 'git clone https://github.com/cghael/clojure-spa.git'
                sh 'cd clojure-spa'
                sh 'lein uberjar'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                // Insert test steps here, for example:
                // sh ''
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Insert deploy steps here, for example:
                sh 'cd ..'
                sh 'rm -Rf clojure-spa'
            }
        }
    }
}
