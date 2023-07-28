package database;
import java.util.ArrayList;

import models.Film;

import java.sql.*;


public enum FilmDAO {
	
	INSTANCE;
	
	Film oneFilm = null;
	Connection c = null;
    Statement s = null;
    ResultSet r = null;
    
	String user = "allenjoe";
    String password = "guggebhO4";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	private FilmDAO() {}

	
	private Statement getConnection(){
		// loading jdbc driver for mysql
		try{		
		    Class.forName("com.mysql.jdbc.Driver");
		    c = DriverManager.getConnection(url, user, password);
		    s = c.createStatement();
		    
		} catch(Exception e) { System.out.println(e); }
	   return s;
    }
	private void closeConnection(){
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Used when iterating through a ResultSet to convert returned data into Film objects:
	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
   //Creates an ArrayList of all the films in the DB and returns to the controller.
   public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		getConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films";
		    System.out.println(selectSQL);
		    ResultSet rs1 = s.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    s.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

   //Searches for a film using it's unique ID number:
   public Film getFilmByID(int id){
	   
		getConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    System.out.println(selectSQL);
		    ResultSet rs1 = s.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    s.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
   //Inserts a user-created film into the database. returns a boolean to determine the success of the operation
	public boolean insertFilm(Film f) throws SQLException {

		boolean inserted = false;
		try {
			
			String sql = "insert into films(title,year,director,stars,review) values ('" + f.getTitle() + "','" + f.getYear() + "','" + f.getDirector() + "','" + f.getStars() + "','" + f.getReview() + "');";
			System.out.println(sql);
			inserted = getConnection().execute(sql);
			closeConnection();
			inserted = true;
		} catch (SQLException s) {
			throw new SQLException("Film Not Added");
			
		}
		return inserted;
	}
	/* Updates a film using it's ID number and it's updated attributes. Returns a boolean to 
	 * determine the success of the operation.
	 */
	public boolean updateFilm(Film f) throws SQLException {
		boolean updated = false;
		try{
			String sql = "UPDATE films SET title='"+f.getTitle()+"',year='"+f.getYear()+"',director='"+f.getDirector()+"',stars='"+f.getStars()+"',review='"+f.getReview()+"'WHERE id='"+f.getId()+"';";
			String output = sql.replaceAll("%20", " ").replaceAll("%2C", ",");
			updated = getConnection().execute(output);
			closeConnection();
			updated = true;
		} catch(SQLException s) {
			throw new SQLException("Film Not Updated");
		}
		return updated;	
	}
	/* Deletes a film using it's ID. Returns a boolean to 
	 * determine the success of the operation.
	 */
	public boolean deleteFilm(int id) throws SQLException{
		boolean deleted = false;
		try {
			String sql = "DELETE FROM films WHERE id="+ id + ";";
			System.out.println(sql);
			deleted = getConnection().execute(sql);
			closeConnection();
			deleted = true;
		}
		catch(SQLException s) {
			throw new SQLException("Film Not Deleted");
		}
		return deleted;
	}

	/*Searches for a film using a substring of the film's title.*/
	public ArrayList<Film> getFilmByTitle(String titleString) {
		ArrayList<Film> searchResult = new ArrayList<Film>();
		getConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where title LIKE '%"+titleString+"%';";
		    ResultSet rs1 = s.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	searchResult.add(oneFilm);
		    }
		    s.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return searchResult;
	}
		
		
}
