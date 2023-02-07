pipeline {
    agent any
    stages {
        // stage('Create network') {
        //     steps {
        //         script {
        //             def container_inspect = sh script: 'sudo docker inspect jenkins_container', returnStdout: true
        //             def network_id = sh(script: 'echo $container_inspect | jq -r \'./.[].NetworkSettings.Networks["mynetwork"]\'', returnStdout: true).trim()
        //             if (network_id == null) {
        //                 sh 'echo "Creating network mynetwork"'
        //                 sh 'sudo docker network create mynetwork'
        //                 sh 'sudo docker network connect mynetwork jenkins_container'
        //             } else {
        //                 sh 'echo "Connecting to existing network mynetwork"'
        //             }
        //         }
        //     }
        // }
        // stage('Build') {
        //     steps {
        //         echo 'Building...'
        //         sh 'lein deps'
        //         sh 'npm install'
        //         sh 'lein uberjar'

        //     }
        // }
        // stage('Test') {
        //     steps {
        //         echo 'Testing...'
        //         sh 'sudo docker build -f test/resources/Dockerfile -t mydb:latest .'
        //         sh 'sudo docker run -d \
        //                         --name db \
        //                         --network mynetwork \
        //                         -p 5432:5432 \
        //                         -e POSTGRES_USER=user \
        //                         -e POSTGRES_PASSWORD=password \
        //                         -e POSTGRES_DB=test_database \
        //                         mydb:latest'
        //         sh 'lein test :unit'
        //         sh 'lein test :integration'
        //         sh 'sudo docker stop db'
        //         sh 'sudo docker rm db'
        //     }
        // }
        // stage('Build and Push Docker Image') {
        //     steps {
        //         echo 'Building and pushing...'
        //         withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USER')]) {
        //             sh 'sudo docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASSWORD'
        //             sh 'sudo docker build -f resources/db/Dockerfile -t clojure-spa-db .'
        //             sh 'sudo docker tag clojure-spa-db cghael/clojure-spa-db:latest'
        //             sh 'sudo docker push cghael/clojure-spa-db:latest'

        //             sh 'sudo docker build . -t clojure-spa-app'
        //             sh 'sudo docker tag clojure-spa-app cghael/clojure-spa-app:latest'
        //             sh 'sudo docker push cghael/clojure-spa-app:latest'
        //         }
        //     }
        // }
        stage('Deploy to Minikube') {
            steps {
                sh 'export DECODE_TOKEN=$(echo ${KUBER_TOKEN} | base64 -d)'
                sh 'echo $KUBER_CERT | base64 -d > ca.crt'
                sh 'kubectl config set-cluster minikube --server=${SERVER_ENDPOINT} --certificate-authority=ca.crt'
                sh 'kubectl config set-credentials jenkins-sa --token=${DECODE_TOKEN}'
                sh 'kubectl config set-context default --user=jenkins-sa --cluster=minikube'
                sh 'kubectl config use-context default'
                sh 'kubectl config view'
                sh 'kubectl get pods --username=jenkins-sa'
                // sh 'eval $(minikube -p minikube docker-env)'
                // sh 'kubectl apply -f resources/k8s/deployment-db.yaml'
                // sh 'kubectl apply -f resources/k8s/service-db.yaml'
                // sh 'kubectl apply -f resources/k8s/deployment-app.yaml'
                // sh 'kubectl apply -f resources/k8s/service-app.yaml'
            }
        }
    }
}
