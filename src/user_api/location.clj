(ns user-api.location
  (:require [schema.core :as s]))

(s/defschema Location
  {:lat s/Num :lon s/Num :id s/Int})

(def locations {})

(defn location_id [n] 
  (keyword (str "id_" n)))

;; create a new location
(defn create [lat lon]
  (let [id (count locations)
        location {:lat lat :lon lon :id id}]
    (def locations (assoc locations (location_id id) location))
    location))

(defn list []
  ; get all locations
  (vals locations))

(defn lookup [id]
  (get locations (location_id id)))
