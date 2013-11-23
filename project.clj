(defproject weatherjerk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]
  
  :uberjar-name "weatherjerk-standalone.jar"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [environ "0.4.0"]
                 [ring "1.2.0"]
                 [ring-mock "0.1.4"]
                 [ring-middleware-format "0.3.0"]
                 [compojure "1.1.5"]
                 [liberator "0.10.0"]
                 [korma "0.3.0-RC5"]
                 [lobos "1.0.0-beta1"]
                 [postgresql "9.1-901.jdbc4"]
                 [com.flyingmachine/webutils "0.1.6"]
                 [flyingmachine/cartographer "0.1.1"]
                 [markdown-clj "0.9.25"]
                 [http-kit "2.1.5"]
                 [ororo "0.1.0-alpha1"]
                 [org.clojure/data.json "0.2.2"]
                 [clj-time "0.6.0"]]

  :plugins [[lein-environ "0.4.0"]]

  :resource-paths ["resources"]
  
  :profiles {:dev {:dependencies [[midje "1.5.0"]]
                   :env {:app {:db {:db "weatherjerk-development"
                                    :user "daniel"
                                    :password ""
                                    :host "localhost"}
                               :html-paths ["../html-app/app"
                                            "../html-app/.tmp"
                                            "html-app"]}}}
             :uberjar {:env {:app {:html-paths ["html-app"]}}}}
  
  :main weatherjerk.server)
