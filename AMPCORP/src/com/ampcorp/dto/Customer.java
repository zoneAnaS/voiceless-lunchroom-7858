package com.ampcorp.dto;

import java.time.LocalDate;

public interface Customer{
	/**
	 * this method gives the customerId
	 * @return customerId
	 */
	public int getCustomerId();
	/**
	 * this method sets the customerId
	 * @param customerId
	 */
	public void setCustomerId(int customerId);
	/**
	 * this method gives the firstName
	 * @return firstName
	 */
	public String getFirstName();
	/**
	 * this method sets the firstName
	 * @param firstName
	 */
	public void setFirstName(String firstName);
	/**
	 * this method gives the lastName
	 * @return lastName
	 */
	public String getLastName();
	/**
	 * this method sets the lastName
	 * @param lastName
	 */
	public void setLastName(String lastName);
	/**
	 * this method gives the userName
	 * @return userName
	 */
	public String getUserName();
	/**
	 * this method sets the userName
	 * @param userName
	 */
	public void setUserName(String userName);
	/**
	 * this method gives the password
	 * @return password
	 */
	public String getPassword();
	/**
	 * this method sets the password
	 * @param password
	 */
	public void setPassword(String password);
	/**
	 * this method gives the regDate
	 * @return regDate
	 */
	public LocalDate getRegDate();
	/**
	 * this method sets the regDate
	 * @param regDate
	 */
	public void setRegDate(LocalDate regDate);
	/**
	 * this method gives the type
	 * @return type
	 */
	public String getType();
	/**
	 * this method sets the type
	 * @param type
	 */
	public void setType(String type);
}
