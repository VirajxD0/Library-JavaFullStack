<%-- borrow-book.jsp - Form to borrow a book --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-bookmark-plus"></i> Borrow a Book</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/borrows/borrow" method="post" class="row g-3">

            <div class="col-md-6">
                <label class="form-label">Select Book *</label>
                <select name="bookId" class="form-select" required>
                    <option value="">-- Choose a Book --</option>
                    <c:forEach var="book" items="${books}">
                        <option value="${book.bookId}">
                            ${book.title} (Available: ${book.availableCopies})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-6">
                <label class="form-label">Select Member *</label>
                <select name="memberId" class="form-select" required>
                    <option value="">-- Choose a Member --</option>
                    <c:forEach var="member" items="${members}">
                        <c:if test="${member.active}">
                            <option value="${member.memberId}">
                                ${member.firstName} ${member.lastName} (${member.email})
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-warning">
                    <i class="bi bi-bookmark-plus"></i> Borrow Book
                </button>
                <a href="${pageContext.request.contextPath}/borrows" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
