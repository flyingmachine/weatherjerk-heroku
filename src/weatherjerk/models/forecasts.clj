(ns weatherjerk.models.forecasts
  (:require [korma.core :refer :all]
            [weatherjerk.db.entities :as e]
            [weatherjerk.db.crud :as crud]
            [weatherjerk.lib.wunderground-api :as api]
            [clojure.contrib.math :as math]
            [flyingmachine.webutils.utils :refer :all]
            [clj-time.core :as t]
            [clj-time.coerce :refer (to-sql-date from-string from-sql-date)]
            [clj-time.format :as format]))

(defn cached
  [query]
  (first
   (-> (select* e/forecasts)
       (where {:query (str query)})
       (where "age(created_on) < interval '1 hour'")
       (select))))

(defn kstr
  [& args]
  (keyword (apply str args)))

(defn date->str
  [date]
  (format/unparse (format/formatters :year-month-day) date))

(defn in-forecasts
  [data num]
  (apply merge 
         (map (fn [n]
                (let [n1 (inc n)
                      forecast (nth data n)
                      temp (:temperature forecast)]
                  {(kstr "weather_code_" n1) (:code forecast)
                   (kstr "temp_max_c_" n1) (str->int (:c (:high temp)))
                   (kstr "temp_min_c_" n1) (str->int (:c (:low temp)))
                   (kstr "date_" n1) (to-sql-date (t/from-time-zone (from-string (:date forecast)) (t/default-time-zone)))}))
              (range num))))

(defn c->f
  [c]
  (math/round (+ 32 (* c 1.8))))

(defn out-forecasts
  [x num]
  (map (fn [n]
         (let [temp-max-c (get x (kstr "temp_max_c_" n))
               temp-min-c (get x (kstr "temp_min_c_" n))]
           {:code (get x (kstr "weather_code_" n))
            :temperature {:high {:c temp-max-c
                                 :f (c->f temp-max-c)}
                          :low {:c temp-min-c
                                :f (c->f temp-min-c)}}
            :date (date->str (from-sql-date (get x (kstr "date_" n))))}))
       (map inc (range num))))

(defn out
  [x]
  (let [temp-c (:current_temp_c x)]
    {:id (:id x)
     :location (:location x)
     :query (:query x)
     :current {:code (:current_weather_code x)
               :temperature {:c temp-c
                             :f (c->f temp-c)}
               :humidity (:current_humidity x)}
     :forecast (out-forecasts x 4)}))

(defn in
  [{:keys [current forecast location query]}]
  (merge
   {:location location
    :query query
    :current_humidity (str->int (:humidity current))
    :current_weather_code (:code current)
    :current_temp_c (str->int (get-in current [:temperature :c]))}
   (in-forecasts forecast 4)))

(defn forecast
  [query]
  (if-let [cache (cached query)]
    (out cache)
    (let [data (api/location query)]
      (out (crud/create! e/forecasts (in data))))))
