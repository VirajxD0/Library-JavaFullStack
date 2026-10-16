-- ============================================================
-- Flyway V1 - Baseline schema for Library Management System
-- Compatible with MySQL 8 and H2 (MODE=MYSQL).
-- Entities remain the source of truth; this script mirrors them
-- so prod can run with ddl-auto=validate + Flyway.
-- ============================================================

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS authors (
    author_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    biography TEXT,
    date_of_birth DATE,
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS publishers (
    publisher_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    website VARCHAR(200),
    phone VARCHAR(20),
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    publication_year INT,
    edition VARCHAR(50),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    description TEXT,
    language VARCHAR(30),
    category_id BIGINT,
    publisher_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_books_category FOREIGN KEY (category_id) REFERENCES categories (category_id),
    CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id) REFERENCES publishers (publisher_id)
);

CREATE TABLE IF NOT EXISTS book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_book_authors_book FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE,
    CONSTRAINT fk_book_authors_author FOREIGN KEY (author_id) REFERENCES authors (author_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS members (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(300),
    membership_date DATE NOT NULL,
    membership_type VARCHAR(20),
    active BOOLEAN NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS borrow_records (
    borrow_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) NOT NULL,
    fine_amount DECIMAL(10, 2),
    created_at DATETIME,
    CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES books (book_id),
    CONSTRAINT fk_borrow_member FOREIGN KEY (member_id) REFERENCES members (member_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    member_id BIGINT UNIQUE,
    CONSTRAINT fk_users_member FOREIGN KEY (member_id) REFERENCES members (member_id)
);

CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_category ON books (category_id);
CREATE INDEX idx_borrow_status_due ON borrow_records (status, due_date);
CREATE INDEX idx_borrow_member ON borrow_records (member_id);
