package com.library.controller;

import com.library.dto.BorrowDTO;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BorrowController - Spring MVC Controller for Borrow JSP views
 */
@Controller
@RequestMapping("/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    /**
     * GET /borrows - List all borrow records
     */
    @GetMapping
    public String listBorrows(Model model) {
        List<BorrowDTO> borrows = borrowService.getAllBorrows();
        model.addAttribute("borrows", borrows);
        return "borrow/borrow-history";
    }

    /**
     * GET /borrows/borrow - Show borrow form
     */
    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {
        model.addAttribute("books", bookService.getAvailableBooks());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("request", new java.util.HashMap<>());
        return "borrow/borrow-book";
    }

    /**
     * POST /borrows/borrow - Process book borrowing
     */
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long bookId, @RequestParam Long memberId, Model model) {
        try {
            borrowService.borrowBook(bookId, memberId);
            return "redirect:/borrows";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("books", bookService.getAvailableBooks());
            model.addAttribute("members", memberService.getAllMembers());
            return "borrow/borrow-book";
        }
    }

    /**
     * GET /borrows/return/{id} - Return a borrowed book
     */
    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
        return "redirect:/borrows";
    }

    /**
     * GET /borrows/overdue - Show overdue borrows
     */
    @GetMapping("/overdue")
    public String listOverdue(Model model) {
        List<BorrowDTO> overdue = borrowService.getOverdueBorrows();
        model.addAttribute("borrows", overdue);
        return "borrow/overdue-list";
    }
}
