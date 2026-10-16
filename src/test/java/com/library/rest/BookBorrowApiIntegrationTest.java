package com.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API integration tests - pagination, RBAC (librarian vs member),
 * and the borrow/return lifecycle with fine calculation.
 * Full Spring context on H2; auth simulated with mock users.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookBorrowApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "MEMBER")
    void listBooks_returnsPagedEnvelope() throws Exception {
        mockMvc.perform(get("/api/v1/books?page=0&size=2&sort=title,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.size").value(2))
                .andExpect(jsonPath("$.data.totalElements").exists());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void advancedSearch_filtersByTitle_andAvailability() throws Exception {
        mockMvc.perform(get("/api/v1/books/search/advanced")
                        .param("title", "java")
                        .param("availableOnly", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void createBook_asMember_isForbidden() throws Exception {
        BookDTO dto = new BookDTO();
        dto.setTitle("Forbidden Book");
        dto.setIsbn("978-0000000991");
        dto.setTotalCopies(1);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void createBook_asLibrarian_returns201() throws Exception {
        BookDTO dto = new BookDTO();
        dto.setTitle("Librarian Book");
        dto.setIsbn("978-0000000992");
        dto.setTotalCopies(2);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Librarian Book"));
    }

    @Test
    void books_withoutAuth_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void borrow_and_return_lifecycle() throws Exception {
        // Borrow book 1 as member 1 (seeded data)
        String borrowBody = "{\"bookId\":1,\"memberId\":1}";
        String borrowResponse = mockMvc.perform(post("/api/v1/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(borrowBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("BORROWED"))
                .andExpect(jsonPath("$.data.dueDate").exists())
                .andReturn().getResponse().getContentAsString();

        Long borrowId = objectMapper.readTree(borrowResponse).path("data").path("borrowId").asLong();

        // Return it - on-time return has zero fine
        mockMvc.perform(put("/api/v1/borrows/" + borrowId + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RETURNED"))
                .andExpect(jsonPath("$.data.fineAmount").value(0));

        // Returning twice is a conflict
        mockMvc.perform(put("/api/v1/borrows/" + borrowId + "/return"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void borrow_missingIds_isBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void overdue_and_stats_endpoints_work() throws Exception {
        mockMvc.perform(get("/api/v1/borrows/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(get("/api/v1/borrows/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeBorrows").exists())
                .andExpect(jsonPath("$.data.overdueBorrows").exists());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void validationFailure_returns400() throws Exception {
        BookDTO dto = new BookDTO(); // missing title/isbn
        dto.setTotalCopies(1);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Validation failed")));
    }
}
