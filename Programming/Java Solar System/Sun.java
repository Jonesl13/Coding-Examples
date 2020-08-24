public class Sun extends Point{
	
	/**
	 * Creates a new sun around the given user parameters
	 
	 * @param centreDist the polar distance from point 0,0 to place the sun.
	 * @param centreAngle the polar angle in degrees to place from 0,0.
	 * @param size the given size of the sun.
	 * @param colour the colour of this sun, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>.
	 
	 */
	public Sun(double centreDist, double centreAngle, double size, String colour){
		
		this.centreDist = centreDist;
		this.centreAngle = centreAngle;
		this.size = size;
		this.colour = colour;
	
	}
}
