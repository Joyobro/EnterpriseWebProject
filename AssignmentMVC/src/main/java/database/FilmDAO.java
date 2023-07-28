package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import models.Film;

public enum FilmDAO {

	//creating FilmDAO's single object
	INSTANCE;
	
	Film oneFilm = null;
	Connection conn = null;
	Statement stmt = null;
	String user = "allenjoe";
	String password = "guggebhO4";
	// Note none default port used, 6306 not 3306
	String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

	//Constructor method is private so only one object can be created.
	private FilmDAO() {
	}

	boolean connected = false;	
	
	private Statement openConnection() {
		// loading jdbc driver for mysql
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(e);
		}

		// connecting to database
		try {
			// connection string for films database, including username and password
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			
		} catch (SQLException se) {
			System.out.println(se);
		}
		return stmt;
	}

	private void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs) {
		Film thisFilm = null;
		try {
			thisFilm = new Film(rs.getInt("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director"),
					rs.getString("stars"), rs.getString("review"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thisFilm;
	}

	public ArrayList<Film> getAllFilms() {

		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				allFilms.add(oneFilm);
			}

			stmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}

		return allFilms;
	}

	public Film getFilmByID(int id) {

		openConnection();
		oneFilm = null;
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films where id=" + id;
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
			}

			stmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}

		return oneFilm;
	}

	// Accepts a parameter of Film object to add to the database.
	public Boolean insertFilm(Film f) throws SQLException {
		Boolean b = false;
		openConnection();
		try {
			String insertSQL = "insert into films (title, year, director, stars, review) values ('" + f.getTitle() + "','"
					+ f.getYear() + "','" + f.getDirector() + "','" + f.getStars() + "','" + f.getReview() + "');";
			System.out.println(insertSQL);
			stmt.executeUpdate(insertSQL);
			stmt.close();
			closeConnection();
			b = true;
		} 
		catch (SQLException s) {
			//throw new SQLException("Film Not Added");
			s.printStackTrace();
		}
		return b;
	}

	// Accepts a parameter of Film object to update a film record.
	public Boolean updateFilm(Film f) throws SQLException {
		openConnection();
		Boolean b =false;
	   try {
		   String sql = "UPDATE films SET title= '" + f.getTitle() + "', year= '" + f.getYear() + "', director= '" + f.getDirector() + "', stars= '" + f.getStars() + "', review= '" + f.getReview() + "' WHERE id='" + f.getId() + "';";
		   System.out.println(sql);
		   stmt.executeUpdate(sql);
		   stmt.close();
		   closeConnection();
		   b = true;
	   }
	   catch (SQLException s) {
		   throw new SQLException("Film Not Updated");
	   }
	   return b;
	   
	   
   }

	// Accepts a parameter of Film id to delete a film record.
	public Boolean deleteFilm(int id) throws SQLException {
		Boolean b = false;
		openConnection();
		try {
			String sql = "delete from films where id=" + id;
			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			closeConnection();
			b = true;
		} catch (SQLException s) {
			//throw new SQLException("Film not deleted");
			s.printStackTrace();
		}
		return b;
	}
	
	public ArrayList<Film> searchFilms(String searchStr) {
		ArrayList<Film> searchResult = new ArrayList<Film>();
		openConnection();
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films WHERE title LIKE '%"+searchStr+"%';";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				searchResult.add(oneFilm);
			}

			stmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}

		return searchResult;
	}
}


