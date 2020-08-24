import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;


public class SlidePuzzle implements ActionListener{
	
	private JFrame f = new JFrame();
	private JPanel mainPanel = new JPanel();
	private JPanel midPanel = new JPanel();
	private GridLayout gLayout = new GridLayout(3,4);
	private BorderLayout bLayout = new BorderLayout();
	private int blankTile = 0, pressed, numTiles = 12, score = 0;
	private JButton[] b = new JButton[numTiles];
	private JButton random = new JButton("Random");
	private JLabel scoreB = new JLabel("Score: 0");
	private ImageIcon tmp;
	private boolean started = false;
	
	//Constructor
	public SlidePuzzle(){
		
		f.setContentPane(mainPanel);
		midPanel.setLayout(gLayout);
		mainPanel.setLayout(bLayout);
			
		//Assigning imageIcons to each tile
		for (int count = 0; count < numTiles; count++){
			ImageIcon i = new ImageIcon("bart"+count+".jpg");
			b[count] = new JButton(i);
		}	
		
		//Adding the tiles to the midPanel
		for (int count = 0; count < numTiles; count++){
			midPanel.add(b[count]);
			b[count].addActionListener(this);
		}
		
		mainPanel.add(midPanel);
		mainPanel.add("North", scoreB);
		mainPanel.add("South", random);
		random.addActionListener(this);
		
		f.setTitle("Swingin' Simpsons");
		f.setSize(444,400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		
		//Checking the tiles and identifying the pressed tile and blank tile
		for(int i = 0; i < numTiles; i++){
			if(e.getSource() == b[i]){
				pressed = i;
			}
			
			if(b[i].getIcon().toString() == "bart0.jpg"){
				blankTile = i;
			}
		}
		
		//Calls the swap method with the pressed tile and the blank tile and increases the score
		if(validMove(pressed)==true ){
			swap(blankTile, pressed);
			upScore();
		}

		if(e.getSource() == random){
			started = true;
			randomise();
		}
	}
	
	//Checks if the given move is valid, returns true. Returns false if invalid
	private boolean validMove(int pressed){

		if((pressed < 12 && pressed > -1)&&(((pressed == blankTile+1) && ((blankTile != 3) && (blankTile != 7) && (blankTile != 11))||((pressed == blankTile-1) && ((blankTile != 4) && (blankTile != 8) && (blankTile != 0)))||(pressed == blankTile+4) && (blankTile != 8)||(pressed == blankTile-4) && (blankTile != -4)))){
			return true;
		}else{
			return false;
		}
	}
	
	//Swaps the two given tiles by changing their imageIcons
	private void swap(int blank, int secondTile){
		
			tmp = new ImageIcon(b[secondTile].getIcon().toString());
			b[blank].setIcon(tmp);
			
			tmp = new ImageIcon("bart0.jpg");
			b[secondTile].setIcon(tmp);
			blankTile = secondTile;
	}
	
	//Increases the score and checks if the user won
	private void upScore(){
		
		boolean win = true;
		
		//Loop that checks if tiles 0-9 are in the right order
		for(int i = 0; i < 10; i++){
			String temp = b[i].getIcon().toString();
			if (i != (temp.charAt(4)-'0')){
				win = false;
			}
		}
		
		if(score == -1){
			win = false;
		}
		
		score++;
		scoreB.setText("Score: "+score);
		
		//If the user wins
		if (win == true){
			scoreB.setText("You Win! Score: "+score);
			
			//Opens a new instance of the scoreboard
			ScoreBoard b = new ScoreBoard(score);
			score = 0; //Sets score to 0 so that the user can replay without their score persisting
		}
	}
	
	//Randomly generates a path and goes through the puzzle for 120 moves
	private void randomise(){
		
		Random ran = new Random();
		score = -1;	//Setting the user's score to 0
		upScore();	//Updating the score display
		
		for(int count = 0; count < 120; count++){
			
			//Generates a random number between 0 and 3 in order to decide where to go
			switch (ran.nextInt(4)){
				
				//Move right
				case 0: if(validMove(blankTile+1)==true){
							swap(blankTile,blankTile+1);
	
						}
						break;
				
				//Move left
				case 1: if(validMove(blankTile-1)==true){
							swap(blankTile,blankTile-1);

						}
						break;
				
				//Move up
				case 2: if(validMove(blankTile+4)==true){
							swap(blankTile,blankTile+4);

						}
						break;
				
				//Move down		
				case 3: if(validMove(blankTile-4)==true){
							swap(blankTile,blankTile-4);

						}
						break;
						
			}
		}
	}
}
