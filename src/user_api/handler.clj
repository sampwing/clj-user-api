(ns user-api.handler
  (:require [user-api.authenticate :as auth]
            [user-api.token :as token]
            [user-api.user :as user]
            [user-api.location :as location]
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

    (GET* "/location" []
          :summary "List all locations"
          (ok (location/list)))

    (GET* "/location/:id" []
          :summary "List location information"
          :path-params [id :- s/Int]
          (ok (location/lookup id)))

    (POST* "/location" []
           :body-params [lat :- s/Num lon :- s/Num]
           :summary "Create a new location"
           (ok (location/create lat lon)))


    (GET* "/t" []
      :summary "test authentication"
      (let [r (auth/t)]
        (ok r)))

    (GET* "/user" []
      :return Message
      :query-params [name :- String]
      :summary "get user"
      (ok {:message (str "Hello, " name)}))

    (POST* "/user" []
      :return user/User
      :body-params [username :- String password :- String]
      :summary "Create a new user by providing username/pw"
      (created (user/create username password)))

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
      (ok (token/make-token-resp username)))

    (PUT* "/pw_reset" []
      :query-params [username :- String token :- Long expires :- Long]
      :summary "change password granted the token is valid"
      (ok {:result (token/test-token token expires username)}))

    ;; verify email
    (GET* "/verify_email" []
      ;; generate a token and an expires time to include in a verification email
      :query-params [username :- String]
      :summary "Create an email verification token"
      (ok (token/make-token-resp username)))

    (PUT* "/verify_email" []
      ;; verify email using token
      :query-params [username :- String token :- Long expires :- Long]
      :summary "verify user's email address"
      (ok {:result (token/test-token token expires username)}))
    ))
