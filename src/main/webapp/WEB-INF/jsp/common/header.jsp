<%-- header.jsp - Common header with Spring Security auth-aware navigation --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { min-height: 100vh; background-color: #343a40; }
        .sidebar a { color: #adb5bd; text-decoration: none; padding: 10px 20px; display: block; }
        .sidebar a:hover, .sidebar a.active { color: #fff; background-color: #495057; }
        .content { padding: 20px; }
        .card { border: none; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .stat-card { transition: transform 0.2s; }
        .stat-card:hover { transform: translateY(-5px); }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-none d-md-block sidebar p-0">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3 mb-3">
                        <i class="bi bi-book"></i> Library System
                    </h5>
                    <%-- Show logged-in user info (Spring Security principal) --%>
                    <c:if test="${pageContext.request.userPrincipal != null}">
                        <div class="px-3 py-2 mb-2" style="background:#495057; color:#fff;">
                            <small><i class="bi bi-person-circle"></i> ${pageContext.request.userPrincipal.name}</small><br/>
                            <%-- Display role via request.isUserInRole (requires SecurityContext) --%>
                            <c:choose>
                                <c:when test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                                    <span class="badge bg-danger">ADMIN</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-success">USER</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
                                <i class="bi bi-speedometer2"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/books">
                                <i class="bi bi-book"></i> Books
                            </a>
                        </li>
                        <%-- ADMIN only links --%>
                        <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/members">
                                <i class="bi bi-people"></i> Members <span class="badge bg-danger">ADMIN</span>
                            </a>
                        </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/borrows">
                                <i class="bi bi-arrow-left-right"></i> Borrows
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/borrows/borrow">
                                <i class="bi bi-plus-circle"></i> New Borrow
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/borrows/overdue">
                                <i class="bi bi-exclamation-triangle"></i> Overdue
                            </a>
                        </li>
                        <li class="nav-item mt-3">
                            <a class="nav-link text-info" href="${pageContext.request.contextPath}/api/v1/books" target="_blank">
                                <i class="bi bi-code-slash"></i> REST API
                            </a>
                        </li>
                        <%-- Auth links --%>
                        <c:choose>
                            <c:when test="${pageContext.request.userPrincipal != null}">
                                <li class="nav-item mt-3">
                                    <form action="${pageContext.request.contextPath}/logout" method="post" class="px-3">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="btn btn-sm btn-outline-light w-100"><i class="bi bi-box-arrow-right"></i> Logout</button>
                                    </form>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item mt-3"><a class="nav-link" href="${pageContext.request.contextPath}/login"><i class="bi bi-box-arrow-in-right"></i> Login</a></li>
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/register"><i class="bi bi-person-plus"></i> Register</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </nav>

            <main class="col-md-10 ms-sm-auto content">
                <%-- Global permission note for USER vs ADMIN demo --%>
                <c:if test="${pageContext.request.userPrincipal != null && pageContext.request.isUserInRole('ROLE_USER') && !pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <div class="alert alert-info py-1 small"><i class="bi bi-info-circle"></i> You are logged in as <b>USER</b> — you can view & borrow, but Add/Edit/Delete Books & Members require <b>ADMIN</b>. Login as <code>admin/Admin@123</code> to test ADMIN.</div>
                </c:if>
