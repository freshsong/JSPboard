<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, jspBoard.dao.JBoardDao,jspBoard.dto.BDto" %>    
<jsp:useBean id="db" class="jspBoard.dao.DBConnect" scope="page" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <script>
    alert("비밀번호를 다시 확인하세요.");
    history.go(-1);
   </script>
</body>
</html>