(ns user-api.event
  (:require [schema.core :as s]
            [user-api.util :as util]
            [user-api.db :as db]
            [rethinkdb.query :as r]))

(s/defschema Event
  {:id s/Int :name s/Str})

(s/defschema EventPins
  {:id s/Int :pin_ids [s/Int]})

(def events {})

(def eventpins {})

(defn create [event_name]
  (let [event {:name event_name :pins []}]
    (db/insert-record {:table db/event :record event})))

(defn lookup [id]
  (db/get-record {:table db/event :id id}))
;
;(defn add_pin [id pin]
;  (let [event (lookup id)
;        pin (db/get-record {:table db/pin :id pin})]
;    (if (complement (or (nil? event)
;                        (nil? pin)))
;      (db/insert-record {:table event })

;; create a new event
;(defn create [event_name]
;  (let [id (count events)
;        event {:name event_name :id id}
;        eventpin {:id id :pin_ids []}]
;    (def events (assoc events (util/gen_id id) event))
;    event))

;;add a pin to the event
(defn add_pin [id pin_id]
  (let [eventp (get eventpins (util/gen_id id) [])]
    (def eventpins (assoc eventpins (util/gen_id id) (conj eventp pin_id)))))

(defn list []
  ; get all events
  (db/get-all-records {:table db/event}))

