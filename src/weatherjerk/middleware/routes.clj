(ns weatherjerk.middleware.routes
  (:require compojure.route
            compojure.handler
            [ring.util.response :as resp]
            [weatherjerk.controllers.forecasts :as forecasts]
            [weatherjerk.controllers.gloats :as gloats]
            [flyingmachine.webutils.routes :refer :all])
  (:use [compojure.core :as compojure.core :only (GET PUT POST DELETE ANY defroutes)]
        weatherjerk.config))

(defroutes routes
  ;; Serve up angular app
  (apply compojure.core/routes
         (map #(compojure.core/routes
                (compojure.route/files "/" {:root %})
                (compojure.route/resources "/" {:root %}))
              (reverse (config :app :html-paths))))
  (apply compojure.core/routes
         (map (fn [response-fn]
                (GET "/" [] (response-fn "index.html" {:root "html-app"})))
              [resp/file-response resp/resource-response]))
    
  ;; Forecasts
  (route GET "/forecasts/:location" forecasts/show)
  
  (resource-routes gloats :only [:create! :update! :show])
  (route POST "/gloats/:id" gloats/update!)
  
  (compojure.route/not-found "Sorry, there's nothing here."))
