package com.ampcorp.dto;

import java.time.LocalDate;

public interface Bill {
	public int getBillId();
	public void setBillId(int billId);
	public Customer getCustomer();
	public void setCustomer(Customer customer);
	public LocalDate getBillDate();
	public void setBillDate(LocalDate billDate);
	public String getStatus();
	public void setStatus(String status);
	public int getUnit();
	public void setUnit(int unit);
	public int getUnitCharge();
	public void setUnitCharge(int unitCharge);
	public int getPreviousAmount();
	public void setPreviousAmount(int previousAmount);
	public int getFixedCharge();
	public void setFixedCharge(int fixedCharge);
	public double getTax();
	public void setTax(double tax);
	public int getCurrentAmount();
	public int getTotalAmount();
}
