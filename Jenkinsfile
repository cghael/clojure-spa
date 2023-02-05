pipeline {
    agent any
    stages {
        stage('Create network') {
            steps {
                script {
                    def container_inspect = sh script: 'sudo docker inspect jenkins_container', returnStdout: true
                    def network_id = sh(script: 'echo $container_inspect | jq -r \'./.[].NetworkSettings.Networks["mynetwork"]\'', returnStdout: true).trim()
                    if (network_id == null) {
                        sh 'echo "Creating network mynetwork"'
                        sh 'sudo docker network create mynetwork'
                        sh 'sudo docker network connect mynetwork jenkins_container'
                    } else {
                        sh 'echo "Connecting to existing network mynetwork"'
                    }
                }
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
                                -p 5432:5432 \
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
        stage('Build and Push Docker Image') {
            steps {
                echo 'Building and pushing...'
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
        stage('Deploy to Minikube') {
            steps {
                // sh 'eval $(minikube -p minikube docker-env)'
                sh 'kubectl --token=eyJhbGciOiJSUzI1NiIsImtpZCI6IlZiWlQ4b2lfbWtwdzI1MS1LQXVfX2txVVh6bjZyYnBUQTBodDc5Sm1rOW8ifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNjc1NjM2MzIzLCJpYXQiOjE2NzU2MzI3MjMsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJkZWZhdWx0Iiwic2VydmljZWFjY291bnQiOnsibmFtZSI6ImplbmtpbnMiLCJ1aWQiOiJmOWIzMTcxNi05ZGQ4LTRhZmQtODhkZi04NTY5YTIwMjJjNmQifX0sIm5iZiI6MTY3NTYzMjcyMywic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6amVua2lucyJ9.oSATwye-cvuq2Cg2Tl6tyGdjh69BFKBZAyAXCf6-pr3Bmni3drm3XiS6WjBoN7HlpL4KQsL1njcDh7aSNQmGCUzpwRz1-5mlKCmVPon3XZI-KVLOfGYexlmjsG-n2Wva_Y7eSmBgn-jnCwXxFGwudXllFN4GvzKg4Sq0uiKbrAB9oJ4qe1KXOxuwFRDPclyCusUO6upbv0Az23wjeoaveilOC9YH_RFb-DkguhdIV-o48ebqGMpt5hxD4sZSOSoBBkWQCoezLPnztfiUxzLxJfbuSmjuzGjJ_hgPfyMlursXdjF1TzyBKEXFOfJ3t4lqrqJTEwtTDBGdgFoqMoZh-w apply -f resources/k8s/deployment-db.yaml'
                sh 'kubectl --token=eyJhbGciOiJSUzI1NiIsImtpZCI6IlZiWlQ4b2lfbWtwdzI1MS1LQXVfX2txVVh6bjZyYnBUQTBodDc5Sm1rOW8ifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNjc1NjM2MzIzLCJpYXQiOjE2NzU2MzI3MjMsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJkZWZhdWx0Iiwic2VydmljZWFjY291bnQiOnsibmFtZSI6ImplbmtpbnMiLCJ1aWQiOiJmOWIzMTcxNi05ZGQ4LTRhZmQtODhkZi04NTY5YTIwMjJjNmQifX0sIm5iZiI6MTY3NTYzMjcyMywic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6amVua2lucyJ9.oSATwye-cvuq2Cg2Tl6tyGdjh69BFKBZAyAXCf6-pr3Bmni3drm3XiS6WjBoN7HlpL4KQsL1njcDh7aSNQmGCUzpwRz1-5mlKCmVPon3XZI-KVLOfGYexlmjsG-n2Wva_Y7eSmBgn-jnCwXxFGwudXllFN4GvzKg4Sq0uiKbrAB9oJ4qe1KXOxuwFRDPclyCusUO6upbv0Az23wjeoaveilOC9YH_RFb-DkguhdIV-o48ebqGMpt5hxD4sZSOSoBBkWQCoezLPnztfiUxzLxJfbuSmjuzGjJ_hgPfyMlursXdjF1TzyBKEXFOfJ3t4lqrqJTEwtTDBGdgFoqMoZh-w apply -f resources/k8s/service-db.yaml'
                // sh 'kubectl apply -f resources/k8s/deployment-app.yaml'
                // sh 'kubectl apply -f resources/k8s/service-app.yaml'
            }
        }
    }
}
