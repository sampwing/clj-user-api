(ns user-api.util
  (:require [schema.core :as s]
            [rethinkdb.core :refer [connect close]]
            [rethinkdb.query :as r]))

(defn gen_id [n]
  (keyword (str "id_" n)))

