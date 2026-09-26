<%-- edit-book.jsp - Form to edit an existing book --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-pencil"></i> Edit Book</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <form:form action="${pageContext.request.contextPath}/books/update/${book.bookId}" 
                   method="post" modelAttribute="book" class="row g-3">

            <div class="col-md-6">
                <label class="form-label">Title *</label>
                <form:input path="title" class="form-control" required="required"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">ISBN *</label>
                <form:input path="isbn" class="form-control" required="required"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Publication Year</label>
                <form:input path="publicationYear" type="number" class="form-control"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Total Copies</label>
                <form:input path="totalCopies" type="number" class="form-control" min="1"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Available Copies</label>
                <form:input path="availableCopies" type="number" class="form-control" min="0"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Category</label>
                <form:select path="categoryId" class="form-select">
                    <form:option value="">-- Select Category --</form:option>
                    <c:forEach var="cat" items="${categories}">
                        <form:option value="${cat.categoryId}">${cat.name}</form:option>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-md-6">
                <label class="form-label">Publisher</label>
                <form:select path="publisherId" class="form-select">
                    <form:option value="">-- Select Publisher --</form:option>
                    <c:forEach var="pub" items="${publishers}">
                        <form:option value="${pub.publisherId}">${pub.name}</form:option>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-12">
                <label class="form-label">Description</label>
                <form:textarea path="description" class="form-control" rows="3"/>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-circle"></i> Update Book
                </button>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> Cancel
                </a>
            </div>
        </form:form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
