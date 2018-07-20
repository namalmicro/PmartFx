package com.pmart;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabWindowController implements Initializable {

	@FXML
	ListView<String> listForm;
	
	private ObservableList<String> subListForm;
	
	@FXML
	TabPane main_tabPane;
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		generateSubList();
		
		selectListForm();
		
		
	}
	
	private void generateSubList() {
		
		subListForm = FXCollections.observableArrayList();
		subListForm.add("Product");
		subListForm.add("Order");
		listForm.setItems(subListForm);
		
		
	}
	
	private void selectListForm() {
		
		listForm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				int i = listForm.getSelectionModel().getSelectedIndex();
				
				if(i == 0) {
					
					try {
						
						Node productForm = FXMLLoader.load(getClass().getResource("Pmart.fxml"));
						Tab tab = new Tab("Products", productForm);
						main_tabPane.getTabs().add(tab);
						
					}catch (Exception ex) {
						
						Logger.getLogger(MainTabWindowController.class.getName()).log(Level.SEVERE, null, ex);
					}
					
				}else if(i == 1){
					
					try {
						
						Node orderForm = FXMLLoader.load(getClass().getResource("order/OrderProduct.fxml"));
						Tab tab = new Tab("Order", orderForm);
						main_tabPane.getTabs().add(tab);
						
					}catch (Exception ex) {
						
						Logger.getLogger(MainTabWindowController.class.getName()).log(Level.SEVERE, null, ex);
					}
					
				}
				
			}
		});
		
	}
	
	
	
	
	
	
	
	

}
