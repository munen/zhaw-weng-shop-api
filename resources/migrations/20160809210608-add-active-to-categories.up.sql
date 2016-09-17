ALTER TABLE categories ADD active boolean DEFAULT false;
UPDATE categories SET active = false;
