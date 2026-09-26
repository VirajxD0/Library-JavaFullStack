<%-- overdue-list.jsp - Display overdue borrow records --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-exclamation-triangle text-danger"></i> Overdue Books</h2>

<div class="card">
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty borrows}">
                <div class="alert alert-warning">
                    <strong>${borrows.size()}</strong> book(s) are overdue!
                </div>
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Book</th>
                            <th>Member</th>
                            <th>Due Date</th>
                            <th>Days Overdue</th>
                            <th>Fine</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="borrow" items="${borrows}">
                            <tr class="table-danger">
                                <td><strong>${borrow.bookTitle}</strong></td>
                                <td>${borrow.memberName}</td>
                                <td><fmt:formatDate value="${borrow.dueDate}" pattern="MMM dd, yyyy"/></td>
                                <td><span class="badge bg-danger">${borrow.daysOverdue} days</span></td>
                                <td><span class="text-danger fw-bold">$${borrow.fineAmount}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/borrows/return/${borrow.borrowId}" 
                                       class="btn btn-sm btn-success"
                                       onclick="return confirm('Return this book and calculate fine?')">
                                        <i class="bi bi-arrow-return-left"></i> Return
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-success">
                    <i class="bi bi-check-circle"></i> No overdue books! All books are returned on time.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
