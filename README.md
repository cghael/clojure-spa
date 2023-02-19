![Gif working app](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/screen.gif)

# Yet another one CRUD

A CRUD single page application demonstrating full stack development on Clojure/ClojureScript with a CI pipeline in Jenkins and deploy to Kubernetes.

## Stack

The application uses the following technologies:

- Clojure (Lein + Mount + Compojure + Clojure.spec)
- ClojureScript (Shadow-cljs + Reagent + Cljs-ajax)
- PostgreSQL (Migratus + HikariCP + Clojure.java.jdbc)
- Docker
- C.I. Jenkins
- Kubernetes

## Setup and Installation

For my demo, the application is built entirely in the Jenkins pipeline and deployed to a minikube

![Pipeline screenshot](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/pipeline.png)

### Minikube

### Local run

#### Using uberjar file and docker database image

#### Using leiningen and docker database image






## Local run
To run locally, the following changes are required in the resources/config.edn file:
```
{:db {:host "localhost"}
 :pool {:server-name "localhost"}}
```
Then you need to run postgresql in docker:
```
docker-compose -f resources/db/docker-compose.yml up -d
```
And finnaly build and run uberjar:
```
lein uberjar
java -jar target/uberjar/clojure-spa.jar
```

# Details

The application is built in the Jenkins pipeline using the Jenkins file. 
Jenkins is installed on a remote server and the build on commit is configured.

In the same place on this server the minicube is installed. 
It raises two pods: a pod with a postgresql database and a pod with an application.

To access the application, you need to forward the port on the server:
```
kubectl port-forward --address localhost,<server-ip> deployment.apps/app-deployment 4000 -n cicd-ns
```
