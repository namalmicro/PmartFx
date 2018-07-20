package com.pmart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pmart.dbconnection.JDBCPostgreConnection;

import javafx.event.ActionEvent;

public class LoginController implements Initializable{
	
	@FXML
	private AnchorPane loginAnchorPane;
	@FXML
	private TextField txt_email;
	@FXML
	private TextField txt_password;
	@FXML
	private Button btn_login;
	@FXML
	private Button btn_cancel;

	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		conn = JDBCPostgreConnection.getConnection();
		
		

	}
	

	@FXML
	public void handleLogin(ActionEvent event) throws IOException {
		
		if(txt_email.getText().equals(getEmail()) && txt_password.getText().equals(getPassword())) {
			
			Stage stage= (Stage)loginAnchorPane.getScene().getWindow();
			stage.close();
			
			Stage primaryStage = new Stage();
			
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainTabWindow.fxml"));
			Scene scene = new Scene(root,1100,600);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setTitle("Product Mart");
			primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.setTitle("Loading...");
			
			primaryStage.show();
			
			
			
		}else {
			
			Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or Password", ButtonType.OK);
			alert.setTitle("Invalid");
			alert.showAndWait();
			
		}
		
	}
	
	
	private String getEmail(){
		
		String email = "";
		
		try {
			
			pst = conn.prepareStatement("SELECT email FROM users WHERE email = ?");
			pst.setString(1, txt_email.getText());
			rs = pst.executeQuery();
			
			if(rs.next()) {
				email = rs.getString(1);
				rs.close();
				
			}
			
		}catch (Exception ex) {
			
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
		return email;
	}

	
	
	private String getPassword() {
		
		String password = "";
		
		try {
			
			pst = conn.prepareStatement("SELECT upassword FROM users WHERE upassword = ?");
			pst.setString(1, txt_password.getText());
			rs = pst.executeQuery();
			
			if(rs.next()) {
				password = rs.getString(1);
				rs.close();
			}
			
		}catch (Exception ex) {
			
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
		return password;
	}

	

	@FXML
	public void handleCancel(ActionEvent event) {
		
		Stage stage = (Stage)loginAnchorPane.getScene().getWindow();
		stage.close();
		
	}



	
}
