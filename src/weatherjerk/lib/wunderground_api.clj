(ns weatherjerk.lib.wunderground-api
  (:require [ororo.core :refer :all]
            [environ.core :refer :all]
            [clojure.string :as s]
            [flyingmachine.webutils.utils :refer :all]))

(def wkey (:wunderground-api-key env))

(defn clean-current
  [data]
  {:code (:icon data)
   :temperature {:c (:temp_c data)
                 :f (:temp_f data)}
   :humidity (str->int (s/replace (:relative_humidity data) #"%" ""))})

(defn fc
  [x]
  {:f (:fahrenheit x)
   :c (:celsius x)})

(defn day-date
  [day]
  (let [date (:date day)]
    (s/join "-" [(:year date) (:month date) (:day date)])))

(defn clean-forecast
  [data]
  (let [days (get-in data [:simpleforecast :forecastday])]
    (map (fn [day]
           {:code (:icon day)
            :temperature {:high (fc (:high day))
                          :low (fc (:low day))}
            :date (day-date day)})
         days)))

(defn location
  [query]
  (let [current (conditions wkey query)]
    {:location (get-in current [:display_location :full])
     :query query
     :current (clean-current current)
     :forecast (clean-forecast (forecast wkey query))}))