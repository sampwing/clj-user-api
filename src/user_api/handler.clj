(ns user-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(s/defschema Message {:message String})

(def API_SECRET "abc")

;; -- USER STUFF
(s/defschema User {:username String :password String})

(def users {})

;; create a new user
(defn create-user [username password]
  ;; do stuff to create a user here and return in
  (let [user {:username username :password password}]
    (def users (assoc users (keyword username) user))
    user))

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

(defapi app
  (swagger-ui)
  (swagger-docs
    :title "User API")
  (swaggered "api"
    :description "api base"

    (GET* "/user" []
      :return Message
      :query-params [name :- String]
      :summary "get user"
      (ok {:message (str "Hello, " name)}))

    (POST* "/user" []
      :return User
      :body-params [username :- String password :- String]
      :summary "Create a new user by providing username/pw"
      (created (create-user username password)))

    ;; attempt to login as a user
    (POST* "/login" []
      :body-params [username :- String password :- String]
      :summary "Authenticate as a user"
      (if (true) 
        (ok {:success true})
        (unauthorized {:success false})))

    ;; password reset
    (GET* "/pw_reset" []
      :query-params [username :- String]
      :summary "Create a password reset token"
      (ok (make-token-resp username)))

    (PUT* "/pw_reset" []
      :query-params [username :- String token :- Long expires :- Long]
      :summary "change password granted the token is valid"
      (ok {:result (test-token token expires username)}))

    ;; verify email
    (GET* "/verify_email" []
      ;; generate a token and an expires time to include in a verification email
      :query-params [username :- String]
      :summary "Create an email verification token"
      (ok (make-token-resp username)))

    (PUT* "/verify_email" []
      ;; verify email using token
      :query-params [username :- String token :- Long expires :- Long]
      :summary "verify user's email address"
      (ok {:result (test-token token expires username)}))
    ))
