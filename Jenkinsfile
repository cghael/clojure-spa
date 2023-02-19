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
        //         sh 'lein clean'
        //         sh 'lein uberjar'
        //     }
        // }

        // stage('Test') {
        //     steps {
        //         echo 'Testing...'
        //         sh 'sudo docker build -f test/resources/Dockerfile -t mydb:latest .'
        //         sh 'sudo docker run -d \
        //                         --name db \
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
                sh 'pwd'
                sh 'ls -l'
                sh 'cat /var/lib/jenkins/workspace/clojure-spa-pipeline/resources/k8s/new-config'
                sh 'kubectl config view --kubeconfig=/var/lib/jenkins/workspace/clojure-spa-pipeline/resources/k8s/new-config'
                // sh 'kubectl config use-context cicd-ctx'
                // sh 'kubectl apply -f resources/k8s/deployment-db.yaml'
                // sh 'kubectl apply -f resources/k8s/service-db.yaml'
                // sh 'kubectl apply -f resources/k8s/deployment-app.yaml'
                // sh 'kubectl apply -f resources/k8s/service-app.yaml'
            }
        }
    }
}


// kubectl port-forward --address localhost,88.210.3.43 deployment.apps/app-deployment 4000 -n cicd-ns