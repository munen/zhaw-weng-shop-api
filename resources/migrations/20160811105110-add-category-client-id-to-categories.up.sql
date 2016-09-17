ALTER TABLE products ADD category_client_id text NOT NULL;
UPDATE products SET category_client_id = 'none';
