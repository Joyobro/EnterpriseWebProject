<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Delete Film</title>
</head>
<body>
		<form method="POST" action="./DeleteFilm">
			<h1><f:out value="${film.title}"></f:out></h1>
			<input name="DeleteId" type="hidden" value="${film.id}"></input>
			<label>Do you wish to delete?</label> 
			<input type="submit" value="Yes">	
		</form>
</body>
</html>