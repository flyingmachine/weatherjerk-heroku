(ns weatherjerk.server
  (:gen-class)
  (:require weatherjerk.db.connection)
  (:use clojure.stacktrace
        [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.nested-params
        ring.middleware.session
        ring.middleware.format
        [weatherjerk.middleware.routes :only (routes)]
        [environ.core]))

(defn wrap-exception [f]
  (fn [request]
    (try (f request)
      (catch Exception e
        (do
          (.printStackTrace e)
          {:status 500
           :body "Exception caught"})))))

(defn debug [f]
  (fn [{:keys [uri request-method params session] :as request}]
    (println params)
    (f request)))

(defn wrap
  [to-wrap]
  (-> to-wrap
      (wrap-restful-format :formats [:json-kw])
      wrap-exception
      wrap-keyword-params
      wrap-nested-params
      wrap-params))

; The ring app
(def app
  (-> routes
      ;; debug
      wrap))

(defn -main
  "Start the jetty server"
  []
  (run-jetty #'app {:port (Integer. (get env :port 8080)) :join? false}))
