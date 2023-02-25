package com.ampcorp.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TransactionImpl implements Transaction {
	private int transactionId;
	private int billID;
	private int totalAmount;
	private LocalDate transactionDate;
	public TransactionImpl(int billID, int totalAmount, LocalDate transactionDate) {
		super();
		this.billID = billID;
		this.totalAmount = totalAmount;
		this.transactionDate = transactionDate;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getBillID() {
		return billID;
	}
	public void setBillID(int billID) {
		this.billID = billID;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(billID, transactionId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionImpl other = (TransactionImpl) obj;
		return billID == other.billID && transactionId == other.transactionId;
	}
	@Override
	public String toString() {
		return "Transaction [Transaction ID=" + transactionId + ", Bill ID=" + billID + ", Amount=" + totalAmount
				+ ", Date=" + transactionDate + "]";
	}
}
	
	

