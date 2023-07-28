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
		<legend>Update an Existing Film Record: </legend>
		<form method="POST" action="./UpdateFilm">
			<input type="hidden" name="UpdateId" value="${film.id}"></input> 
			<label for="title">Title:</label> <input name="upd_title" type="text" value="${film.title}"> 
			<label for="title">Year:</label> <input name="upd_year" type="number" value="${film.year}"> 
			<label for="title">Director:</label> <input name="upd_director" type="text" value="${film.director}"> 
			<label for="title">Stars:</label> <input name="upd_stars" type="text" value="${film.stars}"> 
			<label for="title">Review:</label> <input name="upd_review" type="text" value="${film.review}">

			<input type="submit" value="Update">
		</form>
	</fieldset>
</body>
</html>