package com.library.legacy.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * BookServlet - Raw Servlet demonstration (No Spring MVC)
 * 
 * This class demonstrates the Servlet API:
 * - HttpServlet: Base class for HTTP servlets
 * - @Servlet: Annotation-based servlet registration (替代 web.xml)
 * - doGet(): Handle HTTP GET requests
 * - doPost(): Handle HTTP POST requests
 * - Request/Response objects
 * - RequestDispatcher for forwarding
 * 
 * Key differences from Spring MVC:
 * - No dependency injection
 * - Manual request parameter extraction
 * - Manual response writing
 * - No automatic view resolution
 * 
 * This is for educational purposes. In production, use Spring MVC.
 */
@WebServlet("/legacy/books/*")
public class BookServlet extends HttpServlet {

    /**
     * Initialization - called once when the servlet is first loaded
     * Equivalent to @PostConstruct in Spring
     */
    @Override
    public void init() throws ServletException {
        System.out.println("BookServlet initialized");
    }

    /**
     * Handle GET requests
     * 
     * Demonstrates:
     * - Request path parsing
     * - Response writing (PrintWriter)
     * - Setting response content type
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Set response content type
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            // List all books
            out.println("<html><head><title>Legacy Books</title></head><body>");
            out.println("<h1>Legacy Book Servlet - All Books</h1>");
            out.println("<p>This is a raw Servlet (no Spring MVC)</p>");
            out.println("<ul>");
            out.println("<li>Effective Java</li>");
            out.println("<li>Clean Code</li>");
            out.println("<li>Head First Java</li>");
            out.println("</ul>");
            out.println("<a href='/'>Back to Dashboard</a>");
            out.println("</body></html>");
        } else {
            // Get book by ID
            String id = path.substring(1);
            out.println("<html><head><title>Book Details</title></head><body>");
            out.println("<h1>Book Details (ID: " + id + ")</h1>");
            out.println("<p>This is a raw Servlet response</p>");
            out.println("<a href='/legacy/books'>Back to List</a>");
            out.println("</body></html>");
        }
    }

    /**
     * Handle POST requests
     * 
     * Demonstrates:
     * - Reading form parameters (req.getParameter)
     * - Redirect after POST (POST-Redirect-GET pattern)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read form parameters
        String title = req.getParameter("title");
        String isbn = req.getParameter("isbn");

        // Process the data (in real app, save to database)
        System.out.println("Saving book: " + title + " (ISBN: " + isbn + ")");

        // Redirect to GET (PRG pattern prevents form resubmission)
        resp.sendRedirect(req.getContextPath() + "/legacy/books");
    }

    /**
     * Cleanup - called when the servlet is destroyed
     * Equivalent to @PreDestroy in Spring
     */
    @Override
    public void destroy() {
        System.out.println("BookServlet destroyed");
    }
}
