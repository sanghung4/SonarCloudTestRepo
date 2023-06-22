-- customer_region
-- (1) Foreign keys should always map to other primary keys, and they should fully cascade in this case
ALTER TABLE customer_region
DROP CONSTRAINT fk_customer_region_customer;

ALTER TABLE customer_region
ALTER COLUMN id_customer TYPE UUID USING id_customer::uuid;

ALTER TABLE customer_region
ADD CONSTRAINT fk_customer_region_customer
FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- catalog
-- (2) Foreign keys should always map to other primary keys, and they should fully cascade in this case
ALTER TABLE catalog
DROP CONSTRAINT fk_catalog_customer;

ALTER TABLE catalog
ALTER COLUMN id_customer TYPE UUID USING id_customer::uuid;

ALTER TABLE catalog
ADD CONSTRAINT fk_catalog_customer
FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- product
-- (3) Logic generally belongs in the backend, and a value range is not a known requirement
ALTER TABLE product
DROP CONSTRAINT chk_product_delivery_in_days;

-- (4) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_name ON product(name);

-- (5) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_description ON product(description);

-- (6) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_category_1_name ON product(category_1_name);

-- (7) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_category_2_name ON product(category_2_name);

-- (8) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_category_3_name ON product(category_3_name);

-- (9) This column was supposed to be indexed, because it is used in searching as annotated with IDX
CREATE INDEX idx_product_manufacturer_part_number ON product(manufacturer_part_number);

-- (10) This data comes from external so we cannot guarantee content
ALTER TABLE product ALTER COLUMN name drop NOT NULL;
ALTER TABLE product ALTER COLUMN description drop NOT NULL;
ALTER TABLE product ALTER COLUMN image_full_size drop NOT NULL;
ALTER TABLE product ALTER COLUMN part_number drop NOT NULL;
ALTER TABLE product ALTER COLUMN manufacturer drop NOT NULL;
ALTER TABLE product ALTER COLUMN category_1_name drop NOT NULL;
ALTER TABLE product ALTER COLUMN unspsc drop NOT NULL;

-- (11) We may have to create this as a placeholder prior to sync, so no date/time
ALTER TABLE product ALTER COLUMN max_sync_datetime drop NOT NULL;

-- catalog_product
-- (12) When initially created there is no product mapping, and no immediate sync
ALTER TABLE catalog_product ALTER COLUMN id_product drop NOT NULL;
ALTER TABLE catalog_product ALTER COLUMN sell_price drop NOT NULL;
ALTER TABLE catalog_product ALTER COLUMN list_price drop NOT NULL;
ALTER TABLE catalog_product ALTER COLUMN uom drop NOT NULL;
ALTER TABLE catalog_product ALTER COLUMN last_pull_datetime drop NOT NULL;
ALTER TABLE catalog_product ALTER COLUMN sku_quantity drop NOT NULL;

-- (13) No requirements were given around values, and these come from an external source
ALTER TABLE catalog_product
DROP CONSTRAINT chk_catalog_product_sell_price;

ALTER TABLE catalog_product
DROP CONSTRAINT chk_catalog_product_list_price;

ALTER TABLE catalog_product
DROP CONSTRAINT chk_catalog_product_sku_quantity;

-- (14) Foreign keys in a parent-child relationship should always fully cascade in a parent deletion
ALTER TABLE catalog_product
DROP CONSTRAINT fk_catalog_product_catalog;

ALTER TABLE catalog_product
ADD CONSTRAINT fk_catalog_product_catalog
FOREIGN KEY (id_catalog) REFERENCES catalog (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- (15) Foreign keys in a parent-child relationship should always fully cascade in a parent deletion
ALTER TABLE catalog_product
DROP CONSTRAINT fk_catalog_product_product;

ALTER TABLE catalog_product
ADD CONSTRAINT fk_catalog_product_product
FOREIGN KEY (id_product) REFERENCES product (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- territory_exclusion
-- (16) Foreign keys in a parent-child relationship should always fully cascade in a parent deletion
ALTER TABLE territory_exclusion
DROP CONSTRAINT fk_territory_exclusion_product;

ALTER TABLE territory_exclusion
ADD CONSTRAINT fk_territory_exclusion_product
FOREIGN KEY (id_product) REFERENCES product (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- authorized_user
-- (17) Logic does not belong in a database
ALTER TABLE authorized_user
DROP CONSTRAINT chk_authorized_user_email;

-- sync_log
-- (18) Logic does not belong in a database
ALTER TABLE sync_log
DROP CONSTRAINT chk_sync_log_status;

-- (19) The end time is not known when we first create this record
ALTER TABLE sync_log ALTER COLUMN end_datetime drop NOT NULL;

