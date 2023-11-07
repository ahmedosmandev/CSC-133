package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

// Represents the game's model
public class GameWorld extends Observable {
	
	private boolean endCol = false;
	private boolean crashCol = false;
	private boolean foodCol = false;
	private boolean isPause = false;
	private boolean positionable;
	private double nonDisplayClock;
	private boolean sound = false;
	private int contWidth;
	private int contHeight;
	
	private int mapHeight = 10;
	private int mapWidth = 10;
	private int lives;
	private Random rand = new Random();
	private int timer = 0;
	private int clock = 0;
	private Ant ant;
	private int flagSize = 15;
	private int antSize = 25;
	private boolean isExit = true;
	private int winningFlag = 10; 
	private GameObject lastCollision; 
private ArrayList<GameObject> colliderList = new ArrayList<GameObject>();	
GameObjectCollection gameObjectList;
	
	public GameWorld() {
		gameObjectList = new GameObjectCollection();
		ant = new Ant(this,antSize, 0, new Point (500, 500));
		lastCollision = ant; 
	}

	// Setting map dimensions
		public void setMapHeight(int height) {
			this.mapHeight = height;
			System.out.println("" + height);
		}
		
		public void setMapWidth(int width) {
			this.mapWidth = width; 
		}
	
	// Starting game and reset game objects
		public void init() {

			// Creates a player
			gameObjectList.add(ant);
			

			// Creating flags (between 4 and 9 flags)
			int flagCount = 4 + rand.nextInt(6); // Randomly choose between 4 and 9 flags
			for (int i = 1; i <= flagCount; i++) {
				Point location = new Point(randX(), randY());
				gameObjectList.add(new Flag(this, flagSize, location, i));
			}

			// Creating at least 2 spiders
			int spiderCount = 2 + rand.nextInt(2); // Randomly choose 2 or 3 spiders
			for (int i = 1; i <= spiderCount; i++) {
				Point location = new Point(randX(), randY());
				gameObjectList.add(new Spider(this, randObjSize(), location));
			}

			// Creating at least 2 food stations
			int foodStationCount = 2 + rand.nextInt(3); // Randomly choose 2, 3, or 4 food stations
			for (int i = 1; i <= foodStationCount; i++) {
				gameObjectList.add(new FoodStation(this, randObjSize(), new Point(randX(), randY())));
			}
			
			
			this.setChanged();
			this.notifyObservers();
		}
	
		// Press d to display
	public void display() {
		System.out.println("Lives left: " + ant.getLives()
		+ ", Current clock: " + timer + ", Last flag reached: " + ant.getLastFlagReached() + ", Food Level: " + ant.getFoodLevel()+ ", Health: " + ant.getHealthLevel());
		
	}
	
	//Press m to display 
	public void map() {
		System.out.println();
		IIterator elements = gameObjectList.getIterator();
		while(elements.hasNext()) {
			GameObject temp = ((GameObject) elements.getNext());
			System.out.println(temp.toString());
		}
	}
	
	private void lifeReset() {
		ant.resetAnt();
	}
	
	public void toggleSound() {
		this.sound = !(this.sound);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void tick(int i) {
		if(ant.getHealthLevel()!=100 && ant.getFoodLevel() != 0 && ant.getLives()!=0) {
			ant.setHeading(ant.getHeading());
			ant.setFoodLevel(300);
			Timer();
			
			IIterator elements = gameObjectList.getIterator();
			while(elements.hasNext()) {
				GameObject temp = ((GameObject) elements.getNext());
				if(temp instanceof Moveable) {
					if(temp instanceof Spider) {
						((Moveable) temp).setHeading(((Moveable)temp).getHeading());
						((Moveable) temp).move();
					}
					else {
						((Moveable) temp).move();
					}
				}
				}
			}
			else {
				if(ant.getLives()!= 0) {
					lifeReset(); 
				}
				else {
					System.out.println("Game Over");
			}
		}
		notifyobs();
	}
	
	public void foodStationCollision(GameObject temp) {
	    if (temp instanceof FoodStation) {
	        FoodStation foodStation = (FoodStation) temp;
	        if (foodStation.getCapacity() != 0) {
	            ant.setFoodLevel(ant.getFoodLevel() + foodStation.getCapacity());
	            foodStation.setCapacity(0);
	            foodStation.setColor(ColorUtil.rgb(0, 255, 191));
	            // Add a new FoodStation to replace the consumed one
	            gameObjectList.add(new FoodStation(this, randObjSize(), new Point(randX(), randY())));
	            setFoodCol(true); // Set the foodCol flag to true
	            notifyobs();
	        }
	    }
	}

	public void counterTime() {
		nonDisplayClock += (1/ 50.00);
		timer = (int) nonDisplayClock;
	}
	
	public int getAntFlagReached() {
		return ant.getLastFlagReached();
	}
	
	public int getFoodLevel() {
		return ant.getFoodLevel();
	}
	public int getAntHealthLevel() {
		return ant.getHealthLevel();
	}
	public boolean isSound() {
		return sound; 
	}
	
	public void spiderCollision() {
		if(ant.getHealthLevel() != 100) {
			ant.setHealthLevel(-5);
			ant.setColor(ColorUtil.rgb((ant.getRed()-5), 0, 0));
			ant.setRed(5);
			ant.checkSpeed();
			
		}
	}
	private int randX() {
		return rand.nextInt(mapWidth);
	}
	private int randY() {
		return rand.nextInt(mapHeight);
	}
	
	private int randObjSize() {
		return 15+rand.nextInt(25);
	}
	public void flagCollision(int flagNumber) {
		ant.flagCollision(flagNumber);
	}
	public void setAntSpeed(int x) {
		ant.setSpeed(ant.getSpeed() + x);
		notifyobs();
	}
	
	public void changeHeading(char change) {
		ant.changeHeading(change);
		notifyobs(); 
	}
	
	public int getLives() {
		return ant.getLives();
	}
	
	public int getClock() {
		return timer; 
	}
	

	
	public void Timer() {
		timer += 1;
	}
	
	public void gameOverLoss() {
		System.out.println("Game over, you failed!! LOSER");
		ant = null;
		this.exit();
	}
	
	public void gameOverWin() {
		System.out.println("Game over, you win!!! Total time: " + this.clock);
	}
	
	private void notifyobs() {
		this.setChanged();
		this.notifyObservers(); 
	}
	
	
	
	public Ant getAnt() {
		return ant;
	}
	public boolean isPause() {
		return isPause;
	}
	
	public void setIsPause(boolean isPause) {
		this.isPause = isPause;
	}
	
	public boolean isPositionable() {
		return positionable;
	}
	public void setPositionable(boolean positionable) {
		this.positionable = positionable;
	}
	
	
	public boolean isFoodCol() {
		return foodCol;
	}
	
	public void setFoodCol(boolean foodCol) {
		this.foodCol = foodCol;
	}
	
	public boolean isCrashCol() {
		return crashCol; 
	}
	
	public void setCrashCol(boolean crashCol) {
		this.crashCol = crashCol;
	}
	public boolean isEndCollision() {
		return endCol;
	}
	public void setEndCollision(boolean endCol) {
		this.endCol = endCol; 
	}
	// Exits for the Game. 'x' 
		public void exit() {
			if (isExit) {
				System.exit(0);
			}
		}
		
		public void quitGame() {
			System.out.println("Do you want to exit? (y/n)");
			isExit = true;
		}
		
		public void dontQuit() {
			isExit = false;
		}


		public void setFoodConsumptionRate(int foodConsumptionRate) {
			ant.setFoodConsumptionRate(foodConsumptionRate);
			System.out.println("Food Consumption Rate set to: " + foodConsumptionRate);
		}
		
		public void setWidthHeight(int width, int height) {
			contWidth = width;
			contHeight = height;
		}

		
		
	
	

	
	
}