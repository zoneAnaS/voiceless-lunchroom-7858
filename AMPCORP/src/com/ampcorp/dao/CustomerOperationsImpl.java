package com.ampcorp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ampcorp.dto.Bill;
import com.ampcorp.dto.Transaction;
import com.ampcorp.exception.CustomerException;

public class CustomerOperationsImpl implements CustomerOperations {
	private boolean isloggedin=false;
	public CustomerOperationsImpl() {}
	@Override
	public boolean login(String username, String password) {
		Connection con=null;
		String LOGIN_QUERY="SELECT * FROM CUSTOMER WHERE USERNAME=? AND PASSWORD=?";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(LOGIN_QUERY);
				ps.setString(1, username);
				ps.setString(2, password);
				ResultSet rs=ps.executeQuery();
				
				if(rs.next()) {
					isloggedin=true;
				}else {
					throw new CustomerException("Invalid Credentials!");
				}
			}catch(SQLException e) {
				throw new CustomerException("Something went wrong!");
			}
		}catch(CustomerException e) {
			System.out.println(e.getMessage());
		}
		
		return isloggedin;
	}

	@Override
	public boolean payBill(int billId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generateBill(int billId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Transaction> showTransactionHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> showAllUnpaidBill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> showAllPaidBills() {
		// TODO Auto-generated method stub
		return null;
	}

}
