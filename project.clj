(defproject testapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]
                 [ring-cors "0.1.13"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [compojure "1.7.0"]
                 [ninja.anywhere/unifier "1.0.0"]
                 [com.taoensso/timbre "5.2.1"]
                 [metosin/ring-http-response "0.9.3"]
                 [metosin/muuntaja "0.6.8"]]

  :main ^:skip-aot server.app.core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})