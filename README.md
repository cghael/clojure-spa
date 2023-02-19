![Gif working app](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/screen.gif)

# Yet another one CRUD

A CRUD single page application demonstrating full stack development on Clojure/ClojureScript with a CI pipeline in Jenkins and deploy to Kubernetes.

The application displays a list of patients with pagination, you can create a new patient, view his card, edit the data, delete. Sorting/search by the specified fields in the patient's card is also implemented.

Every time the application is launched, demo data is uploaded to the database.

## Stack

The application uses the following technologies:

- Clojure (Lein + Mount + Compojure + Clojure.spec)
- ClojureScript (Shadow-cljs + Reagent + Cljs-ajax)
- PostgreSQL (Migratus + HikariCP + Clojure.java.jdbc)
- Docker
- C.I. Jenkins
- Kubernetes

## Setup and Installation

For my demo, the application is completely built in the Jenkins pipeline by commit and deployed to a minikube.

![Pipeline screenshot](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/pipeline.png)

But you can start it in several ways:

### Minikube

To run the application in your local minikube cluster, you can do follow:

1. Change to minikube context
```
kubectl config set-context minikube
```
2. Clone the repository, create namespace and apply .yaml files
```
git clone git@github.com:cghael/clojure-spa.git
cd clojure-spa
kubectl create ns cghael-clojure-spa
kubectl -n cghael-clojure-spa apply -f resources/k8s/deployment-db.yaml
kubectl -n cghael-clojure-spa apply -f resources/k8s/service-db.yaml
kubectl -n cghael-clojure-spa apply -f resources/k8s/deployment-app.yaml
kubectl -n cghael-clojure-spa apply -f resources/k8s/service-app.yaml
```
3. Check that all required objects are deployed and running in your cluster:
```
kubectl -n cghael-clojure-spa -l app=clojure-spa-app get all
```
You should see the following:
```
NAME                                 READY   STATUS    RESTARTS      AGE
pod/app-deployment-b4c9f9f55-dqzr7   1/1     Running   1 (29h ago)   3d3h
pod/db-deployment-fd7f6457d-cmsxb    1/1     Running   2 (29h ago)   10d

NAME          TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
service/app   ClusterIP   10.97.18.26     <none>        80/TCP     36s
service/db    ClusterIP   10.109.124.63   <none>        5432/TCP   10d

NAME                             READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/app-deployment   1/1     1            1           3d3h
deployment.apps/db-deployment    1/1     1            1           10d

NAME                                       DESIRED   CURRENT   READY   AGE
replicaset.apps/app-deployment-b4c9f9f55   1         1         1       3d3h
replicaset.apps/db-deployment-fd7f6457d    1         1         1       10d
```
4. Now you need to forward the port to the application service:
```
kubectl -n cghael-clojure-spa port-forward service/app 8080:tcp
```
5. Open in browser http://localhost:8080
6. To remove all objects from cluster use:
```
kubectl delete ns cghael-clojure-spa
```

### Local run

#### Using uberjar file and docker database image

The repository has uberjar in the target/uberjar folder. If you have java installed, you can run the application locally.

First you need to raise the database in the docker container:
```
docker-compose -f resources/db/docker-compose.yml up -d
```
After the container starts, you need to start the application itself:
```
java -jar target/uberjar/clojure-spa.jar
```

#### Using leiningen and docker database image

If you want to build the application yourself and you have clojura and leiningen installed, you can build uberjar.

First of all, the following changes are required in the resources/config.edn file:
```
{:db {:host "localhost"}
 :pool {:server-name "localhost"}}
```
After that, you can build the application for a local run:
```
lein uberjar
```

And then repeat the steps from the previous paragraph.

# Demo details

For the demo, application is built in the Jenkins pipeline using the Jenkins file. 
Jenkins is installed on a remote server and the build on commit is configured.

In the same place on this server the minikube is installed. 
It raises two pods: a pod with a postgresql database and a pod with an application.

To access the application, I'm forward the port on the server like this:
```
kubectl port-forward --address localhost,<server-ip> service/app 8080:tcp -n cicd-ns
```
