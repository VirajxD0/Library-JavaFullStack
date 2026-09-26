package com.library.service.impl;

import com.library.dto.MemberDTO;
import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.MemberMapper;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * MemberServiceImpl - Business logic for Member operations
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return memberMapper.toDTOList(members);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        return memberMapper.toDTO(member);
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        if (member.getMembershipDate() == null) {
            member.setMembershipDate(LocalDate.now());
        }
        member.setActive(true);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toDTO(savedMember);
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));

        if (memberDTO.getFirstName() != null) existing.setFirstName(memberDTO.getFirstName());
        if (memberDTO.getLastName() != null) existing.setLastName(memberDTO.getLastName());
        if (memberDTO.getEmail() != null) existing.setEmail(memberDTO.getEmail());
        if (memberDTO.getPhone() != null) existing.setPhone(memberDTO.getPhone());
        if (memberDTO.getAddress() != null) existing.setAddress(memberDTO.getAddress());

        Member updated = memberRepository.save(existing);
        return memberMapper.toDTO(updated);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        member.setActive(false); // Soft delete - deactivate instead of removing
        memberRepository.save(member);
    }
}
