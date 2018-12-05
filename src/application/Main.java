package application;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
	private static MainMenu menu;
	private static BattleStage battleStage;
	@Override
	public void start(Stage primaryStage)  {
		// TODO Auto-generated method stub
		StackPane root = new StackPane();
		
		menu = new MainMenu();
		battleStage = new BattleStage();
		
	}
		

}
