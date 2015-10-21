(ns user-api.authenticate
  (:require [user-api.user :as user]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-jwt.core :refer :all]))

(def JWT-KEY "abc")

(defrecord Claim [iss exp iat])



(defn- build-claim 
  [& {:keys [iss exp iat] :or {iss "joe" exp 1 iat 0}}]
  (println (str iss exp iat))
  {:iss iss :exp exp :iat iat})

(defn- build-static-claim
  []
  (let [now (t/now)]
    {:iss "joe" :exp (t/plus now (t/minutes 1)) :iat now}))

(defn build-token 
  [& params]
  (println "building..")
  ;(let [claim (build-claim params)]
  (let [claim (build-static-claim )]
    (println "built...")
    (-> claim jwt (sign :H256 JWT-KEY) to-str)))

(defn verify-token
  [token]
  (println (str "verify" token))
  (-> token str->jwt (verify JWT-KEY)))

(defn t []
  (let [token (build-token {})
        r-token (verify-token token)]
    (println token)
    (println r-token)))

