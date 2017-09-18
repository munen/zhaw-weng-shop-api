ALTER TABLE categories DROP COLUMN client_id RESTRICT;
ALTER TABLE products DROP COLUMN client_id RESTRICT;
ALTER TABLE products DROP COLUMN category_client_id RESTRICT;
