(ns weatherjerk.config
  (:use environ.core))

(defn config
  [& keys]
  (get-in env keys))
