(ns zhaw-weng-shop-api.test.db.core
  (:require [zhaw-weng-shop-api.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [zhaw-weng-shop-api.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'zhaw-weng-shop-api.config/env
                 #'zhaw-weng-shop-api.db.core/*db*)
    (migrations/migrate ["migrate"] (env :database-url))
    (f)))

(deftest test-products
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (testing "generated functions from HugSQL are working"
      (let [category {:client_id "some_client_uuid_4_category"
                      :title    "Test Category 1"
                      :description "spec description"
                      :image_url "http://example.com/image.png"
                      :data "{}"}
            category_id (:id (db/create-category! t-conn category))
            product {:client_id  "some-uuid"
                     :due_date   (java.util.Date.)
                     :done       false
                     :priority   "1"
                     :title      "Test Product 1"
                     :category_id category_id
                     :category_client_id (:client_id category)}
            id (:id (db/create-product! t-conn product))]

        (is (= (assoc product :id id )
               (db/get-product t-conn {:id id :category_id category_id})))

        (is (= 1
               (db/update-product!
                t-conn
                (assoc product
                       :id id
                       :title "Test Product Updated"))))

        (is (= (assoc product :id id :title "Test Product Updated")
               (db/get-product t-conn {:id id :category_id category_id})))

        (is (= 1 (db/delete-product!
                  t-conn
                  {:id id :category_id category_id})))

        (is (= nil
               (db/get-product t-conn {:id id :category_id category_id})))))))
