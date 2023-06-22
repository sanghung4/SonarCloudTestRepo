ALTER TABLE audit
ADD COLUMN IF NOT EXISTS s3_date_time TIMESTAMP NULL;

ALTER TABLE audit
ADD COLUMN IF NOT EXISTS sftp_date_time TIMESTAMP NULL;

ALTER TABLE audit
ADD COLUMN IF NOT EXISTS sftp_location VARCHAR(200) NULL;

CREATE TABLE IF NOT EXISTS audit_error
(
    id              UUID         NOT NULL,
    audit_id        UUID         NOT NULL,
    part_number     VARCHAR(200),
    error           TEXT,
    error_date_time TIMESTAMP,
    CONSTRAINT pk_audit_error PRIMARY KEY (id),
    CONSTRAINT fk_audit_audit_id FOREIGN KEY (audit_id) REFERENCES audit (id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE audit
DROP CONSTRAINT fk_audit_customer;

ALTER TABLE audit
ADD CONSTRAINT fk_audit_customer
FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE audit
DROP CONSTRAINT fk_audit_sync;

ALTER TABLE audit
ADD CONSTRAINT fk_audit_sync
FOREIGN KEY (id_sync) REFERENCES sync_log (id) ON UPDATE CASCADE ON DELETE CASCADE;

