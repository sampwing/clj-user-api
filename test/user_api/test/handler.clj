(ns user-api.test.handler
  (require [clojure.test :refer :all]
           [user-api.db :as db]
           [user-api.handler :as handler]))

(deftest test-handler
  (db/initialize)
  (testing "location"
    (let [response (handler/app {:request-method :get :uri "/api/location"})]
      (is (:status response) 200)))
  (testing "event"
    (let [response (handler/app {:request-method :get :uri "/api/event"})]
      (is (:status response) 200))))
