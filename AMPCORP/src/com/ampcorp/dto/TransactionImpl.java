package com.ampcorp.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TransactionImpl implements Transaction {
	private int transactionId;
	private Bill bill;
	private int totalAmount;
	private LocalDate transactionDate;
	public TransactionImpl(Bill bill, int totalAmount, LocalDate transactionDate) {
		super();
		this.bill = bill;
		this.totalAmount = totalAmount;
		this.transactionDate = transactionDate;
	}
	
	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
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
		return Objects.hash(transactionId);
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
		return transactionId == other.transactionId;
	}
	
}
