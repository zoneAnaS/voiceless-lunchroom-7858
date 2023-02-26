package com.ampcorp.dao;

import java.util.List;

import com.ampcorp.dto.Bill;
import com.ampcorp.dto.Transaction;

public interface CustomerOperations {
	/**
	 * This method takse customer login details and return true if credential matches in the database
	 * else if credentials does not match then it returns false
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean login(String username,String password);
	/**
	 * This method starts a transaction and it sets the bill status of the current bill as paid and
	 * sets status of all the previous bills as paid
	 * the transaction rolls back if there's something wrong and returns false
	 * else it commits the changes and returns true
	 * @param billId
	 * @return boolean
	 */
	public boolean payBill(int billId);
	/**
	 * This method shows the bill detail if bill id is present in the bill table
	 * this method directly prints the detail in the console
	 * @param billId
	 */
	public void generateBill(int billId);
	/**
	 * This method returns a List of all Transactions of the logged in customer 
	 * @return List<Transaction>
	 */
	public List<Transaction> showTransactionHistory();
	/**
	 * This method returns list of all unpaid bills of the logged in customer
	 * @return
	 */
	public List<Bill> showAllUnpaidBill();
	/**
	 * This method returns list of all paid bills of the logged in customer
	 * @return
	 */
	public List<Bill> showAllPaidBills();
	/**
	 * This method return Bill object if the bill id is present for the logged in customer
	 * @param billId
	 * @return Bill object
	 */
	public Bill getBillByBillId(int billId);
}
