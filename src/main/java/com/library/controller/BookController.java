package com.library.controller;

import com.library.dto.BookDTO;
import com.library.service.BookService;
import com.library.service.CategoryService;
import com.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookController - Spring MVC Controller for JSP views
 * 
 * Key Differences from REST Controller:
 * - @Controller (not @RestController) - returns view names, not JSON
 * - Returns String (JSP view name) instead of ResponseEntity
 * - Uses Model to pass data to JSP pages
 * - Uses @ModelAttribute for form data binding
 * 
 * View Resolution:
 * When this controller returns "book/list-books", Spring resolves it to:
 *   /WEB-INF/jsp/book/list-books.jsp
 * (configured in application.properties: prefix + suffix)
 */
@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthorService authorService;

    /**
     * GET /books - Display list of all books
     * 
     * @param model - Spring's model object to pass data to JSP
     * @return JSP view name (resolved to /WEB-INF/jsp/book/list-books.jsp)
     */
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        List<BookDTO> books = bookService.getAllBooks(page, size);
        model.addAttribute("books", books); // Data available in JSP as ${books}
        return "book/list-books"; // View name
    }

    /**
     * GET /books/add - Show the add book form
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDTO()); // Empty book for form binding
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "book/add-book";
    }

    /**
     * POST /books/save - Save a new book
     * 
     * @ModelAttribute: Binds form data to the BookDTO object
     * @Valid: Triggers validation on the bound object
     * BindingResult: Contains validation errors (if any)
     */
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookDTO bookDTO, Model model) {
        try {
            bookService.createBook(bookDTO);
            return "redirect:/books"; // Redirect to book list after save
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("authors", authorService.getAllAuthors());
            return "book/add-book"; // Stay on form if error
        }
    }

    /**
     * GET /books/edit/{id} - Show edit form with existing book data
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookDTO book = bookService.getBookById(id);
        model.addAttribute("book", book); // Pre-populate form with existing data
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "book/edit-book";
    }

    /**
     * POST /books/update - Update an existing book
     */
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") BookDTO bookDTO, Model model) {
        try {
            bookService.updateBook(id, bookDTO);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("authors", authorService.getAllAuthors());
            return "book/edit-book";
        }
    }

    /**
     * GET /books/delete/{id} - Delete a book and redirect to list
     */
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    /**
     * GET /books/view/{id} - View book details
     */
    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        BookDTO book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book/view-book";
    }

    /**
     * GET /books/search - Search books
     */
    @GetMapping("/search")
    public String searchBooks(@RequestParam String keyword, Model model) {
        List<BookDTO> results = bookService.searchBooks(keyword);
        model.addAttribute("books", results);
        model.addAttribute("keyword", keyword);
        return "book/list-books";
    }
}
