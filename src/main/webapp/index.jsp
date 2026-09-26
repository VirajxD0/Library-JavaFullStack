<%-- 
    index.jsp - Welcome page that redirects to dashboard
    
    This is configured as the welcome-file in web.xml
    It redirects to the dashboard controller
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Redirect to the dashboard
    response.sendRedirect(request.getContextPath() + "/dashboard");
%>
