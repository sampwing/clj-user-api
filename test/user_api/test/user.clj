(ns user-api.test.user
  (require [clojure.test :refer :all]
           [user-api.db :as db]
           [user-api.user :as user]))

(deftest test-user
  (db/initialize)
  (let [username "test_user"
        password "password"]
    (testing "find user by username - fail"
      (let [found-user (user/find username)]
        (println found-user)
        (is (nil? found-user))))
    (testing "create user"
      (let [created-user (user/create username password)]
        (println created-user)
        (and (is (:username created-user) username)
             (is (:password created-user) password))))
    (testing "find user by username - pass"
      (let [found-user (user/find username)]
        (println found-user)
        (and (is (:username found-user) username)
             (is (:password found-user) password))))))
