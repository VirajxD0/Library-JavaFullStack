<%-- 404.jsp - Page Not Found Error Page --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>404 - Page Not Found</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container text-center mt-5">
        <h1 class="display-1 text-muted">404</h1>
        <h2>Page Not Found</h2>
        <p class="text-muted">The page you're looking for doesn't exist.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Dashboard</a>
    </div>
</body>
</html>
