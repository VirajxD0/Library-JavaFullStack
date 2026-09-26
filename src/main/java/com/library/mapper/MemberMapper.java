package com.library.mapper;

import com.library.dto.MemberDTO;
import com.library.entity.Member;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * MemberMapper - Converts between Member entity and MemberDTO
 */
@Component
public class MemberMapper {

    /**
     * Convert Member entity to MemberDTO
     */
    public MemberDTO toDTO(Member member) {
        if (member == null) return null;

        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.getMemberId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setAddress(member.getAddress());
        dto.setMembershipDate(member.getMembershipDate());
        dto.setMembershipType(member.getMembershipType());
        dto.setActive(member.getActive());
        return dto;
    }

    /**
     * Convert MemberDTO to Member entity
     */
    public Member toEntity(MemberDTO dto) {
        if (dto == null) return null;

        Member member = new Member();
        member.setMemberId(dto.getMemberId());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setMembershipDate(dto.getMembershipDate());
        member.setMembershipType(dto.getMembershipType());
        member.setActive(dto.getActive());
        return member;
    }

    public List<MemberDTO> toDTOList(List<Member> members) {
        List<MemberDTO> dtos = new ArrayList<>();
        for (Member member : members) {
            dtos.add(toDTO(member));
        }
        return dtos;
    }
}
