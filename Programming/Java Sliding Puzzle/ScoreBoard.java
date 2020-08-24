import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class ScoreBoard implements ActionListener{
	
	private GridLayout gLayout = new GridLayout(12,1);
	private GridLayout gLayout1 = new GridLayout(1,2);
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel backPanel = new JPanel();
	private JFrame sFrame = new JFrame();
	private JLabel pTitle = new JLabel("PLAYER");
	private JLabel sTitle = new JLabel("SCORE");
	private	String[] players = new String[10];
	private int[] scores = new int[10];
	private JLabel[] pLabel = new JLabel[10];
	private JLabel[] sLabel = new JLabel[10];
	private JButton enter = new JButton("Enter Your Name");
	private JTextField nameEnter = new JTextField();
	private String fileName = "scores.txt";
	private String line = null;
	private int score;
	private	int worst = 0;
	
	public ScoreBoard(int inputScore){
		
		score = inputScore;
		
		for(int count = 0; count < 10; count++){
		pLabel[count] = new JLabel("");
		sLabel[count] = new JLabel("");
		}
		
		backPanel.setLayout(gLayout1);
		leftPanel.setLayout(gLayout);
		rightPanel.setLayout(gLayout);
		leftPanel.add(pTitle);
		rightPanel.add(sTitle);
		readFile();		
		
		for(int count = 0; count < 10; count++){
			leftPanel.add(pLabel[count]);
			rightPanel.add(sLabel[count]);
		}
		
		//Only grants the user to add their score if it's high enough
		if(isHighScore(score) == true){
			leftPanel.add(enter);
			enter.addActionListener(this);
			rightPanel.add(nameEnter);
		}
		
		backPanel.add(leftPanel);
		backPanel.add(rightPanel);
				 
		sFrame.setContentPane(backPanel);
		sFrame.setTitle("Score Board");
		sFrame.setSize(400,450);
		sFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		sFrame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e){
		
		if(e.getSource() == enter){
			addScore(score, nameEnter.getText());
		}
	}
    
    //Adds the users score and deletes the largest (worst) score on the board
	private void addScore(int userScore, String userName){
		
		int tmp = 0;
		for (int count = 0; count < 10; count++){
			
				if (scores[count] > worst){
					worst = scores[count];
					tmp = count;
				}	
		}
		scores[tmp] = userScore;
		players[tmp] = userName;
		sort();
		displayScores();
		nameEnter.setVisible(false);
		enter.setVisible(false);
	}
	
	//Uses selection sort to sort the scores lowest to highest
	private void sort(){
		
		for (int count = 0; count < 9; count++)
        {
            int index = count;
            
            for (int j = count + 1; j < 10; j++){
            
                if (scores[j] < scores[index]){
                    index = j;
				}
			}
			
            int smallerNumber = scores[index]; 
            String smallerNumberP = players[index];
             
            scores[index] = scores[count];
            players[index] = players[count];
            
            scores[count] = smallerNumber;
            players[count] = smallerNumberP;
        }

	}
	
	//Checks if the user's score should be on the high scores page
	private boolean isHighScore (int userScore){
		boolean high = false;
		
		for (int count = 0; count < 10; count++){
			
				if (userScore < scores[count] || scores[count] == 0){
					high = true;
				}
		}
		
		return high;
	}
	
    
    private void readFile(){
		
		try {
			
			//Scanner finds a premade file that contains the scores
			Scanner s = new Scanner(new File(fileName));	
					
			for(int count = 0; count < 10; count++){
				
				players[count] = s.nextLine();
				scores[count] = Integer.parseInt(s.nextLine());
			}
			
			displayScores();
			}
			catch(FileNotFoundException ex) {
				System.out.println("Error opening file: " + fileName);                
			}
	}
	
	private void displayScores(){
		
		try {
			
			//Printwriter creates a new file that overwrites the original file
			PrintWriter w = new PrintWriter(fileName);
			for(int count = 0; count < 10; count++){
				
				pLabel[count].setText(players[count]);
				sLabel[count].setText(String.valueOf(scores[count]));
				
				w.println(players[count]);
				w.println(scores[count]);
			}
			w.close();
		}
		
		catch(FileNotFoundException ex) {
			System.out.println("Error opening file: " + fileName);                
		}
	}
}

