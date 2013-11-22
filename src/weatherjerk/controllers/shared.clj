(ns weatherjerk.controllers.shared
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        weatherjerk.utils))

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})

(defmacro id
  []
  '(str->int (:id params)))

(defmacro validator
  "Used in malformed? which is why truth values are reversed"
  [params validation]
  `(fn [ctx#]
     (if-valid
      ~params ~validation errors#
      false
      [true {:errors errors#
             :representation {:media-type "application/json"}}])))


;; working with liberator
(defn exists?
  [record]
  (if record
    {:record record}))

(defn record-in-ctx
  [ctx]
  (get ctx :record))

(def exists-in-ctx? record-in-ctx)

(defn errors-in-ctx
  [ctx]
  {:errors (get ctx :errors)})

(defmacro can-delete-record?
  [record auth]
  `(fn [_#]
     (let [record# ~record
           auth# ~auth]
       (if (or (author? record# auth#) (moderator? auth#))
         {:record record#}))))

(defmacro can-update-record?
  [record auth]
  `(fn [_#]
     (let [auth# ~auth
           record# ~record]
       (if (and (not (:deleted record#))
                (or (moderator? auth#)
                    (author? record# auth#)))
         {:record record#}))))