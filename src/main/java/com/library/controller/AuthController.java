package com.library.controller;

import com.library.dto.RegisterRequest;
import com.library.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * AuthController - Handles JSP views for Login / Register / AccessDenied
 * 
 * Spring Security handles POST /login automatically, but we still provide GET /login view
 * and manual /register handling via AuthService.
 * 
 * Flow:
 * - GET /login -> auth/login.jsp (form posts to /login)
 * - POST /login -> Spring Security authenticates, redirects to /dashboard or /login?error
 * - GET /register -> auth/register.jsp
 * - POST /register -> AuthService.register() then redirect to /login
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // resolves to /WEB-INF/jsp/auth/login.jsp
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            authService.register(request);
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/login?registered=true";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
