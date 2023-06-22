INSERT INTO customer_legacy (id, id_customer, id_branch, id_erp_account, name, name_erp, is_bill_to)
VALUES (uuid_in(md5(random()::text || random()::text)::cstring), '123', '2006', '123', 'AMCO ELECTRIC CO OF LUBBOCK FALSE', 'ECLIPSE', true);

INSERT INTO product_legacy (id, id_product, name, description, image_full_size, price, part_number, unit_of_measure,
                            manufacturer, category_1_code, category_1_name, unspsc, image_thumb, scale_start, scale_end,
                            list_price, manufacturer_part_number, category_2_code, category_2_name, category_3_code,
                            category_3_name, category_4_code, category_4_name, category_5_code, category_5_name,
                            delivery_in_days, buyer_id)
VALUES (uuid_in(md5(random()::text || random()::text)::cstring),'MSC-20544', '4" PVC DWV Repair Coupling', 'Coupling Fitting; Type Repair; Size A 4"; Size B 4"; End Connection Hub x Hub; Material PVC; Material Specification ASTM D2665/F1866; Temperature Rating 140 Deg F; Application DWV, Sewer, Strom Drainage; Applicable Standard NSF; Warranty 5 Year Limited',
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_LRG.jpg', 1, '30544', 'meters', 'Generic', 'DFT_PRD_CC1', 'DFT_PRD_CN1', '40172608', 'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_SML.jpg', 0, 0, 0, 'PDWVSCP',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'MSC-30544'),
       (uuid_in(md5(random()::text || random()::text)::cstring), 'MSC-20546', '4" PVC DWV Repair Coupling 2', 'Coupling Fitting; Type Repair; Size A 4"; Size B 4"; End Connection Hub x Hub; Material PVC; Material Specification ASTM D2665/F1866; Temperature Rating 140 Deg F; Application DWV, Sewer, Strom Drainage; Applicable Standard NSF; Warranty 5 Year Limited',
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_LRG.jpg', 1, '30544', 'meters', 'Generic', 'DFT_PRD_CC1', 'DFT_PRD_CN1', '40172608', 'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_SML.jpg', 0, 0, 0, 'PDWVSCP',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'MSC-30544'),
       (uuid_in(md5(random()::text || random()::text)::cstring), 'MSC-20545', '5" PVC DWV Repair Coupling 6', 'Coupling Fitting; Type Repair; Size A 4"; Size B 4"; End Connection Hub x Hub; Material PVC; Material Specification ASTM D2665/F1866; Temperature Rating 140 Deg F; Application DWV, Sewer, Strom Drainage; Applicable Standard NSF; Warranty 5 Year Limited',
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_LRG.jpg', 1, '30544', 'meters', 'Generic', 'DFT_PRD_CC1', 'DFT_PRD_CN1', '40172608', 'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_SML.jpg', 0, 0, 0, 'PDWVSCP',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'MSC-30544'),
       (uuid_in(md5(random()::text || random()::text)::cstring), 'MSC-20547', '5" PVC DWV Repair Coupling 6', 'Coupling Fitting; Type Repair; Size A 4"; Size B 4"; End Connection Hub x Hub; Material PVC; Material Specification ASTM D2665/F1866; Temperature Rating 140 Deg F; Application DWV, Sewer, Strom Drainage; Applicable Standard NSF; Warranty 5 Year Limited',
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_LRG.jpg', 1, '30544', 'meters', 'Generic', 'DFT_PRD_CC1', 'DFT_PRD_CN1', '40172608', 'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-130_4IN_SML.jpg', 0, 0, 0, 'PDWVSCP',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'MSC-30544');

INSERT INTO catalog_legacy (id, id_customer, id_branch, id_product)
VALUES (uuid_in(md5(random()::text || random()::text)::cstring), '123', '2006', 'MSC-20544'),
       (uuid_in(md5(random()::text || random()::text)::cstring), '123', '2006', 'MSC-20546'),
       (uuid_in(md5(random()::text || random()::text)::cstring), '123', '2006', 'MSC-20545'),
       (uuid_in(md5(random()::text || random()::text)::cstring), '123', '2006', 'MSC-20547');