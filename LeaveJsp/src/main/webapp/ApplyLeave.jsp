<%@ page import="com.java.leaveme.LeaveDetails" %>
<%@page import="java.sql.Date"%>
<%@ page import="com.java.leaveme.LeaveDAO" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		LeaveDetails LL = new LeaveDetails();
		LL.setEmpId(Integer.parseInt(request.getParameter("empno")));
		LL.setLeaveStartDate(Date.valueOf(request.getParameter("lstart")));
		LL.setLeaveEndDate(Date.valueOf(request.getParameter("lend")));
		LL.setLeaveReason(request.getParameter("reason"));
		LeaveDAO dao = new LeaveDAO();
		out.println(dao.applyLeave(LL));
		
	%>
</body>
</html>