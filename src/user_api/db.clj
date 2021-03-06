(ns user-api.db
  (:require [rethinkdb.core :refer [connect close]]
            [rethinkdb.query :as r]
            [environ.core :refer [env]]))

(def conn (connect :host "127.0.0.1" :port 28015))

(def name (env :database-name))
(def user "users")
(def location "locations")
(def location-geo-index "geo")
(def event "events")
(def pin "pins")

(defn insert-record [{:keys [table record]}]
  ; generic
  (let [response (-> (r/db name)
                     (r/table table)
                     (r/insert record)
                     (r/run conn))]
    (println response)
    (if (= (:inserted response) 1)
      (assoc record :id (first (:generated_keys response))))))

(defn get-record [{:keys [table id]}]
  (-> (r/db name)
      (r/table table)
      (r/get id)
      (r/run conn)))

(defn get-all-records [{:keys [table]}]
  (-> (r/db name)
      (r/table table)
      (r/all)
      (r/run conn)))

(defn get-nearest [{:keys [table point index args]}]
  (let [addtl-args (assoc (if (nil? args)
                            {}
                            args) "index" index)]
    (println addtl-args)
    (let [r (-> (r/db name)
        (r/get-nearest table point addtl-args)
        (r/run conn))]
      (println r))))

(defn initialize []
  (let [dbs (-> (r/db-list) (r/run conn))]
    (println dbs)
    (if (some #{name} dbs)
      (-> (r/db-drop name)
          (r/run conn))))
  (-> (r/db-create name)
      (r/run conn))
  (-> (r/db name)
      (r/table-create user)
      (r/run conn))
  (-> (r/db name)
      (r/table-create location)
      (r/run conn))
  (-> (r/db name)
      (r/table location)
      (r/index-create location-geo-index (r/fn [row]
                                               (r/get-field row (keyword location-geo-index)))
                      {:geo true})
      (r/run conn))
  (-> (r/db name)
      (r/table-create event)
      (r/run conn))
  (-> (r/db name)
      (r/table-create pin)
      (r/run conn)))
