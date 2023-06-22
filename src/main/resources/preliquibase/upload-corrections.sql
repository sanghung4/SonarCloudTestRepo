-- FK to customer
ALTER TABLE upload
DROP CONSTRAINT fk_upload_customer;

ALTER TABLE upload
ADD CONSTRAINT fk_upload_customer
FOREIGN KEY (id_customer) REFERENCES customer (id) ON UPDATE CASCADE ON DELETE CASCADE;

-- FK to user
ALTER TABLE upload
DROP CONSTRAINT fk_upload_user;

ALTER TABLE upload
ADD CONSTRAINT fk_upload_user
FOREIGN KEY (id_user) REFERENCES authorized_user (id) ON UPDATE CASCADE ON DELETE CASCADE;

