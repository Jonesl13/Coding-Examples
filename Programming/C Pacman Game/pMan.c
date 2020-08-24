#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#include <ncurses.h>
#include <unistd.h>
#include <sys/time.h>
#include <sys/types.h>

#define WAIT 3

//ᗣPACMANᗣ by Luke Jones
int main(){
	
	initscr();/* Start curses mode */
	
	curs_set(0);
	keypad(stdscr, TRUE);
	srand(time(NULL)); 
	
	WINDOW *window;
	
	//Creating the bespoke arena
	char arena[25][23] = { 
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','*','*','*','*','*','*','*','*','*','*','#','*','*','*','*','*','*','*','*','*','*','#'},
		{'#','@','#','#','#','*','#','#','#','#','*','#','*','#','#','#','#','*','#','#','#','@','#'},
		{'#','*','#','#','#','*','#','#','#','#','*','#','*','#','#','#','#','*','#','#','#','*','#'},
		{'#','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','#'},
		{'#','*','#','#','#','*','#','*','#','#','#','#','#','#','#','*','#','*','#','#','#','*','#'},
		{'#','*','*','*','*','*','#','*','#','#','#','#','#','#','#','*','#','*','*','*','*','*','#'},
		{'#','#','#','#','#','*','#','*','*','*','*','#','*','*','*','*','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#','#','#','#',' ','#',' ','#','#','#','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#',' ',' ',' ',' ',' ',' ',' ',' ',' ','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#',' ','#','#','#',' ','#','#','#',' ','#','*','#','#','#','#','#'},
		{' ',' ',' ',' ',' ','*',' ',' ','#','M','M',' ','M','M','#',' ',' ','*',' ',' ',' ',' ',' '},
		{'#','#','#','#','#','*','#',' ','#','#','#',' ','#','#','#',' ','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#',' ',' ',' ',' ',' ',' ',' ',' ',' ','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#',' ','#','#','#','#','#','#','#',' ','#','*','#','#','#','#','#'},
		{'#','#','#','#','#','*','#',' ','#','#','#','#','#','#','#',' ','#','*','#','#','#','#','#'},
		{'#','*','*','*','*','*','*','*','*','*','*','#','*','*','*','*','*','*','*','*','*','*','#'},
		{'#','*','#','#','#','*','#','#','#','#','*','#','*','#','#','#','#','*','#','#','#','*','#'},
		{'#','@','*','*','#','*','*','*','*','*','*','C','*','*','*','*','*','*','#','*','*','@','#'},
		{'#','#','#','*','#','*','#','*','#','#','#','#','#','#','#','*','#','*','#','*','#','#','#'},
		{'#','*','*','*','*','*','#','*','*','*','*','#','*','*','*','*','#','*','*','*','*','*','#'},
		{'#','*','#','#','#','#','#','#','#','#','*','#','*','#','#','#','#','#','#','#','#','*','#'},
		{'#','*','#','#','#','#','#','#','#','#','*','#','*','#','#','#','#','#','#','#','#','*','#'},
		{'#','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
	};
	int row, col;
	
	//Printing out the arena
	for(row = 0; row<25; row++){
		for(col = 0; col<23; col++){
			printw("%c",arena[row][col]);
			;
		}
		printw("\n");
	}
	printw("Press END to exit");
	refresh();			/* Print it on to the real screen */
	
	int key = getch(),			/* Wait for user input */
	pacRow = 18, //Pacmans location coordinates
	pacCol = 11,
	score = 0,
	lives = 3,
	
	height = 4, //The size and location of the score window
	width = 20,
	starty = 10,	
	startx = 25;
	
	int ghostRow[]={11,11,11,11}; //arrays used as there are 4 ghosts
	int ghostCol[]={9,10,12,13};
	
	char prev[] = {' ',' ',' ',' '};
	
	//Creating the window that displays the lives and score
	window = newwin(height, width, starty, startx);
	
	//Function to print the lives out as pacman characters
	void printLives(){
		int place = 7;
		mvwprintw(window,1,place,"    ");
		if (lives > 0){
			for (int counter = 0; counter < lives; counter++){
				mvwprintw(window,1,place,"%c",'C');
				place++;
			}
		}else{
			mvwprintw(window,1,1,"YOU LOSE       ");
			score = 200;
		}
		wrefresh(window);
	}

	box(window,0,0);
	wborder(window, '|', '|', '-', '-', '+', '+', '+', '+');
	mvwprintw(window,1,1,"Lives: ");
	printLives();
	mvwprintw(window,2,1,"Current Score: %d",score);
	wrefresh(window);
		
	//Returns 1 if move is valid, 0 if not
	int validMove(int r, int c){
		int move = 0;
		
		if (arena[r][c] == '#'){
			move  =0;
		}else if(arena[r][c] =='*'&& score < 190){
			
			score++; //Incrementing the score if the next move is a pill
			
			if(score == 190){ //If pacman has collected all the pills
				mvwprintw(window,2,1,"YOU WIN           ");
			}else if(score < 190){
				mvwprintw(window,2,1,"Current Score: %d",score);
			}
			move = 1;
			wrefresh(window);
			
		}else if(arena[r][c] == 'M'){
			
			lives -= 1;
			printLives();
			
			arena[pacRow][pacCol] = ' ';
			mvaddch(pacRow, pacCol, ' ');
			
			pacRow = 18;
			pacCol = 11;
			
			arena[pacRow][pacCol] = 'C';
			mvaddch(pacRow, pacCol, 'C');
			
			refresh();
			
		}else if(score < 190){ //The user cannot move if game is won
			move = 1;
		}
		return move;
	}
	
	//Returns 1 if move is valid, 0 if not
	int ghostMove(int r, int c){
		int move = 0;
		
		if (arena[r][c] == '#'){
			move  = 0;
		}else if(arena[r][c] =='*'){
			
			move = 1;
			
		}else if(arena[r][c] == 'C'){
			lives -= 1;
			printLives();
			
			arena[pacRow][pacCol] = ' ';
			mvaddch(pacRow, pacCol, ' ');
			
			pacRow = 18;
			pacCol = 11;
			
			arena[pacRow][pacCol] = 'C';
			mvaddch(pacRow, pacCol, 'C');
			
			refresh();
			
		}else if(arena[r][c] == 'M'){
			move = 0;
			
		}else if(score < 190){ //Cannot move if game is won
			move = 1;
		}
		return move;
	}
	

	//Taking inputs until the END key is pressed (terminates the program)
	while(key != KEY_END) {
		
		key = getch(); //key is set to the next input
		timeout(300);	//Wait before moving past input
		
		
		//PACMAN MOVEMENT ----------------------------------------------
		if(key == KEY_LEFT){
			
			if (pacRow == 11 && pacCol ==0){ //Checking if pacman has reached a portal
				mvaddch(pacRow, pacCol, ' ');
				pacCol = 22;
			}
			
			if(validMove(pacRow,pacCol-1) == 1){
				
				arena[pacRow][pacCol] = ' '; //Setting current place to empty
				mvaddch(pacRow, pacCol, ' ');
				pacCol--;
				arena[pacRow][pacCol] = 'C';
				mvaddch(pacRow, pacCol, 'C'); //Moving pacman to the left place
				refresh();
			}	
		}
		if(key == KEY_RIGHT){
			
			if (pacRow == 11 && pacCol ==22){ //Checking if pacman has reached a portal
				mvaddch(pacRow, pacCol, ' ');
				pacCol = 0;
			}
			
			if(validMove(pacRow,pacCol+1) ==1){
			
								
				arena[pacRow][pacCol] = ' '; //Setting current place to empty
				mvaddch(pacRow, pacCol, ' '); 
				pacCol++;
				arena[pacRow][pacCol] = 'C';
				mvaddch(pacRow, pacCol, 'C');
				refresh();	
			}
		}
		if(key == KEY_UP){
			
			if(validMove(pacRow-1,pacCol) ==1){
				
				arena[pacRow][pacCol] = ' ';  //Setting current place to empty
				mvaddch(pacRow, pacCol, ' ');
				pacRow--;
				arena[pacRow][pacCol] = 'C';
				mvaddch(pacRow, pacCol, 'C');
				refresh();	
			}
		}
		if(key == KEY_DOWN){
			
			if(validMove(pacRow+1,pacCol) ==1){
				
				arena[pacRow][pacCol] = ' '; //Setting current place to empty
				mvaddch(pacRow, pacCol, ' '); 
				pacRow++;
				arena[pacRow][pacCol] = 'C';
				mvaddch(pacRow, pacCol, 'C');
				refresh();	
			}
		}
		
		//GHOST MOVEMENT -----------------------------------------------
		for(int turn = 0; turn < 4; turn++){
			int r = rand() % 4;
			
			switch(r){
				
				//Move left
				case 0:
				
					if (ghostRow[turn] == 11 && ghostCol[turn] == 0){ //Checking if ghost has reached a portal
						mvaddch(ghostRow[turn], ghostCol[turn], ' ');
						ghostCol[turn] = 22;
					}
					
					if(ghostMove(ghostRow[turn],ghostCol[turn]-1) == 1){
						
						arena[ghostRow[turn]][ghostCol[turn]] = prev[turn]; //Setting current place
						mvaddch(ghostRow[turn], ghostCol[turn], prev[turn]);
						
						ghostCol[turn]--;
						prev[turn] = arena[ghostRow[turn]][ghostCol[turn]];
						arena[ghostRow[turn]][ghostCol[turn]] = 'M';
						mvaddch(ghostRow[turn], ghostCol[turn], 'M'); //Moving ghost to the left place
						refresh();
				}	
				break;
				
				//Move right
				case 1:
				
					if (ghostRow[turn] == 11 && ghostCol[turn] ==22){ //Checking if ghost has reached a portal
						mvaddch(ghostRow[turn], ghostCol[turn], ' ');
						ghostCol[turn] = 0;
					}
					
					if(ghostMove(ghostRow[turn],ghostCol[turn]+1) ==1){
						
						arena[ghostRow[turn]][ghostCol[turn]] = prev[turn]; //Setting current place
						mvaddch(ghostRow[turn], ghostCol[turn], prev[turn]);
						
						ghostCol[turn]++;
						prev[turn] = arena[ghostRow[turn]][ghostCol[turn]];
						arena[ghostRow[turn]][ghostCol[turn]] = 'M';
						mvaddch(ghostRow[turn], ghostCol[turn], 'M');
						refresh();	
				}
				break;
				
				//Move up
				case 2:
					
					if(ghostMove(ghostRow[turn]-1,ghostCol[turn]) ==1){
						
						arena[ghostRow[turn]][ghostCol[turn]] = prev[turn]; //Setting current place
						mvaddch(ghostRow[turn], ghostCol[turn], prev[turn]);
						
						ghostRow[turn]--;
						prev[turn] = arena[ghostRow[turn]][ghostCol[turn]];
						arena[ghostRow[turn]][ghostCol[turn]] = 'M';
						mvaddch(ghostRow[turn], ghostCol[turn], 'M');
						refresh();	
				}
				break;
				
				//Move down
				case 3:
				
					if(ghostMove(ghostRow[turn]+1,ghostCol[turn]) ==1){
						
						arena[ghostRow[turn]][ghostCol[turn]] = prev[turn]; //Setting current place
						mvaddch(ghostRow[turn], ghostCol[turn], prev[turn]);
						
						ghostRow[turn]++;
						prev[turn] = arena[ghostRow[turn]][ghostCol[turn]];
						arena[ghostRow[turn]][ghostCol[turn]] = 'M';
						mvaddch(ghostRow[turn], ghostCol[turn], 'M');
						refresh();	
				}
				break;
				
				}
				
		}
		
		
	}
	
	

	
	
	endwin();/* End curses mode	*/
	
	
	return 0;
}
