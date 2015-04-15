(ns user-api.pin
  (:require [schema.core :as s]
            [user-api.util :as util]))

(s/defschema Pin
  {:location_id s/Int :user_id s/Int :pin_id s/Int})

(def pins {})

;; create a new pin
(defn create [location_id user_id]
  (let [id (count pins)
        pin {:location_id location_id :user_id user_id :id id}]
    (def pins (assoc pins (util/gen_id id) pin))
    pin))

(defn list []
  ; get all pins
  (vals pins))

(defn lookup [id]
  (get pins (util/gen_id id)))
