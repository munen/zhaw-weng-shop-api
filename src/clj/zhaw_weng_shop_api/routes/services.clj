(ns zhaw-weng-shop-api.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [zhaw-weng-shop-api.db.core :as db]
            [schema.core :as s]))

(s/defschema Product {(s/optional-key :id) Long
                      (s/optional-key :category_id) Long
                      :client_id String
                      :category_client_id String
                      :title String
                      :description String
                      :image_url String
                      :data String
                      :quantity Integer
                      :price Integer
                      :in_stock Boolean})

(s/defschema Category {(s/optional-key :id) Long
                       :description String
                       :image_url String
                       :data String
                       :client_id String
                       :title String})

(defn add-product! [new-product category_id]
  "Add an product to the Database and return it as a map with the new ID"
  (let [id (:id (db/create-product! (assoc new-product :category_id category_id)))
        product (assoc new-product :id id)]
    product))

(defn add-category! [new-category]
  "Add an category to the Database and return it as a map with the new ID"
  (let [id (:id (db/create-category! new-category))
        category (assoc new-category :id id)]
    category))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api" []

           (context "/categories" []
                    :tags ["Category API"]
                    :description "Note: In `data` you can "

                    (POST "/" []
                          :return Category
                          :body [category Category]
                          :summary "Create and save a category"
                          (ok (add-category! category)))

                    (GET "/:id" []
                            :path-params [id :- Long]
                            :summary "Retrieve a category"
                            (ok (db/get-category {:id id})))

                    (PUT "/:id" []
                            :path-params [id :- Long]
                            :return Category
                            :body [category Category]
                            :summary "Updates a category"
                            (db/update-category! (assoc category :id id))
                            (ok (db/get-category {:id id})))

                    (DELETE "/:id" []
                            :path-params [id :- Long]
                            :summary "Deletes a category"
                            (ok (db/delete-category! {:id id}))))

           (context "/category/:category_id" []
                    :tags ["Products API"]
                    :path-params [category_id :- Long]

                    (DELETE "/products/:id" []
                            :path-params [id :- Long]
                            :summary "Deletes an product"
                            (ok (db/delete-product! {:id id
                                                   :category_id category_id})))

                    (PUT "/products/:id" []
                         :path-params [id :- Long]
                         :return Product
                         :body [product Product]
                         :summary "Updates an product"
                         (db/update-product! (assoc product :id id
                                                  :category_id category_id))
                         (ok (db/get-product {:id id
                                            :category_id category_id})))

                    (POST "/products" []
                          :return Product
                          :body [product Product]
                          :summary "Create and save an product"
                          (ok (add-product! product category_id)))

                    (GET "/products" []
                         :return [Product]
                         :summary "Retrieve all products"
                         (ok (db/get-products {:category_id category_id}))))

           (context "/tests" []
                    :tags ["Practice HTTP based services"]

                    (GET "/plus" []
                         :return       Long
                         :query-params [x :- Long, {y :- Long 1}]
                         :summary      "x+y with query-parameters. y defaults to 1."
                         (ok (+ x y)))

                    (POST "/minus" []
                          :return      Long
                          :form-params [x :- Long, y :- Long]
                          :summary     "x-y with body-parameters."
                          (ok (- x y)))

                    (PUT "/echo" []
                         :return   [{:hot Boolean}]
                         :body     [body [{:hot Boolean}]]
                         :summary  "echoes a vector of anonymous hotties"
                         (ok body))

                    (POST "/echo" []
                          :return   (s/maybe Product)
                          :body     [product (s/maybe Product)]
                          :summary  "echoes a Product from json-body"
                          (ok product)))))
