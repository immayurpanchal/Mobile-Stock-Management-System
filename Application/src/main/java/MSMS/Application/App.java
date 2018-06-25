package MSMS.Application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application 
{

    public static void main( String[] args )
    {
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Resources/Main.fxml"));
		Scene scene = new Scene(root,700,600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mobile Stock Management System - Developed By Kaizen");
		primaryStage.show();
		
		//Change Default Close behaviour to custom.
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
	}
}
