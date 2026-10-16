-- ============================================================
-- V2 - Performance indexes for the access patterns actually hit:
-- book search (title/isbn), category filter, borrow lookups.
-- ============================================================

CREATE INDEX idx_books_isbn ON books (isbn);
CREATE INDEX idx_books_language ON books (language);
CREATE INDEX idx_members_email ON members (email);
CREATE INDEX idx_users_username ON users (username);
