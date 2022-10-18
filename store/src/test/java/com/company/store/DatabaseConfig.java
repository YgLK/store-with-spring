package com.company.store;


import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    // - - - - - - - - - -
    // PREPARED DATA:
    // - 9 Products
    // - 2 Users
    // - 2 Orders
    // - 7 OrderItems
    // - - - - - - - - - -


    // Fill test database with sample data
    public void fillTestDatabase(){
        try {
            // create connection
            Connection connection = getConnection();
            // execute queries
            // methods' call order matters, pay attention when changing its order
            addProducts(connection);
            addUsers(connection);
            addOrders(connection);
            addOrderItems(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Remove all the data from database
    public void cleanTestDatabase(EntityManager entityManager){
        try {
            // methods' call order matters (foreign keys), pay attention when changing its order
            deleteOrderItems(entityManager);
            deleteProducts(entityManager);
            deleteOrders(entityManager);
            deleteUsers(entityManager);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanTestDatabase(Connection connection){
        try {
            // methods' call order matters (foreign keys), pay attention when changing its order
            deleteOrderItems(connection);
            deleteProducts(connection);
            deleteOrders(connection);
            deleteUsers(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    // ADD DATA

    public void addProducts(Connection connection) throws SQLException {
        String addProductsQuery = " INSERT INTO store_products(price, productName, quantityStock) VALUES " +
                "(33, 'Shorts', 15), " +
                "(3, 'Milk chocolate', 12), " +
                "(2, 'Milk', 34), " +
                "(20,'Baseball cap', 23), " +
                "(50, 'Football ball', 17), " +
                "(31,'Sport T-shirt', 56), " +
                "(2.5, 'Pepper chips', 120), " +
                "(11.3, 'Quick snack', 324), " +
                "(4 ,'Hot dog', 51);";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(addProductsQuery);
    }

    public void addUsers(Connection connection) throws SQLException {
        String addUsersQuery = "INSERT INTO store_users(email, password, firstname, lastname, role, enabled) VALUES" +
                "('my@email.com', '$2a$10$MMSaTF3.y5Y.hziKiIdkz.LQpta9hxyYiNEUKv52gD0g30m5iv2yK', 'Greg', 'Josh','ROLE_USER', 1)," +
                "('admin', '$2a$10$oAtp0x39eNBPmCcHl1iztOZTSdbwroKNlgtDlVr5wofHUoeUYqhka', 'Carl', 'Johnson','ROLE_ADMIN', 1);";
        //    Password encryption:
        //    car123 -> $2a$10$oAtp0x39eNBPmCcHl1iztOZTSdbwroKNlgtDlVr5wofHUoeUYqhka
        //    123 -> $2a$10$MMSaTF3.y5Y.hziKiIdkz.LQpta9hxyYiNEUKv52gD0g30m5iv2yK

        //    sample credentials:
        //    (1) Login: my@mail.com
        //    Password: 123
        //    (2) Login: admin
        //    Password: car123

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(addUsersQuery);
    }

    public void addOrders(Connection connection) throws SQLException {
        String addOrdersQuery = "INSERT INTO store_orders(orderDate, user_id) VALUES" +
                "(NOW(), 2)," +
                "(NOW(), 1);";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(addOrdersQuery);

    }

    public void addOrderItems(Connection connection) throws SQLException {
        String addOrdersQuery = "INSERT INTO store_order_items(order_id, product_id, quantity) VALUES\n" +
                "(1, 3, 5)," +
                "(1, 5, 10)," +
                "(1, 1, 9)," +
                "(1, 7, 13)," +
                "(2, 3, 3)," +
                "(2, 9, 6)," +
                "(2, 4, 5);";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(addOrdersQuery);
    }


    // DELETE DATA


    // delete with SQL query

    public void deleteUsers(Connection connection) throws SQLException {
        String deleteUsersQuery = "DELETE FROM store_users; ALTER TABLE store_users AUTO_INCREMENT = 1;";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(deleteUsersQuery);
    }

    public void deleteProducts(Connection connection) throws SQLException {
        String deleteProductsQuery = "DELETE FROM store_products; ALTER TABLE store_products AUTO_INCREMENT = 1;";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(deleteProductsQuery);
    }

    public void deleteOrders(Connection connection) throws SQLException {
        String deleteOrdersQuery = "DELETE FROM store_orders; ALTER TABLE store_orders AUTO_INCREMENT = 1;";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(deleteOrdersQuery);
    }

    public void deleteOrderItems(Connection connection) throws SQLException {
        String deleteOrderItemsQuery = "DELETE FROM store_order_items; ALTER TABLE store_order_items AUTO_INCREMENT = 1;";

        // execute the query
        Statement statement = connection.createStatement();
        statement.execute(deleteOrderItemsQuery);
    }


    // delete with EntityManager

    public void deleteProducts(EntityManager entityManager) throws SQLException {
        String deleteProductsQuery = "DELETE FROM Product p";

        // execute the query
        entityManager.createQuery(deleteProductsQuery).executeUpdate();
    }

    public void deleteUsers(EntityManager entityManager) throws SQLException {
        String deleteUsersQuery = "DELETE FROM User u";

        // execute the query
        entityManager.createQuery(deleteUsersQuery).executeUpdate();
    }

    public void deleteOrders(EntityManager entityManager) throws SQLException {
        String deleteOrdersQuery = "DELETE FROM Order";

        // execute the query
        entityManager.createQuery(deleteOrdersQuery).executeUpdate();
    }

    public void deleteOrderItems(EntityManager entityManager) throws SQLException {
        String deleteOrderItemsQuery = "DELETE FROM OrderItem o";

        // execute the query
        entityManager.createQuery(deleteOrderItemsQuery).executeUpdate();
    }


}
