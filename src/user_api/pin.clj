(ns user-api.pin
  (:require [schema.core :as s]
            [user-api.util :as util]
            [user-api.db :as db]
            [rethinkdb.query :as r]))

(s/defschema Pin
  {:location_id s/Int :user_id s/Int :pin_id s/Int})

(defn find [{:keys [location_id user_id]}]
  (first (-> (r/db db/name)
             (r/table db/pin)
             (r/filter (r/fn [row]
                             (and (r/eq location_id (r/get-field row :location_id))
                                  (r/eq user_id (r/get-field row :user_id))))))))

(defn create [location_id user_id]
  (let [pin {:location_id location_id, :user_id user_id}
        found-pin (find {:location_id location_id :user_id user_id})]
    (if (nil? found-pin)
      (db/insert-record {:table db/pin :record pin}))))

(defn list []
  ; get all pins
  (db/get-all-records {:table db/pin}))

(defn lookup [id]
  (db/get-record {:table db/pin :id id}))
