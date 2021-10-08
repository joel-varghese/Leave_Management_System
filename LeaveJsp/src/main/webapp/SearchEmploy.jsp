<%@ page import="com.java.leaveme.Employee" %>
<%@ page import="com.java.leaveme.EmployDAO" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		int empno = Integer.parseInt(request.getParameter("empno"));
		Employee employee = new EmployDAO().searchEmploy(empno);
		if (employee!=null) {
			out.println("Employee Name  " +employee.getEmpName());
		}
	%>
</body>
</html>