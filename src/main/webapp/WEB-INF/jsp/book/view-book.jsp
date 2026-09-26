<%-- view-book.jsp - Display book details --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-book"></i> Book Details</h2>

<div class="card">
    <div class="card-body">
        <div class="row">
            <div class="col-md-8">
                <h3>${book.title}</h3>
                <p class="text-muted">ISBN: <code>${book.isbn}</code></p>

                <table class="table table-bordered">
                    <tr>
                        <th style="width: 30%">Category</th>
                        <td>${book.categoryName}</td>
                    </tr>
                    <tr>
                        <th>Publisher</th>
                        <td>${book.publisherName}</td>
                    </tr>
                    <tr>
                        <th>Publication Year</th>
                        <td>${book.publicationYear}</td>
                    </tr>
                    <tr>
                        <th>Edition</th>
                        <td>${book.edition}</td>
                    </tr>
                    <tr>
                        <th>Language</th>
                        <td>${book.language}</td>
                    </tr>
                    <tr>
                        <th>Total Copies</th>
                        <td>${book.totalCopies}</td>
                    </tr>
                    <tr>
                        <th>Available Copies</th>
                        <td>
                            <span class="badge ${book.availableCopies > 0 ? 'bg-success' : 'bg-danger'}">
                                ${book.availableCopies}
                            </span>
                        </td>
                    </tr>
                </table>

                <c:if test="${not empty book.description}">
                    <h5>Description</h5>
                    <p>${book.description}</p>
                </c:if>

                <c:if test="${not empty book.authors}">
                    <h5>Authors</h5>
                    <ul>
                        <c:forEach var="author" items="${book.authors}">
                            <li>${author.firstName} ${author.lastName}</li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>
        </div>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/books/edit/${book.bookId}" class="btn btn-warning">
                <i class="bi bi-pencil"></i> Edit
            </a>
            <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Back to List
            </a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
