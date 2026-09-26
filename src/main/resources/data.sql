-- ============================================================
-- Library Management System - Seed Data
-- This file is executed on application startup
-- ============================================================

-- Categories
INSERT INTO categories (name, description, created_at, updated_at) VALUES
('Fiction', 'Fictional literature and novels', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Non-Fiction', 'Non-fictional works and essays', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Technology', 'Computer Science and Technology', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Science', 'Scientific publications', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('History', 'Historical works and accounts', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Authors
INSERT INTO authors (first_name, last_name, email, biography, created_at, updated_at) VALUES
('James', 'Gosling', 'james@gosling.dev', 'Creator of Java programming language', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Joshua', 'Bloch', 'josh@bloch.dev', 'Author of Effective Java', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Craig', 'Walls', 'craig@walls.dev', 'Spring in Action author', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Robert', 'Martin', 'robert@clean.dev', 'Author of Clean Code', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Publishers
INSERT INTO publishers (name, address, website, created_at) VALUES
('Penguin Random House', 'New York, USA', 'https://penguinrandomhouse.com', CURRENT_TIMESTAMP),
('O''Reilly Media', 'Sebastopol, CA', 'https://oreilly.com', CURRENT_TIMESTAMP),
('McGraw-Hill', 'New York, USA', 'https://mheducation.com', CURRENT_TIMESTAMP);

-- Books
INSERT INTO books (title, isbn, publication_year, total_copies, available_copies, description, language, category_id, publisher_id, created_at, updated_at) VALUES
('Effective Java', '978-0134685991', 2018, 5, 3, 'Best practices for Java programming', 'English', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring in Action', '978-1617294947', 2022, 3, 2, 'Comprehensive guide to Spring Framework', 'English', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Clean Code', '978-0132350884', 2008, 6, 5, 'A handbook of agile software craftsmanship', 'English', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Head First Java', '978-0596009205', 2005, 3, 3, 'A brain-friendly guide to Java', 'English', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Design Patterns', '978-0201633610', 1994, 4, 4, 'Elements of reusable object-oriented software', 'English', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Book-Author relationships (Many-to-Many)
INSERT INTO book_authors (book_id, author_id) VALUES
(1, 2),  -- Effective Java -> Joshua Bloch
(2, 3),  -- Spring in Action -> Craig Walls
(3, 4),  -- Clean Code -> Robert Martin
(4, 1),  -- Head First Java -> James Gosling
(5, 1);  -- Design Patterns -> James Gosling

-- Members
INSERT INTO members (first_name, last_name, email, phone, membership_date, membership_type, active, created_at, updated_at) VALUES
('Alice', 'Johnson', 'alice@email.com', '555-0101', '2024-01-15', 'STANDARD', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob', 'Smith', 'bob@email.com', '555-0102', '2024-02-20', 'PREMIUM', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Charlie', 'Brown', 'charlie@email.com', '555-0103', '2024-03-10', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
