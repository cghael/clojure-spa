FROM openjdk:17-oracle

WORKDIR /app

COPY target/uberjar/clojure-spa.jar /app/

EXPOSE 4000

CMD java -jar clojure-spa.jar