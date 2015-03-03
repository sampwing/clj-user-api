(ns user-api.token
  (:require [schema.core :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(def API_SECRET "abc")

;; -- TOKEN STUFF
;; test token expiration
(defn test-expiration [expires]
  (let [expire-date (c/from-long expires)]
    (if (t/after? (t/now) expire-date)
      ;; if the token has expire raise exception
      (throw (Exception. "token has expired"))
      ;; otherwise return the expiration
      expires)))

;; test token
(defn test-token [token expires username] 
  (let [hash-val (hash (str username API_SECRET (test-expiration expires)))]
    (= hash-val token)))

;; create a token object with it's expiration
(defn make-token-resp [username]
  ;; set the expiration to 1 minute from now
  (let [expires (c/to-long (t/plus (t/now) (t/minutes 1)))
        token (hash (str username API_SECRET expires))] 
    {:token token :expires expires}))
