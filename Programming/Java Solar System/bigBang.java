
public class bigBang{

	public bigBang(){
		
		double place = 0.0; //Used to keep track of the next position 
		
		Planet[] planets = new Planet[9];
		Moon[] moons = new Moon[205];
		Asteroid[] belt = new Asteroid[200];
		
		SolarSystem system = new SolarSystem(1700,1000);
		
			Sun sun = new Sun(0.0,0.0,50.0, "yellow");								//The sun
				place = sun.setNextPlace(place);
			
			planets[0] = new Planet(6.0, "grey", 1.512, sun);							//Mercury
				place = planets[0].setNextPlace(place);
				
			planets[1] = new Planet(8.0, "saddlebrown", 1.145, sun);					//Venus
				place = planets[1].setNextPlace(place);
				
			planets[2] = new Planet(10.0, "blue", 1.077, sun);						//Earth
				
				moons[0] = new Moon(1.0, "white", 4.0, 20.0, planets[2]);		//Earth's Moon
				place = planets[2].setNextPlace(place);
				
			planets[3] = new Planet(9.0, "crimson", 0.935, sun);						//Mars
				place = planets[3].setNextPlace(place);

			
				for (int count = 0; count <belt.length; count++){					//Martian-Jovian asteroid belt
					belt[count] = new Asteroid(0.2,"darkgray",0.05,place+8);				
				}
				
				
			planets[4] = new Planet(25.0, "coral", 0.46587, sun);						//Jupiter
			
				moons[1] = new Moon(2.0, "gold", 3.07561, 35.0, planets[4]);			//Io
				moons[2] = new Moon(1.5, "paleturquoise", 2.531, 40.0, planets[4]);		//Europa
				moons[3] = new Moon(3.0, "ivory", 2.0173, 45.0, planets[4]);			//Ganymede
				moons[4] = new Moon(2.5, "darkslateblue", 1.723, 50.0, planets[4]);		//Callisto
				
				place = planets[4].setNextPlace(place);
				
			planets[5] = new Planet(18.0, "lightpink", 0.1451, sun);					//Saturn
			
				for(double count = 5; count < moons.length; count ++){
				
					moons[(int)count] = new Moon(2.0, "ivory", count/203, 30.0, planets[5]);	//Saturn's ring
				}
			
				place = planets[5].setNextPlace(place);
				
			planets[6] = new Planet(15.0, "darkseagreen", 0.073, sun);				//Uranus
				place = planets[6].setNextPlace(place);	
				
			planets[7] = new Planet(13.0, "royalblue", 0.047, sun);					//Neptune
				place = planets[7].setNextPlace(place);
				
			planets[8] = new Planet(9.0, "orange", 0.022, sun);						//The planet Pluto ;^)
				place = planets[8].setNextPlace(place);

			
			//Infinite loop to spin the solar system until the window is closed
			while (true){
				
				sun.drawSun(system);
				
				//Drawing all the planets
				for(int count = 0; count < planets.length; count++){
					planets[count].draw(system);	
				}
				
				//Drawing moons
				for(int count = 0; count < moons.length; count++){
					moons[count].drawMoon(system);
				}
		
				//Drawing the asteroid belt
				for (int count = 0; count < belt.length; count++){
					belt[count].draw(system);
				}
			
			system.finishedDrawing();
			
			}
	}
}
