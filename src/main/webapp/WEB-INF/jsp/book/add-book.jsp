<%-- 
    add-book.jsp - Form to add a new book
    
    Demonstrates:
    - Spring Form Tags (form:form, form:input, form:select)
    - form:form binds to the "book" ModelAttribute
    - form:select for dropdown lists
    - Validation error display
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-plus-circle"></i> Add New Book</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <%-- 
            form:form - Spring MVC Form Tag
            - action: URL to submit to
            - method: HTTP method (POST)
            - modelAttribute: name of the Model attribute to bind to
            - commandName: alias for modelAttribute
        --%>
        <form:form action="${pageContext.request.contextPath}/books/save" 
                   method="post" modelAttribute="book" class="row g-3">

            <div class="col-md-6">
                <label class="form-label">Title *</label>
                <form:input path="title" class="form-control" required="required"/>
                <form:errors path="title" cssClass="text-danger"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">ISBN *</label>
                <form:input path="isbn" class="form-control" required="required" placeholder="978-XXXXXXXXXX"/>
                <form:errors path="isbn" cssClass="text-danger"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Publication Year</label>
                <form:input path="publicationYear" type="number" class="form-control" min="1900" max="2030"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Total Copies *</label>
                <form:input path="totalCopies" type="number" class="form-control" min="1" value="1"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Edition</label>
                <form:input path="edition" class="form-control" placeholder="e.g., 3rd Edition"/>
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

            <div class="col-md-6">
                <label class="form-label">Language</label>
                <form:input path="language" class="form-control" value="English"/>
            </div>

            <div class="col-12">
                <label class="form-label">Description</label>
                <form:textarea path="description" class="form-control" rows="3"/>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-circle"></i> Save Book
                </button>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> Cancel
                </a>
            </div>
        </form:form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
