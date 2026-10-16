# 📚 Library Management System

A production-quality **Library Management REST API + Web UI** built with **Java 17, Spring Boot, Spring MVC, Spring Data JPA and MySQL** — featuring JWT security, librarian/member RBAC, borrowing with fines, and full DevOps packaging.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![Tests](https://img.shields.io/badge/Tests-22_passing-brightgreen)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> **TL;DR** — `mvn spring-boot:run` → login at `http://localhost:8080/login` as `librarian / Librarian@123`. Swagger at `/swagger-ui.html`. Docker via `docker compose up --build`.

---

## 📑 Table of Contents

- [What This Project Does](#-what-this-project-does)
- [What Is Implemented](#-what-is-implemented)
- [Quick Start](#-quick-start)
- [User Roles & Permissions](#-user-roles--permissions)
- [API Reference](#-api-reference)
- [Borrowing & Fines](#-borrowing--fines)
- [Architecture](#️-architecture)
- [Database & Migrations](#️-database--migrations)
- [Tests](#-tests)
- [Project Layout](#-project-layout)
- [Tech Stack](#️-tech-stack)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

---

## 🎯 What This Project Does

This is a complete library system with **two front doors over one backend**:

1. **Web UI (JSP)** — librarians and members log in through a browser, browse the catalog, manage books/members, and handle borrowing from HTML pages.
2. **REST API (JSON)** — every operation is also exposed as versioned endpoints under `/api/v1/`, secured with JWT, documented in Swagger, and consumable by any client (mobile app, React frontend, scripts).

Underneath, a layered Spring Boot service owns all business rules (availability checks, due dates, fine math, role checks), persists to MySQL (or H2 for local dev) via JPA, and migrates its own schema with Flyway. The whole stack — app + database — runs with one `docker compose` command.

In short: **catalog + circulation + secure access + search + tests + docs + deployment, in one repo.**

---

## ✅ What Is Implemented

### 📖 Catalog Management
- Full CRUD for **books, authors, publishers, categories and members**.
- Books carry ISBN (unique), publication year, edition, language, total/available copy counts, and many-to-many author links via a `book_authors` join table.
- All write operations are **librarian-only**; members get read access.

### 🔄 Circulation (Borrow / Return)
- Borrow any in-stock book for a member → due date auto-set to **today + 14 days**, available copies decremented.
- Return calculates the fine automatically: **$0.50 per overdue day** (precise `BigDecimal` money math).
- Business rules enforced: no borrowing with zero copies (`409 Conflict`), no double returns (`409`), unknown IDs (`404`).
- Dedicated **overdue list** and **borrowing stats** endpoints for librarians.

### 🔐 Security
- **Spring Security 6** with two filter chains:
  - `/api/**` → stateless **JWT** (24 h expiry, `Authorization: Bearer` header).
  - JSP pages → stateful **form login** with sessions.
- Passwords hashed with **BCrypt** (salted, adaptive) — never stored or returned in plain text.
- **RBAC**: `ROLE_LIBRARIAN` vs `ROLE_MEMBER` (legacy `ROLE_ADMIN`/`ROLE_USER` still accepted), enforced at **both** URL level and method level (`@PreAuthorize`).
- Consistent JSON error model (`{success, message, data}`) for 400/401/403/404/409/500 via `@RestControllerAdvice`.

### 🔍 Search & Discovery
- Real pagination and sorting: `?page=0&size=10&sort=title,asc`, returning a page envelope (`content`, `page`, `size`, `totalElements`, `totalPages`, `last`).
- Multi-criteria advanced search combining keyword, title, ISBN, category, author, language, publication year and availability in one AND-query (JPA Specifications).
- Simple keyword search, available-books and by-category endpoints kept for the web UI.

### 🖥️ Web Interface
- 15 JSP/JSTL pages: login, register, dashboard with stats, book/member/borrow lists and forms, overdue view, and error pages (403/404/500).
- Role-aware UI — Add/Edit/Delete controls hidden from members.

### 🧪 Quality & DevOps
- **22 automated tests**: Mockito unit tests, `@DataJpaTest` repository tests, full `@SpringBootTest` + MockMvc API integration tests (including RBAC 403/401 checks and the borrow→return lifecycle).
- **Flyway** versioned migrations that run on both H2 (dev) and MySQL 8 (prod).
- **Swagger/OpenAPI** UI with JWT bearer support out of the box.
- **Docker**: multi-stage `Dockerfile` + `docker-compose.yml` (app + MySQL), Spring Actuator health checks.

---

## 🚀 Quick Start

### Prerequisites

| Software | Version | Check |
|---|---|---|
| JDK | 17+ (tested on 17 and 22) | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| MySQL | 8.0+ (prod/Docker only) | `mysql --version` |
| Docker + Compose | any recent (Docker path only) | `docker --version` |

No Tomcat install needed — embedded Tomcat is included.

### Option A — Dev (H2, zero setup) ⭐ start here

```bash
mvn spring-boot:run
```

Flyway creates the schema in-memory and the app seeds 5 categories, 4 authors, 3 publishers, 5 books, 3 members and 4 users.

| What | URL |
|---|---|
| Login (JSP) | http://localhost:8080/login |
| Dashboard | http://localhost:8080/dashboard |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API docs (JSON) | http://localhost:8080/v3/api-docs |
| Health | http://localhost:8080/actuator/health |
| H2 console | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:librarydb`, user `sa`, empty password) |

### Option B — Docker (app + MySQL)

```bash
MYSQL_ROOT_PASSWORD=rootpass docker compose up --build
```

- App → http://localhost:8080 (prod profile, Flyway migrates on start)
- MySQL → `localhost:3306`, database `library_db`
- Stop: `docker compose down` (add `-v` to wipe the `mysql-data` volume)

### Option C — Prod (local MySQL)

```bash
# 1. Create the database
mysql -u root -p -e "CREATE DATABASE library_db;"

# 2. Set credentials (env vars take precedence over the properties file)
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=<your-password>

# 3. Run with the prod profile (Flyway migrates, Hibernate validates)
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run
```

### Build the WAR

```bash
mvn clean package          # runs all 22 tests, outputs target/*.war
java -jar target/*.war     # run the packaged artifact
```

---

## 👥 User Roles & Permissions

Seeded automatically on first start:

| Username | Password | Role | Permissions |
|---|---|---|---|
| `librarian` | `Librarian@123` | `ROLE_LIBRARIAN` | Full access: catalog writes, member management, all borrows |
| `member` | `Member@123` | `ROLE_MEMBER` | Browse/search, borrow & return, own history |
| `admin` | `Admin@123` | `ROLE_ADMIN` | Legacy alias of librarian |
| `user` | `User@123` | `ROLE_USER` | Legacy alias of member |

| Action | Librarian | Member | Anonymous |
|---|---|---|---|
| Register / login | ✅ | ✅ | ✅ |
| Browse, search, view books | ✅ | ✅ | ✅ (search only) |
| Create / edit / delete books, members, categories | ✅ | ❌ 403 | ❌ 401 |
| Borrow / return books | ✅ | ✅ | ❌ 401 |
| Full borrow list / stats | ✅ | ❌ 403 | ❌ 401 |

---

## 📡 API Reference

Base path `/api/v1`. All endpoints except `/auth/**` (and public search) require `Authorization: Bearer <token>`. Responses are wrapped as `{success, message, data}`.

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | public | Register (201) |
| `POST` | `/auth/login` | public | Login → `{token, role}` |
| `GET` | `/auth/me` | user | Current user profile |

### Books

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/books?page=0&size=10&sort=title,asc` | user | Paginated + sorted list |
| `GET` | `/books/{id}` | user | Single book |
| `POST` | `/books` | librarian | Create (201) |
| `PUT` | `/books/{id}` | librarian | Update |
| `DELETE` | `/books/{id}` | librarian | Delete |
| `GET` | `/books/search?keyword=java` | public | Keyword search (title/ISBN) |
| `GET` | `/books/search/advanced?title=&isbn=&categoryId=&authorId=&language=&publicationYear=&availableOnly=` | user | Multi-criteria + paged + sorted |
| `GET` | `/books/available` | public | In-stock books |
| `GET` | `/books/category/{categoryId}` | user | Books by category |

### Members / Categories / Borrows

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET/POST` | `/members`, `PUT/DELETE /members/{id}` | librarian | Member management |
| `GET/POST` | `/categories`, `PUT/DELETE /categories/{id}` | read: user, write: librarian | Categories |
| `GET` | `/borrows` | librarian | All borrow records |
| `GET` | `/borrows/{id}` | user | Single record |
| `GET` | `/borrows/member/{memberId}` | user | History for one member |
| `POST` | `/borrows` `{"bookId":1,"memberId":1}` | user | Borrow (201, 14-day due date) |
| `PUT` | `/borrows/{id}/return` | user | Return (auto-calculates fine) |
| `GET` | `/borrows/overdue` | user | Overdue records |
| `GET` | `/borrows/stats` | librarian | `{activeBorrows, overdueBorrows}` |

### Example session

```bash
# Login and grab a token
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"librarian","password":"Librarian@123"}' \
  | python3 -c "import sys,json;print(json.load(sys.stdin)['data']['token'])")

# Paginated + sorted books
curl "http://localhost:8080/api/v1/books?page=0&size=5&sort=title,asc" \
  -H "Authorization: Bearer $TOKEN"

# Multi-criteria search
curl "http://localhost:8080/api/v1/books/search/advanced?title=java&availableOnly=true" \
  -H "Authorization: Bearer $TOKEN"

# Borrow and return
curl -X POST http://localhost:8080/api/v1/borrows \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"bookId":1,"memberId":1}'
curl -X PUT http://localhost:8080/api/v1/borrows/1/return \
  -H "Authorization: Bearer $TOKEN"
```

---

## 💰 Borrowing & Fines

1. **Borrow** — validates book + member exist and a copy is available; decrements `availableCopies`; creates a `BORROWED` record with `dueDate = today + 14 days`. One `@Transactional` method.
2. **Return** — stamps `returnDate`, computes `fine = $0.50 × days overdue` (zero if on time), flips status to `RETURNED`, restores the copy.
3. **Overdue detection** — `dueDate < today AND status = BORROWED`, exposed via `/borrows/overdue`.

---

## 🏗️ Architecture

```
              ┌──────────┐      ┌──────────┐      ┌────────────┐      ┌────┐
  HTTP ──────▶│Controller│ ───▶ │ Service  │ ───▶ │ Repository │ ──▶ │ DB │
  (JSP/JSON)  │  + DTOs  │      │@Transac-  │      │Spring Data │      └────┘
              └──────────┘      │ tional   │      │JPA + Specs │
                       ▲        └──────────┘      └────────────┘
                       │   GlobalExceptionHandler · SecurityFilterChain (JWT)
```

- **Controller** (`controller/` JSP, `rest/` JSON) — HTTP mapping, `@Valid` validation, status codes.
- **Service** (`service/` + `impl/`) — business rules, `@Transactional` boundaries, entity↔DTO via `mapper/`.
- **Repository** (`repository/`) — derived queries, `@Query` JPQL, Specifications.
- **Cross-cutting** — `GlobalExceptionHandler` for uniform errors; `JwtAuthenticationFilter` + dual `SecurityFilterChain` for auth.
- **DTOs** decouple the API from entities (no password leaks, no circular `Book ↔ Author` JSON, flattened names).

---

## 🗄️ Database & Migrations

```
categories (1) ──┐
                 ├─▶ books ── book_authors (join) ── authors
publishers ──────┘      │
                        └─ borrow_records ── members
users ── member_id (OneToOne, optional)
```

- Migrations: `src/main/resources/db/migration/` — `V1__baseline_schema.sql` (8 tables, FKs, hot-path indexes), `V2__performance_indexes.sql`. `initial.sql` at the repo root mirrors the baseline.
- Dev (H2): Flyway migrates, `ddl-auto=update`, `DataInitializer` seeds idempotently.
- Prod (MySQL): Flyway owns the schema, `ddl-auto=validate`.

---

## 🧪 Tests

```bash
mvn test
```

| Suite | Type | Count | Covers |
|---|---|---|---|
| `BookRepositoryTest` | `@DataJpaTest` (H2) | 3 | case-insensitive search, title/ISBN JPQL, in-stock filter |
| `BookServiceImplTest` | Mockito unit | 3 | Pageable forwarding, Specification search, page envelope |
| `BorrowServiceImplTest` | Mockito unit | 7 | 14-day due date, copy decrement, 409 on no stock, $0 vs $3.00 fines, double-return 409, 404s |
| `BookBorrowApiIntegrationTest` | `@SpringBootTest` + MockMvc | 9 | paged API, advanced search, member-403, librarian-201, no-token-401, borrow→return→conflict lifecycle, validation-400, overdue/stats |

Tests run on an isolated `testdb` H2 instance — dev data is never touched.

---

## 📁 Project Layout

```
.
├── pom.xml                          # Boot 3.2.5, Security+JWT, Flyway, springdoc, H2/MySQL
├── Dockerfile / docker-compose.yml  # container build + app+MySQL stack
├── initial.sql                      # canonical baseline schema
├── execution.md                     # step-by-step run guide
└── src/
    ├── main/java/com/library/
    │   ├── controller/      # JSP web pages
    │   ├── rest/            # JSON REST API
    │   ├── service/         # interfaces + impl/ (rules, transactions)
    │   ├── repository/      # Spring Data JPA + Specifications
    │   ├── specification/   # multi-criteria search specs
    │   ├── entity/          # 8 JPA entities
    │   ├── dto/ · mapper/   # API contracts + converters
    │   ├── security/        # JWT provider/filter, dual chains, RBAC
    │   ├── exception/       # global error handling
    │   └── config/          # data seeding, OpenAPI, MVC config
    ├── main/resources/      # application*.properties, db/migration/
    ├── main/webapp/         # 15 JSP pages
    └── test/                # 4 suites, 22 tests
```

---

## 🛠️ Tech Stack

Java 17 · Spring Boot 3.2.5 · Spring MVC · Spring Data JPA (Hibernate) · Spring Security + JWT · BCrypt · Flyway · springdoc-openapi · MySQL 8 / H2 · JSP + JSTL + Bootstrap 5 · JUnit 5 + Mockito + AssertJ · Docker & Compose · Spring Actuator

---

## 🧰 Troubleshooting

| Problem | Cause | Fix |
|---|---|---|
| `Port 8080 already in use` | previous run alive | `pkill -f library-management-system` or kill the PID from `lsof -i :8080` |
| `401` on API calls | missing/expired JWT (24 h TTL) | login again, send `Authorization: Bearer <token>` |
| `403` on add/edit/member APIs | logged in as member | login as `librarian` |
| Whitelabel page on `/` | not logged in | go to `/login` first |
| MySQL `Access denied` | wrong user/password | check `SPRING_DATASOURCE_*` env vars, ensure `library_db` exists |
| JSP 404 / not rendering | stale build | `mvn clean package`, verify dependencies resolve |

---

## 📄 License

MIT — free to use, modify and share.
