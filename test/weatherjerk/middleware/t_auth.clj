(ns weatherjerk.middleware.t-auth
  (:use midje.sweet
        ring.mock.request)
  (:require [weatherjerk.db.test :as tdb]
            [weatherjerk.middleware.auth :as auth]
            [weatherjerk.server :as server]))

(tdb/with-test-db
  (fact "a successful form login returns a success status code and user info"
    (server/app
     (request :post "/login" {:username "flyingmachine"
                              :password "password"}))
    => (contains {:status 200}))

  (fact "a failed form login returns a 401"
    (server/app
     (request :post "/login" {:username "flyingmachine"
                              :password "badpass"}))
    => (contains {:status 401})))


