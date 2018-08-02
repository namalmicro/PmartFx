package com.pmart.user;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pmart.dbconnection.JDBCPostgreConnection;
import com.pmart.validation.DataValidation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UserController implements Initializable {
	
	@FXML
	private AnchorPane userAnchorPane;
	@FXML
	private TextField txtEmail;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private PasswordField txtConfirmPassword;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private ComboBox<Role> cmbRoleName;
	@FXML
	private Label lblEmailError;
	@FXML
	private Label lblPasswordError;
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	
	private ObservableList<Role> role;
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		conn = JDBCPostgreConnection.getConnection();
		
		role = FXCollections.observableArrayList();
		
		try {
			
			pst = conn.prepareStatement("SELECT * FROM role");
			rs = pst.executeQuery();
			
			while(rs.next()) {
				role.add(new Role(rs.getString(1), rs.getString(2)));
			}
			
			cmbRoleName.setItems(role);
			
			
		}catch (Exception ex) {
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
		
		}
		
		cmbRoleName.setConverter(new StringConverter<Role>() {
			
			@Override
			public String toString(Role object) {
				
				return object.getRoleName();
			}
			
			@Override
			public Role fromString(String string) {
				
				return null;
			}
		});
		
		cmbRoleName.valueProperty().addListener((obs, oldValue, newValue)->{
			
			if(newValue != null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, newValue.getRoleID(), ButtonType.OK);
				alert.show();
			}
			
		});
		
		
		
	}

	
	@FXML
	public void handleUserRegister(ActionEvent event) {
		
		boolean isValidEmail = DataValidation.isValidEmail(txtEmail, lblEmailError, "Invalid email! Please try again!");
		boolean isPasswordMatched = DataValidation.isPasswordMatched(txtPassword, txtConfirmPassword, lblPasswordError, "Password is not matched!");
		
		if(isValidEmail && isPasswordMatched) {
			
			//lblEmailError.setText("Valid Email.");
			//lblPasswordError.setText("Valid Password.");
			
			try {
				
				String insert = "INSERT INTO users(email,upassword,firstname,lastname,roleid) VALUES (?,?,?,?,?)";
				
				pst = conn.prepareStatement(insert);
				pst.setString(1, txtEmail.getText());
				pst.setString(2, txtPassword.getText());
				pst.setString(3, txtFirstName.getText());
				pst.setString(4, txtLastName.getText());
				pst.setString(5, cmbRoleName.getValue().getRoleID());
				
				int i = pst.executeUpdate();
				
				if(i == 1) {
					Alert alert = new Alert(AlertType.INFORMATION, "User registered sucessfully!.", ButtonType.OK);
					alert.show();
				}
				
			}catch (Exception ex) {
				Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				
			}
			
			
			
			
		}
		
	}
	
	
	@FXML
	public void handleSignIn(ActionEvent event) {
		
		Stage primaryStage = new Stage();
		
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/com/pmart/Login.fxml"));
			Scene scene = new Scene(root,470,200);
			//scene.getStylesheets().add(getClass().getResource("/pmart/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initOwner(userAnchorPane.getScene().getWindow());
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
			System.out.println("Couldn't load the User Login Window");
		}
		
		
	}


}
