ALTER TABLE categories ADD created_at timestamp DEFAULT NOW();
ALTER TABLE categories ADD updated_at timestamp DEFAULT NOW();
