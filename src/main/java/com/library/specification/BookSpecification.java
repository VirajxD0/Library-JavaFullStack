package com.library.specification;

import com.library.dto.BookSearchCriteria;
import com.library.entity.Book;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * BookSpecification - builds a JPA Specification from BookSearchCriteria.
 * Combines keyword/title/isbn/category/author/language/year/availability
 * with AND semantics so the API supports true multi-criteria search.
 */
public final class BookSpecification {

    private BookSpecification() {}

    public static Specification<Book> fromCriteria(BookSearchCriteria criteria) {
        Specification<Book> spec = Specification.where(null);
        if (criteria == null) {
            return spec;
        }

        if (StringUtils.hasText(criteria.getKeyword())) {
            String like = "%" + criteria.getKeyword().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("isbn")), like)));
        }

        if (StringUtils.hasText(criteria.getTitle())) {
            String like = "%" + criteria.getTitle().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), like));
        }

        if (StringUtils.hasText(criteria.getIsbn())) {
            String like = "%" + criteria.getIsbn().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("isbn")), like));
        }

        if (criteria.getCategoryId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("categoryId"), criteria.getCategoryId()));
        }

        if (criteria.getAuthorId() != null) {
            spec = spec.and((root, query, cb) -> {
                var authors = root.join("authors", JoinType.INNER);
                query.distinct(true);
                return cb.equal(authors.get("authorId"), criteria.getAuthorId());
            });
        }

        if (StringUtils.hasText(criteria.getLanguage())) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("language")), criteria.getLanguage().toLowerCase()));
        }

        if (criteria.getPublicationYear() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("publicationYear"), criteria.getPublicationYear()));
        }

        if (Boolean.TRUE.equals(criteria.getAvailableOnly())) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThan(root.get("availableCopies"), 0));
        }

        return spec;
    }
}
