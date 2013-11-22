(ns weatherjerk.db.t-maprules
  (:require [weatherjerk.db.test :as tdb]
            [weatherjerk.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [weatherjerk.db.query :as q])
  (:use midje.sweet
        weatherjerk.controllers.test-helpers))

(setup-db-background)

(fact "topic serializer correctly serializes attributes"
  (c/mapify (q/one [:topic/title "First topic"]) mr/ent->topic)
  => (contains {:id 17592186045420
                :title "First topic"
                :post-count 2
                :author-id 17592186045418}))

(fact "topic serializer correctly serializes relationships"
  (:first-post (c/mapify (q/one [:topic/title "First topic"]) mr/ent->topic {:include :first-post}))
  => (contains {:content "T1 First post content"}))
