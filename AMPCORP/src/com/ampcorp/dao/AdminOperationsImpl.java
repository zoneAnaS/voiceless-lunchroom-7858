package com.ampcorp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.ampcorp.dto.Bill;
import com.ampcorp.dto.BillImpl;
import com.ampcorp.dto.Customer;
import com.ampcorp.dto.CustomerImpl;
import com.ampcorp.exception.AdminException;

public class AdminOperationsImpl implements AdminOperations {
	public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
	private boolean isloggedin=false;
	
	public AdminOperationsImpl() {};
	@Override
	public boolean login(String username, String password) {
		Connection con=null;
		String LOGIN_QUERY="SELECT * FROM ADMIN WHERE USERNAME=? AND PASSWORD=?";
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
					throw new AdminException("Invalid Credentials!");
				}
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		
		return isloggedin;
	}

	@Override
	public boolean RegisterNewCustomer(Customer customer) {
		boolean flag=false;
		Connection con=null;
		String INSERT_QUERY="INSERT INTO CUSTOMER (firstName,lastName,userName,password,regDate,type) VALUES(?,?,?,?,?,?)";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(INSERT_QUERY);
				ps.setString(1,customer.getFirstName());
				ps.setString(2,customer.getLastName());
				ps.setString(3,customer.getUserName());
				ps.setString(4,customer.getPassword());
				ps.setDate(5,Date.valueOf(customer.getRegDate()));
				ps.setString(6,customer.getType());
				if(ps.executeUpdate()>0) {
					flag=true;
				}else {
					throw new AdminException("Customer not added!");
				}
			}catch(SQLException  e) {
				throw new AdminException("Username already exists");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return flag;
	}

	@Override
	public boolean deleteCustomer(int CID) {
		boolean flag=false;
		Connection con=null;
		String DELETE_QUERY="UPDATE CUSTOMER SET status='inactive' where customerId=? and status='active'";
		String DELETE_QUERY1="UPDATE BIll SET customerStatus='inactive' where custID=?";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(DELETE_QUERY);
				ps.setInt(1, CID);
				if(ps.executeUpdate()>0){
					ps=con.prepareStatement(DELETE_QUERY1);
					ps.setInt(1, CID);
					ps.executeUpdate();
					flag=true;
				}else {
					throw new AdminException("No cutomer found with customer_id="+CID);
				}
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return flag;
	}

	@Override
	public boolean addBillOfCustomer(Bill bill) {
		boolean flag=false;
		Connection con=null;
		String INSERT_QUERY="INSERT INTO Bill (custID,billDate,units,PreviousAmount,CurrentAmount,FIXEDCHARGE,Tax,TOTALAMOUNT,unitCharge) VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(INSERT_QUERY);
				ps.setInt(1, bill.getCustomer().getCustomerId());
				ps.setDate(2, Date.valueOf(bill.getBillDate()));
				ps.setInt(3, bill.getUnit());
				
				
				//checking if bill already present
				if(this.isBillPresent(bill.getCustomer().getCustomerId(), bill.getBillDate())) {
					throw new AdminException("Bill already present for the given month");
				}
				
				
				//getting the remaining balance of previous month
				int prevAmount=getPreviousRemainingAmount(bill.getCustomer().getCustomerId(),bill.getBillDate());
				bill.setPreviousAmount(prevAmount);
				
				
				//adding fixedcharge,tax,unitcharge from the properties file
				int fixedCharge;
				double tax;
				int unitCharge;
				if(bill.getCustomer().getType().equals("private")) {
					ResourceBundle rb=ResourceBundle.getBundle("private");
					try {
						fixedCharge=Integer.parseInt(rb.getString("fixedCharge"));
						tax=Double.parseDouble(rb.getString("tax"));
						unitCharge=Integer.parseInt(rb.getString("unitCharge"));
					} catch (Exception e1) {
						throw new AdminException("keys in private.properties not valid");
					}
				}else {
					ResourceBundle rb=ResourceBundle.getBundle("commercial");
					try {
						fixedCharge=Integer.parseInt(rb.getString("fixedCharge"));
						tax=Double.parseDouble(rb.getString("tax"));
						unitCharge=Integer.parseInt(rb.getString("unitCharge"));
					} catch (Exception e1) {
						throw new AdminException("keys in private.properties not valid");
					}
				}
				ps.setInt(4, bill.getPreviousAmount());
				bill.setFixedCharge(fixedCharge);
				bill.setUnitCharge(unitCharge);
				bill.setTax(tax);
				ps.setInt(5, bill.getCurrentAmount());
				ps.setInt(6, fixedCharge);
				ps.setDouble(7, tax);
				ps.setInt(8, bill.getTotalAmount());
				ps.setInt(9, unitCharge);
				if(ps.executeUpdate()>0) {
					flag=true;
				}else {
					throw new AdminException("Bill not added!");
				}
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return flag;
	}

	@Override
	public List<Bill> getAllBillOfCustomer(int CID) {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill WHERE custID=? and customerStatus='active'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, CID);
				ResultSet rs=ps.executeQuery();
				Customer customer=getCustomerByCustomerId(CID);
				if(customer==null) {
					throw new AdminException("Customer not found!");
				}else {
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
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,customer);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				}
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}

	@Override
	public List<Bill> viewAllPaidBillsOfCustomer(int CID) {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill WHERE custID=? and customerStatus='active' and status='paid'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, CID);
				ResultSet rs=ps.executeQuery();
				Customer customer=getCustomerByCustomerId(CID);
				if(customer==null) {
					throw new AdminException("Customer not found!");
				}else {
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
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,customer);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				}
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}

	@Override
	public List<Bill> viewAllUnpaidBillsOfCustomer(int CID) {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill WHERE custID=? and customerStatus='active' and status='unpaid'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, CID);
				ResultSet rs=ps.executeQuery();
				Customer customer=getCustomerByCustomerId(CID);
				if(customer==null) {
					throw new AdminException("Customer not found!");
				}else {
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
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,customer);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				}
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}

	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> customerList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM CUSTOMER WHERE STATUS='active'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					String firstName=rs.getString("firstName");
					String lastName=rs.getString("lastName");
					String userName=rs.getString("userName");
					String password=rs.getString("password");
					String type=rs.getString("type");
					Date regDate=rs.getDate("regDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					int customerId=rs.getInt("customerId");
					Customer customer=new CustomerImpl(firstName,lastName,userName,password,LocalDate.parse(date),type);
					customer.setCustomerId(customerId);
					customerList.add(customer);
				}
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return customerList;
	}

	@Override
	public List<Customer> getAllInactiveCustomer() {
		List<Customer> customerList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM CUSTOMER WHERE STATUS='inactive'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					String firstName=rs.getString("firstName");
					String lastName=rs.getString("lastName");
					String userName=rs.getString("userName");
					String password=rs.getString("password");
					String type=rs.getString("type");
					Date regDate=rs.getDate("regDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					int customerId=rs.getInt("customerId");
					Customer customer=new CustomerImpl(firstName,lastName,userName,password,LocalDate.parse(date),type);
					customer.setCustomerId(customerId);
					customerList.add(customer);
				}
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return customerList;
	}
	public Customer getAllCustomerByCustomerId(int CID) {
		Connection con=null;
		Customer customer=null;
		String GET_QUERY="SELECT * FROM CUSTOMER WHERE customerId=?";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, CID);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					String firstName=rs.getString("firstName");
					String lastName=rs.getString("lastName");
					String userName=rs.getString("userName");
					String password=rs.getString("password");
					String type=rs.getString("type");
					Date regDate=rs.getDate("regDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					int customerId=rs.getInt("customerId");
					customer=new CustomerImpl(firstName,lastName,userName,password,LocalDate.parse(date),type);
					customer.setCustomerId(customerId);
				}
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return customer;
	}
	@Override
	public Customer getCustomerByCustomerId(int CID) {
		Connection con=null;
		Customer customer=null;
		String GET_QUERY="SELECT * FROM CUSTOMER WHERE customerId=? and status='active'";
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
				ps.setInt(1, CID);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					String firstName=rs.getString("firstName");
					String lastName=rs.getString("lastName");
					String userName=rs.getString("userName");
					String password=rs.getString("password");
					String type=rs.getString("type");
					Date regDate=rs.getDate("regDate");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String date=formatter.format(regDate);
					int customerId=rs.getInt("customerId");
					customer=new CustomerImpl(firstName,lastName,userName,password,LocalDate.parse(date),type);
					customer.setCustomerId(customerId);
				}
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return customer;
	}
	@Override
	public int getPreviousRemainingAmount(int CID, LocalDate Date) {
		LocalDate currentBillDate=Date;
		LocalDate prevMonth=currentBillDate.minusMonths(1);
		java.sql.Date previousDate=java.sql.Date.valueOf(prevMonth);
		int prevBalance=0;
		String SELECT_QUERY="SELECT TotalAmount FROM BILL B WHERE custID=? AND status='unpaid' AND DATE_FORMAT(B.billDate,'%b %Y')=DATE_FORMAT(?,'%b %Y')";
		Connection con=null;
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(SELECT_QUERY);
				ps.setInt(1, CID);
				ps.setDate(2,previousDate);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					prevBalance=rs.getInt("TotalAmount");
					break;
				}
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return prevBalance;
	}
	@Override
	public boolean isBillPresent(int CID, LocalDate Date) {
		java.sql.Date currDate=java.sql.Date.valueOf(Date);
		boolean isPresent=false;
		String SELECT_QUERY="SELECT PreviousAmount FROM BILL B WHERE custID=? AND DATE_FORMAT(B.billDate,'%b %Y')=DATE_FORMAT(?,'%b %Y')";
		Connection con=null;
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(SELECT_QUERY);
				ps.setInt(1, CID);
				ps.setDate(2,currDate);
				ResultSet rs=ps.executeQuery();
				
				isPresent=rs.next();
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return isPresent;
	}
	
	public void displayBills(List<Bill> billList) {
		if(billList.isEmpty()) {
			System.out.println(RED+"No bills found"+RESET);
			return;
		}
		System.out.println(GREEN+"\n\nGreen"+RESET+"= paid bill");
		System.out.println(RED+"RED"+RESET+"= unpaid bill");
		System.out.println("===============================================================================================");
		for(int i=0;i<billList.size();i++) {
			if(billList.get(i).getStatus().equals("paid")) {
				System.out.println("\t"+GREEN+billList.get(i)+RESET);
			}else {
				System.out.println("\t"+RED+billList.get(i)+RESET);
			}
			if(i!=billList.size()-1) {
		System.out.println("--------------------------------------------------------------------------------------------");
			}
		}
		System.out.println("===============================================================================================\n\n");
	}
	@Override
	public List<Bill> getAllBills() {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill order by billDate";
		HashMap<Integer,Customer> map=new HashMap<>();
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
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
							cus=this.getAllCustomerByCustomerId(cusId);
							map.put(cusId,cus);
						}
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,cus);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}
	@Override
	public List<Bill> getAllUnpaidBills() {
		List<Bill> billList=new ArrayList<>();
		Connection con=null;
		String GET_QUERY="SELECT * FROM Bill where status='unpaid' order by billDate";
		HashMap<Integer,Customer> map=new HashMap<>();
		try {
			try {
				con=DB_connection.makeConnection();
				PreparedStatement ps=con.prepareStatement(GET_QUERY);
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
							cus=this.getAllCustomerByCustomerId(cusId);
							map.put(cusId,cus);
						}
						Bill bill=new BillImpl(LocalDate.parse(date),units,unitCharge,PreviousAmount,FIXEDCHARGE,tax,cus);
						bill.setBillId(BillId);
						bill.setStatus(status);
						
						billList.add(bill);
					}
				
				
				
			}catch(SQLException  e) {
				throw new AdminException("Something went wrong!");
			}
		}catch(AdminException e) {
			System.out.println(RED+e.getMessage()+RESET);
		}finally {
			DB_connection.closeConnection();
		}
		return billList;
	}
	

}
