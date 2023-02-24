package com.ampcorp.ui;


import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.ampcorp.dao.AdminOperationsImpl;
import com.ampcorp.dao.CustomerOperationsImpl;
import com.ampcorp.dao.DB_connection;
import com.ampcorp.dto.Bill;
import com.ampcorp.dto.BillImpl;
import com.ampcorp.dto.Customer;
import com.ampcorp.dto.CustomerImpl;

public class Main {
	public static final String ANSI_BOLD = "\033[1m";
	public static final String ANSI_RESET = "\033[0m";
	public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
	public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String oopsMessage=RED+"Oops! You cannot enter that"+RESET;
    
    private static String loginMenu;
	private static String adminMenu;
	private static String welcomeText;
	private static String customerType;
	private static String monthMenu;
	
	
	static {
		welcomeText=RED+ANSI_BOLD+"   Welcome to AmpCorp!!"+ANSI_RESET+RESET;
		customerType="+=========+====================+\n"
					+"|  "+YELLOW+ANSI_BOLD+"Choice"+ANSI_RESET+RESET+" |   "+YELLOW+ANSI_BOLD+"Customer Type"+ANSI_RESET+RESET+"    |\n"
					+"+=========+====================+\n"
					+"|    1    |      Private       |\n"
					+"+------------------------------+\n"
					+"|    2    |     Commercial     |\n"
					+"+---------+--------------------+";
		
		loginMenu="+========================+\n"
				 +"|        "+RED+ANSI_BOLD+"Login"+ANSI_RESET+RESET+"           |\n"
				 +"+=========+==============+\n"
				 +"|  "+YELLOW+ANSI_BOLD+"Choice"+ANSI_RESET+RESET+" |  "+YELLOW+ANSI_BOLD+"User Type"+ANSI_RESET+RESET+"   |\n"
				 +"+---------+--------------+\n"
				 +"|    1    |    Admin     |\n"
				 +"+---------+--------------+\n"
				 +"|    2    |   Customer   |\n"
				 +"+---------+--------------+\n"
				 +"|    0    |     Exit     |\n"
				 +"+---------+--------------+\n";
		
		adminMenu="+===========================================+\n"
				 +"|               "+RED+ANSI_BOLD+"Admin Menu"+ANSI_RESET+RESET+"                  |\n"
				 +"+=========+=================================+\n"
				 +"|  "+YELLOW+ANSI_BOLD+"Choice"+ANSI_RESET+RESET+RESET+" |          "+YELLOW+ANSI_BOLD+"Operations"+ANSI_RESET+RESET+"             |\n"
				 +" =========+=================================+\n"
				 +"|    1    |        Register new user        |\n"
				 +"+-------------------------------------------+\n"
				 +"|    2    |          Delete user            |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    3    |         Show all users          |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    4    |      Show all deleted users     |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    5    |           Add bill              |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    6    |      Show all bills of user     |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    7    |   Show all paid bills of user   |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    8    |  Show all unpaid bills of user  |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    9    |        Show all bills           |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    10   |      Show all unpaid bills      |\n"
				 +"+---------+---------------------------------+\n"
				 +"|    0    |            Logout               |\n"
				 +"+---------+---------------------------------+\n";
		
		monthMenu="+=========================+\n"
				 +"|    "+RED+ANSI_BOLD+"Choose bill month"+ANSI_RESET+RESET+"    |\n"
				 +"+=========+===============+\n"
				 +"|  "+YELLOW+ANSI_BOLD+"Choice"+ANSI_RESET+RESET+" |   "+YELLOW+ANSI_BOLD+"User Type"+ANSI_RESET+RESET+"   |\n"
				 +"+=========+===============+\n"
				 +"|    1    |      Jan      |\n"
				 +"+---------+---------------+\n"
				 +"|    2    |      Feb      |\n"
				 +"+---------+---------------+\n"
				 +"|    3    |      Mar      |\n"
				 +"+---------+---------------+\n"
				 +"|    4    |      Apr      |\n"
				 +"+---------+---------------+\n"
				 +"|    5    |      May      |\n"
				 +"+---------+---------------+\n"
				 +"|    6    |      Jun      |\n"
				 +"+---------+---------------+\n"
				 +"|    7    |      Jul      |\n"
				 +"+---------+---------------+\n"
				 +"|    8    |      Aug      |\n"
				 +"+---------+---------------+\n"
				 +"|    9    |      Sep      |\n"
				 +"+---------+---------------+\n"
				 +"|    10   |      Oct      |\n"
				 +"+---------+---------------+\n"
				 +"|    11   |      Nov      |\n"
				 +"+---------+---------------+\n"
				 +"|    12   |      Dec      |\n"
				 +"+---------+---------------+\n";
	
	}
	
	public static void clearScreen() {
		for (int i = 0; i < 50; i++) {
            System.out.println();
        }
	}
	public static LocalDate getDate(int month,int year) {
		String monthText;
		if(month<10) {
			monthText="0"+month;
		}else {
			monthText=""+month;
		}
		return LocalDate.parse(year+"-"+monthText+"-"+"01");
	}
	public static void main(String[] args) {
		
		//table testing
		System.out.println();
		Scanner sc=new Scanner(System.in);
		System.out.println(welcomeText);
		int mainMenuChoice=0;
		int limit=3;
		do {
			
			System.out.println(loginMenu);
			try {
				mainMenuChoice=sc.nextInt();
			}catch(Exception e) {
				System.out.println(oopsMessage);
				mainMenuChoice=0;
			}
			
			if(mainMenuChoice==1) {
				AdminOperationsImpl admin=new AdminOperationsImpl();
				String username;
				String password;
				int tries=0;
				do {
					System.out.println("Please Enter Admin username");
					username=sc.next();
					System.out.println("Please Enter Admin password");
					password=sc.next();
					tries++;
				}while(!admin.login(username, password) && tries!=limit);
				if(tries==limit) {
					System.out.println(RED+"\nMaximum try limit reached\nRedirecting to main menu\n"+RESET);
					continue;
				}
				System.out.println(GREEN+"\nLogin Successfull!!\n"+RESET);
				int adminChoice=0;
				do {
					System.out.println(adminMenu);
					try {
						adminChoice=sc.nextInt();
					}catch(Exception e) {
						System.out.println(RED+"Oops! you cannot enter that"+RESET);
						break;
					}
					if(adminChoice==1) {
						try {
							System.out.println("Please enter first name");
							String firstName=sc.next();
							System.out.println("Please enter last name");
							String lastName=sc.next();
							System.out.println("Please enter username");
							String customer_username=sc.next();
							System.out.println("Please enter password");
							String customer_password=sc.next();
							LocalDate regDate=LocalDate.now();
							int typeChoice;
							String type="private";
							do {
								System.out.println(customerType);
								typeChoice=sc.nextInt();
								if(typeChoice==1) {
									break;
								}else if(typeChoice==2) {
									type="commercial";
								}else{
									System.out.println(BLUE+"please try again"+RESET);
								}
							}while(typeChoice!=1 && typeChoice!=2);
							System.out.println("Are you sure you want to add new user? Y/N");
							String addAns=sc.next();
							if(!addAns.toLowerCase().equals("y")) {
								System.out.println(RED+"Adding Terminated!"+RESET);
								continue;
							}
							
							CustomerImpl customer= new CustomerImpl(firstName,lastName,customer_username,customer_password,regDate,type);
							if(admin.RegisterNewCustomer(customer)) {
								System.out.println(GREEN+"Customer with name "+firstName+" "+lastName+" added successfully!"+RESET);
							}
						}catch(Exception e) {
							System.out.println(oopsMessage);
							continue;
						}
						
					}else if(adminChoice==2) {
						System.out.println("Please enter customer_id");
						int cus_id=sc.nextInt();
						if(admin.deleteCustomer(cus_id)) {
							System.out.println(GREEN+"Successfully Deleted!"+RESET);
						}
					}else if(adminChoice==3) {
						List<Customer> customers=admin.getAllCustomer();
						if(customers.size()>0) {
							for(int i=0;i<customers.size();i++) {
								System.out.println((i+1)+". "+customers.get(i));
							}
						}else {
							System.out.println(RED+"No active customer found!!"+RESET);
						}
					}else if(adminChoice==4) {
						List<Customer> customers=admin.getAllInactiveCustomer();
						if(customers.size()>0) {
							for(int i=0;i<customers.size();i++) {
								System.out.println((i+1)+". "+customers.get(i));
							}
						}else {
							System.out.println(RED+"No inactive customer found!!"+RESET);
						}
					}else if(adminChoice==5) {
						int monthChoice=0;
						do {
							System.out.println(monthMenu);
							monthChoice=sc.nextInt();
							if(monthChoice<=0 || monthChoice>=13) {
								System.out.println(RED+"Invalid input! try again."+RESET);
							}
						}
						while(monthChoice<=0 || monthChoice>=13);
						
						int billyear=0;
						do {
							if(billyear!=0) {
								System.out.println(RED+"Please enter valid year"+RESET);
							}
							System.out.println("please enter bill year(Note format should be: 'YYYY')");
							billyear=sc.nextInt();
						}while(billyear<=999 || billyear>=10000);
						
						LocalDate billDate=getDate(monthChoice,billyear);
						System.out.println(billDate);
						if(!LocalDate.now().isAfter(billDate)) {
							System.out.println(RED+"Bill date cannot be after the current date! add bill again"+RESET);
							continue;
						}
						int billUnit=0;
						do {
							System.out.println("please enter bill unit");
							billUnit=sc.nextInt();
							if(billUnit<0) {
								System.out.println(RED+"Bill Unit cannot be negative"+RESET);
							}
						}while(billUnit<0);
						Customer cus=null;
						int customerEntryTries=0;
						do {
							customerEntryTries++;
							System.out.println("please enter customer id");
							int customerId=sc.nextInt();
							cus=admin.getCustomerByCustomerId(customerId);
							if(cus==null) {
								System.out.println(RED+"No customer found!"+RESET);
							}
							if(customerEntryTries==3 && cus==null) {
								customerEntryTries=0;
								System.out.println("Do you want to exit? Y/N");
								String exitAns=sc.next();
								if(exitAns.toLowerCase().equals("y")) {
									customerEntryTries=-1;
									break;
								}
							}
						}while(cus==null);
						if(customerEntryTries==-1) {
							System.out.println(RED+"Operation terminated"+RESET);
							continue;
						}
						Bill bill = new BillImpl(billDate,billUnit,cus);
						if(admin.addBillOfCustomer(bill)) {
							System.out.println(GREEN+"Bill added successfull for "+cus.getFirstName()+" "+cus.getLastName()+RESET);
						}
						
					}else if(adminChoice==6) {
						Customer cus=null;
						int customerEntryTries=0;
						do {
							customerEntryTries++;
							System.out.println("please enter customer id");
							int customerId=sc.nextInt();
							cus=admin.getCustomerByCustomerId(customerId);
							if(cus==null) {
								System.out.println(RED+"Invalid customer id"+RESET);
							}
							if(customerEntryTries==3 && cus==null) {
								customerEntryTries=0;
								System.out.println("Do you want to exit? Y/N");
								String exitAns=sc.next();
								if(exitAns.toLowerCase().equals("y")) {
									customerEntryTries=-1;
									break;
								}
							}
						}while(cus==null);
						if(customerEntryTries==-1) {
							System.out.println(RED+"Operation terminated"+RESET);
							continue;
						}
						List<Bill> billList=admin.getAllBillOfCustomer(cus.getCustomerId());
						admin.displayBills(billList);
					}else if(adminChoice==7) {
						Customer cus=null;
						int customerEntryTries=0;
						do {
							customerEntryTries++;
							System.out.println("please enter customer id");
							int customerId=sc.nextInt();
							cus=admin.getCustomerByCustomerId(customerId);
							if(cus==null) {
								System.out.println(RED+"Invalid customer id"+RESET);
							}
							if(customerEntryTries==3 && cus==null) {
								customerEntryTries=0;
								System.out.println("Do you want to exit? Y/N");
								String exitAns=sc.next();
								if(exitAns.toLowerCase().equals("y")) {
									customerEntryTries=-1;
									break;
								}
							}
						}while(cus==null);
						if(customerEntryTries==-1) {
							System.out.println(RED+"Operation terminated"+RESET);
							continue;
						}
						List<Bill> billList=admin.viewAllPaidBillsOfCustomer(cus.getCustomerId());
						admin.displayBills(billList);
					}else if(adminChoice==8) {
						Customer cus=null;
						int customerEntryTries=0;
						do {
							customerEntryTries++;
							System.out.println("please enter customer id");
							int customerId=sc.nextInt();
							cus=admin.getCustomerByCustomerId(customerId);
							if(cus==null) {
								System.out.println(RED+"Invalid customer id"+RESET);
							}
							if(customerEntryTries==3 && cus==null) {
								customerEntryTries=0;
								System.out.println("Do you want to exit? Y/N");
								String exitAns=sc.next();
								if(exitAns.toLowerCase().equals("y")) {
									customerEntryTries=-1;
									break;
								}
							}
						}while(cus==null);
						if(customerEntryTries==-1) {
							System.out.println(RED+"Operation terminated"+RESET);
							continue;
						}
						List<Bill> billList=admin.viewAllUnpaidBillsOfCustomer(cus.getCustomerId());
						admin.displayBills(billList);
					}else if(adminChoice==9) {
						List<Bill> billList=admin.getAllBills();
						admin.displayBills(billList);
					}else if(adminChoice==10) {
						List<Bill> billList=admin.getAllUnpaidBills();
						admin.displayBills(billList);
					}
					else if(adminChoice!=0){
						System.out.println(RED+"Invalid Input!"+RESET);
					}
				}while(adminChoice!=0);
				
				
				System.out.println(GREEN+"Logged Out!!"+RESET);
				
				
			}else if(mainMenuChoice==2) {
				
				
				CustomerOperationsImpl customer=new CustomerOperationsImpl();
				String username;
				String password;
				int tries=0;
				do {
					System.out.println("Please Enter Customer username");
					username=sc.next();
					System.out.println("Please Enter Customer password");
					password=sc.next();
					tries++;
				}while(customer.login(username, password)!=true || tries==limit);
				if(tries==limit) {
					System.out.println(RED+"\nMaximum try limit reached\nRedirecting to main menu\n"+RESET);
					continue;
				}
				System.out.println(GREEN+"Login Successfull!!"+RESET);
				System.out.println(customer.welcomeCustomerText());
				customer.generateBill(2);
				
				
				
				
			}else if(mainMenuChoice!=0){
				System.out.println(RED+"Invalid Input!!"+RESET);
			}
		}while(mainMenuChoice!=0);
		
		
		
		
		
		
		
		
		try {
			DB_connection.closeConnection();
		}catch(Exception e) {
			
		}
		
		sc.close();
		System.out.println(RED+ANSI_BOLD+ "            AmpCorp");
		System.out.println(YELLOW+ANSI_BOLD+"Electricity that powers progress     ");
		System.out.println(GREEN+ANSI_BOLD+ "    Thank you for Visiting!"+RESET+ANSI_RESET);
	}
	
	
}
