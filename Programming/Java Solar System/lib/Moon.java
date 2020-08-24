public class Moon extends Planet{
	
	Planet planet;
	private double dist;

	
	/**
	 * Places a moon around the given moon
	 
	 * @param size the size of the moon in diameter.
	 * @param colour the colour of this moon, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 * @param speed the angle multiplier of the moon that defines how fast it orbits the sun.
	 * @param dist the distance from the planet to orbit
	 * @param planet the planet instance that the moon should orbit
	 
	 */
	public Moon(double size, String colour, double speed, double dist,Planet planet){
		
		super(size,colour,speed,planet.returnSun());
		
		this.size = size;
		this.colour = colour;
		this.speed = speed;
		this.planet = planet;
		this.dist = dist;
		
	}
	
	public void drawMoon(SolarSystem system){			//Separate from point's .draw() method as the moons orbit a planet instead of the sun
		
		angle = angle+ speed;
		system.drawSolarObjectAbout(dist, angle, size, colour, planet.getDist(), planet.getAngle());
		
	
	}
}
