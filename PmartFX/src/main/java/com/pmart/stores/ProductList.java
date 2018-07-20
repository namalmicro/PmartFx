package com.pmart.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pmart.dbconnection.JDBCPostgreConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductList {
	
	private ObservableList<Product> products;
	
	public ProductList() {
		
		products = FXCollections.observableArrayList();
	}


	public ObservableList<Product> getProducts() {
		return products;
	}

	public void setProducts(ObservableList<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product item){
		products.add(item);
    }
    
    public void deleteProduct(Product item){
    	products.remove(item);
    }
	
	
	public void loadProducts(){
		
		products.clear();
		
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
         
        //Product product = null;
        
        String query = "SELECT p.* , s.qty , s.datein FROM products p INNER JOIN stock s ON p.pid = s.productid";
        
        try {           
            conn = JDBCPostgreConnection.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
             
            while (rs.next()) {
            	//product = new Product();
            	
            	products.add(new Product(rs.getInt("pid"), rs.getString("barcode"), rs.getString("productname"), rs.getDouble("pricein"), rs.getDouble("priceout"), rs.getInt("qty"),rs.getDate("datein")));
            	
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	
	
}

	
	
	
	
	
	

	
	

}
