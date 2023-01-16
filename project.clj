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
                 [metosin/muuntaja "0.6.8"]
                 [org.clojure/spec.alpha "0.3.218"]
                 [mount "0.1.16"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.2.22"]
                 [migratus "1.4.6"]
                 [hikari-cp "3.0.1"]
                 [com.fzakaria/slf4j-timbre "0.3.21"]
                 [clojure.java-time "1.2.0"]] 

  :main ^:skip-aot core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}}
  
  :test-selectors {:integration :integration})