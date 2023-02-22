package com.ampcorp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ampcorp.exception.ConnectionException;

public class DB_connection {
	public static String url;
	public static String username;
	public static String password;
	public static Connection con;
	
	static {
		con=null;
		try {
			ResourceBundle rb=ResourceBundle.getBundle("connectionDetails");
			try {
				url=rb.getString("url");
				username=rb.getString("username");
				password=rb.getString("password");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new ConnectionException("keys in connectionDetails not valid");
			}
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new ConnectionException("Driver Class not loaded!");
			}
		}catch(ConnectionException e) {
			e.getMessage();
		}
	}
	public static Connection makeConnection() {
		try {
			try {
				con=DriverManager.getConnection(url,username,password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new ConnectionException("Connection unsuccessfull!");
			}
		}catch(ConnectionException e) {
			e.getMessage();
		}
		return con;
		
		
	}
	public static void closeConnection() {
	try {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ConnectionException("Connection was not closed!");
		}
	}catch(ConnectionException e) {
		e.getMessage();
	}
	
	}
}
