public class Planet extends Point{
	
	private Sun sun;
	
	
	/**
	 * Places a planet around the given sun
	 
	 * @param size the size of the planet in diameter.
	 * @param colour the colour of this planet, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 * @param speed the angle multiplier of the planet that defines how fast it orbits the sun.
	 * @param sun the Sun instance that the planet should orbit
	 
	 */
	public Planet(double size, String colour, double speed, Sun sun){
		
		this.size = size;
		this.colour = colour;
		this.speed = speed;
		this.sun = sun;
		centreDist = sun.getCentreDist();
		centreAngle = sun.getCentreAngle();
		
	}
	
	public Sun returnSun(){					//Used when program contains multiple suns
		return sun;
	}
}
