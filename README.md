# Library Management System — Java Full Stack (Spring Security + JWT)

Interview-ready project covering **Core Java, JDBC, Servlets, JSP, Spring, Spring Boot, Hibernate, REST APIs, Spring Security + JWT + BCrypt + RBAC**.

> Build once, run in Eclipse via Embedded Tomcat (no external Tomcat needed). H2 for dev, MySQL for prod.

---

## 1. What This Project Demonstrates

| Layer | Tech | Where to Show in Interview |
|---|---|---|
| **Core Java** | OOP (entity encapsulation), Collections (`List/Set/Map`), Exception handling, Generics, Lambda/Stream (`legacy.core.BookManager`) | `entity/Book.java` business methods, `service/impl/` streams |
| **JDBC** | `DriverManager`, `PreparedStatement` (SQL injection safe), `ResultSet` mapping, `try-with-resources` | `legacy/jdbc/JdbcBookDao.java` — manual mapping vs Hibernate |
| **Servlets** | `HttpServlet` lifecycle `init/doGet/doPost/destroy`, `HttpSession`, `@WebServlet`, `RequestDispatcher`, Filter | `legacy/servlet/BookServlet.java` + `legacy.auth.*` |
| **JSP** | JSTL `c:forEach/c:if`, Spring `form:form/form:input`, EL `${book.title}`, `<%@ include %>`, Bootstrap 5 | `webapp/WEB-INF/jsp/**` (15 pages) |
| **Spring MVC** | `@Controller`, `@RequestMapping`, `Model`, `ViewResolver` (`prefix=/WEB-INF/jsp/ suffix=.jsp`) | `controller/BookController.java` vs `rest/BookRestController.java` |
| **Spring Boot** | `SpringApplication.run`, Auto-configuration, `application*.properties`, Profiles `dev/prod`, Embedded Tomcat | `LibraryManagementApplication.java`, `config/` |
| **Hibernate/JPA** | `@Entity/@Table/@Id/@GeneratedValue`, `@ManyToOne`, `@ManyToMany(joinTable)`, `@OneToOne`, JPQL `@Query`, `ddl-auto=update` | `entity/Book.java` (3 relations), `repository/BookRepository.java` |
| **REST APIs** | `@RestController`, `ResponseEntity`, `@RequestBody/@PathVariable`, DTO, `ApiResponse` wrapper, proper status codes | `rest/*` — 18 endpoints |
| **Security** | Spring Security 6 `SecurityFilterChain`, `BCryptPasswordEncoder`, `DaoAuthenticationProvider`, `JWT` (`jjwt 0.12.5`), `OncePerRequestFilter`, `ROLE_USER/ROLE_ADMIN` | `security/SecurityConfig.java`, `JwtTokenProvider.java` |

**Key Files for Demo:** `legacy/` (manual) vs `security/` (modern) to explain *evolution* from Servlet → Spring Boot.

---

## 2. Prerequisites — Check Before Starting

| Software | Version | Check Command / Link |
|---|---|---|
| **JDK** | 17 or 22 (project `pom.xml: java.version=17`, works on 22) | `java -version` → should show 17+ |
| **Eclipse IDE** | 2023+ Enterprise (or STS 4) | `Eclipse → About` |
| **Maven** | 3.9+ (often bundled with STS; else install) | `mvn -version` or Eclipse `Window → Preferences → Maven` |
| **MySQL** | 8.0+ *only if you want prod profile* (H2 works without) | `mysql --version` |
| **Postman / curl** | Latest | For REST testing |

### Eclipse Plugins (2 min, required for JSP + Boot)

1. **Spring Tools 4 (STS)** : `Help → Eclipse Marketplace → Search "Spring Tools 4" → Install → Restart`
   - Gives `Run As → Spring Boot App` and Boot Dashboard
2. **Lombok** (optional, not used heavily but in pom) : `Help → Install New Software → https://projectlombok.org/p2` → `Max` bug if missing, do `Maven → Update Project`
3. Verify: `Window → Show View → Other → Spring → Boot Dashboard` appears.

---

## 3. Project Structure (Verified — 70+ Files)

```
library-management-system/
├── pom.xml                               # Spring Boot 3.2.5 + Security + JJWT + H2/MySQL + JSP/JSTL
├── README.md                             # THIS FILE
├── CONCEPTS_TO_LEARN.txt                 # All concepts to learn (you asked)
├── .gitignore
├── src/main/java/com/library/
│   ├── LibraryManagementApplication.java # @SpringBootApplication entry
│   ├── config/
│   │   ├── WebMvcConfig.java             # "/" → redirect /dashboard, /403 mapping
│   │   └── DataInitializer.java          # Seeds 5 categories, 4 authors, 3 publishers, 5 books, 3 members + 2 users (admin/user BCrypt)
│   ├── entity/                           # 7 entities
│   │   ├── Role.java                     # Enum ROLE_USER/ROLE_ADMIN
│   │   ├── User.java                     # @Entity users (username/email unique, BCrypt password, Role, OneToOne Member)
│   │   ├── Book.java                     # @ManyToOne Category/Publisher + @ManyToMany Author (join book_authors)
│   │   ├── Author.java / Publisher.java / Category.java / Member.java / BorrowRecord.java
│   ├── repository/                       # 7 Spring Data JPA repos
│   │   ├── UserRepository.java           # findByUsername, existsByUsername
│   │   ├── BookRepository.java           # findByTitleContainingIgnoreCase, searchBooks(@Query), findByAuthorId
│   │   └── BorrowRecordRepository.java   # findOverdueRecords, countByStatus
│   ├── dto/                              # 7 DTOs
│   │   ├── RegisterRequest.java / LoginRequest.java / JwtResponse.java / UserDTO.java
│   │   └── BookDTO.java / MemberDTO.java / BorrowDTO.java / ApiResponse.java
│   ├── mapper/                           # 5 mappers (Entity ↔ DTO, never expose password)
│   │   ├── UserMapper.java / BookMapper.java ...
│   ├── security/                         # MODERN AUTH — Spring Security + JWT
│   │   ├── SecurityConfig.java           # 2 SecurityFilterChains: /api/** STATELESS+JWT, /** STATEFUL formLogin
│   │   ├── JwtTokenProvider.java         # generate/validate/getUsername (JJWT HS256, Base64 secret)
│   │   ├── JwtAuthenticationFilter.java  # OncePerRequestFilter reads Bearer token
│   │   └── UserDetailsServiceImpl.java   # loads User → UserDetails (authorities)
│   ├── service/
│   │   ├── AuthService.java + impl/AuthServiceImpl.java # register(BCrypt), login(AuthenticationManager→JWT)
│   │   ├── BookService.java / MemberService.java / BorrowService.java / CategoryService.java + impl/
│   ├── controller/                       # JSP MVC (@Controller returns view names)
│   │   ├── AuthController.java           # GET /login → auth/login.jsp, GET/POST /register, GET /403
│   │   ├── DashboardController.java      # GET /dashboard (stats model)
│   │   ├── BookController.java / MemberController.java / BorrowController.java
│   ├── rest/                             # REST (@RestController returns JSON)
│   │   ├── AuthRestController.java       # POST /api/v1/auth/register, /login → JwtResponse, GET /me
│   │   └── BookRestController.java / MemberRestController.java / BorrowRestController.java / CategoryRestController.java
│   ├── exception/
│   │   ├── ResourceNotFoundException.java / DuplicateResourceException.java
│   │   └── GlobalExceptionHandler.java   # @RestControllerAdvice → 404/409/400/401/403 JSON
│   └── legacy/                           # LEGACY DEMO — Core Java / JDBC / Servlet
│       ├── core/BookManager.java         # Collections + Stream + Lambda (no framework)
│       ├── jdbc/JdbcBookDao.java         # raw PreparedStatement + ResultSet mapping
│       └── servlet/BookServlet.java      # @WebServlet HttpServlet doGet/doPost
├── src/main/resources/
│   ├── application.properties            # prefix/suffix, ddl-auto=update, jwt.secret/expiration, server.port=8080
│   ├── application-dev.properties        # H2 jdbc:h2:mem:librarydb + console enabled
│   ├── application-prod.properties       # MySQL jdbc:mysql://localhost:3306/library_db
│   └── data.sql                          # optional seed (DataInitializer is primary)
├── src/main/webapp/
│   ├── index.jsp                         # redirects → /dashboard
│   └── WEB-INF/
│       ├── web.xml                       # welcome-file + 404/500 error pages
│       └── jsp/
│           ├── common/header.jsp         # Bootstrap5 sidebar, auth-aware (isUserInRole), Logout form with _csrf
│           ├── common/footer.jsp
│           ├── auth/login.jsp            # Spring Security form (POST /login, _csrf), demo creds table
│           ├── auth/register.jsp         # form:form modelAttribute=registerRequest, role select
│           ├── dashboard/dashboard.jsp   # 4 stat cards + quick actions
│           ├── book/list-books.jsp       # table + c:forEach, role hides Add/Edit/Delete for USER
│           ├── book/add-book.jsp / edit-book.jsp / view-book.jsp
│           ├── member/list-members.jsp / add-member.jsp / edit-member.jsp
│           ├── borrow/borrow-book.jsp / borrow-history.jsp / overdue-list.jsp
│           └── error/404.jsp / 500.jsp / 403.jsp  # 403 for AccessDenied
└── target/                               # generated after Maven build (WAR)
```

Verify count: `find library-management-system -type f | wc -l` should be ~80 before build, ~150 after.

---

## 4. Step-by-Step Execution in Eclipse (Checked)

### Step 0 — Download / Copy Project
Place `library-management-system` folder under `~/Java FSD proj/` (no spaces issue now, but avoid deep nesting).
If from Git: `git clone <url>` then use that folder.

### Step 1 — Import as Maven Project
1. Open Eclipse → `File → Import → Maven → Existing Maven Projects → Next`
2. `Root Directory → Browse → select ".../library-management-system"` → Eclipse detects `pom.xml` → Check `pom.xml` line 1 appears in dialog → `Finish`
3. Wait bottom-right `Importing Maven projects` → `Updating Maven dependencies` completes (0 errors).
   - **Check:** `Project Explorer` shows `library-management-system` with `JRE System Library [JavaSE-17]`, `Maven Dependencies` (contains `spring-boot-starter-security-3.2.5.jar`, `jjwt-api-0.12.5.jar`). If missing: `Right click → Maven → Update Project → Force Update → OK`.

### Step 2 — Configure JDK
1. `Right click project → Properties → Java Build Path → Libraries` → JRE should be 17+ (you have JDK 22 → fine). If red: `Add Library → JRE System Library → Workspace default (22) → Finish`.
2. `Project → Clean → Clean all` → should be `BUILD SUCCESS`.

### Step 3 — Build (First Time)
1. `Right click project → Run As → Maven clean` → console `BUILD SUCCESS`
2. `Right click → Run As → Maven install` → wait 30-60s → console ends `BUILD SUCCESS` and `target/library-management-system-1.0.0.war` created.
   - **Alternative CLI:** `cd ".../library-management-system" && mvn clean package -DskipTests`
   - **If `mvn: command not found`:** Use Eclipse embedded: `Right click → Run As → Maven build... → Goals: clean install → Run`. Or install Maven via `brew install maven`.

### Step 4 — Run the App (3 Ways — Use Method A)

**Method A — Recommended (STS):** `Right click project → Run As → Spring Boot App` → Console shows:
```
Seeding database...
Created ADMIN user: admin / Admin@123
Created USER user: user / User@123
Database seeded successfully! Categories:5 Authors:4 Books:5 Members:3 Users:2
Library Management System Started!
JSP Views:  http://localhost:8080/
REST API:   http://localhost:8080/api/v1/
H2 Console: http://localhost:8080/h2-console
Started LibraryManagementApplication in 4.2 seconds
```
**Method B — Java Application:** `Right click LibraryManagementApplication.java → Run As → Java Application` (same output)

**Method C — CLI:** `mvn spring-boot:run -Dspring-boot.run.profiles=dev` (if maven installed)

**Verify Running:** Bottom view `Boot Dashboard` shows `library-management-system :8080 [running]`. Or `lsof -i :8080` shows LISTEN.

### Step 5 — Access the App (Test Each URL)

> First request will redirect to login because all JSP routes are protected.

| What | URL | Expected |
|---|---|---|
| **Login (JSP)** | `http://localhost:8080/login` | Blue login page with demo table |
| **Register** | `http://localhost:8080/register` | Form with username/email/password/role |
| **Dashboard** | `http://localhost:8080/dashboard` or `http://localhost:8080/` | After login → 4 stat cards (Total Books 5, Members 3, etc.) |
| **Books** | `http://localhost:8080/books` | Table of 5 books. USER sees View only; ADMIN sees Edit/Delete |
| **Add Book** | `http://localhost:8080/books/add` | USER → `403 Access Denied` page; ADMIN → form works |
| **REST Login (Postman)** | `POST http://localhost:8080/api/v1/auth/login` Body `{"username":"admin","password":"Admin@123"}` | `200 {"success":true,"data":{"token":"eyJ...","role":"ROLE_ADMIN"}}` |
| **REST Books (protected)** | `GET http://localhost:8080/api/v1/books` Header `Authorization: Bearer eyJ...` | `200` list; without header → `401 {"success":false,"message":"Unauthorized"}` |
| **H2 Console** | `http://localhost:8080/h2-console` | JDBC URL `jdbc:h2:mem:librarydb`, User `sa`, empty pwd → Connect → see `users, books, members...` |

**Default Credentials (from `DataInitializer.java:seed`):**

| Username | Password | Role | Permissions |
|---|---|---|---|
| `admin` | `Admin@123` | `ROLE_ADMIN` | Full: add/edit/delete books, manage members, borrow/return/overdue, all APIs |
| `user` | `User@123` | `ROLE_USER` | Limited: view/search books, view dashboard, borrow/return (cannot add/edit/delete) |
| *(you create)* | — | — | via Register page, choose role |

**Role Test Checklist:**
1. Login as `user` → `Books` → confirm `Add New Book` button hidden, `Edit/Delete` icons hidden, try direct `http://localhost:8080/books/add` → `403` page.
2. Logout → `Login` as `admin` → button visible, try `Members` → visible (USER hides Members menu).
3. Postman without token → `GET /api/v1/books` → `401`; with USER token → `200` but `POST /api/v1/books` with USER token may be allowed (API currently `authenticated()` for all verbs — restrict further if needed via `@PreAuthorize`).

### Step 6 — Test Registration Flow
1. Go `http://localhost:8080/register` → fill `username: test1, email: test1@test.com, password: Test@123, confirm: Test@123, role: USER, firstName: Test` → `Register`
2. Redirect `→ /login?registered=true` → login with `test1/Test@123` → should reach dashboard.
3. Check H2: `SELECT * FROM users;` → see `test1` with `password` starting `$2a$10$` (BCrypt hashed, never plain).
4. Try duplicate `admin` → error `Username already exists` (proof of `DuplicateResourceException`).

### Step 7 — Test JWT Flow in Postman (Step-by-Step)

1. **Register via API** (optional): `POST http://localhost:8080/api/v1/auth/register`
   ```json
   {"username":"apiuser","email":"apiuser@test.com","password":"Api@123","confirmPassword":"Api@123","role":"ROLE_USER","firstName":"Api","lastName":"User"}
   ```
   → `201 Created`
2. **Login:** `POST http://localhost:8080/api/v1/auth/login`
   ```json
   {"username":"admin","password":"Admin@123"}
   ```
   → Copy `data.token` (eyJ...)
3. **Use Token:** `GET http://localhost:8080/api/v1/books` → `Headers → Authorization: Bearer eyJ...` → `200`
4. **Test Protected Write:** `POST http://localhost:8080/api/v1/books` with same token + Body `{"title":"New Book","isbn":"978-0000000001","totalCopies":3}` → `201`
5. **Verify Expiry:** Token valid 24h (`jwt.expiration=86400000`). After expiry, API returns `401`. Need to login again.

### Step 8 — Switch to MySQL (Optional Prod Test)

1. Install MySQL: `brew install mysql && brew services start mysql` (or XAMPP)
2. Create DB: `mysql -u root -p -e "CREATE DATABASE library_db;"`
3. Edit `src/main/resources/application-prod.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   ```
4. Change active profile: `application.properties: spring.profiles.active=prod` (or Run Config → Arguments → VM args: `-Dspring.profiles.active=prod`)
5. Re-run `Spring Boot App` → logs now use MySQL, check `mysql -u root -p -e "USE library_db; SHOW TABLES;"` → 7 tables.

### Step 9 — Build WAR for External Tomcat

1. `mvn clean package` → `target/library-management-system-1.0.0.war`
2. Copy to Tomcat `webapps/` → `catalina.sh run` → access `http://localhost:8080/library-management-system-1.0.0/` (or rename WAR to `ROOT.war` for `/`)

### Step 10 — Stop App

Eclipse Boot Dashboard → click `■` red stop icon, or console `Terminate` (red square). Verify `lsof -i :8080` empty before re-run.

---

## 5. REST API Quick Reference (All Need JWT Except /auth/**)

| Method | Endpoint | Role | Body | Success |
|---|---|---|---|---|
| POST | `/api/v1/auth/register` | PUBLIC | `RegisterRequest` | 201 |
| POST | `/api/v1/auth/login` | PUBLIC | `{"username","password"}` | 200 + `JwtResponse{token}` |
| GET | `/api/v1/auth/me` | Authenticated | Header Bearer | 200 `UserDTO` |
| GET | `/api/v1/books` | Authenticated | — | 200 list |
| GET | `/api/v1/books/{id}` | Authenticated | — | 200 |
| POST | `/api/v1/books` | ADMIN* | `BookDTO` | 201 |
| PUT | `/api/v1/books/{id}` | ADMIN* | `BookDTO` | 200 |
| DELETE | `/api/v1/books/{id}` | ADMIN* | — | 200 |
| GET | `/api/v1/books/search?keyword=java` | PUBLIC | — | 200 |
| GET | `/api/v1/members` | ADMIN | — | 200 |
| POST | `/api/v1/members` | ADMIN | `MemberDTO` | 201 |
| GET | `/api/v1/borrows` | USER/ADMIN | — | 200 |
| POST | `/api/v1/borrows` | USER/ADMIN | `{"bookId":1,"memberId":1}` | 201 |
| PUT | `/api/v1/borrows/{id}/return` | USER/ADMIN | — | 200 |
| GET | `/api/v1/borrows/overdue` | USER/ADMIN | — | 200 |

`*` API chain currently `authenticated()` for all book writes — add `@PreAuthorize("hasRole('ADMIN')")` in `BookRestController` if you want strict 403 for USER writes (JSP already strict).

**cURL Example (JWT):**
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"Admin@123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/books
```

---

## 6. Database Schema (Auto-created via `ddl-auto=update`)

```
categories (1) ──┐
                 ├─► books (5) ── book_authors (join) ── authors (4)
publishers (3) ──┘      │
                        └─ borrow_records ── members (3)
                                │
users (2) ── member_id (OneToOne optional)   // new for Security
```

Check H2: `http://localhost:8080/h2-console` → `SELECT * FROM users;` → see BCrypt hashes.

---

## 7. Common Commands (Eclipse → Right click → Run As → Maven build... → Goals)

```bash
# If using CLI (install maven first: brew install maven)
mvn clean install                # build + test skip? use -DskipTests
mvn spring-boot:run              # run dev (H2)
mvn spring-boot:run -Dspring-boot.run.profiles=prod  # run prod (MySQL)
mvn clean package                # build WAR → target/*.war
```

If `mvn` not found in Eclipse, use `Run As → Maven build...` dialog.

---

## 8. Troubleshooting — Verified Fixes

| Problem | Cause | Fix |
|---|---|---|
| `Port 8080 already in use` | Previous run not stopped | Boot Dashboard → Stop, or `lsof -i :8080 → kill -9 <PID>` or change `application.properties: server.port=8081` |
| `Whitelabel Error Page` after `/` | Not logged in (Security redirects) | Go to `/login` first, login as admin |
| `403 Access Denied` on `/books/add` | Logged as USER | Login as `admin/Admin@123` → ADMIN required (see `SecurityConfig: hasRole("ADMIN")`) |
| `401 Unauthorized` on `/api/v1/books` | Missing/invalid JWT | Do `POST /api/v1/auth/login` → copy token → add Header `Authorization: Bearer <token>` |
| `JSP not rendering` (404 on jsp) | Missing `tomcat-embed-jasper` | Check `pom.xml` has it with `scope provided`, do `Maven → Update Project` |
| `H2 Console 403` | CSRF or headers | Already `frameOptions disable` + CSRF ignoring for h2; ensure `SecurityConfig` permits `/h2-console/**` |
| `BadCredentialsException` | Wrong password | Check demo `Admin@123` case-sensitive (A capital) |
| `Username already exists` on register | Duplicate seed | Use unique username/email |
| `Lombok errors` | No Lombok plugin | Install Lombok P2 or remove lombok from pom |
| `Maven dependencies not resolved` | Network/offline | `Maven → Update Project → Force Update`, check internet |
| `MySQL Access denied` | Wrong pwd/host | Create DB, update `application-prod.properties`, ensure `3306` open |
| `JWT Invalid` after 24h | Token expired | Login again to get new token (`jwt.expiration=86400000`) |

**Verify Build Success:** Console ends `BUILD SUCCESS`, no `ERROR`. If `Compilation failure: cannot find symbol PasswordEncoder` → do `Maven → Update Project`.

**Verify Auth Working:** Check `target/classes/com/library/security/` has 4 `.class` files after build.

---

## 9. Interview Demo Script (5 min)

1. **Show Layers:** Open `entity/Book.java` → explain `@Entity/@ManyToOne/@ManyToMany`, then `repository/BookRepository.java` → `findByTitleContainingIgnoreCase`, then `service/impl/BookServiceImpl.java` → `@Transactional`, then `controller/ vs rest/` difference.
2. **Show Evolution:** Open `legacy/jdbc/JdbcBookDao.java` (raw `PreparedStatement`) vs `repository/` (Spring Data JPA auto), `legacy/servlet/BookServlet.java` vs `controller/BookController.java`.
3. **Show Security:** Open `security/SecurityConfig.java` → 2 chains, `BCrypt`, `hasRole`, then `JwtTokenProvider.java` → `signWith` + `expiration`, then `JwtAuthenticationFilter.java` → `SecurityContextHolder`.
4. **Live Demo:** Login as USER → try Add Book → 403; login as ADMIN → add succeeds; then Postman → login API → JWT → access `/api/v1/books` with/without Bearer.
5. **Answer Why:** `Why DTO?` (decouple), `Why BCrypt?` (salted adaptive), `Why JWT stateless for API but session for JSP?` (see `SecurityConfig` comments).

---

## 10. Resume Snippet (Copy-Paste)

```latex
\begin{tabularx}{\linewidth}{@{}X r@{}}
\textbf{LIBRARY MANAGEMENT SYSTEM (FULL STACK — SECURE)} \\
\end{tabularx}
Java Full Stack Library Management System (Core Java, JDBC, Servlets, JSP, Spring Boot, Hibernate, REST APIs, MySQL, Spring Security, JWT) 
\vspace{-8pt}
\begin{itemize}[label=$\circ$,noitemsep, topsep=1pt]
  \item Secured application with Spring Security 6 + JWT (stateless API via JJWT filter + stateful JSP formLogin) and BCrypt hashing with RBAC (ROLE\_ADMIN for book/member management, ROLE\_USER for browsing/borrowing).
  \item Built RESTful APIs with DTOs, global exception handling and role-based access; developed JSP/JSTL frontend with session management and protected dashboard.
  \item Designed normalized schema with 7 tables including users/roles, demonstrating One-to-One and Many-to-Many JPA mappings with lazy/eager strategies.
\end{itemize}
```

---

**Done. If `mvn clean install` succeeds and `http://localhost:8080/login` shows login page with `admin/Admin@123` hint, execution steps are verified.**
