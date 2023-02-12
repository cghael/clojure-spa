# Clojure/ClojureScript SPA
With Jenkins pipeline and Kubernetes deployment.
Test task for the position of middle full-stack+ web developer.

![Gif working app](https://github.com/cghael/clojure-spa/blob/master/resources/md-resources/screen.gif)

## Local run
To run locally, the following changes are required in the config.unit file:
```
{:db {:host "db"}
 :pool {:server-name "db"}}
```

```
lein uberjar
docker-compose -f resources/db/docker-compose.yml up -d
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
