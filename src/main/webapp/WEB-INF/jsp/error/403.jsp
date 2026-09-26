<%-- 403.jsp - Access Denied (Spring Security) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>403 - Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container text-center mt-5">
        <h1 class="display-1 text-danger">403</h1>
        <h2>Access Denied</h2>
        <p class="text-muted">You don't have permission to access this resource.<br>
        <small>ROLE_ADMIN required. Your role: <b>${pageContext.request.userPrincipal != null ? pageContext.request.userPrincipal.name : 'ANONYMOUS'}</b></small></p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Go to Dashboard</a>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">Login with different account</a>
    </div>
</body>
</html>
