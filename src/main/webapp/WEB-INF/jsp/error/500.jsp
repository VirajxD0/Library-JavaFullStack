<%-- 500.jsp - Internal Server Error Page --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>500 - Server Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container text-center mt-5">
        <h1 class="display-1 text-danger">500</h1>
        <h2>Internal Server Error</h2>
        <p class="text-muted">Something went wrong on our end. Please try again later.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Dashboard</a>
    </div>
</body>
</html>
