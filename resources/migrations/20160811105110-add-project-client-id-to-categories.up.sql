ALTER TABLE products ADD project_client_id text NOT NULL;
UPDATE products SET project_client_id = 'none';
