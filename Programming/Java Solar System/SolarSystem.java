import java.util.*;
import java.util.Collections;
import javafx.scene.input.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.JFXPanel;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import java.awt.event.*;

import java.util.concurrent.*;


/**
 * This class provides a simple window in which grahical objects can be drawn. 
 * @author Joe Finney
 */
public class SolarSystem extends JFrame implements WindowListener
{
	// Size of window
	private int width;
	private int height;

	private boolean exiting = false;

    // Basic button state
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;

    // Add list of object to draw in the next frame...
    private ArrayList<javafx.scene.shape.Circle> drawList = new ArrayList<>();
    private boolean frameComplete = false;

    // JavaFX containers
    private Scene scene;
    private Group root;
    private JFXPanel jfxPanel;

    private AnimationTimer timer;

	/**
	 * Create a view of a SolarSystem.
	 * The SolarSystem will be created with the default size of 300x300 pixels.
	 *
	 */
	public SolarSystem(int width, int height)
	{   
        this.setTitle("The Solar System");
        this.width = width;
        this.height = height;
        this.addWindowListener(this);

        jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new java.awt.Dimension(width, height));
        this.setContentPane(jfxPanel);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        root = new Group();
        scene = new Scene(root, width, height, Color.BLACK);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX();
            }
        });
	}

    private void initFX() {

        EventHandler<KeyEvent> keyDownHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) 
                    up = true;
                if (keyEvent.getCode() == KeyCode.DOWN) 
                    down = true;
                if (keyEvent.getCode() == KeyCode.LEFT) 
                    left = true;
                if (keyEvent.getCode() == KeyCode.RIGHT) 
                    right = true;
            }
        };

        EventHandler<KeyEvent> keyUpHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) 
                    up = false;
                if (keyEvent.getCode() == KeyCode.DOWN) 
                    down = false;
                if (keyEvent.getCode() == KeyCode.LEFT) 
                    left = false;
                if (keyEvent.getCode() == KeyCode.RIGHT) 
                    right = false;
            }
        };

        scene.setOnKeyPressed(keyDownHandler);
        scene.setOnKeyReleased(keyUpHandler);
        jfxPanel.setScene(scene);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                try
                {
                    if (frameComplete & !exiting)
                    {
                        root.getChildren().clear();

                        for (javafx.scene.shape.Circle c : drawList)
                            root.getChildren().add(c);

                        drawList.clear();
                        frameComplete = false;
                    }
                } catch (Exception e) {}
            }
        };
        
        timer.start();
    }

	/**
	 * Close this SolarSystem window.
	 * 
	 */
	public void exit()
    {
        try
        {
            if (!exiting)
            {
                com.sun.javafx.application.PlatformImpl.tkExit();
                exiting = true;
            }
        } catch (Exception e)
        {
        }
    }


	//
    // Derive a Color object from a given string representation
	// 
	private Color getColourFromString(String col)
	{
		Color colour = Color.web(col);
		return colour;
	}
	
	/**
	 * Draws a round shape in the window at the given co-ordinates that represents an object in the solar system.
	 * The SolarSystem class uses <i>Polar Co-ordinates</i> to represent the location
	 * of objects in the solar system.
	 *
	 * @param distance the distance from the sun to the object.
	 * @param angle the angle (in degrees) that represents how far the planet is around its orbit of the sun.
	 * @param diameter the size of the object.
	 * @param col the colour of this object, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 */
	public void drawSolarObject(double distance, double angle, double diameter, String col)
	{
		double centreOfRotationX = ((double) width) / 2.0; 
		double centreOfRotationY = ((double) height) / 2.0; 

		double rads = Math.toRadians(angle);
		double x = (int) (centreOfRotationX + distance * Math.sin(rads));
		double y = (int) (centreOfRotationY + distance * Math.cos(rads));

		synchronized (this)
		{
			if (drawList.size() >= 10000)
			{
				System.out.println("\n\n");
				System.out.println(" *********************************************************** ");
				System.out.println(" ***** Only 10000 Objects Supported per Solar System! ***** ");
				System.out.println(" *********************************************************** ");
				System.out.println("\n\n");
				System.out.println("If you are't trying to add this many things");
				System.out.println("to your SolarSystem, then you have probably");
				System.out.println("forgotten to call the finishedDrawing() method");
				System.out.println("See the JavaDoc documentation for more information");
				System.out.println("\n-- Joe");

                System.exit(0);
			}
			else
			{
                javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(0,0,diameter);
                circle.setTranslateX(x);
                circle.setTranslateY(y);
                circle.setFill(Color.web(col));

                drawList.add(circle);
			}
		}
	}

	/**
	 * Draws a round shape in the window at the given co-ordinates. 
	 * The SolarSystem class uses <i>Polar Co-ordinates</i> to represent the location
	 * of objects in the solar system. This method operates in the same fashion as drawSolarObject, but
	 * provides additional co-ordinates to allow the programmer to use an arbitrary point about which
	 * the object orbits (e.g. a planet rather than the sun).
	 *
	 * @param distance the distance from this object to the point about which it is orbiting.
	 * @param angle the angle (in degrees) that represents how far the object is around its orbit.
	 * @param diameter the size of the object.
	 * @param col the colour of this object, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 * @param centreOfRotationDistance the distance part of the polar co-ordinate about which this object orbits.
	 * @param centreOfRotationAngle the angular part of the polar co-ordinate about which this object orbits.
	 */
	public void drawSolarObjectAbout(double distance, double angle, double diameter, String col, double centreOfRotationDistance, double centreOfRotationAngle)
	{
		Color colour = this.getColourFromString(col);
		double centrerads = Math.toRadians(centreOfRotationAngle);
		double centreOfRotationX = (((double) width) / 2.0) + centreOfRotationDistance * Math.sin(centrerads); 
		double centreOfRotationY = (((double) height) / 2.0) + centreOfRotationDistance * Math.cos(centrerads); 

		double rads = Math.toRadians(angle);
		double x = (int) (centreOfRotationX + distance * Math.sin(rads));
		double y = (int) (centreOfRotationY + distance * Math.cos(rads));

		synchronized (this)
		{
			if (drawList.size() >= 10000)
			{
				System.out.println("\n\n");
				System.out.println(" *********************************************************** ");
				System.out.println(" ***** Only 10000 Objects Supported per Solar System! ***** ");
				System.out.println(" *********************************************************** ");
				System.out.println("\n\n");
				System.out.println("If you are't trying to add this many things");
				System.out.println("to your SolarSystem, then you have probably");
				System.out.println("forgotten to call the finishedDrawing() method");
				System.out.println("See the JavaDoc documentation for more information");
				System.out.println("\n-- Joe");

                System.exit(0);
			}
			else
			{
                javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(0,0,diameter);
                circle.setTranslateX(x);
                circle.setTranslateY(y);
                circle.setFill(Color.web(col));

                drawList.add(circle);
			}
		}

	}

	/**
	 * Makes all objects drawn recently drawn to be made visible on the screen.
	 *  
	 * Once called, all suns, planets and moons are displayed in the window.
	 */
	public void finishedDrawing()
	{
		try
		{
            frameComplete = true;
            while(frameComplete)
			    Thread.sleep(1);
		}
		catch (Exception e) { }
	}

	/** 
	 * Gets the width of the SolarSystem window, in pixels.
	 * @return the width in pixels
	 */
	public int getWidth()
	{
		return width;
	}

	/** 
	 * Gets the height of the SolarSystem window, in pixels.
	 * @return the height in pixels
	 */
	public int getHeight()
	{
		return height;
	}

	/** 
	 * Determines if the user is currently pressing the cursor up button.
	 * @return true if the up button is pressed, false otherwise.
	 */
	public boolean upPressed()
	{
		return up;
	}

	/** 
	 * Determines if the user is currently pressing the cursor down button.
	 * @return true if the down button is pressed, false otherwise.
	 */
	public boolean downPressed()
	{
		return down;
	}

	/** 
	 * Determines if the user is currently pressing the cursor left button.
	 * @return true if the left button is pressed, false otherwise.
	 */
	public boolean leftPressed()
	{
		return left;
	}

	/** 
	 * Determines if the user is currently pressing the cursor right button.
	 * @return true if the right button is pressed, false otherwise.
	 */
	public boolean rightPressed()
	{
		return right;
	}

    
    public void windowClosing(WindowEvent e) 
    {
        this.exit();
    }

    public void windowClosed(WindowEvent e) 
    {
        this.exit();
    }

    public void windowOpened(WindowEvent e) {};

    public void windowIconified(WindowEvent e) {};

    public void windowDeiconified(WindowEvent e) {};

    public void windowActivated(WindowEvent e) {};

    public void windowDeactivated(WindowEvent e) {};

    public void windowGainedFocus(WindowEvent e) {};

    public void windowLostFocus(WindowEvent e) {};

    public void windowStateChanged(WindowEvent e) {};
    
}
