import java.util.Random;

public class Asteroid extends Point{
	
	Random rand = new Random();
	Planet planet;
	
	/**
	 * Places asteroids randomly around the given user parameters
	 
	 * @param size the size of the asteroid in diameter.
	 * @param colour the colour of this asteroid, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 * @param speed the angle multiplier of the asteroid that defines how fast it spins.
	 * @param dist the distance from the sun of this asteroid.
	 
	 */
	public Asteroid(double size, String colour, double speed, double dist){
		
		this.size = (size) + ((size+1.5) -(size)) * rand.nextDouble();				//Assigns random size, speed, and distance from the given parameters
		this.colour = colour;
		this.speed = (speed) + ((speed+0.8) -(speed)) * rand.nextDouble();
		nextPlace = rand.nextInt(25) + dist;
	}
	

}
