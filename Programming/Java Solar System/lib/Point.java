import java.util.*;

public class Point{
	
	public double centreDist = 0.0 ,
	 centreAngle = 0.0,
	 nextPlace = 0.0,
	 size = 0.0,
	 angle = 0.0,
	 speed = 0.0;
	 
	protected String colour = new String();	

	
	/**
	 * Sets the next location for the next planet to be placed.
	 * Calculates this by taking the size of the planet as parameter n and adding it to a distance of 50.0
	 
	 * @param n the size of the current planet.
	 */
	public double setNextPlace(double n){		//Update the next place location so that planets aren't overlapping
		
		nextPlace = n + size + 50.0;
		return nextPlace;
	}
	
	
	public double getDist(){					//getDist and getAngle used to show the current location of the planet/sun/moon
		return nextPlace;
	}
	
	public double getAngle(){
		return angle;
	}

	public double getCentreDist(){			//getCentreDist and getCentreAngle used to return the location of the sun
		return centreDist;
	}
	
	public double getCentreAngle(){
		return centreAngle;
	}
	
	
	public void drawSun(SolarSystem system){	//Draws a sun around polar coordinates centreDist and centreAngle
		system.drawSolarObject(centreDist,centreAngle,size,colour);
	}
	
	public void draw(SolarSystem system){		//Drawing a solar orb around centreDist and centreAngle
		
		angle = angle + speed;			//Adding the speed multiplier to the objects current location to make it move
		system.drawSolarObjectAbout(nextPlace, angle, size, colour, centreDist, centreAngle);
		
	
	}
}
