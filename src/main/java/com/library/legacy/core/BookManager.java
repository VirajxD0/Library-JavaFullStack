package com.library.legacy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookManager - Pure Core Java demonstration (No frameworks)
 * 
 * This class demonstrates Core Java concepts WITHOUT using any framework:
 * - OOP: Classes, Objects, Encapsulation, Inheritance
 * - Collections Framework: ArrayList, HashMap
 * - Exception Handling: try-catch-finally
 * - Generics: Generic methods and classes
 * - Lambda Expressions (Java 8+)
 * - Stream API (Java 8+)
 * 
 * This is shown for comparison with the Spring/Hibernate approach.
 * In a real project, you would use the Spring Service layer instead.
 */
public class BookManager {

    // Inner class - demonstrates encapsulation
    private static class Book {
        private Long id;
        private String title;
        private String isbn;
        private int totalCopies;
        private int availableCopies;

        // Constructor
        public Book(Long id, String title, String isbn, int totalCopies) {
            this.id = id;
            this.title = title;
            this.isbn = isbn;
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
        }

        // Business logic method
        public boolean isAvailable() {
            return availableCopies > 0;
        }

        // Getters
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getIsbn() { return isbn; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }

        public void decreaseCopies() {
            if (availableCopies > 0) availableCopies--;
        }

        public void increaseCopies() {
            if (availableCopies < totalCopies) availableCopies++;
        }

        @Override
        public String toString() {
            return "Book{id=" + id + ", title='" + title + "', available=" + availableCopies + "}";
        }
    }

    // In-memory storage using Collections Framework
    private Map<Long, Book> bookStore = new HashMap<>();
    private Long nextId = 1L;

    /**
     * Add a book - demonstrates Collections Framework (HashMap)
     */
    public Book addBook(String title, String isbn, int totalCopies) {
        Book book = new Book(nextId++, title, isbn, totalCopies);
        bookStore.put(book.getId(), book);
        return book;
    }

    /**
     * Find book by ID - demonstrates exception handling
     */
    public Book findBookById(Long id) {
        try {
            Book book = bookStore.get(id);
            if (book == null) {
                throw new IllegalArgumentException("Book not found with id: " + id);
            }
            return book;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search books - demonstrates Stream API and Lambda expressions
     * 
     * Java 8+ features:
     * - Lambda expression: (book) -> book.getTitle().contains(keyword)
     * - Stream API: .stream().filter().collect()
     */
    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();

        // Using Stream API with Lambda expression
        bookStore.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(results::add); // Method reference

        return results;
    }

    /**
     * Get all books - demonstrates for-each loop (Core Java)
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookStore.values());
    }

    /**
     * Borrow a book - demonstrates conditional logic
     */
    public boolean borrowBook(Long bookId) {
        Book book = findBookById(bookId);
        if (book != null && book.isAvailable()) {
            book.decreaseCopies();
            return true;
        }
        return false;
    }

    /**
     * Return a book
     */
    public boolean returnBook(Long bookId) {
        Book book = findBookById(bookId);
        if (book != null) {
            book.increaseCopies();
            return true;
        }
        return false;
    }

    /**
     * Demo method to show all Core Java concepts
     */
    public static void main(String[] args) {
        BookManager manager = new BookManager();

        // Add books
        manager.addBook("Effective Java", "978-0134685991", 5);
        manager.addBook("Clean Code", "978-0132350884", 3);
        manager.addBook("Head First Java", "978-0596009205", 4);

        // Display all books
        System.out.println("=== All Books ===");
        for (Book book : manager.getAllBooks()) {
            System.out.println(book);
        }

        // Search using Stream API
        System.out.println("\n=== Search Results (Java) ===");
        List<Book> results = manager.searchBooks("java");
        results.forEach(System.out::println);

        // Borrow a book
        System.out.println("\n=== Borrowing Book 1 ===");
        if (manager.borrowBook(1L)) {
            System.out.println("Book borrowed successfully!");
        }
        System.out.println("Book after borrowing: " + manager.findBookById(1L));
    }
}
