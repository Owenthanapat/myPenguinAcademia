package monster;

import application.*;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import util.dieable;
import util.fireable;

public class Monster implements fireable,dieable{
	protected int hp;
	protected int atk;
	protected String imagepath;
	protected int score;
	protected int h,k;
	protected int m,c;
	protected int r;
	protected double speedMon,speed;
	protected Canvas bulletPicture;
	protected GraphicsContext monsterGC;
	protected int startTimeAt;
	private Thread control;
	public boolean fired = false;
	protected int damage = 10;
	
	public Monster(GraphicsContext gc,int h,int k,String imagepath) {
		this.hp = 20;
		this.atk = 1;
		this.imagepath = "Mon1.jpg";
		this.score = 100;
		monsterGC = gc;
		this.h = h;
		this.k = k;
		this.startTimeAt = Main.timer.currentTime;
		System.out.println(this.imagepath);
		r =  (int) (this.LoadImage(this.imagepath).getHeight())/2;
		System.out.println(r);
		//this.controlThread();
	}
	public Monster(int hp,int atk,String imagepath,int score,int m,int c,double speedMon,double speed,GraphicsContext monsterGC) {
		this.hp = hp;
		this.atk = atk;
		this.imagepath = "bullet-transperent.jpg";
		this.score = score;
		this.m = m;
		this.c = c;
		this.speed = speed;
		this.speedMon = speedMon;
		this.monsterGC = monsterGC;
		this.r = (int) (this.LoadImage(this.imagepath).getHeight())/2;
		BattleStage.addMonster(this);
	}
	private Image LoadImage(String imagePath) {
		return new Image(ClassLoader.getSystemResource(imagepath).toString());
	}
	public void draw() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
					monsterGC.clearRect(h, k, 84, 67);
					k = k+10;
					h = h+10;
					monsterGC.drawImage(this.LoadImage(imagepath), h, k);
				
			}
			private Image LoadImage(String imagePath) {
				return new Image(ClassLoader.getSystemResource(imagePath).toString());
			}
		});
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public Canvas getBulletPicture() {
		return bulletPicture;
	}
	public void setBulletPicture(Canvas bulletPicture) {
		this.bulletPicture = bulletPicture;
	}
	public GraphicsContext getMonsterGC() {
		return monsterGC;
	}
	public void setMonsterGC(GraphicsContext monsterGC) {
		this.monsterGC = monsterGC;
	}
	public void remove() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				monsterGC.clearRect(h, k, this.LoadImage(imagepath).getWidth(), this.LoadImage(imagepath).getHeight());
			}
			private Image LoadImage(String imagePath) {
				return new Image(ClassLoader.getSystemResource(imagePath).toString());
			}
		});
	}
	public void controlThread() {
		// TODO Auto-generated method stub
		control = new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(3000);
					this.fire();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		control.start();
	}
	public void update() {
		
	}
	public int getCenterX() {
		return h+r;
	}
	public int getCenterY() {
		return k+r;
	}
	public int getR() {
		return r;
	}
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		new bulletMonster("owen", 80, "bulletMons1.jpg", 1, 0, this, monsterGC);
	}
	public boolean isAt(Penguin e) {
		int deltaX = e.getCenterX()-this.getCenterX();
		int deltaY = e.getCenterY()-this.getCenterY();
		int deltaR = e.getR()+this.getR();
		/*System.out.println("debug-------------------------------");
		System.out.println(e.getH());
		System.out.println(e.getCenterX());
		System.out.println(e.getK());
		System.out.println(e.getCenterY());
		System.out.println(this.getH());
		System.out.println(this.getCenterX());
		System.out.println(this.getK());
		System.out.println(this.getCenterY());
		System.out.println(deltaR);
		System.out.println(Math.sqrt((deltaX*deltaX+deltaY*deltaY)));
		System.out.println("debug-------------------------------");*/
		if (Math.sqrt((deltaX*deltaX+deltaY*deltaY)) < Math.abs(deltaR)) {
			return true;
		}
		else {
			return false;
		}
	}
	public int getDamage() {
		return this.damage;
	}
}