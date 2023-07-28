// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry). 

function getRequestObject() {
	if (window.XMLHttpRequest) {
		return (new XMLHttpRequest());
	} else if (window.ActiveXObject) {
		return (new ActiveXObject("Microsoft.XMLHTTP"));
	} else {
		return (null);
	}
}

// Insert the html data into the element 
// that has the specified id.

function htmlInsert(id, htmlData) {
	document.getElementById(id).innerHTML = htmlData;
}

// Return escaped value of textfield that has given id.
// The builtin "escape" function url-encodes characters.

function getValue(id) {
	return (escape(document.getElementById(id).value));
}

//Handles POST requests.
function ajaxPost(address, format, film) {
	var request = getRequestObject();
	request.open("POST", address, true);
	request.setRequestHeader("Content-Type", format);

	let data;
	switch (format) {

		case "text/xml":
			/* Creating an XML document and converting
			 the film JSON object into XML */
			let xmlDoc = document.implementation.createDocument(null, 'film', null);
			let rootElement = xmlDoc.getElementsByTagName('film')[0];

			for (let key in film) {

				let element = xmlDoc.createElement(key);
				element.textContent = film[key];
				rootElement.appendChild(element);
			}
			/* Converting the XML document to a string
			 via XMLSerializer API */
			data = new XMLSerializer().serializeToString(xmlDoc);

			break;

		case "application/json":
			/* Setting data to film object */
			data = JSON.stringify(film);
			break;
		default:
			data = film.title + "#" + film.year + "#" + film.director + "#" + film.stars + "#" + film.review;
	}

	request.onload = function() {
		if (request.status == 200 & request.readyState == 4) {
			
			var addModal = document.querySelector("#addModal");
			
			//Hide the add modal and display success modal
			addModal.style.display = "none";
			$('#successModal').modal('show');	
			
			var form = document.getElementById("addForm");
			// Clear the form values
			form.reset();
			
			if(document.getElementById("filmTable")){
				document.getElementById("filmTable").parentNode.removeChild("filmTable");
			}
			
			/*  Removing the "modal-backdrop" class to fix an issue 
			where the webpage was still in a darkened state after 
			the 'success' modal was closed. */
			$('#successModal').on('hide.bs.modal', function () {
 				$('.modal-backdrop').remove();
			}); 
		}
		else {
			alert("Couldn't add film to the pool. Please try again.");
			return;
		}
	}
	request.onerror = function() {
		alert("Couldn't add film to the pool. An error occurred.");
	};

	console.log(data);
	request.send(data);

}
//Handles PUT requests.
function ajaxPut(address, format, film){
	var request = getRequestObject();
	request.open("PUT", address, true);
	request.setRequestHeader("Content-Type", format);
	
	let data;
	switch (format) {

		case "text/xml":
			/* Creating an XML document and converting
			 the film JSON object into XML */
			let xmlDoc = document.implementation.createDocument(null, 'film', null);
			let rootElement = xmlDoc.getElementsByTagName('film')[0];

			for (let key in film) {

				let element = xmlDoc.createElement(key);
				element.textContent = film[key];
				rootElement.appendChild(element);
			}
			/* Converting the XML document to a string
			 via XMLSerializer API */
			data = new XMLSerializer().serializeToString(xmlDoc);
			console.log(data);
			break;

		case "application/json":
			/* Setting data to film object */
			data = JSON.stringify(film);
			break;
		default:
			data = film.title + "#" + film.year + "#" + film.director + "#" + film.stars + "#" + film.review;
	}

	request.onload = function() {
		if (request.status == 200 & request.readyState == 4) {
			
			var updModal = document.querySelector("#updModal");
			console.log("SUCCESS");
			//Hide the update modal and display success modal
			updModal.style.display = "none";
			$('#successModal .modal-body').html('Movie updated successfully!');
			$('#successModal').modal('show');	
			
			var form = document.getElementById("updForm");
			// Clear the form values
			form.reset();
			var table = document.getElementById("filmTable");
			table.parentNode.removeChild(table);
			
			/*  Removing the "modal-backdrop" class to fix an issue 
			where the webpage was still in a darkened state after 
			the 'success' modal was closed. */
			$('#successModal').on('hide.bs.modal', function () {
 				$('.modal-backdrop').remove();
			});
		}
		else {
			alert("Couldn't update film. Please try again.");
			return;
		}
	}
	request.onerror = function() {
		alert("Couldn't add film to the pool. An error occurred.");
	};
	console.log(data);
	request.send(data);	
	
}
/* Handles GET requests. responseHandler function returns a different method 
   based on the requested data format. */
function ajaxResult(address, format, responseHandler) {
	var request = getRequestObject();
	request.onreadystatechange = function() { responseHandler(request); };
	request.open("GET", address, true);
	request.setRequestHeader("Accept", format);
	request.send(null);
}

/*Handles DELETE requests. */
function ajaxDelete(address) {
	var request = getRequestObject();
	request.open("DELETE", address, true);
	request.send(null);

	request.onload = function() {
		if (request.status == 200 & request.readyState == 4) {

			//Hide the delete modal and display success modal
			$('#delModal').modal('hide');
			
			var table = document.getElementById("filmTable");
			table.parentNode.removeChild(table);
		}
		else {
			alert("Couldn't delete film. Please try again.");
			return;
		}
	}
	request.onerror = function() {
		alert("Couldn't delete film. An error occurred.");
	};

}

// Given an element, returns the body content.

function getBodyContent(element) {
	element.normalize();
	return (element.childNodes[0].nodeValue);
}
// Takes as input an array of headings (to go into th elements)
// and an array-of-arrays of rows (to go into td
// elements). Builds an xhtml table from the data.


function getElementValues(element, subElementNames) {
	var values = new Array(subElementNames.length);
	for (var i = 0; i < subElementNames.length; i++) {
		var name = subElementNames[i];
		var subElement = element.getElementsByTagName(name)[0];
		values[i] = getBodyContent(subElement);
	}
	return (values);
}
/*initialises HTML table, obtains heading names and rows from 
the respective methods and returns completed table. */
function getTable(headings, rows) {
	var table = "<table border='1' id='filmTable' class='display'>\n" +
		getTableHeadings(headings) +
		getTableBody(rows) +
		"</table>";
		
	return (table);
}

/* initiliases the DataTables library and adds it to the film table. This is
used  for pagination and to improve the display of the table. */
/* function initialiseDataTable() {
	$(document).ready(function(){
		$('#filmTable').DataTable({
			"paging": true,
			"pageLength": 5
			//dom: '<"top"f>rt<"bottom"p><"clear">',
	
		});
	});
} */

function getTableHeadings(headings) {
	var firstRow = " <thead><tr>";
	for (var i = 0; i < headings.length; i++) {
		firstRow += "<th>" + headings[i] + "</th>";
	}
	firstRow += "</tr></thead>\n";
	return (firstRow);
}

function getTableBody(rows) {
	var body = "<tbody>";
	for (var i = 0; i < rows.length; i++) {
		body += "  <tr>";
		var row = rows[i];
		for (var j = 0; j < row.length; j++) {
			body += "<td>" + row[j] + "</td>";
		}
		body += "<td><button id='updateFilm' data-film-id='" + row[0] + "'><img src='img/update.png'/></button></td><td><button id='deleteFilm' data-film-id='" + row[0] + "'><img src='img/delete.png'</button></td>";
		body += "</tr>\n";
	}
	body += "</tbody>";
	return (body);
}
