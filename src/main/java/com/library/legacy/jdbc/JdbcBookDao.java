package com.library.legacy.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcBookDao - Raw JDBC demonstration (No Hibernate/Spring Data)
 * 
 * This class demonstrates JDBC (Java Database Connectivity):
 * - Connection management
 * - PreparedStatement (prevents SQL injection)
 * - ResultSet mapping (database rows to Java objects)
 * - Exception handling for SQL operations
 * - Resource cleanup with try-with-resources
 * 
 * Key differences from Hibernate/Spring Data JPA:
 * - Manual SQL writing
 * - Manual object mapping
 * - Manual connection management
 * - No automatic transaction management
 * 
 * This is for educational purposes. In production, use Spring Data JPA.
 */
public class JdbcBookDao {

    // Database connection details
    private static final String DB_URL = "jdbc:h2:mem:librarydb;MODE=MYSQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    static {
        try {
            // Load H2 driver (required for JDBC)
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found", e);
        }
    }

    /**
     * Get a database connection
     * 
     * In production, you would use a connection pool (HikariCP, C3P0)
     * instead of creating a new connection for each operation.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Find all books using raw JDBC
     * 
     * Demonstrates:
     * - PreparedStatement (prevents SQL injection)
     * - ResultSet (iterates over database rows)
     * - try-with-resources (auto-closes resources)
     * - Manual object mapping (ResultSet -> Book)
     */
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        // try-with-resources ensures Connection, PreparedStatement, ResultSet are closed
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iterate over each row in the result set
            while (rs.next()) {
                books.add(mapRow(rs)); // Map row to Book object
            }
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    /**
     * Find a book by ID
     */
    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id); // Set parameter (prevents SQL injection)
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching book: " + e.getMessage());
        }
        return null;
    }

    /**
     * Insert a new book
     * 
     * Demonstrates:
     * - PreparedStatement with RETURN_GENERATED_KEYS
     * - Getting auto-generated primary key
     */
    public long insert(Book book) {
        String sql = "INSERT INTO books (title, isbn, total_copies, available_copies) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getTotalCopies());
            ps.setInt(4, book.getAvailableCopies());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Return generated ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting book: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Update a book
     */
    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, isbn = ? WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setLong(3, book.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a book by ID
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
        return false;
    }

    /**
     * Map a ResultSet row to a Book object
     * 
     * This is the manual equivalent of what Hibernate does automatically:
     * ResultSet row -> Java object (ORM mapping)
     */
    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getLong("book_id"),
            rs.getString("title"),
            rs.getString("isbn"),
            rs.getInt("total_copies"),
            rs.getInt("available_copies")
        );
    }

    /**
     * Simple Book POJO for JDBC demo
     */
    public static class Book {
        private Long id;
        private String title;
        private String isbn;
        private int totalCopies;
        private int availableCopies;

        public Book(Long id, String title, String isbn, int totalCopies, int availableCopies) {
            this.id = id;
            this.title = title;
            this.isbn = isbn;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Getters
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getIsbn() { return isbn; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }

        @Override
        public String toString() {
            return "Book{id=" + id + ", title='" + title + "'}";
        }
    }
}
