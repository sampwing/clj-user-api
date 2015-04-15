(ns user-api.util
  (:require [schema.core :as s]))

(defn gen_id [n]
  (keyword (str "id_" n)))
