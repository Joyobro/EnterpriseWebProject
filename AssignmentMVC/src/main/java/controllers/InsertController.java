package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

/**
 * Servlet implementation class InsertController
 */
@WebServlet("/InsertFilm")
public class InsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public InsertController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher rd = request.getRequestDispatcher("Insert.jsp");
		rd.include(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FilmDAO dao = FilmDAO.INSTANCE;
		
		Film addedFilm = new Film();
		addedFilm.setTitle(request.getParameter("add_title").replaceAll("\\*|'|=|,", "#"));
		addedFilm.setYear(Integer.parseInt(request.getParameter("add_year")));
		addedFilm.setDirector(request.getParameter("add_director"));
		addedFilm.setStars(request.getParameter("add_stars"));
		addedFilm.setReview(request.getParameter("add_review"));
		
		try {
			dao.insertFilm(addedFilm);
			response.sendRedirect("./Home");
			RequestDispatcher rd = request.getRequestDispatcher("Home.jsp");
			rd.include(request, response);
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
