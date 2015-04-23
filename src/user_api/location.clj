(ns user-api.location
  (:require [schema.core :as s]
            [user-api.util :as util]
            [user-api.db :as db]
            [rethinkdb.query :as r]))

(s/defschema Location
  {:lat s/Num :lon s/Num :id s/Int})

(defn find [lat lon]
  (first (-> (r/db db/name)
             (r/table db/location)
             (r/filter (r/fn [row]
                             (and (r/eq lat (r/get-field row :lat))
                                  (r/eq lon (r/get-field row :lon))))))))

(defn create [lat lon]
  (let [location {:lat lat :lon lon}
        found-location (find lat lon)]
    (if (nil? found-location)
      (db/insert-record {:table db/location :record location}))))

(defn list []
  ; get all locations
  (db/get-all-records {:table db/location}))

(defn lookup [id]
  (db/get-record {:table db/location :id id}))

