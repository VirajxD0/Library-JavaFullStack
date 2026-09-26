package com.library.controller;

import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DashboardController - Main dashboard and home page
 * 
 * This controller handles the main dashboard view that shows:
 * - Total books count
 * - Total members count
 * - Active borrows count
 * - Recent activity
 */
@Controller
public class DashboardController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BorrowService borrowService;

    /**
     * GET /dashboard - Show the dashboard with statistics
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Pass statistics to the dashboard JSP
        model.addAttribute("totalBooks", bookService.getAllBooks(0, 1000).size());
        model.addAttribute("totalMembers", memberService.getAllMembers().size());
        model.addAttribute("activeBorrows", borrowService.getActiveBorrowCount());
        model.addAttribute("overdueBorrows", borrowService.getOverdueBorrows().size());
        model.addAttribute("availableBooks", bookService.getAvailableBooks().size());
        return "dashboard/dashboard";
    }
}
