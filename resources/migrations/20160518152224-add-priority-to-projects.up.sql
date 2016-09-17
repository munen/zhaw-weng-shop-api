ALTER TABLE products ADD priority varchar(1);
ALTER TABLE products ALTER COLUMN priority SET DEFAULT '1';
UPDATE products SET priority = '1';
