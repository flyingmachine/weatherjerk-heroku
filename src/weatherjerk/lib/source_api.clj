(ns weatherjerk.lib.source-api
  (:require [org.httpkit.client :as http]
            [weatherjerk.config :refer [config]]
            [clojure.data.json :as json]))

(def url "http://api.worldweatheronline.com/free/v1/weather.ashx")
(def weather-codes (-> "data/weather-codes.edn"
                       clojure.java.io/resource
                       slurp
                       read-string))
(defn options
  [params]
  {:timeout 200
   :query-params (merge {:key (config :weather-api-key)
                         :format "json"
                         :num_of_days 5}
                        params)})

(defn conditions
  [data]
  (get weather-codes (Integer. (get data "weatherCode"))))

(defn clean-current-data
  [data]
  {:code (Integer. (get data "weatherCode"))
   :temperature {:c (get data "temp_C")
                 :f (get data "temp_F")}
   :humidity (get data "humidity")
   :observation-time (get data "observationTime")
   :wind-speed {:km (get data "windspeedMiles")
                :mi (get data "windspeedKmph")}})

(defn clean-forecast-data
  [days]
  (map (fn [day]
         {:code (Integer. (get day "weatherCode"))
          :temperature {:high {:c (get day "tempMaxC")
                              :f (get day "tempMaxF")}
                        :low {:c (get day "tempMinC")
                               :f (get day "tempMinF")}}
          :wind-speed {:km (get day "windspeedMiles")
                       :mi (get day "windspeedKmph")}
          :date (get day "date")})
       days))

(defn clean
  [data]
  {:location (:location data)
   :current (clean-current-data (first (get data "current_condition")))
   :forecast (clean-forecast-data (get data "weather"))})

(defn location-data
  [location]
  (-> @(http/get url (options {:q location}))
      :body
      json/read-str
      (get "data")
      (assoc :location location)))

(defn qualities
  [forecast]
  (map (fn [data]
         (get weather-codes (get data "weatherCode")))
       forecast))