package com.pmart.order;

public class OrderList {
	
	private int no;
	private int productId;
	private String barcode;
	private String productName;
	private double priceOut;
	private int qty;
	private double amount;
	
	public OrderList(int no, int productId, String barcode, String productName, double priceOut, int qty,
			double amount) {
		
		this.no = no;
		this.productId = productId;
		this.barcode = barcode;
		this.productName = productName;
		this.priceOut = priceOut;
		this.qty = qty;
		this.amount = amount;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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

	public double getPriceOut() {
		return priceOut;
	}

	public void setPriceOut(double priceOut) {
		this.priceOut = priceOut;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
	
	
	
	
	
	

}
