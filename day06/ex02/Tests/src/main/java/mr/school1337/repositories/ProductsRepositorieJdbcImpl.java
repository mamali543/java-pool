package main.java.mr.school1337.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import main.java.mr.school1337.models.Product;
import main.java.mr.school1337.repositories.ProductsRepositorie;

public class ProductsRepositorieJdbcImpl implements ProductsRepositorie {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private String tableName = "test.product";
    DataSource dataSource;

    public ProductsRepositorieJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM " + tableName;
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    Double price = rs.getDouble("price");
                    products.add(new Product(id, name, price));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return products;
    }

    @Override
    public Optional<Product> findById(Long id)
    {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        Product product = null;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                     product = new Product(rs.getLong("id"), rs.getString("name"), rs.getDouble("price"));
                }
                else
                    new RuntimeException();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Optional.of(product);
    }

    @Override
    public void update(Product product){
        String query = "UPDATE "+tableName+" SET name = ? ,price = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setLong(3, product.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0)
            {
                try(ResultSet rs = stmt.getGeneratedKeys()){
                    if (rs.next()){
                        String name = rs.getString(2);
                        System.out.println(ANSI_BLUE + "New product name: " + name + ANSI_RESET);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void save(Product product){
        String query = "INSERT INTO " + tableName + " (name, price) VALUES (?, ?)";

        // Using JDBC to execute the insert and retrieve the generated key
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            int affectedRows = pstmt.executeUpdate();
        
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1); // Retrieve the first field in the ResultSet
                        product.setId(id);
                        System.out.println("Generated ID: " + id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id){
        String query = "DELETE FROM " + tableName + " WHERE id = ?"; 
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate(); 
    
            if (affectedRows > 0) {
                System.out.println("Deleted successfully. Rows affected: " + affectedRows);
            } else {
                System.out.println("No rows deleted. Check if the ID exists.");
            }
        } catch (SQLException e) {
            System.err.println("SQL error during delete: " + e.getMessage());
        }
    }
}