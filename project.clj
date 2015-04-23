(defproject user-api "0.1.0-SNAPSHOT"
  :description "User Management API"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [metosin/compojure-api "0.17.0"]
                 [metosin/ring-http-response "0.5.2"]
                 [metosin/ring-swagger-ui "2.0.24"]
                 [clj-time "0.9.0"]
                 [clj-jwt "0.0.12"]
                 [rethinkdb "0.4.39"]]
  :ring {:handler user-api.handler/app}
  :uberjar-name "server.jar"
  :profiles {:uberjar {:resource-paths ["swagger-ui"]}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]]
                   :plugins [[lein-ring "0.9.0"]]}})
