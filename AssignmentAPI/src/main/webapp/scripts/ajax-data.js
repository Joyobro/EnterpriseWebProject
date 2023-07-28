function getFilmTable(rows) {
	var headings = ["ID","Title", "Year", "Director", "Stars", "Review"];
	return (getTable(headings, rows));
}

function showXmlFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var xmlDocument = request.responseXML;
		console.log(xmlDocument);
		var films = xmlDocument.getElementsByTagName("film");
		var rows = new Array();
		var subElementNames = ["id", "title", "year", "director","stars","review"];

		for (var i = 0; i < films.length; i++) {
			rows[i] =
				getElementValues(films[i], subElementNames);
		}
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
	}
}

function showJsonFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;
		var data = eval("(" + rawData + ")");
		var rows = new Array();
	    for(var i=0; i<data.length; i++) {
	      var film = data[i];
	      rows[i] = [film.id,film.title,
	                 film.year, film.director, film.stars, film.review];
	    }    
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
	}
}

function showStringFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;

		var rowStrings = rawData.split("\\n");
		console.log(rowStrings);
		var rows = new Array(rowStrings.length - 1);
		for (var i = 1; i < rowStrings.length; i++) {
			rows[i - 1] = rowStrings[i-1].split("#");
		}
		console.log(rows);
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
	}
}
  var getFilmsBtn = document.getElementById("getFilms");
  
  /* When the user clicks the relavant button, open the loading modal 
  and start the loading process. */ 
  getFilmsBtn.onclick = function() {
	    $('#loadingModal').modal('show');  
	    filmTable('data-format','tableRegion');
	}
/*Performs a GET request for film records matching the 
user-specified ID. */ 

$("#btnSearchID").click(function() {
	var searchID = $("#txtSearchID").val();
	if (searchID == "") {
		alert("Search term cannot be blank.");
	}
	else {
		if (document.getElementById("filmTable")) {
			$("#filmTable").remove();
		}
		$("#loadingModal").modal('show');
		$.ajax({
			type: "GET",
			data: { id: searchID },
			url: "films-api?id=" + searchID,
			headers: {
				'Content-Type': 'application/json',
				'Accept': 'application/json',
			},
			success: function(response) {
				if (Object.keys(response).length === 0 || typeof response.id === 'undefined') {
					closeLoadingModal();
					alert("Please enter a valid ID!");
					console.log("Invalid ID entered: " + searchID);
				}
				else if (Object.keys(response).length > 0) {
					console.log(response);
					var row = [];
					row.push([response.id, response.title, response.year, response.director, response.stars, response.review]);
					console.log(row);
					htmlInsert("tableRegion", getFilmTable(row));
					closeLoadingModal();
				}
			},
			error: function(error) {
				closeLoadingModal();
				alert("Please enter a valid ID.");
				console.log(error);

			}
		});

	}
});

$("#btnSearchTitle").click(function(){
	var searchTitle = $("#txtSearchTitle").val();
	console.log(searchTitle);
	if (searchTitle == "") {
		alert("Search term cannot be blank.");
	}
	else {
		if (document.getElementById("filmTable")) {
			$("#filmTable").remove();
		}
		$("#loadingModal").modal('show');
		$.ajax({
			type: "GET",
			data: { title: searchTitle },
			url: "films-api?title="+searchTitle,
			headers: {
				'Content-Type': 'application/json',
				'Accept': 'application/json',
			},
			success: function(response) {
				if (response.length > 0){

					closeLoadingModal();
					var rows = [];
					for (var i = 0; i < response.length; i++) {
						rows.push([response[i].id, response[i].title, response[i].year, response[i].director, response[i].stars, response[i].review]);
					}
					console.log(rows);
					htmlInsert("tableRegion", getFilmTable(rows));
				}
				else {
					closeLoadingModal();
					alert("Please enter a valid title.");
					
				}
			},
			error: function(error) {
				closeLoadingModal();
				alert("Please enter a valid title.");
				console.log(error);
			}
		});
	}
});

  /* When update request is made, the film ID hidden in the button is
  	used to retrieve the corresponding attribute values and dynamically
  	update the modal so the user doesn't have to retype the original values. 
  	Uses event delegation to attach an event listener to the update button that
  	is dynamically created with the table and not intialised upon loading of web page. */
 
  $(document).on("click", "#updateFilm",function() {
	var filmId = $(this).data("film-id");
	
	$.ajax({
		type: 'GET',
		url: '/AssignmentAPI/films-api',
		data: {id: filmId},
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json',
		},
		success: function(response){
			$("#updModal").modal("show");
			
			$("#upd-id").val(response.id);
			$("#upd-title").val(response.title);
			$("#upd-year").val(response.year);
			$("#upd-director").val(response.director);
			$("#upd-stars").val(response.stars);
			$("#upd-review").val(response.review);
			
		},
		error: function(){
			alert('Could not retrieve movie data.');
		}
	});
});

/*Delete button pressed, delete modal shows on page. */
$(document).on("click", "#deleteFilm",function(){
	var filmId = $(this).data("film-id");
	
	$.ajax({
		type: 'GET',
		url: '/AssignmentAPI/films-api',
		data: { id: filmId },
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json',
		},
		success: function(response) {
			$("#del-id").val(response.id);
			$("#del-title").text("Are you sure to wish to delete " + response.title + "?");
			$("#delModal").modal("show");
			
		}
	});
	
});

 // Gets the loading modal
  var loadingModal = document.getElementById("loadingModal");

  // Gets the spinner
  var spinner = document.getElementById("spinner");

  // Close the modal when the loading is complete
  function closeLoadingModal() {
    loadingModal.style.display = "none";
	$('#loadingModal').modal('hide');
	$('body').removeClass('modal-open');
	$('div').removeClass('modal-backdrop');
  }
  

function filmTable(formatField, resultRegion) {
	var format = getValue(formatField);
	//initialiseDataTable();
	var address = "films-api";
	var responseHandler = findHandler(format);
	ajaxResult(address, format,
		function(request){
			responseHandler(request, resultRegion);
			//Closes the 'loading' modal when request is complete
			closeLoadingModal();
		});
}
function filmPost(addTitle, addYear, addDirector, addStars, addReview, formatField){
	var format = getValue(formatField);	
	var newTitle = getValue(addTitle);
	var newYear= getValue(addYear);
	var newDirector = getValue(addDirector);
	var newStars = getValue(addStars);
	var newReview = getValue(addReview);
	var address = "films-api?format="+format+"&title="+newTitle+"&year="+newYear+"&director="+newDirector+"&stars="+newStars+"&review="+newReview;
	let film;
	
	/* VALIDATION CHECKS */
	 
	 	//checks for blank user entries
	if (newTitle === "" || newYear === "" || newDirector === "" || newStars === "" || newReview === "") {
	    // Display an error message
	    alert("All fields are required!");
	    return;
	  }
		// Check if the year is a valid number
  	else if (Number.isNaN(newYear) || newYear < 1895) {

    	alert("Please enter a valid year. This must be a year no earlier than 1895, the year containing the oldest movie on record!");
    	return;
 	  }
 	
 	else if (!newTitle.match(/^[a-zA-Z0-9\s]*$/)) {
		alert("Please enter a valid title. Only letters and special characters are allowed.");
		return;
	}
	else if (!newDirector.match(/^[a-zA-Z0-9\s]+$/)) {
		alert("Please enter a valid director. Only letters and special characters are allowed.");
		return;
	}
	else if (!newStars.match(/^[a-zA-Z0-9\s]+$/)) {
		alert("Please enter valid movie stars. Only letters and special characters are allowed.");
		return;
	}
	else if (!newReview.match(/^[a-zA-Z0-9\s]+$/)) {
		alert("Please enter a valid review. Only letters and special characters are allowed.");
		return;
	}
	
	else if (newReview.length > 1000) {
  		alert("Please enter a review that is no longer than 1000 characters.");
  		return;
	}
	
	else{
		film = {title: newTitle, year: newYear, director: newDirector, stars: newStars, review: newReview};
		ajaxPost(address, format, film);		
	}
}

function filmPut(updId, updTitle, updYear, updDirector, updStars, updReview, formatField){
	var format = getValue(formatField);	
	var newId = getValue(updId);
	var newTitle = getValue(updTitle);
	var newYear= getValue(updYear);
	var newDirector = getValue(updDirector);
	var newStars = getValue(updStars);
	var newReview = getValue(updReview);
	var address = "films-api?format="+format+"&id="+newId+"&title="+newTitle+"&year="+newYear+"&director="+newDirector+"&stars="+newStars+"&review="+newReview;
	let film;
	
		
	/* VALIDATION CHECKS */
	 
	 	//checks for blank user entries
	if (newTitle === "" || newYear === "" || newDirector === "" || newStars === "" || newReview === "") {
	    // Display an error message
	    alert("All fields are required!");
	    return;
	  }
		// Check if the year is a valid number
  	else if (Number.isNaN(newYear) || newYear < 1895) {

    	alert("Please enter a valid year. This must be a year no earlier than 1895, the year containing the oldest movie on record!");
    	return;
 	  }
 	
 	else if (!newTitle.match(/^[a-zA-Z0-9 ?\/#-.]+$/)) {
		alert("Please enter a valid title. Only letters and special characters are allowed.");
		return;
	}
	else if (!newDirector.match(/^[a-zA-Z0-9 ?\/#-.]+$/)) {
		alert("Please enter a valid director. Only letters and special characters are allowed.");
		return;
	}
	else if (!newStars.match(/^[a-zA-Z0-9 ?\/#-.]+$/)) {
		alert("Please enter valid movie stars. Only letters and special characters are allowed.");
		return;
	}
	else if (!newReview.match(/^[a-zA-Z0-9\s?\/#-.]+$/)) {
		alert("Please enter a valid review. Only letters and special characters are allowed.");
		return;
	}
	
	else if (newReview.length > 1000) {
  		alert("Please enter a review that is no longer than 1000 characters.");
  		return;
	}
	
	else{
		film = {id: newId, title: newTitle, year: newYear, director: newDirector, stars: newStars, review: newReview};
		console.log(film);
		ajaxPut(address, format, film);		
	}
}

function filmDelete(delId){
	var id = getValue(delId);
	var address = "films-api?id="+id;
	ajaxDelete(address);
}

function findHandler(format) {
	
	if (format == "text/xml") {
		return (showXmlFilmInfo);		
	} else if (format == "application/json") {
		return (showJsonFilmInfo);
	} else if (format == "text/plain") {
		return (showStringFilmInfo);
	}
}