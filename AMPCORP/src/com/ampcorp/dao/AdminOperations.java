package com.ampcorp.dao;



import java.time.LocalDate;
import java.util.List;
import com.ampcorp.dto.Bill;
import com.ampcorp.dto.Customer;

public interface AdminOperations {
	/**
	 * This method takes login details and returns true if a data is found in the database with the given login details
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean login(String username,String password);
	/**
	 * This method takes a Customer object and addds it to the Customer table in the database
	 * and returns true if adding is successfull else returns false with an error
	 * @param customer
	 * @return boolean
	 */
	public boolean RegisterNewCustomer(Customer customer);
	/**
	 * This methods sets status of customer as inactive in the customer table and in the bill table
	 * it returns true if customer is present and has been inactive sucessfull
	 * returns false if customer is already inactive or not present
	 * @param  customer id
	 * @return boolean
	 */
	public boolean deleteCustomer(int CID);
	/**
	 * This method adds bill to a specific customer
	 * it return true if the adding is successfull
	 * it returns false if the customer has a bill already for given month
	 * @param bill
	 * @return boolean
	 */
	public boolean addBillOfCustomer(Bill bill);
	/**
	 * this method returns all the bills associated with the given customer id 
	 * @param CID
	 * @return List<Bill>
	 */
	public List<Bill> getAllBillOfCustomer(int CID);
	/**
	 * this method returns all the paid bills associated with the given customer id 
	 * @param CID
	 * @return List<Bill>
	 */
	public List<Bill> viewAllPaidBillsOfCustomer(int CID);
	/**
	 * this method returns all the unpaid bills associated with the given customer id 
	 * @param CID
	 * @return List<Bill>
	 */
	public List<Bill> viewAllUnpaidBillsOfCustomer(int CID);
	/**
	 * this method returns all the active customers from the database
	 * @return List<Customer>
	 */
	public List<Customer> getAllCustomer();
	/**
	 * this method returns all the inactive customers from the database
	 * @return List<Customer>
	 */
	public List<Customer> getAllInactiveCustomer();
	/**
	 * This method return a Customer object if the customer is present for the given customer id
	 * else returns null
	 * @param CID
	 * @return Customer object
	 */
	public Customer getCustomerByCustomerId(int CID);
	/**
	 * This method returns previous month's amount if the previous month bill is present and is unpaid
	 * else it returns 0
	 * @param customer id
	 * @param current bill date
	 * @return int
	 */
	public int getPreviousRemainingAmount(int CID,LocalDate Date);
	/**
	 * This method returns true if the bill for given customer id and date is present
	 * else it returns false
	 * @param customer id
	 * @param current bill date
	 * @return
	 */
	public boolean isBillPresent(int CID,LocalDate Date);
	/**
	 * this method return list of all bill present in the database(paid/unpaid)
	 * @return List<Bill>
	 */
	public List<Bill> getAllBills();
	/**
	 * This method return list of all unpaid bill in the database
	 * @return
	 */
	public List<Bill> getAllUnpaidBills();
}
