package com.ampcorp.dto;

import java.time.LocalDate;
import java.util.Objects;

public class CustomerImpl implements Customer{
	private int customerId;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private LocalDate regDate;
	private String type;
	public CustomerImpl(String firstName, String lastName, String userName, String password, LocalDate regDate,
			String type) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.regDate = regDate;
		this.type = type;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDate getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		return Objects.hash(userName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerImpl other = (CustomerImpl) obj;
		return Objects.equals(userName, other.userName);
	}
	@Override
	public String toString() {
		return "Customer "+this.customerId+" Type: "+this.type+" Name: "+this.firstName+" "+this.lastName+" UserName: "+this.userName+" regDate: "+this.regDate;
	}
	
	
}
