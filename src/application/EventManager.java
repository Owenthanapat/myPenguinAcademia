package application;

import javafx.scene.media.AudioClip;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import util.controlable;;

public class EventManager {
	private static Stage PRIMARY;
	private static Scene BATTLESCENE;
	private static Scene MENUSCENE;
	private static Scene PAUSESCENE;
	private static Scene SCORESCENE;
	private StackPane rootBattleStage;
	private Pane rootScoreStage;
	private AudioClip sound;
	
	//detect all used key true if it was pressed , false for not.
	public static boolean UP,DOWN,LEFT,RIGHT,ESCAPE,Z,X,C,V ;
	
	public EventManager(Scene menu , Scene battle , Scene pause , Stage primary) {
		// TODO Auto-generated constructor stub
		this.PRIMARY = primary;
		this.MENUSCENE = menu;
		this.BATTLESCENE = battle;
		this.PAUSESCENE = pause;
		//this.newScoreScene();
		this.newScoreScene();
		//click = this.LoadMedia("click.mp3");
		sound = new AudioClip(ClassLoader.getSystemResource("click.mp3").toString());
	}
	
	//mainStage setting
	public void setMainMenuStart(Button mainMenuStart) {
		mainMenuStart.setOnAction(e ->{
			MainMenu.raiseTheSun();
			sound.play();
			Thread wait = new Thread(()->  {
				try {
					Thread.sleep(1100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						BattleStage.startBattleStage();
						PRIMARY.setScene(BATTLESCENE);
						//BattleStage.startMonster();
					}
				});
			});
			wait.start();
		});
		mainMenuStart.setOnMouseEntered(e -> {
			System.out.println("here");
		});
	}
	public void setMainMenuExit(Button mainMenuExit) {
		mainMenuExit.setOnAction(e -> {
			Penguin.stop();
			BattleStage.stop();
			Time.stop();
			PRIMARY.close();
		});
	}
	
	//battleStage setting
	public void setBattleKeyPress(Scene battle) {
		battle.setOnKeyPressed((KeyEvent event) -> {
			String new_code = event.getCode().toString();
			//System.out.println(new_code);
			if (event.getCode() == KeyCode.UP) UP = true;
			if (event.getCode() == KeyCode.DOWN) DOWN = true;
			if (event.getCode() == KeyCode.RIGHT) RIGHT = true;
			if (event.getCode() == KeyCode.LEFT) LEFT = true;
			if (event.getCode() == KeyCode.Z) Z = true;
			if (event.getCode() == KeyCode.X) X = true;
			if (event.getCode() == KeyCode.C) C = true;
			if (event.getCode() == KeyCode.V) V = true;
			if (event.getCode() == KeyCode.ESCAPE) {
				BattleStage.pauseMonster();
				PRIMARY.setScene(PAUSESCENE);
			}
		});
		battle.setOnKeyReleased((KeyEvent event) -> {
			if (event.getCode() == KeyCode.UP) UP = false;
			if (event.getCode() == KeyCode.DOWN) DOWN = false;
			if (event.getCode() == KeyCode.RIGHT) RIGHT = false;
			if (event.getCode() == KeyCode.LEFT) LEFT = false;
			if (event.getCode() == KeyCode.Z) Z = false;
			if (event.getCode() == KeyCode.X) X = false;
			if (event.getCode() == KeyCode.C) C = false;
			if (event.getCode() == KeyCode.V) V = false;
		});
	}
	
	//pauseStage setting
	public void setPauseStageResume(Button pauseStageResume) {
		pauseStageResume.setOnAction(e -> {
			BattleStage.resumeMonster();
			PRIMARY.setScene(BATTLESCENE);
		});
	}
	public void setPauseStageBacktoMenu(Button pauseStageBacktoMenu) {
		pauseStageBacktoMenu.setOnAction(e -> {
			BattleStage.restartBattleStage();
			this.newBattleScene();
			PRIMARY.setScene(MENUSCENE);
		});
	}
	public void setPauseStageExit(Button pauseStageExit) {
		pauseStageExit.setOnAction(e -> {
			Penguin.stop();
			BattleStage.stop();
			Time.stop();
			PRIMARY.close();
		});
	}
	public void setPauseStageRestart(Button pauseStageRestart) {
		pauseStageRestart.setOnAction(e-> {
			BattleStage.restartBattleStage();
			this.newBattleScene();
			BattleStage.startBattleStage();
			PRIMARY.setScene(BATTLESCENE);
		});
	}
	
	//getter
	public static Stage getPrimary() {
		return PRIMARY;
	}

	public static Scene getBattleScene() {
		return BATTLESCENE;
	}

	public static Scene getMenuScene() {
		return MENUSCENE;
	}

	public static Scene getPauseScene() {
		return PAUSESCENE;
	}
	private Media LoadMedia(String mediaPath) {
		return new Media(ClassLoader.getSystemResource(mediaPath).toString());
	}
	public void newBattleScene() {
		rootBattleStage = new StackPane();
		rootBattleStage.setId("battlePane");
		BattleStage battleStage = new BattleStage();
		rootBattleStage.getChildren().addAll(battleStage);
		this.BATTLESCENE = new Scene(rootBattleStage , util.reference.WIDTH , util.reference.HIGH);
		this.BATTLESCENE.getStylesheets().addAll(this.getClass().getResource("battle.css").toExternalForm());
		this.setBattleKeyPress(BATTLESCENE);
	}
	public void newScoreScene() {
		rootScoreStage = new Pane();
		rootScoreStage.setId("scorePane");
		ScoreStage scoreStage = new ScoreStage();
		scoreStage.getBackToMenu().setOnAction(e -> {
			BattleStage.restartBattleStage();
			this.newBattleScene();
			PRIMARY.setScene(MENUSCENE);
		});
		scoreStage.getRestart().setOnAction(e -> {
			BattleStage.restartBattleStage();
			this.newBattleScene();
			BattleStage.startBattleStage();
			PRIMARY.setScene(BATTLESCENE);
		});
		rootScoreStage.getChildren().addAll(scoreStage);
		this.SCORESCENE = new Scene(rootScoreStage , util.reference.WIDTH , util.reference.HIGH);
		this.SCORESCENE.getStylesheets().addAll(this.getClass().getResource("score.css").toExternalForm());
	}
	public static void dead() {
		BattleStage.restartBattleStage();
		UP = false;
		DOWN = false;
		RIGHT = false;
		LEFT = false;
		Z = false;
		X = false;
		C = false;
		V = false;
		PRIMARY.setScene(SCORESCENE);
	}
	
	
}
