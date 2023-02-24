package com.ampcorp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ampcorp.dto.Bill;
import com.ampcorp.dto.BillImpl;
import com.ampcorp.dto.Customer;
import com.ampcorp.dto.CustomerImpl;
import com.ampcorp.dto.Transaction;
import com.ampcorp.exception.CustomerException;

public class CustomerOperationsImpl implements CustomerOperations {
	private boolean isloggedin=false;
	
	public static final String ANSI_BOLD = "\033[1m";
	public static final String ANSI_RESET = "\033[0m";
	public static final String RESET = "\033[0m";
	public static final String YELLOW = "\033[0;33m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String BLUE = "\033[0;34m";
	public static Customer customer;
	public CustomerOperationsImpl() {}
	@Override
	public boolean login(String username, String password) {
		Connection con=null;
		String LOGIN_QUERY="SELECT * FROM CUSTOMER WHERE USERNAME=? AND PASSWORD=? AND status='active'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(LOGIN_QUERY);
				ps.setString(1, username);
				ps.setString(2, password);
				ResultSet rs=ps.executeQuery();
				
				if(rs.next()) {
					String firstName=rs.getString("firstName");
					String lastName=rs.getString("lastName");
					String userName=rs.getString("userName");
					String pswd=rs.getString("password");
					String type=rs.getString("type");
					Date regDate=rs.getDate("regDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					int customerId=rs.getInt("customerId");
					customer=new CustomerImpl(firstName,lastName,userName,pswd,LocalDate.parse(date),type);
					customer.setCustomerId(customerId);
					isloggedin=true;
					
					
				}else {
					throw new CustomerException("Invalid Credentials!");
				}
			}catch(SQLException e) {
				throw new CustomerException("Something went wrong!");
			}
		}catch(CustomerException e) {
			System.out.println(RED+e.getMessage()+RESET);
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
		try {
			Bill bill=getBillByBillId(billId);
			if(bill==null) {
				throw new CustomerException("No bill found!");
			}
			LocalDate date = bill.getBillDate(); // Get the current date
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
	        String dateText = date.format(formatter)+"/"+date.getYear();
	        String color;
	        if(bill.getStatus().equals("paid")) {
	        	color=GREEN;
	        }else {
	        	color=RED;
	        }
	        
			String genratedBill="+++++++++++++++++++++++++++++++++++++++++++++++++++\n"
									+ "\n"
									+ "    Name: "+ANSI_BOLD+bill.getCustomer().getFirstName()+" "+bill.getCustomer().getLastName()+ANSI_RESET+"\tCustomerID: "+ANSI_BOLD+bill.getCustomer().getCustomerId()+ANSI_RESET+"\n"
									+ "    Bill date: "+ANSI_BOLD+dateText+ANSI_RESET+"\tunits consumed:"+ANSI_BOLD+bill.getUnit()+ANSI_RESET+"\n"
									+ "    Bill-ID: "+ANSI_BOLD+bill.getBillId()+ANSI_RESET+"\n"
									+ "    Status: "+color+ANSI_BOLD+bill.getStatus()+RESET+"\n"
									+ "    ======================================\n"
									+ "    User type: "+ANSI_BOLD+bill.getCustomer().getType()+ANSI_RESET+"\n"
									+ "    Fixed charge: "+ANSI_BOLD+bill.getFixedCharge()+ANSI_RESET+"\n"
									+ "    Unit charge(per unit): "+ANSI_BOLD+bill.getUnitCharge()+ANSI_RESET+"\n"
									+ "    Tax rate: "+ANSI_BOLD+bill.getTax()+ANSI_RESET+"\n"
									+ "    Amount(current month excl. Tax): "+ANSI_BOLD+bill.getCurrentAmount()+ANSI_RESET+"\n"
									+ "    Previous balance: "+ANSI_BOLD+bill.getPreviousAmount()+ANSI_RESET+"\n"
									+ "    ======================================\n"
									+ "    Total amount(incl. of taxes): "+ANSI_BOLD+bill.getTotalAmount()+ANSI_RESET+ "\n"
									+ "\n"
									+ "++++++++++++++++++++++++++++++++++++++++++++++++++++\n"+RESET;
			System.out.println(genratedBill);
		}catch(CustomerException e) {
			
		}
		
		// TODO Auto-generated method stub

	}

	@Override
	public List<Transaction> showTransactionHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> showAllUnpaidBill() {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill where status='unpaid' and custID=? order by billDate";
		HashMap<Integer,Customer> map=new HashMap<>();
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, customer.getCustomerId());
				ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						int BillId=rs.getInt("BillId");
						Date regDate=rs.getDate("billDate");
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String date=formatter.format(regDate);
						String status=rs.getString("status");
						int units=rs.getInt("units");
						int PreviousAmount=rs.getInt("PreviousAmount");
						int FIXEDCHARGE=rs.getInt("FIXEDCHARGE");
						double tax=rs.getDouble("tax");
						int unitCharge=rs.getInt("unitCharge");
						int cusId=rs.getInt("custID");
						Customer cus=null;
						if(map.containsKey(cusId)) {
							cus=map.get(cusId);
						}else {
							cus=customer;
							map.put(cusId,cus);
						}
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,cus);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				
				
				
			}catch(SQLException  e) {
				throw new CustomerException("Something went wrong!");
			}
		}catch(CustomerException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}

	@Override
	public List<Bill> showAllPaidBills() {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill where status='paid' and custID=? order by billDate";
		HashMap<Integer,Customer> map=new HashMap<>();
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, customer.getCustomerId());
				ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						int BillId=rs.getInt("BillId");
						Date regDate=rs.getDate("billDate");
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String date=formatter.format(regDate);
						String status=rs.getString("status");
						int units=rs.getInt("units");
						int PreviousAmount=rs.getInt("PreviousAmount");
						int FIXEDCHARGE=rs.getInt("FIXEDCHARGE");
						double tax=rs.getDouble("tax");
						int unitCharge=rs.getInt("unitCharge");
						int cusId=rs.getInt("custID");
						Customer cus=null;
						if(map.containsKey(cusId)) {
							cus=map.get(cusId);
						}else {
							cus=customer;
							map.put(cusId,cus);
						}
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,cus);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				
				
				
			}catch(SQLException  e) {
				throw new CustomerException("Something went wrong!");
			}
		}catch(CustomerException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}
	@Override
	public Bill getBillByBillId(int billId) {
		Connection con=null;
		String LOGIN_QUERY="SELECT * FROM Bill WHERE BillId=? And custID=?";
		Bill bill=null;
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(LOGIN_QUERY);
				ps.setInt(1, billId);
				ps.setInt(2, customer.getCustomerId());
				ResultSet rs=ps.executeQuery();
				
				if(rs.next()) {
					int BillId=rs.getInt("BillId");
					Date regDate=rs.getDate("billDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					String status=rs.getString("status");
					int units=rs.getInt("units");
					int PreviousAmount=rs.getInt("PreviousAmount");
					int FIXEDCHARGE=rs.getInt("FIXEDCHARGE");
					double tax=rs.getDouble("tax");
					int unitCharge=rs.getInt("unitCharge");
					bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,customer);
					bill.setBillId(BillId);
					bill.setStatus(status);
					
				}else {
					throw new CustomerException("No Bill found with given BillID for "+customer.getUserName()+"!");
				}
			}catch(SQLException e) {
				throw new CustomerException("Something went wrong!");
			}
		}catch(CustomerException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}
		
		return bill;
	}
	
	// extra methods
	public String welcomeCustomerText(){
		String welocameText="";
		welocameText=GREEN+"\n******"+ANSI_BOLD+"WELCOME!!"+customer.getFirstName().toUpperCase()+" "+customer.getLastName().toUpperCase()+ANSI_RESET+GREEN+"******\n"+RESET;
		return welocameText;
	}
}
