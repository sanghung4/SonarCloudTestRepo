delete from product;
DELETE FROM sync_log;


-- ---------------------------------------
-- Customer: TEST-01
-- ID: 46021ee0-86f1-43e3-ad2d-f55e10b24c50
-- ---------------------------------------

DELETE FROM customer where id = '46021ee0-86f1-43e3-ad2d-f55e10b24c50';

-- customer
INSERT INTO customer (
    id,
    id_customer,
    id_branch,
    branch_name,
    id_erp,
    name,
    is_bill_to,
    last_update,
    contact_name,
    contact_phone
)
VALUES (
    '46021ee0-86f1-43e3-ad2d-f55e10b24c50', -- id
    'TEST-01', -- id_customer
    'TEST-BRANCH-01', -- id_branch
    'TEST-BRANCH-01', -- branch_name
    'TEST-01', -- id_erp
    'TEST CUSTOMER 01', -- name
    true, -- is_bill_to
    '2023-05-17 00:00:00', -- last_update
    'Tester McTesty', -- contact_name
    '555-555-5555' -- contact_phone
) ON CONFLICT DO NOTHING;

-- catalog
INSERT INTO catalog (
    id,
    id_customer,
    status,
    file_name,
    name,
    last_update,
    date_archived,
    proc_system
) values (
    '875b7e90-fa26-4e21-95e7-0ae696d14758', --id
    '46021ee0-86f1-43e3-ad2d-f55e10b24c50', -- id_customer
    'DRAFT', --status
    'TEST CATALOG 01.csv', -- file_name
    'TEST CATALOG 01', -- name
    '2023-05-17 00:00:00', -- last_update
    NULL, -- date_archived
    'Greenwing' -- proc_system
) ON CONFLICT DO NOTHING;

-- customer_region
INSERT INTO customer_region (
    id,
    id_customer,
    name
) VALUES (
    'cc9237f8-f087-44c1-b1b1-b6b6c3274a56',
    '46021ee0-86f1-43e3-ad2d-f55e10b24c50',
    'FWCALL'
) ON CONFLICT DO NOTHING;

-- catalog_product
INSERT INTO catalog_product (
    id,
    id_catalog,
    part_number
) VALUES (
    'fd4f215c-75a4-4375-b770-69dbaafb5079',
    '875b7e90-fa26-4e21-95e7-0ae696d14758',
    'TEST-PART-01'
) ON CONFLICT DO NOTHING;

INSERT INTO catalog_product (
    id,
    id_catalog,
    part_number
) VALUES (
    'e42fab42-aa2a-4150-a749-cc737d7ce5d3',
    '875b7e90-fa26-4e21-95e7-0ae696d14758',
    'TEST-PART-02'
) ON CONFLICT DO NOTHING;

INSERT INTO catalog_product (
    id,
    id_catalog,
    part_number
) VALUES (
    'b8a9ad45-fe41-4489-8254-0d047cc1fb79',
    '875b7e90-fa26-4e21-95e7-0ae696d14758',
    'TEST-PART-03'
) ON CONFLICT DO NOTHING;

-- product
INSERT INTO product(
	id,
	name,
	description,
	image_full_size,
	part_number,
	manufacturer,
	category_1_name,
	category_2_name,
	category_3_name,
	unspsc,
	image_thumb,
	manufacturer_part_number,
	delivery_in_days,
	max_sync_datetime
) VALUES (
    '0bc60896-5391-4edf-a046-999947b3e12f', -- id
    'Test Product 04', -- name
    'Test Product 04 Description', -- description
    'https://www.reece.com/static/media/logo.18178d36.svg', -- image_full_size
    'TEST-PART-04', -- part_number
    'Test Manufacturer', -- manufacturer
    'Test Category 1', -- category_1_name
    'Test Category 2', -- category_2_name
    'Test Category 3', -- category_3_name
    'TEST-unspsc', -- unspsc
    'https://www.reece.com/static/media/logo.18178d36.svg', -- image_thumb
    'TEST-MFN-04', -- manufacturer_part_number
    7, -- delivery_in_days
    '2023-05-17 00:00:00' -- max_sync_datetime
) ON CONFLICT DO NOTHING;

INSERT INTO catalog_product (
    id,
    id_catalog,
    part_number,
    id_product
) VALUES (
    '35846cab-2b47-4643-a5e1-db093aeedcbd',
    '875b7e90-fa26-4e21-95e7-0ae696d14758',
    'TEST-PART-04',
    '0bc60896-5391-4edf-a046-999947b3e12f'
) ON CONFLICT DO NOTHING;

-- ---------------------------------------
-- Customer: TEST-02
-- ID: 4e8c299a-874d-4d5d-bd5a-40bbf2bbf062
-- Used for testing catalog operations
-- ---------------------------------------

DELETE FROM customer where id = '4e8c299a-874d-4d5d-bd5a-40bbf2bbf062';

-- customer
INSERT INTO customer (
    id,
    id_customer,
    id_branch,
    branch_name,
    id_erp,
    name,
    is_bill_to,
    last_update,
    contact_name,
    contact_phone
)
VALUES (
    '4e8c299a-874d-4d5d-bd5a-40bbf2bbf062', -- id
    'TEST-02', -- id_customer
    'TEST-BRANCH-02', -- id_branch
    'TEST-BRANCH-02', -- branch_name
    'TEST-02', -- id_erp
    'TEST CUSTOMER 02', -- name
    true, -- is_bill_to
    '2023-05-17 00:00:00', -- last_update
    'Tester McTestyFace', -- contact_name
    '555-555-6666' -- contact_phone
) ON CONFLICT DO NOTHING;

-- customer_region
INSERT INTO customer_region (
    id,
    id_customer,
    name
) VALUES (
    'f08b52e7-a7a1-414e-95a1-95e57e152457',
    '4e8c299a-874d-4d5d-bd5a-40bbf2bbf062',
    'FWCALL'
) ON CONFLICT DO NOTHING;

