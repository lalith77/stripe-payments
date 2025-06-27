-- Drop the foreign key constraint first
ALTER TABLE products DROP FOREIGN KEY fk_category;

-- Alter the referenced column
ALTER TABLE categories MODIFY COLUMN id BIGINT;

-- Alter the referencing column
ALTER TABLE products MODIFY COLUMN category_id BIGINT;

-- Re-add the foreign key constraint
ALTER TABLE products
    ADD CONSTRAINT fk_category
        FOREIGN KEY (category_id) REFERENCES categories(id);
