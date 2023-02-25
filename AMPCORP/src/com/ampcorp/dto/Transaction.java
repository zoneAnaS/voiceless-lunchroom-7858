package com.ampcorp.dto;

import java.time.LocalDate;

public interface Transaction {
	public int getTransactionId();
	public void setTransactionId(int transactionId);
	public int getBillID();
	public void setBillID(int billID);
	public int getTotalAmount();
	public void setTotalAmount(int totalAmount);
	public LocalDate getTransactionDate();
	public void setTransactionDate(LocalDate transactionDate);

}
