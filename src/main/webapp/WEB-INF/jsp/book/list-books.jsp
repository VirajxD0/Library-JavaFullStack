<%-- list-books.jsp - Display all books with role-based actions --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="bi bi-book"></i> Books</h2>
    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
    <a href="${pageContext.request.contextPath}/books/add" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> Add New Book
    </a>
    </c:if>
</div>

<%-- Role info --%>
<c:if test="${pageContext.request.isUserInRole('ROLE_USER') && !pageContext.request.isUserInRole('ROLE_ADMIN')}">
    <div class="alert alert-warning py-1 small">USER role: View & borrow only. <b>ADMIN</b> required for Add/Edit/Delete.</div>
</c:if>

<div class="card mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/books/search" method="get" class="row g-3">
            <div class="col-md-8">
                <input type="text" name="keyword" class="form-control" placeholder="Search by title or ISBN..." value="${keyword}"/>
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-outline-primary me-2"><i class="bi bi-search"></i> Search</button>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-secondary">Clear</a>
            </div>
        </form>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty books}">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>ISBN</th>
                            <th>Category</th>
                            <th>Publisher</th>
                            <th>Copies</th>
                            <th>Available</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="book" items="${books}">
                            <tr>
                                <td>${book.bookId}</td>
                                <td>
                                    <strong>${book.title}</strong>
                                    <c:if test="${not empty book.authors}">
                                        <br><small class="text-muted">
                                            <c:forEach var="author" items="${book.authors}" varStatus="status">
                                                ${author.firstName} ${author.lastName}<c:if test="${!status.last}">, </c:if>
                                            </c:forEach>
                                        </small>
                                    </c:if>
                                </td>
                                <td><code>${book.isbn}</code></td>
                                <td>${book.categoryName}</td>
                                <td>${book.publisherName}</td>
                                <td>${book.totalCopies}</td>
                                <td><span class="badge ${book.availableCopies > 0 ? 'bg-success' : 'bg-danger'}">${book.availableCopies}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/books/view/${book.bookId}" class="btn btn-sm btn-outline-info" title="View"><i class="bi bi-eye"></i></a>
                                    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                                    <a href="${pageContext.request.contextPath}/books/edit/${book.bookId}" class="btn btn-sm btn-outline-warning" title="Edit (ADMIN)"><i class="bi bi-pencil"></i></a>
                                    <a href="${pageContext.request.contextPath}/books/delete/${book.bookId}" class="btn btn-sm btn-outline-danger" title="Delete (ADMIN)" onclick="return confirm('Are you sure?')"><i class="bi bi-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info"><i class="bi bi-info-circle"></i> No books found. <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}"><a href="${pageContext.request.contextPath}/books/add">Add the first book!</a></c:if></div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
