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

- [Features](#-features)
- [Quick Start](#-quick-start)
- [User Roles & Permissions](#-user-roles--permissions)
- [API Reference](#-api-reference)
- [Borrowing & Fines](#-borrowing--fines)
- [Search, Pagination & Sorting](#-search-pagination--sorting)
- [Architecture](#️-architecture)
- [Database & Migrations](#️-database--migrations)
- [Configuration](#️-configuration)
- [Tests](#-tests)
- [Project Layout](#-project-layout)
- [Tech Stack](#️-tech-stack)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#-roadmap)
- [License](#-license)

---

## ✨ Features

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
  - `/api/**` → stateless **JWT** (`jjwt`, 24 h expiry, `Authorization: Bearer` header).
  - JSP pages → stateful **form login** with sessions.
- Passwords hashed with **BCrypt** (salted, adaptive) — never stored or returned in plain text.
- **RBAC**: `ROLE_LIBRARIAN` vs `ROLE_MEMBER` (legacy `ROLE_ADMIN`/`ROLE_USER` still accepted for backward compatibility), enforced at **both** URL level and method level (`@PreAuthorize`).
- Consistent JSON error model (`{success, message, data}`) for 400/401/403/404/409/500 via `@RestControllerAdvice`.

### 🔍 Search & Discovery
- Real pagination and sorting on list endpoints: `?page=0&size=10&sort=title,asc`.
- Multi-criteria advanced search combining keyword, title, ISBN, category, author, language, publication year and availability in one AND-query (JPA Specifications).
- Legacy single-keyword search, available-books and by-category endpoints kept for the web UI.

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
Flyway creates the schema in-memory and the app seeds 5 categories, 4 authors, 3 publishers, 5 books, 3 members and 4 users.
What	URL
Login (JSP)	http://localhost:8080/login
Dashboard	http://localhost:8080/dashboard
Swagger UI	http://localhost:8080/swagger-ui.html
API docs (JSON)	http://localhost:8080/v3/api-docs
Health	http://localhost:8080/actuator/health
H2 console	http://localhost:8080/h2-console (JDBC URL jdbc:h2:mem:librarydb, user sa, empty password)
Option B — Docker (app + MySQL)
MYSQL_ROOT_PASSWORD=rootpass docker compose up --build
- App → http://localhost:8080 (prod profile, Flyway migrates on start)
- MySQL → localhost:3306, database library_db
- Override secrets: MYSQL_ROOT_PASSWORD=... JWT_SECRET=... docker compose up --build
- Stop: docker compose down (add -v to wipe the mysql-data volume)
Option C — Prod (local MySQL)
# 1. Create the database
mysql -u root -p -e "CREATE DATABASE library_db;"

# 2. Set credentials (env vars take precedence over the properties file)
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=<your-password>

# 3. Run with the prod profile (Flyway migrates, Hibernate validates)
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run
Build the WAR
mvn clean package          # runs all 22 tests, outputs target/*.war
java -jar target/*.war     # run the packaged artifact
👥 User Roles & Permissions
Seeded automatically on first start:
Username	Password	Role	Permissions
librarian	Librarian@123	ROLE_LIBRARIAN	Full access: catalog writes, member management, all borrows
member	Member@123	ROLE_MEMBER	Browse/search, borrow & return, own history
admin	Admin@123	ROLE_ADMIN	Legacy alias of librarian
user	User@123	ROLE_USER	Legacy alias of member
Permission matrix (REST):
Action	Librarian	Member	Anonymous
POST /auth/login, /auth/register	✅	✅	✅
GET /books, /books/{id}, /books/search, /books/available	✅	✅	✅ (search/available only)
POST/PUT/DELETE /books/**	✅	❌ 403	❌ 401
GET/POST/PUT/DELETE /members/**	✅	❌ 403	❌ 401
POST/PUT/DELETE /categories/**	✅	❌ 403	❌ 401
POST /borrows, PUT /borrows/{id}/return	✅	✅	❌ 401
GET /borrows, /borrows/stats	✅	❌ 403	❌ 401
Try it: log in as member → the Add/Edit/Delete buttons disappear from the UI, and direct API writes return 403.
📡 API Reference
Base path /api/v1. All endpoints except /auth/** (and public search) require Authorization: Bearer <token>. Responses are wrapped as {success, message, data}.
Auth
Method	Endpoint	Auth	Description
POST	/auth/register	public	Register (returns 201)
POST	/auth/login	public	Login → {token, role}
GET	/auth/me	user	Current user profile
Books
Method	Endpoint	Auth	Description
GET	/books?page=0&size=10&sort=title,asc	user	Paginated + sorted list
GET	/books/{id}	user	Single book
POST	/books	librarian	Create (201)
PUT	/books/{id}	librarian	Update
DELETE	/books/{id}	librarian	Delete
GET	/books/search?keyword=java	public	Keyword search (title/ISBN)
GET	/books/search/advanced?title=&isbn=&categoryId=&authorId=&language=&publicationYear=&availableOnly=	user	Multi-criteria + paged + sorted
GET	/books/available	public	In-stock books
GET	/books/category/{categoryId}	user	Books by category
Members / Categories / Borrows
Method	Endpoint	Auth	Description
GET/POST	/members, PUT/DELETE /members/{id}	librarian	Member management
GET/POST	/categories, PUT/DELETE /categories/{id}	read: user, write: librarian	Categories
GET	/borrows	librarian	All borrow records
GET	/borrows/{id}	user	Single record
GET	/borrows/member/{memberId}	user	History for one member
POST	/borrows {"bookId":1,"memberId":1}	user	Borrow (201, sets 14-day due date)
PUT	/borrows/{id}/return	user	Return (auto-calculates fine)
GET	/borrows/overdue	user	Overdue records
GET	/borrows/stats	librarian	{activeBorrows, overdueBorrows}
Example session
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
💰 Borrowing & Fines
1. Borrow — validates the book exists, the member exists, and at least one copy is available; decrements availableCopies and creates a BORROWED record with dueDate = today + 14 days. All inside one @Transactional method.
2. Return — stamps returnDate, computes fine = $0.50 × days overdue (zero if on time), flips status to RETURNED, restores the copy.
3. Overdue detection — dueDate < today AND status = BORROWED, exposed via /borrows/overdue.
🔎 Search, Pagination & Sorting
- GET /books accepts Spring page, size and sort parameters and returns a page envelope: content, page, size, totalElements, totalPages, last.
- GET /books/search/advanced accepts any combination of keyword, title, isbn, categoryId, authorId, language, publicationYear, availableOnly (blank = ignored) plus pagination/sorting — implemented with JPA Specifications AND-combined in BookSpecification.
- The JSP web UI keeps using the simple list/keyword endpoints, so both front-ends share the same service layer.
🏗️ Architecture
Layered design — controllers never touch the database, services own transactions and rules:
              ┌──────────┐      ┌──────────┐      ┌────────────┐      ┌────┐
  HTTP ──────▶│Controller│ ───▶ │ Service  │ ───▶ │ Repository │ ──▶ │ DB │
  (JSP/JSON)  │  + DTOs  │      │@Transac-  │      │Spring Data │      └────┘
              └──────────┘      │ tional   │      │JPA + Specs │
                       ▲        └──────────┘      └────────────┘
                       │   GlobalExceptionHandler · SecurityFilterChain (JWT)
- Controller (controller/ JSP, rest/ JSON) — HTTP mapping, @Valid validation, status codes.
- Service (service/ + impl/) — business rules, @Transactional boundaries, entity↔DTO via mapper/.
- Repository (repository/) — derived queries, @Query JPQL, Specifications.
- Cross-cutting — GlobalExceptionHandler for uniform errors; JwtAuthenticationFilter + dual SecurityFilterChain for auth.
- DTOs decouple the API from entities (no password leaks, no circular Book ↔ Author JSON, flattened categoryName/authorNames).
🗄️ Database & Migrations
categories (1) ──┐
                 ├─▶ books ── book_authors (join) ── authors
publishers ──────┘      │
                        └─ borrow_records ── members
users ── member_id (OneToOne, optional)
- Migrations: src/main/resources/db/migration/ — V1__baseline_schema.sql (8 tables, FKs, hot-path indexes), V2__performance_indexes.sql. initial.sql at the repo root mirrors the baseline as the canonical schema reference.
- Dev (H2): Flyway migrates, ddl-auto=update stays lenient, DataInitializer seeds idempotently.
- Prod (MySQL): Flyway owns the schema, ddl-auto=validate — Hibernate only checks the mapping matches.
- Seeding is single-sourced in DataInitializer (@Transactional CommandLineRunner); data.sql is retired from the startup path to avoid init-order conflicts with Flyway.
⚙️ Configuration
Property	Dev	Prod
Profile	dev (default)	SPRING_PROFILES_ACTIVE=prod
Database	H2 in-memory	MySQL (SPRING_DATASOURCE_URL/USERNAME/PASSWORD)
ddl-auto	update	validate
Flyway	enabled	enabled, validate-on-migrate
JWT	jwt.secret / jwt.expiration (24 h)	override JWT_SECRET env var in real deployments
Docs	/swagger-ui.html, /v3/api-docs	same
Monitoring	/actuator/health, /actuator/info	same
🧪 Tests
mvn test
Suite	Type	Count	Covers
BookRepositoryTest	@DataJpaTest (H2)	3	case-insensitive search, title/ISBN JPQL, in-stock filter
BookServiceImplTest	Mockito unit	3	Pageable forwarding, Specification search, page envelope
BorrowServiceImplTest	Mockito unit	7	14-day due date, copy decrement, 409 on no stock, $0 vs $3.00 fines, double-return 409, 404s
BookBorrowApiIntegrationTest	@SpringBootTest + MockMvc	9	paged API, advanced search, member-403, librarian-201, no-token-401, borrow→return→conflict lifecycle, validation-400, overdue/stats
Tests run on an isolated testdb H2 instance (src/test/resources/application.properties) — dev data is never touched.
📁 Project Layout
.
├── pom.xml                          # Boot 3.2.5, Security+JWT, Flyway, springdoc, H2/MySQL
├── Dockerfile / docker-compose.yml  # container build + app+MySQL stack
├── initial.sql                      # canonical baseline schema
├── execution.md                     # step-by-step run guide
└── src/
    ├── main/java/com/library/
    │   ├── controller/      # JSP web pages (@Controller → Thymeleaf-style views)
    │   ├── rest/            # JSON REST API (@RestController)
    │   ├── service/         # interfaces + impl/ (business rules, transactions)
    │   ├── repository/      # Spring Data JPA + Specifications
    │   ├── specification/   # multi-criteria search specs
    │   ├── entity/          # 8 JPA entities
    │   ├── dto/ · mapper/   # API contracts + entity converters
    │   ├── security/        # JWT provider/filter, dual chains, RBAC
    │   ├── exception/       # ResourceNotFound, Duplicate, NotAvailable + global handler
    │   └── config/          # data seeding, OpenAPI, MVC config
    ├── main/resources/      # application*.properties, db/migration/
    ├── main/webapp/         # 15 JSP pages (auth, dashboard, books, members, borrows)
    └── test/                # 4 suites, 22 tests
🛠️ Tech Stack
Java 17 · Spring Boot 3.2.5 · Spring MVC · Spring Data JPA (Hibernate) · Spring Security + JWT (jjwt) · BCrypt · Flyway · springdoc-openapi · MySQL 8 / H2 · JSP + JSTL + Bootstrap 5 · JUnit 5 + Mockito + AssertJ · Docker & Compose · Spring Actuator
🧰 Troubleshooting
Problem	Cause	Fix
Port 8080 already in use	previous run alive	pkill -f library-management-system or kill the PID from lsof -i :8080
401 on API calls	missing/expired JWT (24 h TTL)	login again, send Authorization: Bearer <token>
403 on add/edit/member APIs	logged in as member	login as librarian
Whitelabel page on /	not logged in	go to /login first
MySQL Access denied	wrong user/password	check SPRING_DATASOURCE_* env vars, ensure library_db exists
JSP 404 / not rendering	stale build or missing Jasper	mvn clean package, verify dependencies resolve
Full runbook: see execution.md (execution.md).
🗺️ Roadmap
- Refresh-token rotation and server-side logout (token denylist)
- Reservation/waitlist queue for out-of-stock books
- Email notifications for due/overdue loans
- Rate limiting on auth endpoints
- Frontend rewrite (React) consuming the REST API
📄 License
MIT — free to use, modify and share.