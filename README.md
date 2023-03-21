<h1 align="center"> Web Store

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
[![Generic badge](https://img.shields.io/badge/MAVEN-<COLOR>.svg)](https://shields.io/)
[![Generic badge](https://img.shields.io/badge/coverage-60/100-yellow.svg)](https://shields.io/)
 </h1>


## Table of Contents

- [Instructions](#instructions)
<!---
 - [Project goals](#project_goals) -->
 

## Instructions <a name="instructions"></a>

MySQL database run in the Docker container is used in the project. <br> 
1. To run the Docker type in the following:
```console
cd store

docker-compose up -d
```

2. Open Docker Desktop and run the shown MySQL container.
3. Run the Tomcat Server from IntelliJ which will build the essential tables with the use of Hibernate.
4. Connect to the MySQL db and fill the database with the test data by entering the following [queries](./store/test_data_queries.sql):
```sql
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
```
5. Rerun the Tomcat Server.
6. All set up! All the data should be visible in the app.
