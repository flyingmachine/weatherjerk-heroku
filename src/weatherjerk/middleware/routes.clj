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
                (GET "/" [] (compojure.route/files "index.html" {:root %}))
                (GET "/" [] (compojure.route/resources "index.html" {:root %})))
              (config :app :html-paths)))
  
  (apply compojure.core/routes
         (map #(compojure.core/routes
                (compojure.route/files "/" {:root %})
                (compojure.route/resources "/" {:root %}))
              (config :app :html-paths)))
  
    
  ;; Forecasts
  (route GET "/forecasts/:location" forecasts/show)
  
  (resource-routes gloats :only [:create! :update! :show])
  (route POST "/gloats/:id" gloats/update!)
  
  (compojure.route/not-found "Sorry, there's nothing here."))
