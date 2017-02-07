/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 * Task to update all possible choices for a square. Creates 3 CheckValid Tasks for each possible choice: Row, Column, and Box
 * After all tasks have been accomplished, it will update the possible choices for the square
 */
package model;

public class UpdateSquareChoicesTask implements Runnable {
	private Square[][] board;
	private int x, y, width, height;
	
	public UpdateSquareChoicesTask(Square[][] board, int x, int y, int width, int height) {
		this.board = board;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void run() {
		//If calling this thread, then will clear the square's choices
		board[x][y].clearChoices();
		
		ValidLock[] validValues = new ValidLock[width * height];
		Thread[][] validCheckers = new Thread[width * height][3];
		
		//For every possible choice i, create 3 CheckValidTasks
		for(int i = 0; i < width * height; i++) {
			validValues[i] = new ValidLock();
			
			//Create one of every CheckValidTask for the square
			for(int j = 0; j < validCheckers[i].length; j++) {
				
				//Second constructor where given a number, the CheckValidTask will check if the given number is valid
				validCheckers[i][j] = new Thread(new CheckValidTask(board, j, validValues[i], x, y, width, height, i + 1));
			}		
		}
		
		//Starting all CheckValid tasks
		for(int i = 0; i < validCheckers.length; i++) {		
			for(int j = 0; j < validCheckers[i].length; j++) {
				validCheckers[i][j].start();
			}		
		}
		
		//Joining all CheckValid tasks
		for(int i = 0; i < validCheckers.length; i++) {		
			for(int j = 0; j < validCheckers[i].length; j++) {
				try {
					validCheckers[i][j].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
		
		//Adding all valid values into choices
		for(int i = 0; i < validValues.length; i++) {
			if(validValues[i].isValid()) {
				board[x][y].toggleChoice(i + 1);
			}
		}
		
	}
}
