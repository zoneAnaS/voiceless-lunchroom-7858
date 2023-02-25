package com.ampcorp.dto;

import java.time.LocalDate;


public class BillImpl implements Bill {
	private int billId;
	private Customer customer;
	private LocalDate billDate;
	private String status;
	private int unit;
	private int unitCharge;
	private int previousAmount;
	private int fixedCharge;
	private double tax;
	private int currentAmount;
	private int totalAmount;
	public BillImpl() {}
	
	public BillImpl(LocalDate billDate, int unit, int unitCharge, int previousAmount, int fixedCharge,
			double tax,Customer customer) {
		super();
		this.billDate = billDate;
		this.status = "unpaid";
		this.unit = unit;
		this.unitCharge = unitCharge;
		this.previousAmount = previousAmount;
		this.fixedCharge = fixedCharge;
		this.tax = tax;
		this.customer=customer;
	}
	public BillImpl(LocalDate billDate, int unit,Customer customer) {
		super();
		this.billDate = billDate;
		this.status = "unpaid";
		this.unit = unit;
		this.customer=customer;
	}
	
	
	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getUnitCharge() {
		return unitCharge;
	}

	public void setUnitCharge(int unitCharge) {
		this.unitCharge = unitCharge;
	}

	public int getPreviousAmount() {
		return previousAmount;
	}

	public void setPreviousAmount(int previousAmount) {
		this.previousAmount = previousAmount;
	}

	public int getFixedCharge() {
		return fixedCharge;
	}

	public void setFixedCharge(int fixedCharge) {
		this.fixedCharge = fixedCharge;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public int getCurrentAmount() {
		this.currentAmount=(unit*unitCharge)+fixedCharge;
		return this.currentAmount;
	}


	public int getTotalAmount() {
		this.totalAmount= (getCurrentAmount()+previousAmount)+(int)((getCurrentAmount()+previousAmount)*(tax/100.0));
		return this.totalAmount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillImpl other = (BillImpl) obj;
		return billId == other.billId;
	}

	@Override
	public String toString() {
		return "Bill ID="+this.billId+"  Name="+this.customer.getFirstName()+" "+this.customer.getLastName()+" BillDate="+this.billDate+" Amount="+"\033[1m"+this.getTotalAmount();
	}
	
	
}
