<%-- 
    dashboard.jsp - Main Dashboard Page
    
    Demonstrates:
    - JSP EL (Expression Language): ${variableName} outputs Java variables
    - JSTL c:if for conditional rendering
    - Bootstrap cards for responsive layout
    - JSP includes for reusable header/footer
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-speedometer2"></i> Dashboard</h2>

<!-- Statistics Cards - Bootstrap grid system -->
<div class="row mb-4">
    <!-- Total Books Card -->
    <div class="col-md-3">
        <div class="card stat-card text-white bg-primary">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-book"></i> Total Books</h5>
                <h2 class="card-text">${totalBooks}</h2>
                <a href="${pageContext.request.contextPath}/books" class="text-white">View All <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </div>
    <!-- Total Members Card -->
    <div class="col-md-3">
        <div class="card stat-card text-white bg-success">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-people"></i> Total Members</h5>
                <h2 class="card-text">${totalMembers}</h2>
                <a href="${pageContext.request.contextPath}/members" class="text-white">View All <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </div>
    <!-- Active Borrows Card -->
    <div class="col-md-3">
        <div class="card stat-card text-white bg-warning">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-arrow-left-right"></i> Active Borrows</h5>
                <h2 class="card-text">${activeBorrows}</h2>
                <a href="${pageContext.request.contextPath}/borrows" class="text-white">View All <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </div>
    <!-- Overdue Card -->
    <div class="col-md-3">
        <div class="card stat-card text-white bg-danger">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-exclamation-triangle"></i> Overdue</h5>
                <h2 class="card-text">${overdueBorrows}</h2>
                <a href="${pageContext.request.contextPath}/borrows/overdue" class="text-white">View All <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </div>
</div>

<!-- Quick Actions -->
<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header"><i class="bi bi-lightning"></i> Quick Actions</div>
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/books/add" class="btn btn-primary me-2">
                    <i class="bi bi-plus-circle"></i> Add New Book
                </a>
                <a href="${pageContext.request.contextPath}/members/add" class="btn btn-success me-2">
                    <i class="bi bi-person-plus"></i> Register Member
                </a>
                <a href="${pageContext.request.contextPath}/borrows/borrow" class="btn btn-warning">
                    <i class="bi bi-bookmark-plus"></i> Borrow Book
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-header"><i class="bi bi-code-slash"></i> API Endpoints</div>
            <div class="card-body">
                <p>Test the REST API:</p>
                <code>GET /api/v1/books</code><br>
                <code>POST /api/v1/books</code><br>
                <code>GET /api/v1/members</code><br>
                <a href="${pageContext.request.contextPath}/api/v1/books" target="_blank" class="btn btn-outline-info btn-sm mt-2">
                    <i class="bi bi-box-arrow-up-right"></i> Try API
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
