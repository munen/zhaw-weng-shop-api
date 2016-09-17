(ns zhaw-weng-api.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [zhaw-weng-api.db.core :as db]
            [schema.core :as s]))

(s/defschema Product {(s/optional-key :id) Long
                    (s/optional-key :project_id) Long
                    :client_id String
                    :project_client_id String
                    :done Boolean
                    :title String
                    :priority (s/enum "1" "2" "3")
                    :due_date java.util.Date})

(s/defschema Category {(s/optional-key :id) Long
                      :client_id String
                      :title String
                      :active Boolean})

(defn add-product! [new-product project_id]
  "Add an product to the Database and return it as a map with the new ID"
  (let [id (:id (db/create-product! (assoc new-product :project_id project_id)))
        product (assoc new-product :id id)]
    product))

(defn add-project! [new-project]
  "Add an project to the Database and return it as a map with the new ID"
  (let [id (:id (db/create-project! new-project))
        project (assoc new-project :id id)]
    project))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api" []

           (context "/categories" []
                    :tags ["Category API"]

                    (POST "/" []
                          :return Category
                          :body [project Category]
                          :summary "Create and save a project"
                          (ok (add-project! project)))

                    (GET "/:id" []
                            :path-params [id :- Long]
                            :summary "Retrieve a project"
                            (ok (db/get-project {:id id})))

                    (PUT "/:id" []
                            :path-params [id :- Long]
                            :return Category
                            :body [project Category]
                            :summary "Updates a project"
                            (db/update-project! (assoc project :id id))
                            (ok (db/get-project {:id id})))

                    (DELETE "/:id" []
                            :path-params [id :- Long]
                            :summary "Deletes a project"
                            (ok (db/delete-project! {:id id}))))

           (context "/project/:project_id" []
                    :tags ["Products API"]
                    :path-params [project_id :- Long]

                    (DELETE "/products/:id" []
                            :path-params [id :- Long]
                            :summary "Deletes an product"
                            (ok (db/delete-product! {:id id
                                                   :project_id project_id})))

                    (PUT "/products/:id" []
                         :path-params [id :- Long]
                         :return Product
                         :body [product Product]
                         :summary "Updates an product"
                         (db/update-product! (assoc product :id id
                                                  :project_id project_id))
                         (ok (db/get-product {:id id
                                            :project_id project_id})))

                    (POST "/products" []
                          :return Product
                          :body [product Product]
                          :summary "Create and save an product"
                          (ok (add-product! product project_id)))

                    (GET "/products" []
                         :return [Product]
                         :summary "Retrieve all products"
                         (ok (db/get-products {:project_id project_id}))))

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
