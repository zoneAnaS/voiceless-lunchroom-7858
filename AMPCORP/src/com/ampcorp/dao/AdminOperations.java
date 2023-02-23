package com.ampcorp.dao;



import java.time.LocalDate;
import java.util.List;
import com.ampcorp.dto.Bill;
import com.ampcorp.dto.Customer;

public interface AdminOperations {
	public boolean login(String username,String password);//done
	public boolean RegisterNewCustomer(Customer customer);//done (show existing custype remainig)
	public boolean deleteCustomer(int CID);//done
	public boolean addBillOfCustomer(Bill bill);
	public List<Bill> getAllBillOfCustomer(int CID);
	public List<Bill> viewAllPaidBillsOfCustomer(int CID);
	public List<Bill> viewAllUnpaidBillsOfCustomer(int CID);
	public List<Customer> getAllCustomer();
	public List<Customer> getAllInactiveCustomer();
	public Customer getCustomerByCustomerId(int CID);
	public int getPreviousRemainingAmount(int CID,LocalDate Date);
	public boolean isBillPresent(int CID,LocalDate Date);
	public List<Bill> getAllBills();
	public List<Bill> getAllUnpaidBills();
}
