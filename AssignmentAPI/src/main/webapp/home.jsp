<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./styles/movie-pool-style.css">
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">

<!-- jQuery, Popper.js, Bootstrap JS-->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"></script>	

<meta charset="ISO-8859-1"></meta>
<meta name="viewport" content="width=device-width, initial-scale=1"></meta>

<title>The Movie Pool</title>
</head>
<body>
	<div class="header">
		<h1>The Movie Pool</h1>
		<div class="searchFunctions">
			<button class="btn-addBtn" data-toggle="modal"
				data-target="#addModal">Can't find a movie? Add it yourself</button>
			<label for="txtSearchID">Search by ID:</label> <input type="text"
				id="txtSearchID">
			<button id="btnSearchID">
				<img src="img/search.png" />
			</button>
			<label for="txtSearchTitle">Search by Title:</label> <input
				type="text" id="txtSearchTitle">
			<button id="btnSearchTitle">
				<img src="img/search.png" />
			</button>
		</div>
	</div>
	
	<!-- Modal for Add Movie -->
	<div class="modal fade" id="addModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Add to the Pool:</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<!-- Modal Body -->
				<div class="modal-body">
					<form id="addForm" class="form-container">
						<label for="add-title"><b>Title:</b></label> <input type="text"
							placeholder="The Adventures of Joe Allen" id="add-title"
							name="add-title" required> <label for="add-year"><b>Year:</b></label>
						<input type="number" placeholder="2023" id="add-year"
							name="add-year" min="1895" required><br> <label
							for="add-director"><b>Director:</b></label> <input type="text"
							placeholder="Joe Allen" id="add-director" name="add-director"
							required> <label for="add-stars"><b>Starring:</b></label>
						<input type="text" placeholder="Joe Allen" id="add-stars"
							name="add-stars" required> <label for="add-review"><b>Review:</b></label>
						<input type="text"
							placeholder="Please make your review informative. NO SPOILERS!"
							id="add-review" name="add-review" required> <select
							id="dataFormat">
							<option selected="selected" value="text/xml">XML</option>
							<option value="application/json">JSON</option>
							<option value="text/plain">String</option>
						</select>
					</form>
				</div>
				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="submit" id="submit-btn"
						onclick="filmPost('add-title','add-year','add-director','add-stars','add-review','dataFormat')"
						class="btn">Add Movie</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">
						Close</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal For Success Message -->
	<div class="modal fade" id="successModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Success!</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">Movie added to the pool successfully.
				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="close" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
		<!-- Modal for Updating Movie -->
	<div class="modal fade" id="updModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Update a Movie:</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<!-- Modal Body -->
				<div class="modal-body">
					<form id="updForm" class="form-container">
						<input type="hidden" id="upd-id"></input>
						
						<label for="upd-title"><b>Title:</b></label> <input type="text"
								 id="upd-title"
							name="upd-title" required> <label for="upd-year"><b>Year</b></label>
						<input type="number" id="upd-year"
							name="upd-year" min="1895" required><br> <label
							for="upd-director"><b>Director:</b></label> <input type="text"
							id="upd-director" name="upd-director"
							required> <label for="upd-stars"><b>Starring:</b></label>
						<input type="text"  id="upd-stars"
							name="upd-stars" required> <label for="upd-review"><b>Review:</b></label>
						<input type="text"
							id="upd-review" name="upd-review" required> <select
							id="dataFormat">
							<option selected="selected" value="text/xml">XML</option>
							<option value="application/json">JSON</option>
							<option value="text/plain">String</option>
						</select>
					</form>
				</div>
				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="submit" id="submit-update-btn"
						onclick="filmPut('upd-id','upd-title','upd-year','upd-director','upd-stars','upd-review','dataFormat')"
						class="btn">Update Movie</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">
						Close</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Delete Modal-->
	<div class="modal fade" id="delModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<p>Delete from the Pool:</p>
					<div class="modal-body">
						<input id="del-id" type="hidden"></input>
						<label id="del-title"></label>
					</div>
					<div class="modal-footer">
						<button type="submit" id="submit-delete-button"
							onclick="filmDelete('del-id')" class="btn">Delete Movie</button>
						<button type="button" class="btn btn-danger" data-dismiss="modal">
							Close</button>

					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- A fieldset that contains the different format options, with the div container used to 
	hold the resulting container -->
	<fieldset>
		<legend>Films</legend>
		<select id="data-format">
			<option value="text/xml">XML</option>
			<option value="application/json">JSON</option>
			<option value="text/plain">String</option>
		</select> <input id="getFilms" type="button" value="Get Film Data"></input>
		<div id="tableRegion"></div>
	</fieldset>
	<!-- Modal that contains a progress bar, GET request executed in the background -->
	<div class="modal fade" id="loadingModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<p>Loading...</p>
					<div class="modal-body">
						<div class="spinner"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Importing my JavaScript files: -->
	<script src=".\scripts\ajax-utils.js" type="text/javascript"></script>
	<script src=".\scripts\ajax-data.js" type="text/javascript"></script>
</body>
</html>