package com.library.service;

import com.library.dto.MemberDTO;
import java.util.List;

/**
 * MemberService Interface - Business operations for library members
 */
public interface MemberService {

    List<MemberDTO> getAllMembers();
    MemberDTO getMemberById(Long id);
    MemberDTO createMember(MemberDTO memberDTO);
    MemberDTO updateMember(Long id, MemberDTO memberDTO);
    void deleteMember(Long id);
}
