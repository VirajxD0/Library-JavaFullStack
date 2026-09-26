<%-- add-member.jsp - Registration form for new members --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../common/header.jsp"/>

<h2 class="mb-4"><i class="bi bi-person-plus"></i> Register New Member</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <form:form action="${pageContext.request.contextPath}/members/save" 
                   method="post" modelAttribute="member" class="row g-3">

            <div class="col-md-6">
                <label class="form-label">First Name *</label>
                <form:input path="firstName" class="form-control" required="required"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Last Name *</label>
                <form:input path="lastName" class="form-control" required="required"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Email *</label>
                <form:input path="email" type="email" class="form-control" required="required"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Phone</label>
                <form:input path="phone" class="form-control" placeholder="555-0100"/>
            </div>

            <div class="col-12">
                <label class="form-label">Address</label>
                <form:textarea path="address" class="form-control" rows="2"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Membership Type</label>
                <form:select path="membershipType" class="form-select">
                    <form:option value="STANDARD">Standard</form:option>
                    <form:option value="PREMIUM">Premium</form:option>
                    <form:option value="STUDENT">Student</form:option>
                </form:select>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-check-circle"></i> Register Member
                </button>
                <a href="${pageContext.request.contextPath}/members" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> Cancel
                </a>
            </div>
        </form:form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
