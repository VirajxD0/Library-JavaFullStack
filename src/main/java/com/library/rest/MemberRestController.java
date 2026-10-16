package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.MemberDTO;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MemberRestController - REST API for Member operations (librarian only).
 */
@RestController
@RequestMapping("/api/v1/members")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class MemberRestController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getAllMembers() {
        List<MemberDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDTO>> getMemberById(@PathVariable Long id) {
        MemberDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(member));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberDTO>> createMember(
            @Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO created = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Member registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO updated = memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Member updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Member deactivated successfully"));
    }
}
