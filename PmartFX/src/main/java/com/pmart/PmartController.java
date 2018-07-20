package com.pmart;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dialog.AlertDialog;
import com.pmart.dbconnection.JDBCPostgreConnection;
import com.pmart.stores.Product;
import com.pmart.stores.ProductList;
import com.pmart.validation.DataValidation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PmartController implements Initializable{

	
	@FXML
	private AnchorPane mainAnchorPane;
	
	@FXML
	private Button btn_addProduct;
	
	@FXML
	private TextField txt_barcode;
	
	@FXML
	private TextField txt_productName;
	
	@FXML
	private TextField txt_priceIn;
	
	@FXML
	private TextField txt_priceOut;
	
	@FXML
	private TextField txt_qty;
	
	@FXML
	private DatePicker txt_dateIn;
	
	@FXML
	private TextField txt_search;
	
	
	@FXML
	private TableView<Product> tableProduct;
	
	private ProductList productList;
	
	@FXML
	private Label errorBarcode;
	
	@FXML
	private Label errorProductName;
	
	@FXML
	private Label errorPriceIn;
	
	@FXML
	private Label errorPriceOut;
	
	@FXML
	private Label errorQty;
	
	private int pid;
	
	@FXML
	private Button btn_browser;
	
	private FileChooser fileChooser;
	private File file;
	private Stage stage;
	private final Desktop desktop = Desktop.getDesktop();
	
	private FileInputStream productImagePath;
	
	@FXML
	private ImageView imageView;
	private Image image;
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		conn = JDBCPostgreConnection.getConnection();
		
		productList = new ProductList();
		
		productList.loadProducts();
		
		tableProduct.setItems(productList.getProducts());
		
		setCellValueFromTableToTextField();
		
		
		
		searchProduct();
		
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
				new FileChooser.ExtensionFilter("Text File", "*.txt"));
		
		
		
		
	}
	
	
	@FXML
	public void handleAddProduct(ActionEvent event) throws SQLException {
		
		boolean isBarcodeNumber = DataValidation.isTextFieldTypeNumber(txt_barcode, errorBarcode, "Barcode must be number");
		boolean isProductNameEmpty = DataValidation.isTextFieldNotEmpty(txt_productName, errorProductName, "ProductName is required");
		boolean isPriceInNumber = DataValidation.isTextFieldTypeNumber(txt_priceIn, errorPriceIn, "PriceIn must be number");
		boolean isPriceOutNumber = DataValidation.isTextFieldTypeNumber(txt_priceOut, errorPriceOut, "PriceOut must be number");
		boolean isQtyNumber = DataValidation.isTextFieldTypeNumber(txt_qty, errorQty, "Qty must be number");
		
		if(isBarcodeNumber && isProductNameEmpty && isPriceOutNumber && isPriceInNumber && isQtyNumber) {
		
		String sql = "INSERT INTO products (barcode, productname, pricein, priceout, pimage) VALUES (?,?,?,?,?)";
		
		String barcode = txt_barcode.getText();
		String productName = txt_productName.getText();
		Double priceIn = Double.valueOf(txt_priceIn.getText());
		Double priceOut = Double.valueOf(txt_priceOut.getText());
		
		
		try {
			
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, barcode);
			pst.setString(2, productName);
			pst.setDouble(3, priceIn);
			pst.setDouble(4, priceOut);
			productImagePath = new FileInputStream(file);
			pst.setBinaryStream(5, productImagePath, file.length());
			
			pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			rs.next();
			Object key = rs.getObject(1);
			
			sql = "INSERT INTO stock (qty, datein, productid) VALUES (?,?,?)";
			
			pst = conn.prepareStatement(sql);
			
			pst.setInt(1, Integer.valueOf(txt_qty.getText()));
			pst.setDate(2, Date.valueOf(txt_dateIn.getValue()));
			pst.setInt(3, Integer.parseInt(String.valueOf(key)));
			
			int i = pst.executeUpdate();
			
			
			
			if(i == 1) {
				
				//System.out.println("Product is added sucessfully.");
				
				AlertDialog.display("Info", "Product is added sucessfully.");
				clearTextField();
				productList.loadProducts();
				
			
				
			}
			
			
		}catch (SQLException ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		catch (FileNotFoundException ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
		
		finally {
			pst.close();
		}
		
		}
		
	}
	
	
	private void setCellValueFromTableToTextField() {
		
		tableProduct.setOnMouseClicked(e -> {
			
			Product product = tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
			
			txt_barcode.setText(product.getBarcode());
			txt_productName.setText(product.getProductName());
			txt_priceIn.setText(String.valueOf(product.getPriceIn()));
			txt_priceOut.setText(String.valueOf(product.getPriceOut()));
			txt_qty.setText(String.valueOf(product.getQty()));
			txt_dateIn.setValue(LocalDate.parse(String.valueOf(product.getDateIn())));
			
			pid = product.getPid();
			
			showProductImage(product.getBarcode());
			
		});
		
	}
	
	
	private void showProductImage(String barcode) {
		
		try {
			
			pst = conn.prepareStatement("SELECT pimage FROM products WHERE barcode = ?");
			pst.setString(1, barcode);
			rs = pst.executeQuery();
			
			
			if(rs.next()) {
				InputStream imageInputStream = rs.getBinaryStream(1);
				
				/*
				OutputStream imageOutputStream = new FileOutputStream(new File("photo.jpg"));
				
				byte[] contents = new byte[1024];
				int size = 0;
				
				while((size = imageInputStream.read(contents)) != -1) {
					imageOutputStream.write(contents, 0, size);
					
				}
				 */
				
				if(imageInputStream != null) {
					image = new Image(imageInputStream);
					imageView.setImage(image);
				}else {
					imageView.setImage(null);
				}
			}     
			
			
		}catch (SQLException ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
		
		}
	
	}
	
	
	@FXML
	public void handleUpdateProduct() throws SQLException {
		
		boolean isBarcodeEmpty = DataValidation.isTextFieldNotEmpty(txt_barcode, errorBarcode, "Barcode is required");
		boolean isProductNameEmpty = DataValidation.isTextFieldNotEmpty(txt_productName, errorProductName, "ProductName is required");
		boolean isPriceInEmpty = DataValidation.isTextFieldNotEmpty(txt_priceIn, errorPriceIn, "PriceIn is required");
		boolean isPriceOutEmpty = DataValidation.isTextFieldNotEmpty(txt_priceOut, errorPriceOut, "PriceOut is required");
		boolean isQtyNumber = DataValidation.isTextFieldTypeNumber(txt_qty, errorQty, "Qty is required to update");
		
		if(isBarcodeEmpty && isProductNameEmpty && isPriceInEmpty && isPriceOutEmpty && isQtyNumber) {
		
		String sql = "UPDATE products SET productname = ?, pricein = ?, priceOut = ?, pimage = ? WHERE barcode = ?";
		
		try {
			
			String barcode = txt_barcode.getText();
			String pname = txt_productName.getText();
			Double priceIn = Double.valueOf(txt_priceIn.getText()) ;
			Double priceOut = Double.valueOf(txt_priceOut.getText());
			
			pst = conn.prepareStatement(sql);
			pst.setString(1, pname);
			pst.setDouble(2, priceIn);
			pst.setDouble(3, priceOut);
			productImagePath = new FileInputStream(file);
			pst.setBinaryStream(4, productImagePath, file.length());
			pst.setString(5, barcode);
			
			int i = pst.executeUpdate();
			
			sql = "UPDATE stock SET qty = ? , datein = ? WHERE productid = ?";
			
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.valueOf(txt_qty.getText()));
			pst.setDate(2, Date.valueOf(txt_dateIn.getValue()));
			pst.setInt(3, pid);
			
			int j = pst.executeUpdate();
			
			
			if(i == 1 && j == 1) {
				AlertDialog.display("Info", "Product Update Successfully.");
				clearTextField();
				productList.loadProducts();
			}
		
		}catch (SQLException ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
			
			
		}catch (FileNotFoundException ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		finally {
			pst.close();
		}
		
		}
		
	}
	
	@FXML
	private void clearTextField() {
		txt_barcode.clear();
		txt_productName.clear();
		txt_priceIn.clear();
		txt_priceOut.clear();
		txt_qty.clear();
		txt_dateIn.setValue(LocalDate.now());
		
	}
	
	@FXML
	private void handleDeleteProduct() throws SQLException {
		
		boolean isBarcodeEmpty = DataValidation.isTextFieldNotEmpty(txt_barcode, errorBarcode, "Barcode is required to delete");
		
	    if(isBarcodeEmpty) {
	   
		String sql = "DELETE FROM products WHERE barcode = ?";
		
		try {
			
			pst = conn.prepareStatement(sql);
			
			pst.setString(1, txt_barcode.getText());
			
			int i = pst.executeUpdate();
			
			if(i == 1) {
				AlertDialog.display("Info", "Product delete sucessfully");
				clearTextField();
				productList.loadProducts();
				
			}
			
		} catch (SQLException ex) {
			
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
			
		}finally {
			pst.close();
		}
		
	    }
	}
	
	
	public void searchProduct() {
	
		txt_search.setOnKeyReleased(e->{
			
			if(txt_search.getText().equals("")) {
				productList.loadProducts();
				
			
			}else {
				
				productList.getProducts().clear();
				
				String sql = "SELECT p.* , s.qty , s.datein FROM products p INNER JOIN stock s ON p.pid = s.productid AND p.barcode LIKE '%"+txt_search.getText()+"%'"
				      +  "UNION SELECT p.* , s.qty , s.datein FROM products p INNER JOIN stock s ON p.pid = s.productid AND p.productname LIKE '%"+txt_search.getText()+"%'";
				
				
				try {
					
					pst = conn.prepareStatement(sql);
					rs = pst.executeQuery();
					
					while(rs.next()) {
						
						productList.addProduct(new Product(rs.getInt("pid"), rs.getString("barcode"), rs.getString("productname"), rs.getDouble("pricein"), rs.getDouble("priceout"), rs.getInt("qty"), rs.getDate("datein")));
					}
					
					tableProduct.setItems(productList.getProducts());
					
				} catch (Exception ex) {
					Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
				
				}finally {
					try {
						pst.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				
			}
			
		
			
		});
	}
	
	
	@FXML
	public void handleManageOrder() {
		
		Stage primaryStage = new Stage();
		
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("order/OrderProduct.fxml"));
			Scene scene = new Scene(root,620,450);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initOwner(mainAnchorPane.getScene().getWindow());
			//primaryStage.initModality(Modality.WINDOW_MODAL);
			//Image icon = new Image(getClass().getResourceAsStream("/img/company-logo.png"));
			//primaryStage.getIcons().add(icon);
			//primaryStage.setResizable(true);
			//primaryStage.setMinHeight(400);
			//primaryStage.setMinWidth(850);
			//primaryStage.setTitle("Micro Cars - Item Master File");
			//primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.setResizable(false);
			primaryStage.show();
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't load the Order Product GUI");
		}	
		
	}
	
	@FXML
	private void handleBrowser() {
		
		stage = (Stage) mainAnchorPane.getScene().getWindow();
		file = fileChooser.showOpenDialog(stage);
		
		/*
		try {
			
			desktop.open(file);
		
		}catch (Exception ex) {
			Logger.getLogger(PmartController.class.getName()).log(Level.SEVERE, null, ex);
		}
		*/
		
		if(file != null) {
			
			//System.out.println(""+file.getAbsolutePath());
			image = new Image(file.getAbsoluteFile().toURI().toString());
			imageView.setImage(image);
			//imageView.setPreserveRatio(true);
			
		}
		
		
	}
	
	
	
	
}
