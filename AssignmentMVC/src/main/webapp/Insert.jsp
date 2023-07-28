<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Update Film</title>
</head>
<body>
	<fieldset>
		<legend>Add a Film:</legend>
		<form method="POST" action="./InsertFilm">
			<label for="title">Title:</label> <input name="add_title" type="text"> 
			<label for="title">Year:</label> <input name="add_year" type="number"> 
			<label for="title">Director:</label> <input name="add_director" type="text"> 
			<label for="title">Stars:</label> <input name="add_stars" type="text"> 
			<label for="title">Review:</label> <input name="add_review" type="text">
			<input type="submit" value="Add Film">
		</form>
	</fieldset>
</body>
</html>