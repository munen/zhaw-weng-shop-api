ALTER TABLE categories ADD client_id text NOT NULL;
UPDATE categories SET client_id = 'none';
