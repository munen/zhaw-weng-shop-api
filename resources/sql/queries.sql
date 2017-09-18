-- :name create-product! :<! :n
-- :doc creates a new product
INSERT INTO products
(title, category_id, description, image_url, data, quantity, price, in_stock)
VALUES (:title, :category_id, :description, :image_url, :data, :quantity, :price, :in_stock)
returning id

-- :name update-product! :! :n
-- :doc update an existing product
UPDATE products
SET title = :title, description = :description, image_url = :image_url, data = :data, quantity = :quantity, price = :price, in_stock = :in_stock, updated_at = NOW()
WHERE category_id = :category_id AND id = :id

-- :name get-product :? :1
-- :doc retrieve a product given the id.
SELECT * FROM products
WHERE category_id = :category_id AND id = :id

-- :name get-products :? :*
-- :doc retrieves all products
SELECT * FROM products
WHERE category_id = :category_id
order by id

-- :name delete-product! :! :n
-- :doc delete a product given the id
DELETE FROM products
WHERE id = :id AND category_id = :category_id




-- :name create-category! :<! :n
-- :doc creates a new category
INSERT INTO categories
(title, description, image_url, data)
VALUES (:title, :description, :image_url, :data)
returning id

-- :name delete-category! :! :n
-- :doc delete a category given the id
DELETE FROM categories
WHERE id = :id

-- :name update-category! :! :n
-- :doc update an existing category
UPDATE categories
SET title = :title, description = :description, image_url = :image_url, data = :data, updated_at = NOW()
WHERE id = :id

-- :name get-category :? :1
-- :doc retrieve a category given the id.
SELECT * FROM categories
WHERE id = :id
