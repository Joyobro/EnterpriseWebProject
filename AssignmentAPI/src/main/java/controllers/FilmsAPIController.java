package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import database.FilmDAO;
import models.Film;
import models.FilmList;


@WebServlet("/films-api")
public class FilmsAPIController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public FilmsAPIController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		FilmDAO dao = FilmDAO.INSTANCE;
		//GSON setup
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PrintWriter out = response.getWriter();

		String result = "";
		String format = request.getHeader("Accept");
		String idString = request.getParameter("id");
		String titleString = request.getParameter("title");
		
		//Determines which search to execute based on the nature of the request 
		if (idString != null || titleString != null) {
			if (idString != null) {
				int id = Integer.parseInt(idString);
				//call to DAO to execute search
				Film film = dao.getFilmByID(id);

				if (film != null) {
					//if statements check 'content-type' and 'accept' headers of GET request.
					if ("text/xml".equals(format)) {
						//if xml, execute JAXB marshaller to create XML data
						response.setContentType("text/xml");
						StringWriter sw = new StringWriter();

						try {
							JAXBContext context = JAXBContext.newInstance(Film.class);
							Marshaller m = context.createMarshaller();
							m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
							m.marshal(film, sw);
							result = sw.toString();
							out.write(result);

						} catch (JAXBException e) {
							out.write("could not find film data.");

						}
					} else if ("application/json".equals(format)) {
						// Converting a Java object to JSON format
						response.setContentType("application/json");
						result = gson.toJson(film);
						out.write(result);
					} else if ("text/plain".equals(format)) {
						//If format is string, set film object with # as delimiter
						response.setContentType("text/plain");
						result = film.getTitle() + "#" + film.getYear() + "#" + film.getDirector() + "#"
								+ film.getStars() + "#" + film.getReview();
						out.write(result);
					} else {
						out.write("Couldn't load film data.");
					}
					request.setAttribute("film", film);
				} else {

				}
			} else if (!(titleString == null)) {
				ArrayList<Film> films = dao.getFilmByTitle(titleString);
				if (films != null) {
					if ("text/xml".equals(format)) {
						try {
							// Converting Java object to XML format
							response.setContentType("text/xml");
							FilmList fl = new FilmList(films);
							StringWriter sw = new StringWriter();
							JAXBContext context = JAXBContext.newInstance(FilmList.class);
							Marshaller m = context.createMarshaller();
							m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
							m.marshal(fl, sw);
							result = sw.toString();
							out.write(result);
						} catch (JAXBException e) {
							e.printStackTrace();
						}
					} else if ("application/json".equals(format)) {
						// Converting a Java object to JSON format
						response.setContentType("application/json");
						result = gson.toJson(films);
						out.write(result);

					} else if ("text/plain".equals(format)) {
						// Converting a Java object to String format
						response.setContentType("text/plain");

						for (int i = 0; i < films.size(); i++) {

							Film currentFilm = films.get(i);
							result = result + currentFilm.getTitle() + "#" + currentFilm.getYear() + "#"
									+ currentFilm.getDirector() + "#" + currentFilm.getStars() + "#"
									+ currentFilm.getReview();
							if (i < films.size()) {
								result = result + "\\n";
							}
						}
						if (result != null) {
							out.write(result);
						}
					} else {
						out.write("Couldn't load film data.");
					}
				}
			}

		}

		else {/* If there is no 'id' or 'title' parameters, fetch ALL films from DB. */
			System.out.println("ALL FILMS");
			ArrayList<Film> allFilms = dao.getAllFilms();
			request.setAttribute("films", allFilms);

			if ("text/xml".equals(format)) {
				try {
					// Converting Java object to XML format
					response.setContentType("text/xml");
					FilmList fl = new FilmList(allFilms);
					StringWriter sw = new StringWriter();
					JAXBContext context = JAXBContext.newInstance(FilmList.class);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(fl, sw);
					result = sw.toString();
					out.write(result);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			} else if ("application/json".equals(format)) {
				// Converting a Java object to JSON format
				response.setContentType("application/json");
				result = gson.toJson(allFilms);
				out.write(result);

			} else if ("text/plain".equals(format)) {
				// Converting a Java object to String format
				response.setContentType("text/plain");

				for (int i = 0; i < allFilms.size(); i++) {

					Film currentFilm = allFilms.get(i);
					result = result + currentFilm.getTitle() + "#" + currentFilm.getYear() + "#"
							+ currentFilm.getDirector() + "#" + currentFilm.getStars() + "#" + currentFilm.getReview();
					if (i < allFilms.size()) {
						result = result + "\\n";
					}
				}
				if (result != null) {
					out.write(result);
				}
			} else {
				out.write("Couldn't load film data.");
			}
		}
	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	   PrintWriter out = response.getWriter();
	   response.setHeader("Cache-Control", "no-cache");
	   response.setHeader("Pragma", "no-cache");
	   //determine format of the response data
	   String format = request.getHeader("Content-Type");
	   FilmDAO dao = FilmDAO.INSTANCE;
	   
	   BufferedReader reader = request.getReader();
	   StringBuilder sb = new StringBuilder();
	   String line;
	   Film filmToAdd = new Film();
	   //accessing the body of the request:   
	   while ((line = reader.readLine()) != null) {
		    sb.append(line);
	   }
		String body = sb.toString();
	   
	   switch (format) {
	   case "text/xml":
		   try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				filmToAdd = (Film) unmarshaller.unmarshal(new StringReader(body));
				System.out.println(filmToAdd);
		   }
		   catch(JAXBException jaxbe) {
			   
		   }
		   
		   
		  break;
	   case "application/json":
		   Gson gson = new Gson();
		   filmToAdd = gson.fromJson(body, Film.class);
		   System.out.println(filmToAdd);
		   	   
		   break;
		   
	   default:
		   String[] fields = body.split("\\#");
		   filmToAdd.setTitle(fields[0]);
		   filmToAdd.setYear(Integer.parseInt(fields[1]));
		   filmToAdd.setDirector(fields[2]);
		   filmToAdd.setStars(fields[3]);
		   filmToAdd.setReview(fields[4]);
		   
		   System.out.println(filmToAdd);
	   }
	   
	    //THEN use DAO to add to database
	   
	   try {
		   dao.insertFilm(filmToAdd);
		   out.write("Record added successfully.");
	   }
	   catch(SQLException sqle) {
		   
	   }
	    
	}

	// *UPDATE*
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		String format = request.getHeader("Content-Type");
		FilmDAO dao = FilmDAO.INSTANCE;
		PrintWriter out = response.getWriter();

		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		Film updatedFilm = new Film();
		
		while ((line = reader.readLine()) != null) {
		    sb.append(line);
		}
		String body = sb.toString();
		
		switch (format) {
		   case "text/xml":
			   try {
			   JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
			   Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			   updatedFilm = (Film) unmarshaller.unmarshal(new StringReader(body));	
			   System.out.println(updatedFilm);
			   }
			   catch(JAXBException jaxbe) {
				   
			   }
			     
			  break;
		   case "application/json":
			   Gson gson = new Gson();
			   updatedFilm = gson.fromJson(body, Film.class);
			   System.out.println(updatedFilm);
			   	   
			   break;
			   
		   default:
			   String[] fields = body.split("\\#");
			   updatedFilm.setTitle(fields[0]);
			   updatedFilm.setYear(Integer.parseInt(fields[1]));
			   updatedFilm.setDirector(fields[2]);
			   updatedFilm.setStars(fields[3]);
			   updatedFilm.setReview(fields[4]);
			   
			   System.out.println(updatedFilm);
		   }
		   
		    //Use DAO to add to database
		   
		   try {
			   dao.updateFilm(updatedFilm);
			   out.write("Record updated successfully.");
		   }
		   catch(SQLException sqle) {
			   sqle.printStackTrace();
		   }
		    
	   
		
		

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache");
	    response.setHeader("Pragma", "no-cache");
	    PrintWriter out = response.getWriter();
	   	    
		FilmDAO dao = FilmDAO.INSTANCE;
		//grab ID from the request url
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);
		try {
			dao.deleteFilm(id);
			out.write("Record deleted successfully.");
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}

	}
	

}
