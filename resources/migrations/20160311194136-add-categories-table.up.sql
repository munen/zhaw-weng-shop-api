CREATE TABLE products
(id SERIAL,
 client_id text NOT NULL,
 done boolean DEFAULT false,
 due_date timestamp NOT NULL,
 title text NOT NULL,
 project_id integer REFERENCES categories(id));
