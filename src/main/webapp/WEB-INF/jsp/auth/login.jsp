<%-- login.jsp - Spring Security formLogin page with JWT info --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Library System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container" style="max-width:480px; margin-top:80px;">
    <div class="card shadow">
        <div class="card-body p-4">
            <h3 class="text-center mb-3"><i class="bi bi-shield-lock"></i> Library Login</h3>
            <p class="text-center text-muted small">Spring Security + BCrypt + JWT</p>

            <%-- Success / Error messages --%>
            <c:if test="${param.registered != null}">
                <div class="alert alert-success">Registration successful! Please login.</div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-info">You have been logged out.</div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">Invalid username or password!</div>
            </c:if>

            <%-- Spring Security expects POST /login with username & password + _csrf token --%>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <%-- CSRF token required for stateful formLogin (disabled for /api/** but enabled for web) --%>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="mb-3">
                    <label class="form-label">Username or Email</label>
                    <input type="text" name="username" class="form-control" placeholder="admin or user" required value="admin"/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Admin@123 / User@123" required value="Admin@123"/>
                </div>
                <button type="submit" class="btn btn-primary w-100"><i class="bi bi-box-arrow-in-right"></i> Login (Session)</button>
            </form>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/register">No account? Register here</a>
            </div>

            <hr class="my-4"/>
            <div class="alert alert-warning small">
                <strong>Demo Accounts (seeded):</strong><br/>
                ADMIN: <code>admin / Admin@123</code> — can manage books & members<br/>
                USER: <code>user / User@123</code> — can view & borrow only<br/><br/>
                <strong>JWT REST Test:</strong><br/>
                <code>POST /api/v1/auth/login {"username":"admin","password":"Admin@123"}</code><br/>
                Then use <code>Authorization: Bearer &lt;token&gt;</code> for <code>/api/v1/books</code>
            </div>
        </div>
    </div>
</div>
</body>
</html>
