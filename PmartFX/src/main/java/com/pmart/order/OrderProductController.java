package com.pmart.order;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pmart.dbconnection.JDBCPostgreConnection;
import com.report.Products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class OrderProductController implements Initializable {

	@FXML
	private AnchorPane mainTabWindowAnchorPane;
	
	@FXML
	private TextField txt_barcode;
	
	@FXML
	private TextField txt_productName;
	
	@FXML
	private TextField txt_priceOut;
	
	@FXML
	private TextField txt_qty;
	
	@FXML
	private TextField txt_invoiceNumber;
	
	@FXML
	private Label lbl_grandTotal;
	
	@FXML
	private Button btn_printInvoice;
	
	@FXML
	private DatePicker dp_orderDate;
	
	
	@FXML
	private TableView<OrderList> tableOrder;
	
	private ObservableList<OrderList> orderData;
	
	int no = 0;
	int productId;
	String barcode;
	String productName;
	double priceOut;
	int qty;
	double amount = 0.0;
	
	private double grandTotal = 0.0;
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	
	
	
	
	

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		conn = JDBCPostgreConnection.getConnection();
		
		orderData = FXCollections.observableArrayList();
		
		// call to auto generated order number method
		txt_invoiceNumber.setText(autoOrderId());
		
		dp_orderDate.setValue(LocalDate.now());
		
		
		
		
	}
	
	
	@FXML
	private void handleScanProduct(KeyEvent event) throws SQLException{
	
		pst = conn.prepareStatement("SELECT * FROM products WHERE barcode = ?");
		pst.setString(1, txt_barcode.getText());
		rs = pst.executeQuery();
		
		if(rs.next()) {
			
			productId = rs.getInt(1);
			barcode = rs.getString(2);
			productName = rs.getString(3);
			priceOut = rs.getDouble(5);
			
			txt_productName.setText(productName);
			txt_priceOut.setText(""+priceOut);
			txt_qty.requestFocus();
			
		}
		rs.close();
		
	}
	
	
	@FXML
	private void handleOrder(){
		
		int qty = Integer.parseInt(txt_qty.getText());
		
		if(qty != 0) {
			
			amount = priceOut * qty;
			
			grandTotal += amount; 
			
			// Update Quantity and Amount of Selling Products
			
			for(OrderList item : orderData) {
				
				if(item.getProductId() == productId) {
					
					int table_qty = item.getQty() + qty;
					double table_amount = item.getAmount() + amount;
					item.setQty(table_qty);
					item.setAmount(table_amount);
					lbl_grandTotal.setText(""+grandTotal);
					tableOrder.getItems().set(tableOrder.getItems().indexOf(item), item);
					
					clearText();
					return;
					
				}
			}
			
			orderData.add(new OrderList(++no, productId, barcode, productName, priceOut, qty, amount));
			
			tableOrder.setItems(orderData);
			lbl_grandTotal.setText(""+grandTotal);
			clearText();
			
		}	
		
	}
	
	private void clearText() {
		
		txt_barcode.clear();
		txt_barcode.requestFocus();
		txt_productName.clear();
		txt_priceOut.clear();
		txt_qty.clear();
		
	}
	
	private String autoOrderId() {
		
		String orderId = "INV00000";
		
		try {
			
			pst = conn.prepareStatement("select max(orderid) from orderheader");
			rs = pst.executeQuery();
			
			if(rs.next()) {
				
				orderId = rs.getString(1);
				int n = Integer.parseInt(orderId.substring(3)) + 1;
				int x = String.valueOf(n).length();
				orderId = orderId.substring(0, 8-x) + String.valueOf(n);
				
			}
			rs.close();
			
		}catch (Exception ex) {
			Logger.getLogger(OrderProductController.class.getName()).log(Level.SEVERE, null, ex);
		
		}
		
		return orderId;
		
	}
	
	@FXML
	private void handlePrintInvoice() {
		
		String sql = "INSERT INTO orderheader(orderid,orderdate) values (?,?)";
		
		try {
			
			pst = conn.prepareStatement(sql);
			pst.setString(1, txt_invoiceNumber.getText());
			pst.setDate(2, Date.valueOf(dp_orderDate.getValue()));
			
			int i = pst.executeUpdate();
			
			if(i == 1) {
				sql = "INSERT INTO orderdetails(orderid,productid,qty,price) values (?,?,?,?)";
				
				for(OrderList oList : orderData) {
					
					pst = conn.prepareStatement(sql);
					pst.setString(1, txt_invoiceNumber.getText());
					pst.setInt(2, oList.getProductId());
					pst.setInt(3, oList.getQty());
					pst.setDouble(4, oList.getPriceOut());
					
					int j = pst.executeUpdate();
					
					if(j == 1) {
						System.out.println("Complete Order");
						
					}
					
				}
				
				txt_invoiceNumber.setText(autoOrderId());
				
			}
			
			priceInvoice();
			
			
		}catch (Exception ex) {
			Logger.getLogger(OrderProductController.class.getName()).log(Level.SEVERE, null, ex);
		
		}finally {
			
			try {
				pst.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		
	}
		

	private void priceInvoice() {
		
		String sourceFile = "G:\\GitRepository\\HomeProjects\\PmartFXLocal\\PmartFX\\src\\main\\java\\com\\report\\Invoice.jrxml";
		
		try {
			
			JasperReport jReport = JasperCompileManager.compileReport(sourceFile);
			
			HashMap<String, Object> para = new HashMap<>();
			para.put("cashier", "Yohan"); // User get from login
			para.put("grandTotal", lbl_grandTotal.getText());
			
			ArrayList<Products> pList = new ArrayList<>();
			
			for(OrderList ol : orderData) {
				
				pList.add(new Products(ol.getProductName(), ""+ ol.getPriceOut(), ""+ ol.getQty(), ""+ ol.getAmount()));
				
			}
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pList);
			
			
			
			JasperPrint jprint = JasperFillManager.fillReport(jReport, para, dataSource);
			
			JasperViewer.viewReport(jprint, false);
			
			
			
		}catch (Exception ex) {
			
			Logger.getLogger(OrderProductController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
