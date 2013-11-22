(ns weatherjerk.db.test
  (:require [datomic.api :as d]
            [weatherjerk.db.query :as q]
            [weatherjerk.db.manage :as manage])
  (:use weatherjerk.config)
  (:import java.io.File))

(def test-db-uri (config :datomic :test-uri))
(def initialized (atom false))

(defmacro with-test-db
  [& body]
  `(binding [q/*db-uri* test-db-uri]
     (initialize)
     ~@body))

(defn initialize
  []
  (doall (manage/reload)))
