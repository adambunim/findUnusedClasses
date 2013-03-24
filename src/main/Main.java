package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {		
		primaryStage.setTitle("Hello World!");		
		ClassesPane classesPane= new ClassesPane(primaryStage);
		primaryStage.setScene(new Scene(classesPane, 700, 800));
		primaryStage.show();				
	}

}
