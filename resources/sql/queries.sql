-- :name create-product! :<! :n
-- :doc creates a new product
INSERT INTO products
(client_id, done, title, due_date, project_id, project_client_id, priority)
VALUES (:client_id, :done, :title, :due_date, :project_id, :project_client_id, :priority)
returning id

-- :name update-product! :! :n
-- :doc update an existing product
UPDATE products
SET title = :title, done = :done, due_date = :due_date, priority = :priority
WHERE project_id = :project_id AND id = :id

-- :name get-product :? :1
-- :doc retrieve a product given the id.
SELECT * FROM products
WHERE project_id = :project_id AND id = :id

-- :name get-products :? :*
-- :doc retrieves all products
SELECT * FROM products
WHERE project_id = :project_id
order by id

-- :name delete-product! :! :n
-- :doc delete a product given the id
DELETE FROM products
WHERE id = :id AND project_id = :project_id




-- :name create-project! :<! :n
-- :doc creates a new project
INSERT INTO projects
(client_id, title, active)
VALUES (:client_id, :title, :active)
returning id

-- :name delete-project! :! :n
-- :doc delete a project given the id
DELETE FROM projects
WHERE id = :id

-- :name update-project! :! :n
-- :doc update an existing project
UPDATE projects
SET title = :title, active = :active
WHERE id = :id

-- :name get-project :? :1
-- :doc retrieve a project given the id.
SELECT * FROM projects
WHERE id = :id
