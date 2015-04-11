(ns user-api.user
  (:require [schema.core :as schema]))

(schema/defschema User 
  {:username String :password String})

(def users {})

;; create a new user
(defn create [username password]
  ;; do stuff to create a user here and return in
  (let [user {:username username :password password}]
    (def users (assoc users (keyword username) user))
    user))

(defn lookup [username] 
  ; lookup a user via username
  (get users username))
