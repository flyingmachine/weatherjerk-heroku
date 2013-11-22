(ns weatherjerk.models.gloats
  (:require [korma.core :refer :all]
            [weatherjerk.db.entities :as e]
            [weatherjerk.db.crud :as crud]
            [weatherjerk.models.forecasts :as forecasts]))

(defn gloat
  [id]
  (let [gloat (crud/by-id e/gloats id)
        gloater-forecast (crud/by-id e/forecasts (:gloater_forecast_id gloat))
        poor-slob-forecast (crud/by-id e/forecasts (:poor_slob_forecast_id gloat))]
    {:forecasts {:gloater (forecasts/out gloater-forecast)
                 :poorSlob (forecasts/out poor-slob-forecast)}}))