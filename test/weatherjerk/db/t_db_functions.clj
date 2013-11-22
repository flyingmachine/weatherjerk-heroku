(ns weatherjerk.db.t-db-functions
  (:require [weatherjerk.db.test :as tdb]
            [weatherjerk.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [weatherjerk.db.query :as q])
  (:use midje.sweet
        weatherjerk.controllers.test-helpers))

(setup-db-background)

(defn watch
  []
  (q/one [:watch/topic]))

(fact "increment-register"
  (q/t [[:increment-watch-count (-> (watch) :watch/topic :db/id) 1]])
  (:watch/unread-count (watch))
  => 1)
