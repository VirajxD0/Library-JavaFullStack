<%-- list-members.jsp - Display all members --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="bi bi-people"></i> Members</h2>
    <a href="${pageContext.request.contextPath}/members/add" class="btn btn-success">
        <i class="bi bi-person-plus"></i> Register New Member
    </a>
</div>

<div class="card">
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty members}">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Membership Type</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="member" items="${members}">
                            <tr>
                                <td>${member.memberId}</td>
                                <td><strong>${member.firstName} ${member.lastName}</strong></td>
                                <td>${member.email}</td>
                                <td>${member.phone}</td>
                                <td>
                                    <span class="badge bg-info">${member.membershipType}</span>
                                </td>
                                <td>
                                    <span class="badge ${member.active ? 'bg-success' : 'bg-secondary'}">
                                        ${member.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/members/edit/${member.memberId}" 
                                       class="btn btn-sm btn-outline-warning">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/members/delete/${member.memberId}" 
                                       class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Deactivate this member?')">
                                        <i class="bi bi-person-dash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> No members registered yet.
                    <a href="${pageContext.request.contextPath}/members/add">Register the first member!</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
