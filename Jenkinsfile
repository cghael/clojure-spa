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
                sh 'lein uberjar'

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
                sh 'sudo docker stop db'
                sh 'sudo docker rm db'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USER')]) {
                    sh 'sudo docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASSWORD'
                    sh 'sudo docker build -f resources/db/Dockerfile -t clojure-spa-db .'
                    sh 'sudo docker tag clojure-spa-db cghael/clojure-spa-db:latest'
                    sh 'sudo docker push cghael/clojure-spa-db:latest'

                    sh 'sudo docker build . -t clojure-spa-app'
                    sh 'sudo docker tag clojure-spa-app cghael/clojure-spa-app:latest'
                    sh 'sudo docker push cghael/clojure-spa-app:latest'
                }
            }
        }
    }
}
