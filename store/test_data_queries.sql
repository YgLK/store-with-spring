#-------- README--------------ś
# Sample login credentials:
# email: admin
# password: cat123
#------------------------------------

# Fill the database with sample data for testing
INSERT INTO store.store_products(price, productName, quantityStock) VALUES
                                     (33, "Shorts", 15),
                                     (3, "Milk chocolate", 12),
                                     (2, "Milk", 34),
                                     (20,"Baseball cap", 23),
                                     (50, "Football ball", 17),
                                     (31,"Sport T-shirt", 56),
                                     (2.5, "Pepper chips", 120),
                                     (11.3, "Quick snack", 324),
                                     (4 ,"Hot dog", 51);

INSERT INTO store.store_users(email, password, firstname, lastname, role, enabled) VALUES
                                    ("my@email.com", "$2a$10$MMSaTF3.y5Y.hziKiIdkz.LQpta9hxyYiNEUKv52gD0g30m5iv2yK", "Greg", "Josh",'ROLE_USER', 1),
                                    ("admin", "$2a$10$oAtp0x39eNBPmCcHl1iztOZTSdbwroKNlgtDlVr5wofHUoeUYqhka", "Carl", "Johnson",'ROLE_ADMIN', 1);

# Password encryption:
# car123 -> $2a$10$oAtp0x39eNBPmCcHl1iztOZTSdbwroKNlgtDlVr5wofHUoeUYqhka
# 123 -> $2a$10$MMSaTF3.y5Y.hziKiIdkz.LQpta9hxyYiNEUKv52gD0g30m5iv2yK

INSERT INTO store.store_orders(orderDate, user_id) VALUES
                                                       (NOW(), 2),
                                                       (NOW(), 1);

INSERT INTO store.store_order_items(order_id, product_id, quantity) VALUES
                                                       (1, 3, 5),
                                                       (1, 5, 10),
                                                       (1, 1, 9),
                                                       (1, 7, 13),
                                                       (2, 3, 3),
                                                       (2, 9, 6),
                                                       (2, 4, 5);

# -- DB QUERIES --
# Get all the data in one table
SELECT * FROM store_users su
                  INNER JOIN store_orders so ON su.id = so.user_id
                  INNER JOIN store_order_items soi ON so.id = soi.order_id
                  INNER JOIN store_products sp ON soi.product_Id = sp.id;

# Get order summary data - used in Order repository
SELECT so.id AS order_id, so.orderDate, su.firstname, su.lastname, su.email, SUM(soi.quantity * sp.price) AS summary FROM store_users su
                  INNER JOIN store_orders so ON su.id = so.user_id
                  INNER JOIN store_order_items soi ON so.id = soi.order_id
                  INNER JOIN store_products sp ON soi.product_Id = sp.id
                  GROUP BY so.id;


UPDATE store_users SET role = 'ROLE_USER';