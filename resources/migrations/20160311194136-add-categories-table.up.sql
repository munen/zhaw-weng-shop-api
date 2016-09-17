CREATE TABLE products
(id SERIAL,
 client_id text NOT NULL,
 done boolean DEFAULT false,
 due_date timestamp NOT NULL,
 title text NOT NULL,
 category_id integer REFERENCES categories(id));
