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
 * Servlet implementation class DeleteController
 */
@WebServlet("/DeleteFilm")
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DeleteController() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		FilmDAO dao = FilmDAO.INSTANCE;
		int idToDelete = Integer.parseInt(request.getParameter("deleteFilm"));
		Film filmToDelete = dao.getFilmByID(idToDelete);
		request.setAttribute("film", filmToDelete);
		
		RequestDispatcher rd = request.getRequestDispatcher("Delete.jsp");
		rd.include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		FilmDAO dao = FilmDAO.INSTANCE;
		int idToDelete = Integer.parseInt(request.getParameter("DeleteId"));
		try {
			dao.deleteFilm(idToDelete);
			response.sendRedirect("./Home");
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
