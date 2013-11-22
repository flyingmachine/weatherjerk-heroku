(ns lobos.config
  (:require [weatherjerk.db.connection :as db])
  (:use lobos.connectivity))

(open-global db/db-config)