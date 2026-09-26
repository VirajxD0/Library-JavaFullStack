package com.library.mapper;

import com.library.dto.BorrowDTO;
import com.library.entity.BorrowRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * BorrowMapper - Converts between BorrowRecord entity and BorrowDTO
 */
@Component
public class BorrowMapper {

    /**
     * Convert BorrowRecord entity to BorrowDTO
     * Flattens Book and Member info into simple fields
     */
    public BorrowDTO toDTO(BorrowRecord record) {
        if (record == null) return null;

        BorrowDTO dto = new BorrowDTO();
        dto.setBorrowId(record.getBorrowId());

        // Flatten Book info
        if (record.getBook() != null) {
            dto.setBookId(record.getBook().getBookId());
            dto.setBookTitle(record.getBook().getTitle());
            dto.setBookIsbn(record.getBook().getIsbn());
        }

        // Flatten Member info
        if (record.getMember() != null) {
            dto.setMemberId(record.getMember().getMemberId());
            dto.setMemberName(record.getMember().getFullName());
            dto.setMemberEmail(record.getMember().getEmail());
        }

        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus());
        dto.setFineAmount(record.getFineAmount());
        dto.setOverdue(record.isOverdue());
        dto.setDaysOverdue(record.calculateDaysOverdue());

        return dto;
    }

    public List<BorrowDTO> toDTOList(List<BorrowRecord> records) {
        List<BorrowDTO> dtos = new ArrayList<>();
        for (BorrowRecord record : records) {
            dtos.add(toDTO(record));
        }
        return dtos;
    }
}
