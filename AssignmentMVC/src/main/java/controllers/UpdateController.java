package controllers;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class UpdateController
 */
@WebServlet("/UpdateFilm")
public class UpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FilmDAO dao = FilmDAO.INSTANCE;
		int idToUpdate = Integer.parseInt(request.getParameter("updateFilm"));
		Film filmToUpdate = dao.getFilmByID(idToUpdate);
		request.setAttribute("film", filmToUpdate);
		RequestDispatcher rd = request.getRequestDispatcher("Update.jsp");
		rd.include(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Film updatedFilm = new Film();
		int idToUpdate = Integer.valueOf(request.getParameter("UpdateId"));
		updatedFilm.setId(idToUpdate);
		updatedFilm.setTitle(request.getParameter("upd_title"));
		updatedFilm.setYear(Integer.parseInt(request.getParameter("upd_year")));
		updatedFilm.setDirector(request.getParameter("upd_director"));
		updatedFilm.setStars(request.getParameter("upd_stars"));
		updatedFilm.setReview(request.getParameter("upd_review"));
		
		FilmDAO dao = FilmDAO.INSTANCE;
		try {
			dao.updateFilm(updatedFilm);
			response.sendRedirect("./Home");
			
		} catch (SQLException sqle) {
			System.out.println(sqle);
			out.write("" + sqle);
		}
		
		
		
	}

}
