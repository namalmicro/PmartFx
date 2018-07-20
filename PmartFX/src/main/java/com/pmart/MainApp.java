package com.pmart;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/com/loading/Load.fxml"));
			
			//Scene scene = new Scene(root,890,360); // pmart window
			//Scene scene = new Scene(root,385,170); // load window
			//Scene scene = new Scene(root,1100,520); // Tab Window 
			//Scene scene = new Scene(root,470,200); // Login Window 
			//Scene scene = new Scene(root,640,400); // User Registered Window
			
			Scene scene = new Scene(root,385,170); // load window
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setTitle("Product Mart");
			primaryStage.initStyle(StageStyle.DECORATED);
			//primaryStage.setTitle("Loading...");
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	@Override
	public void stop() throws Exception {
		try{
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
