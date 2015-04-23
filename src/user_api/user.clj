(ns user-api.user
  (:require [schema.core :as schema]
            [user-api.db :as db]
            [rethinkdb.query :as r]))

(schema/defschema User
  {:username String :password String})

(defn find [username]
  ; find a user via username
  (first (-> (r/db db/name)
             (r/table db/user)
             (r/filter (r/fn [row]
                             (r/eq username (r/get-field row :username))))
             (r/run db/conn))))

(defn create [username password]
  ; create a new user
  (let [user {:username username :password password}
        found-users (find username)]
    (if (nil? found-users)
      (db/insert-record {:table db/user :record user}))))
