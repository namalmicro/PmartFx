package com.loading;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoadController implements Initializable {

	@FXML
	private ProgressBar pbar;
	
	@FXML
	private ProgressIndicator pindicator;
	
	@FXML
	AnchorPane loadAnchorPane;
	
	

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		Task task = taskWorker(10);
		
		pbar.progressProperty().unbind();
		pindicator.progressProperty().unbind();
		pbar.progressProperty().bind(task.progressProperty());
		pindicator.progressProperty().bind(task.progressProperty());
		
		task.setOnSucceeded(e->{
			
			Stage primaryStage1 = (Stage )loadAnchorPane.getScene().getWindow();
			primaryStage1.close();
			
			Stage primaryStage2 = new Stage();
			
			try {
				AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/com/pmart/user/User.fxml"));
				Scene scene = new Scene(root,640,400);
				scene.getStylesheets().add(getClass().getResource("/com/pmart/application.css").toExternalForm());
				primaryStage2.setScene(scene);
				//primaryStage.initOwner(mainAnchorPane.getScene().getWindow());
				//primaryStage.setTitle("Micro Cars - Item Master File");
				//primaryStage2.setTitle("Product Mart");
				
				primaryStage2.show();
			
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Couldn't load the Pmart GUI");
			}	
			
			
		});
		
		Thread thread = new Thread(task);
		thread.start();
		
		
	}
	
	
	@SuppressWarnings("rawtypes")
	private Task taskWorker(int seconds) {
		
		return new Task() {

			@Override
			protected Object call() throws Exception {
				
				for(int i = 0; i < seconds; i++) {
					
					updateProgress(i+1, seconds);
					Thread.sleep(250);
				}
				return true;
			}
		};
		
	}
	
	
	

}
