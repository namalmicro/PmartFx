package com.pmart.stores;

import java.sql.Date;

public class Product {
	
	private int pid;
	private String barcode;
	private String productName;
	private Double priceIn;
	private Double priceOut;
	private int qty;
	private Date dateIn;
	
	public Product(String barcode, String productName, Double priceIn, Double priceOut) {
		
		this.barcode = barcode;
		this.productName = productName;
		this.priceIn = priceIn;
		this.priceOut = priceOut;
	}
	
	
	
	public Product(int pid, String barcode, String productName, Double priceIn, Double priceOut, int qty, Date dateIn) {
		
		this.pid = pid;
		this.barcode = barcode;
		this.productName = productName;
		this.priceIn = priceIn;
		this.priceOut = priceOut;
		this.qty = qty;
		this.dateIn = dateIn;
	}



	public Product() {
		// default constructor
	}

	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getPriceIn() {
		return priceIn;
	}

	public void setPriceIn(Double priceIn) {
		this.priceIn = priceIn;
	}

	public Double getPriceOut() {
		return priceOut;
	}

	public void setPriceOut(Double priceOut) {
		this.priceOut = priceOut;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}





	
	

}
