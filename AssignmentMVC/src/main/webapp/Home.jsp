<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="f"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welsh Goat Films</title>
</head>
<body>
	<h1>Welsh Goat Films</h1>
	<form method="GET" action= "./InsertFilm"> 
		<input type="submit" name="insertFilm" value="Add a New Film"> 
	</form> 
	<table>
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Year</th>
			<th>Director</th>
			<th>Stars</th>
			<th>Review</th>
		</tr>
		<f:forEach items="${films}" var="f">
			<tr>
				<td>${f.getId()}</td>
				<td>${f.getTitle()}</td>
				<td>${f.getYear()}</td>
				<td>${f.getDirector()}</td>
				<td>${f.getStars()}</td>
				<td>${f.getReview()}</td>
				<td>
					<form method="GET" action="./UpdateFilm">
						<input type="hidden" name="updateFilm" value="${f.id}">
						<input type='submit' value='update'>
					</form>
				</td>
				<td>
					<form method="GET" action="./DeleteFilm">
						<input type="hidden" name="deleteFilm" value="${f.id}">
						<input type='submit' value='delete'>
					</form>
				</td>
			</tr>
		</f:forEach>
	</table>
</body>
</html>