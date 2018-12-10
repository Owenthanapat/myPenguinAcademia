package application;

import util.reference;
import monster.*;

import java.util.List;
import java.util.Random;
import java.beans.DefaultPersistenceDelegate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import monster.Monster;

public class BattleStage extends VBox{
	private ProgressBar HP;
	private Label score;
	private Penguin player1;
	private Canvas battleStageCanvas;
	private static List<Bullet> bullets;
	private static List<Bullet> bulletsMonster;
	public static List<Monster> monster;
	private  static Thread bulletsManager;
	private static Thread sekMonster;
	private static Thread monsterManager;
	private static Thread bulletsMonsterManager;
	private GraphicsContext battleStageGC;
	private int startAt;
	private boolean fired = false;
	
	public BattleStage() {
		super(10);
		HBox status = new HBox();
		status.setAlignment(Pos.CENTER);
		
		HP = new ProgressBar(1);
		HP.setPrefWidth(util.reference.PREFWIDTH);
		score = new Label("0");
		score.setPrefWidth(util.reference.PREFWIDTH);
		HP.setPrefWidth(util.reference.PREFWIDTH);
		bullets = new CopyOnWriteArrayList<Bullet>();
		bulletsMonster = new CopyOnWriteArrayList<Bullet>();
		monster = new CopyOnWriteArrayList<Monster>();
		startAt = Main.timer.currentTime;
		
		
		battleStageCanvas = new Canvas(util.reference.WIDTH, util.reference.HIGH-60);
		battleStageGC = battleStageCanvas.getGraphicsContext2D();
		player1 = new Penguin(battleStageGC);
		
		this.bulletsManagerThread();
		this.bulletsMonsterManagerThread();
		
		//monster manager
		monsterManager = new Thread(() -> {
			try {
				while (true) {
					Random rand = new Random();
					int n = rand.nextInt(2)+1;
					this.sekMonster(n);
					System.out.println("monsterManager");
					while(!EventManager.C) {
						Thread.sleep(100);
						for (int i = monster.size()-1 ; i >= 0 ; i--) {
							//System.out.println(i);
							if (Main.timer.currentTime%1 == 0 && !fired) {
								monster.get(i).fire();
								fired = true;
							}
							if (Main.timer.currentTime%3 != 0) {
								fired = false;
							}
							monster.get(i).draw();
							//monster.get(i).fire();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		status.getChildren().addAll(score,HP);
		this.getChildren().addAll(status,battleStageCanvas);
		
	}
	public ProgressBar getHP() {
		return HP;
	}
	public void setHP(ProgressBar hP) {
		HP = hP;
	}
	public Label getScore() {
		return score;
	}
	public void setScore(Label score) {
		this.score = score;
	}
	public Penguin getPlayer1() {
		return player1;
	}
	public void setPlayer1(Penguin player1) {
		this.player1 = player1;
	}
	public Canvas getPenguin1() {
		return battleStageCanvas;
	}
	public void setPenguin1(Canvas penguin1) {
		this.battleStageCanvas = penguin1;
	}
	public static void addPlayerBullet(Bullet e) {
		bullets.add(e);
	}
	public static void addMonsterBullet(Bullet e) {
		bulletsMonster.add(e);
	}
	public static void addMonster(Monster e) {
		monster.add(e);
	}
	private Image LoadImage(String imagePath) {
		return new Image(ClassLoader.getSystemResource(imagePath).toString());
	}
	private void bulletsManagerThread() {
		bulletsManager = new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(16);
					//System.out.println( bullets.size());
					for (int i = bullets.size()-1 ; i >= 0 ; i--) {
						bullets.get(i).draw();
						if (bullets.get(i).getK() < -80 || bullets.get(i).isIn(monster)) {
							//System.out.println("removed");
							bullets.get(i).remove();
							bullets.remove(i);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		bulletsManager.start();
	} 
	private void bulletsMonsterManagerThread() {
		bulletsMonsterManager = new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(16);
					//System.out.println( bullets.size());
					System.out.println(bulletsMonster.size());
					for (int i = bulletsMonster.size()-1 ; i >= 0 ; i--) {
						bulletsMonster.get(i).draw();
						if (bulletsMonster.get(i).isAt(player1)) {
							player1.setHP(player1.getHp()-bulletsMonster.get(i).getDamage());
							System.out.println("damaged");
						}
						if (bulletsMonster.get(i).getK() < -80 || bulletsMonster.get(i).getK() > util.reference.HIGH-60) {
							//System.out.println("removed");
							bulletsMonster.get(i).remove();
							bulletsMonster.remove(i);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		bulletsMonsterManager.start();
	}
	public void sekMonster(int p) {
		monster.clear();
		if(p ==1) {
			int x1 = 0;
			int y1 = 0;
			for(int i =0 ;i<1;i++) {
				monster.add(new Monster(this.battleStageGC,x1,y1,""));
				x1 -=100;
				y1 -=100;
			}
		}
		else if(p==2) {
			int x2 = util.reference.WIDTH;
			int y2 = 0;
			for(int i =0 ;i<1;i++) {
				monster.add(new Monster2(this.battleStageGC,x2,y2,""));
				x2 +=100;
				y2 -=100;
			}
		
			//System.out.println(x);
		}
	}
	public static void startMonster() {
		monsterManager.start();
	}

	public static void pauseMonster()  {
		monsterManager.suspend();
	}
	public static void resumeMonster() {
		monsterManager.resume();
	}
	public static void stop() {
		bulletsManager.interrupt();
		monsterManager.interrupt();
		bullets.clear();
		monster.clear();
	}
	public static void restartBattleStage() {
		monsterManager.interrupt();
		monster.clear();
	}
}
