(ns user-api.location
  (:require [schema.core :as s]
            [user-api.util :as util]))

(s/defschema Location
  {:lat s/Num :lon s/Num :id s/Int})

(def locations {})

;; create a new location
(defn create [lat lon]
  (let [id (count locations)
        location {:lat lat :lon lon :id id}]
    (def locations (assoc locations (util/gen_id id) location))
    location))

(defn list []
  ; get all locations
  (vals locations))

(defn lookup [id]
  (get locations (util/gen_id id)))
