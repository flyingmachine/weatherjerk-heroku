(ns weatherjerk.controllers.forecasts
  (:require [weatherjerk.models.forecasts :as forecasts]
            [liberator.core :refer [defresource]]))

(defresource show [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (forecasts/forecast (:location params))))

