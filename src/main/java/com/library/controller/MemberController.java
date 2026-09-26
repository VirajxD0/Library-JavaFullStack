package com.library.controller;

import com.library.dto.MemberDTO;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MemberController - Spring MVC Controller for Member JSP views
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * GET /members - List all members
     */
    @GetMapping
    public String listMembers(Model model) {
        List<MemberDTO> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "member/list-members";
    }

    /**
     * GET /members/add - Show registration form
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("member", new MemberDTO());
        return "member/add-member";
    }

    /**
     * POST /members/save - Save a new member
     */
    @PostMapping("/save")
    public String saveMember(@ModelAttribute("member") MemberDTO memberDTO, Model model) {
        try {
            memberService.createMember(memberDTO);
            return "redirect:/members";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/add-member";
        }
    }

    /**
     * GET /members/edit/{id} - Show edit form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MemberDTO member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        return "member/edit-member";
    }

    /**
     * POST /members/update/{id} - Update member
     */
    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute("member") MemberDTO memberDTO) {
        memberService.updateMember(id, memberDTO);
        return "redirect:/members";
    }

    /**
     * GET /members/delete/{id} - Soft delete (deactivate) member
     */
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}
