(ns user-api.handler
  (:require [user-api.authenticate :as auth]
            [user-api.token :as token]
            [user-api.user :as user]
            [user-api.location :as location]
            [user-api.event :as event]
            [user-api.pin :as pin]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Message {:message String})

(defapi app
  (swagger-ui)
  (swagger-docs
    :title "User API")
  (swaggered "api"
    :description "api base"

    (GET* "/api/location" []
          :summary "List all locations"
          (ok (location/list [])))

    (GET* "/api/location/:id" []
          :summary "List location information"
          :path-params [id :- s/Int]
          (ok (location/lookup id)))

    (POST* "/api/location" []
           :body-params [lat :- s/Num lon :- s/Num]
           :summary "Create a new location"
           (ok (location/create lat lon)))

    (GET* "/api/event" []
          :summary "List all events"
          (ok (event/list)))

    (GET* "/api/event/:id" []
          :path-params [id :- s/Int]
          (ok (event/lookup id)))

    (POST* "/api/event" []
           :body-params [name :- s/Str]
           (ok (event/create name)))

    (PUT* "/api/event/:eid/pin/:pid" []
          :path-params [eid :- s/Int pid :- s/Int]
          (ok (event/add_pin eid pid)))

    (GET* "/api/t" []
      :summary "test authentication"
      (let [r (auth/t)]
        (ok r)))

    (GET* "/api/user" []
      :query-params [name :- String]
      :summary "get user"
      (let [user (user/find name)]
        (if (nil? user)
          (not-found nil)
          (ok user))))

    (POST* "/api/user" []
      :return user/User
      :body-params [username :- String password :- String]
      :summary "Create a new user by providing username/pw"
      (created (user/create username password)))

    ;; attempt to login as a user
    (POST* "/api/login" []
      :body-params [username :- String password :- String]
      :summary "Authenticate as a user"
      (if (true) 
        (ok {:success true})
        (unauthorized {:success false})))

    ;; password reset
    (GET* "/api/pw_reset" []
      :query-params [username :- String]
      :summary "Create a password reset token"
      (ok (token/make-token-resp username)))

    (PUT* "/api/pw_reset" []
      :query-params [username :- String token :- Long expires :- Long]
      :summary "change password granted the token is valid"
      (ok {:result (token/test-token token expires username)}))

    ;; verify email
    (GET* "/api/verify_email" []
      ;; generate a token and an expires time to include in a verification email
      :query-params [username :- String]
      :summary "Create an email verification token"
      (ok (token/make-token-resp username)))

    (PUT* "/api/verify_email" []
      ;; verify email using token
      :query-params [username :- String token :- Long expires :- Long]
      :summary "verify user's email address"
      (ok {:result (token/test-token token expires username)}))))
