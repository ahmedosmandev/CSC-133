package com.mycompany.a1;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import java.util.ArrayList;
import java.util.Random;

// Represents the game's model
public class GameWorld {
	private Random rand = new Random();
	private int timer;
	private Ant ant;
	private ArrayList<GameObject> gameObjectList = new ArrayList<GameObject>();
	private int flagSize = 15;
	private int antSize = 25;
	private boolean isExit = false;
	private int lastFlagReached;
	private int totalFlagCount = 10; 
	

	// Starting game and reset game objects
	public void init() {
		isExit = false;
		lastFlagReached = 1;
		// Clears the game object list before starting
		gameObjectList.clear();

		// Creates a single Ant
		gameObjectList.add(ant = new Ant(antSize, new Point(randX(), randY()), 0));

		// Creating flags (between 4 and 9 flags)
		int flagCount = 9; //4 + rand.nextInt(6); // Randomly choose between 4 and 9 flags
		for (int i = 1; i <= flagCount; i++) {
			gameObjectList.add(new Flag(flagSize, new Point(randX(), randY()), i));
		}

		// Creating at least 2 spiders
		int spiderCount = 2 + rand.nextInt(2); // Randomly choose 2 or 3 spiders
		for (int i = 1; i <= spiderCount; i++) {
			gameObjectList.add(new Spider(randObjSize(), new Point(randX(), randY())));
		}

		// Creating at least 2 food stations
		int foodStationCount = 2 + rand.nextInt(3); // Randomly choose 2, 3, or 4 food stations
		for (int i = 1; i <= foodStationCount; i++) {
			gameObjectList.add(new FoodStation(randObjSize(), new Point(randX(), randY())));
		}
	}

	// Press 'a' to increase speed or 'b' to decrease speed of ant
	public void setAntSpeed(int x) {
		if (x == 2) {
			ant.setSpeed(ant.getSpeed() + x);
			System.out.println("Speeding Up!");
		} else {
			ant.setSpeed(ant.getSpeed() + x);
			System.out.println("Slowing down!");
		}

	}

	// Press 'l' to turn left or 'r' to turn right of ant
	public void changeHeading(char h) {
		if (h == 'l') {
			ant.changeHeading(h);
			System.out.println("Turning Left!");
		} else {
			ant.changeHeading(h);
			System.out.println("Turning Right!");
		}

	}

	// Press 'c' for food consumption rate of ant
	public void setFoodConsumptionRate(int foodConsumptionRate) {
		ant.setFoodConsumptionRate(foodConsumptionRate);
		System.out.println("Food Consumption Rate set to: " + foodConsumptionRate);
	}

	// Press 1-9 for collision between ant and a flag
	public void flagCollision(int lastFlagReached) {
		int highestFlagNumber = 0;
		for (GameObject gameObject : gameObjectList) {
			if (gameObject instanceof Flag) {
				Flag flag = (Flag) gameObject;
				int flagNumber = flag.getSequenceNumber();
				if (flagNumber == lastFlagReached + 1) {
					ant.flagCollision(flag.getSequenceNumber());
					System.out.println("Got the " + lastFlagReached + " flag");
					return;
				} else if (flagNumber > highestFlagNumber) {
					highestFlagNumber = flagNumber;
				}
			}
		}
		if (highestFlagNumber == lastFlagReached) {
			System.out.println("No more flags!");
		}
	}

	// Press 'f' for collision between ant and a foodstation
	public void foodStationCollision() {
		System.out.println("Colliding with foodstation! Yummy");
		for (int i = 0; i < gameObjectList.size(); i++) {
			if (gameObjectList.get(i) instanceof Ant) {
				for (int j=0; j< gameObjectList.size(); j++) {
					if (gameObjectList.get(j) instanceof FoodStation) {
						if (((FoodStation) gameObjectList.get(j)).getCapacity() != 0)
						{
							int temporary = ((FoodStation) gameObjectList.get(j)).getCapacity();
							((Ant) gameObjectList.get(i)).setFoodLevel(temporary);
							((FoodStation) gameObjectList.get(j)).setCapacity(0);

							break;
						}
					}
				}
			}
		}

		
	}
		

	// Press 'g' for collision between ant and a spider
	public void spiderCollision() {
		System.out.println("Oh no a spider!");

		for (GameObject gameObject : gameObjectList) {
			if (gameObject instanceof Spider) {
				Spider spider = (Spider) gameObject;
				
				if (spider.getX() == ant.getX() && spider.getY() == ant.getY()) {
					int currentHealth = ant.getHealthLevel();
					int damage = spider.getSpiderBiteDamage();
					
					int newHealth = currentHealth - damage;
					
					ant.setHealthLevel(newHealth);
					
					ant.fadeColor();
					
					System.out.println("Ant's health level after spider bite:" + newHealth);
				}
			}
		}
	}

	// Press 't' for game tick
	public void tick() {
		if (!isExit) {
			timer++;
			
		
			int currentFoodLevel = ant.getFoodLevel();
			int currentHealthLevel = ant.getHealthLevel();
			if (currentFoodLevel <= 0 || currentHealthLevel <= 0) {
				antLostLife();
			}
			
			if (lastFlagReached == totalFlagCount) {
				gameWin();
			}

		// update spider's heading with small random changes
		for (GameObject gameObject : gameObjectList) {
			if (gameObject instanceof Spider) {
				Spider spider = (Spider) gameObject;
				int randomChange = -5 + rand.nextInt(11);
				int newHeading = spider.getHeading() + randomChange; 
				spider.setHeading(newHeading);
			}
		}

		// update positions of all moveable objects
		for (GameObject gameObject : gameObjectList) {
			if (gameObject instanceof Moveable) {
				Moveable moveable = (Moveable) gameObject;
				moveable.move();
			}

		}

		// reduce ant's food level based on foodConsumptionRate
		int foodConsumptionRate = ant.getFoodConsumptionRate();
		
		int newFoodLevel = currentFoodLevel - foodConsumptionRate;
		
		ant.setFoodLevel(newFoodLevel);
		System.out.println("Tick Tock: " + timer);
		// increment clock
		timer++;

	}
	}

	// Press 'd' to print the current game/ant state values
	public void printCurrent() {
		for (GameObject gameObject : gameObjectList) {
			if (gameObject instanceof Ant) {
				Ant ant = (Ant) gameObject;
				int lives = ant.getLives();
				int timer = getTime();
				int lastFlagReached = ant.getLastFlagReached();
				int foodLevel = ant.getFoodLevel();
				int healthLevel = ant.getHealthLevel();

				System.out.println("Ant Information: ");
				System.out.println("Lives left " + lives);
				System.out.println("Timer elasped: " + timer);
				System.out.println("Last flag reached: " + lastFlagReached);
				System.out.println("Current food level: " + foodLevel);
				System.out.println("Current health level: " + healthLevel);
			}
		}
	}
	// Press 'm' to print the current map

	public void map() {
		for (GameObject temp : gameObjectList) {
			System.out.println(temp);
		}
	}

	// Press 'x' to exit the game

	public void exit() {
		if (isExit) {
			System.exit(0);
		}
	}

	// Press 'y' to confirm(){
	public void quitGame() {
		System.out.println("Would you like to end the game? (y/n)");
		isExit = true;
	}

	// Press 'n' to cancel(){
	public void dontQuit() {
		System.out.println("Great, lets keep playing. \nEnter a command ");
		isExit = false;
	}

	// Increment the game's elapsed time
	public int getTime() {
		return this.timer;
	}

	// Creating randInts for the game
	private int randX() {
		return rand.nextInt(1000);
	}

	private int randY() {
		return rand.nextInt(1000);
	}

	private int randObjSize() {
		return 15 + rand.nextInt(15);
	}
	
	public void antLostLife() {
		ant.loseLife();
		ant.resetAnt();
		ant.fadeColor();
		 
		if (ant.getLives() <= 0) {
			gameLost();
		}
		
	}
	
	public void gameWin() {
		isExit = true;
		System.out.println("Game Over, you win! Total time: " + timer);
		
	}
	public void gameLost() {
		isExit = true;
		System.out.println("Game over, you failed!");
	}
	public void resetGame() {
		isExit = false;
		lastFlagReached = 1;
		timer = 0;
	}
}
