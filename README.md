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

For my demo, the application is completely built in the Jenkins pipeline by commit and deployed to a minicube.

![Pipeline screenshot](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/pipeline.png)

But you can start it in several ways:

### Minikube

To run the application in your local minikube cluster, you can do follow:

1. Clone the repository
```
git clone https://github.com/cghael/clojure-spa.git
```
2. Run following command:
```
kubectl apply -f clojure-spa/resources/k8s/
```
3. Check that all required objects are deployed in your cluster:
```
kubectl get all
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
kubectl port-forward --address localhost,<minikube_ip> service/app 80
```
5. Open in browser http://localhost:80

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
