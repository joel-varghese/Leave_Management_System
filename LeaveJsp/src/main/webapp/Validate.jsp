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
	int leaveid = Integer.parseInt(request.getParameter("lid"));
	int mgrid = Integer.parseInt(request.getParameter("mgrid"));
	String status = request.getParameter("status");
	String comm = request.getParameter("comm");
	LeaveDAO dao = new LeaveDAO();
	out.println(dao.approveDeny(mgrid, leaveid, status, comm));
%>
</body>
</html>