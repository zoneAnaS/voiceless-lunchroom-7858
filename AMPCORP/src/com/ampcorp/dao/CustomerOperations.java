package com.ampcorp.dao;

import java.util.List;

import com.ampcorp.dto.Bill;
import com.ampcorp.dto.Transaction;

public interface CustomerOperations {
	public boolean login(String username,String password);
	public boolean payBill(int billId);
	public void generateBill(int billId);
	public List<Transaction> showTransactionHistory();
	public List<Bill> showAllUnpaidBill();
	public List<Bill> showAllPaidBills();
}
