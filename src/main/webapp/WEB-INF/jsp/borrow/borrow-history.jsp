<%-- borrow-history.jsp - Display all borrow records --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="bi bi-arrow-left-right"></i> Borrow History</h2>
    <a href="${pageContext.request.contextPath}/borrows/borrow" class="btn btn-warning">
        <i class="bi bi-bookmark-plus"></i> New Borrow
    </a>
</div>

<div class="card">
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty borrows}">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Book</th>
                            <th>Member</th>
                            <th>Borrow Date</th>
                            <th>Due Date</th>
                            <th>Return Date</th>
                            <th>Status</th>
                            <th>Fine</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="borrow" items="${borrows}">
                            <tr class="${borrow.overdue ? 'table-danger' : ''}">
                                <td>${borrow.borrowId}</td>
                                <td>
                                    <strong>${borrow.bookTitle}</strong>
                                    <br><small class="text-muted">${borrow.bookIsbn}</small>
                                </td>
                                <td>${borrow.memberName}</td>
                                <td><fmt:formatDate value="${borrow.borrowDate}" pattern="MMM dd, yyyy"/></td>
                                <td><fmt:formatDate value="${borrow.dueDate}" pattern="MMM dd, yyyy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${borrow.returnDate != null}">
                                            <fmt:formatDate value="${borrow.returnDate}" pattern="MMM dd, yyyy"/>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge ${borrow.status == 'BORROWED' ? 'bg-warning' : (borrow.status == 'RETURNED' ? 'bg-success' : 'bg-danger')}">
                                        ${borrow.status}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${borrow.fineAmount > 0}">
                                        <span class="text-danger">$${borrow.fineAmount}</span>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${borrow.status == 'BORROWED' || borrow.status == 'OVERDUE'}">
                                        <a href="${pageContext.request.contextPath}/borrows/return/${borrow.borrowId}" 
                                           class="btn btn-sm btn-success"
                                           onclick="return confirm('Return this book?')">
                                            <i class="bi bi-arrow-return-left"></i> Return
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> No borrow records found.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
