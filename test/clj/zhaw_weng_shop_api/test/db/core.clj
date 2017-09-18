(ns zhaw-weng-shop-api.test.db.core-test
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
      (let [category {:title    "Test Category 1"
                      :description "spec description"
                      :image_url "http://example.com/image.png"
                      :data "{}"}
            category_id (:id (db/create-category! t-conn category))
            product {:title      "Test Product 1"
                     :description "Product Description"
                     :in_stock true
                     :image_url "http://example.com/image.png"
                     :data "{}"
                     :quantity 2
                     :price 100
                     :category_id category_id}
            id (:id (db/create-product! t-conn product))]

        (is (=
             (assoc product :id id )
             (dissoc (db/get-product t-conn {:id id :category_id category_id})
                     :created_at :updated_at)))

        (is (= 1
               (db/update-product!
                t-conn
                (assoc product
                       :id id
                       :title "Test Product Updated"
                       :in_stock false
                       ))))
        (is (= (assoc product :id id :title "Test Product Updated" :in_stock false)
               (dissoc (db/get-product t-conn {:id id :category_id category_id})
                       :created_at :updated_at)))

        (is (= 1 (db/delete-product!
                  t-conn
                  {:id id :category_id category_id})))

        (is (= nil
               (db/get-product t-conn {:id id :category_id category_id})))))))
