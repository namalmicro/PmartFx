package com.report;

public class Products {
	
	private String pname;
	private String price;
	private String pqty;
	private String amount;
	
	public Products(String pname, String price, String pqty, String amount) {
		
		this.pname = pname;
		this.price = price;
		this.pqty = pqty;
		this.amount = amount;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPqty() {
		return pqty;
	}

	public void setPqty(String pqty) {
		this.pqty = pqty;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	
	
	
	

}
