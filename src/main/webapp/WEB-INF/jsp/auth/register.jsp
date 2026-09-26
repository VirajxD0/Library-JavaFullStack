<%-- register.jsp - User signup --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - Library System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container" style="max-width:600px; margin-top:40px;">
    <div class="card shadow">
        <div class="card-body p-4">
            <h3 class="text-center mb-3">Create Account</h3>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form:form action="${pageContext.request.contextPath}/register" method="post" modelAttribute="registerRequest" class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Username *</label>
                    <form:input path="username" class="form-control" required="required"/>
                    <form:errors path="username" cssClass="text-danger small"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Email *</label>
                    <form:input path="email" class="form-control" required="required"/>
                    <form:errors path="email" cssClass="text-danger small"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">First Name</label>
                    <form:input path="firstName" class="form-control"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Last Name</label>
                    <form:input path="lastName" class="form-control"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Password *</label>
                    <form:password path="password" class="form-control" required="required"/>
                    <form:errors path="password" cssClass="text-danger small"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Confirm Password *</label>
                    <form:password path="confirmPassword" class="form-control" required="required"/>
                </div>
                <div class="col-12">
                    <label class="form-label">Role</label>
                    <form:select path="role" class="form-select">
                        <form:option value="ROLE_USER">USER - Can browse & borrow</form:option>
                        <form:option value="ROLE_ADMIN">ADMIN - Full access</form:option>
                    </form:select>
                    <small class="text-muted">In production, only ADMIN can create ADMIN.</small>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-success w-100">Register</button>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-link w-100">Already have account? Login</a>
                </div>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>
