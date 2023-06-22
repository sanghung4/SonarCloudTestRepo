CREATE TABLE IF NOT EXISTS customer
(
    id            UUID         NOT NULL,
    id_customer   VARCHAR(50)  NOT NULL,
    id_branch     VARCHAR(50),
    branch_name   VARCHAR(100),
    id_erp        VARCHAR(50),
    name          VARCHAR(200) NOT NULL,
    is_bill_to    BOOLEAN,
    last_update   TIMESTAMP,
    contact_name  TEXT,
    contact_phone VARCHAR(50),

    CONSTRAINT pk_customer PRIMARY KEY (id),
    CONSTRAINT uq_id_customer UNIQUE (id_customer)
);

CREATE TABLE IF NOT EXISTS customer_legacy
(
    id             UUID         NOT NULL,
    id_customer    VARCHAR(50)  NOT NULL,
    id_branch      VARCHAR(50),
    id_erp_account VARCHAR(50),
    name           VARCHAR(200) NOT NULL,
    name_erp       VARCHAR(200),
    is_bill_to     BOOLEAN,
    CONSTRAINT pk_customer_legacy PRIMARY KEY (id),
    CONSTRAINT uq_id_customer_legacy UNIQUE (id_customer)
);

CREATE TABLE IF NOT EXISTS customer_region
(
    id          UUID        NOT NULL,
    id_customer VARCHAR(50) NOT NULL,
    name        VARCHAR(50) NOT NULL,

    CONSTRAINT pk_customer_region PRIMARY KEY (id),
    CONSTRAINT fk_customer_region_customer FOREIGN KEY (id_customer) REFERENCES customer (id_customer) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS catalog_status
(
    id VARCHAR(50) NOT NULL,

    CONSTRAINT pk_catalog_status PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS procurement_system
(
    name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_procurement_system PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS catalog
(
    id            UUID         NOT NULL,
    id_customer   VARCHAR(50)  NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    file_name     TEXT         NULL,
    name          VARCHAR(200) NOT NULL,
    last_update   TIMESTAMP    NOT NULL,
    date_archived TIMESTAMP    NULL,
    proc_system   VARCHAR(50)  NULL,

    CONSTRAINT pk_catalog PRIMARY KEY (id),
    CONSTRAINT fk_catalog_customer FOREIGN KEY (id_customer) REFERENCES customer (id_customer) ON UPDATE CASCADE,
    CONSTRAINT fk_catalog_proc_system FOREIGN KEY (proc_system) REFERENCES procurement_system (name) ON UPDATE CASCADE,
    CONSTRAINT fk_catalog_catalog_status FOREIGN KEY (status) REFERENCES catalog_status (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS catalog_legacy
(
    id          UUID        NOT NULL,
    id_customer VARCHAR(50) NOT NULL,
    id_branch   VARCHAR(20),
    id_product  VARCHAR(50) NOT NULL,
    CONSTRAINT pk_catalog_legacy PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id                       UUID         NOT NULL,
    name                     VARCHAR(200) NOT NULL,
    description              TEXT         NOT NULL,
    image_full_size          TEXT         NOT NULL,
    part_number              VARCHAR(200) NOT NULL,
    manufacturer             VARCHAR(200) NOT NULL,
    category_1_name          VARCHAR(100) NOT NULL,
    category_2_name          VARCHAR(100),
    category_3_name          VARCHAR(100),
    unspsc                   VARCHAR(100) NOT NULL,
    image_thumb              TEXT,
    manufacturer_part_number VARCHAR(200),
    delivery_in_days         INT DEFAULT 7,
    max_sync_datetime        TIMESTAMP    NOT NULL,

    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT chk_product_delivery_in_days CHECK (delivery_in_days > 0)
);

CREATE TABLE IF NOT EXISTS product_legacy
(
    id                       UUID         NOT NULL,
    id_product               VARCHAR(20)  NOT NULL,
    name                     VARCHAR(200) NOT NULL,
    description              TEXT         NOT NULL,
    image_full_size          TEXT         NOT NULL,
    price                    FLOAT        NOT NULL,
    part_number              VARCHAR(200) NOT NULL,
    unit_of_measure          VARCHAR(100) NOT NULL,
    manufacturer             VARCHAR(200) NOT NULL,
    category_1_code          VARCHAR(100) NOT NULL,
    category_1_name          VARCHAR(100) NOT NULL,
    unspsc                   VARCHAR(100) NOT NULL,
    image_thumb              TEXT,
    scale_start              INT,
    scale_end                INT,
    list_price               FLOAT,
    manufacturer_part_number VARCHAR(200),
    category_2_code          VARCHAR(100),
    category_2_name          VARCHAR(100),
    category_3_code          VARCHAR(100),
    category_3_name          VARCHAR(100),
    category_4_code          VARCHAR(100),
    category_4_name          VARCHAR(100),
    category_5_code          VARCHAR(100),
    category_5_name          VARCHAR(100),
    delivery_in_days         INT,
    buyer_id                 VARCHAR(100),
    CONSTRAINT pk_product_legacy PRIMARY KEY (id),
    CONSTRAINT chk_product_legacy_price CHECK (price > 0),
    CONSTRAINT uq_product_legacy_id_product UNIQUE (id_product)
);


CREATE TABLE IF NOT EXISTS catalog_product
(
    id                 UUID        NOT NULL,
    id_catalog         UUID        NOT NULL,
    id_product         UUID        NOT NULL,
    sell_price         FLOAT       NOT NULL,
    list_price         FLOAT       NOT NULL,
    uom                VARCHAR(50) NOT NULL,
    last_pull_datetime TIMESTAMP   NOT NULL,
    sku_quantity       INT         NOT NULL,

    CONSTRAINT pk_catalog_product PRIMARY KEY (id),
    CONSTRAINT fk_catalog_product_catalog FOREIGN KEY (id_catalog) REFERENCES catalog (id) ON UPDATE CASCADE,
    CONSTRAINT fk_catalog_product_product FOREIGN KEY (id_product) REFERENCES product (id) ON UPDATE CASCADE,
    CONSTRAINT chk_catalog_product_sell_price CHECK (sell_price > 0),
    CONSTRAINT chk_catalog_product_list_price CHECK (list_price >= 0),
    CONSTRAINT chk_catalog_product_sku_quantity CHECK (sku_quantity > 0)
);

CREATE TABLE IF NOT EXISTS territory_exclusion
(
    id         UUID        NOT NULL,
    id_product UUID        NOT NULL,
    name       VARCHAR(50) NOT NULL,

    CONSTRAINT pk_territory_exclusion PRIMARY KEY (id),
    CONSTRAINT fk_territory_exclusion_product FOREIGN KEY (id_product) REFERENCES product (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS authorized_user
(
    id    UUID         NOT NULL,
    email VARCHAR(200) NOT NULL,
    admin BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_authorized_user PRIMARY KEY (id),
    CONSTRAINT chk_authorized_user_email CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')
);

CREATE TABLE IF NOT EXISTS upload
(
    id              UUID         NOT NULL,
    filename        VARCHAR(200) NOT NULL,
    content         TEXT         NULL,
    id_customer     UUID         NOT NULL,
    upload_datetime TIMESTAMP    NOT NULL,
    id_user         UUID         NOT NULL,

    CONSTRAINT pk_upload PRIMARY KEY (id),
    CONSTRAINT fk_upload_customer FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE,
    CONSTRAINT fk_upload_user FOREIGN KEY (id_user) REFERENCES authorized_user (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS sync_log
(
    id             UUID        NOT NULL,
    start_datetime TIMESTAMP   NOT NULL,
    end_datetime   TIMESTAMP   NOT NULL,
    status         VARCHAR(50) NOT NULL DEFAULT 'STARTED',

    CONSTRAINT pk_sync_log PRIMARY KEY (id),
    CONSTRAINT chk_sync_log_status CHECK (status = 'STARTED' OR status = 'FAILED' OR status = 'COMPLETED' )
);


CREATE TABLE IF NOT EXISTS audit
(
    id              UUID         NOT NULL,
    file_name       VARCHAR(200) NOT NULL,
    s3_location     TEXT         NOT NULL,
    id_customer     UUID         NOT NULL,
    upload_datetime TIMESTAMP    NOT NULL,
    id_sync         UUID         NOT NULL,

    CONSTRAINT pk_audit PRIMARY KEY (id),
    CONSTRAINT fk_audit_customer FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE,
    CONSTRAINT fk_audit_sync FOREIGN KEY (id_sync) REFERENCES sync_log (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS audit_legacy
(
    id            UUID         NOT NULL,
    file_name     VARCHAR(200) NOT NULL,
    timestamp     TIMESTAMP    NOT NULL,
    status        VARCHAR(100) NOT NULL,
    s3_location   TEXT         NOT NULL,
    file_checksum TEXT         NOT NULL,
    errors        TEXT,
    CONSTRAINT pk_punchout_audit_legacy PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BYTEA        NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);