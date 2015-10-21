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

; TODO should replace these with SC Center
(defn list [{:keys [lat lon] :or {:lat 0 :lon 0}}]
  (let [point (r/point lat lon)
        result (db/get-nearest {:table db/location :point point :index db/location-geo-index})]
    (println result)
    result))

(defn lookup [id]
  (db/get-record {:table db/location :id id}))

