/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 * 
 * Board refers to the entire board
 * Square refers to the smallest unit
 * Box refers to a width by height area
 * 
 * Note that x is column and y is row
 */
package model;

import java.util.ArrayList;

public class Sudoku {
	private int[][] answerKey;
	private Square[][] board; 
	private int height, width;

	//Width is box width, height is box height
	public Sudoku(int width, int height) {
		this.height = height;
		this.width = width;
		answerKey = new int[width * height][width * height];
		board = new Square[width * height][width * height];
		setBoard();
	}

	public void setBoard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				board[i][j] = new Square(0, height * width);
			}
		}
	}

	public void enterNumber(int numToAdd, int x, int y) {
		board[x][y].setValue(numToAdd);
	}

	/*
	public void addChoice(int choiceToAdd, int x, int y) {
		board[x][y].addChoice(choiceToAdd);
	}

	public void deleteChoice(int choiceToDelete, int x, int y) {
		board[x][y].deleteChoice(choiceToDelete);
	}
	*/

	public void toggleChoice(int choice, int x, int y) {
		if(choice != 0) {
			board[x][y].toggleChoice(choice);
		}
	}

	public boolean isValid(int x, int y) {
		ValidLock valid = new ValidLock();
		Thread[] validCheckers = new Thread[3];

		for(int i = 0; i < validCheckers.length; i++) {
			validCheckers[i] = new Thread(new CheckValidTask(board, i + 1, valid, x, y, width, height));
		}

		for(int i = 0; i < validCheckers.length; i++) {
			validCheckers[i].start();
		}

		return valid.isValid();
	}

	public String getPossibleChoices(int x, int y) {
		return board[x][y].getPossibleChoices();
	}

	public String getNumberString(int x, int y) {
		return board[x][y].toString();
	}

	public int getNumber(int x, int y) {
		return board[x][y].getValue();
	}

	public void updateAllChoices() {
		Thread[][] everySquare = new Thread[board.length][board[0].length];
		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board[x].length; y++) {
				everySquare[x][y] = updateSquareChoices(x, y);
			}
		}

		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board[x].length; y++) {
				everySquare[x][y].start();;
			}
		}

		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board[x].length; y++) {
				try {
					everySquare[x][y].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			}
		}
	}

	public Thread updateSquareChoices(int x, int y) {
		return new Thread(new UpdateSquareChoicesTask(board, x, y, width, height));
	}

	public void clearChoices(int x, int y) {
		board[x][y].clearChoices();
	}

	public boolean isValue(int value, int x, int y) {
		return (value == board[x][y].getValue());
	}

	/**
	 * Not yet implemented. Will be used eventually to create unique Sudoku boards
	 */
	private void createBoard() {
		answerKey = new int[height * height][width * width];

		for(int row = 0; row < answerKey.length; row++) {
			for(int col = 0; col < answerKey[row].length; col++) {
				answerKey[row][col] = getRandomNumber(getAvailableNumbers(row, col));
			}
		}
	}

	/**
	 * NOT YET FULLY IMPLEMENTED
	 * x refers to the up and down location of what row
	 * y refers to the left and right location of which column
	 * @param x represents the current row
	 * @param y represents the current column
	 * @return ArrayList<Integer> of availableNumbers 
	 */
	private ArrayList<Integer> getAvailableNumbers(int x, int y) {
		System.out.println("Row: " + x + " Col: " + y);
		int lowerBound = x / height * height;
		int upperBound = x / height * (height + 1);
		int leftBound = y / width * width;
		int rightBound = y / width * (width + 1);
		ArrayList<Integer> availableNumbers = new ArrayList<Integer>();

		//Puts the numbers 1 - 9 into availableNumbers;
		for(int i = 1; i <= 9; i++) {
			availableNumbers.add(i);
		}


		//Removes all numbers in the current column
		for(int row = 0; row < answerKey.length; row++){
			if(answerKey[row][y] != 0) {
				availableNumbers.remove((Integer) answerKey[row][y]);
			}
		}

		//Removes all numbers in the current row
		for(int col = 0; col < answerKey[x].length; col++){
			if(answerKey[x][col] != 0) {
				availableNumbers.remove((Integer) answerKey[x][col]);
			}
		}

		//Removes all numbers in the current square
		for(int row = lowerBound; row < upperBound; row++) {
			for(int col = leftBound; col < rightBound; col++) {
				if(answerKey[row][col] != 0){
					availableNumbers.remove( (Integer) answerKey[row][col]);
				}
			}
		}

		for(int num : availableNumbers) {
			System.out.println(num);
		}
		return availableNumbers;
	}

	private int getRandomNumber(ArrayList<Integer> availableNumbers) {
		return availableNumbers.get((int) (Math.random() * availableNumbers.size()));
	}

	@Override
	public String toString() {
		StringBuffer results = new StringBuffer();
		for(int row = 0; row < answerKey.length; row++) {
			for(int col = 0; col < answerKey[row].length; col++) {
				results.append(answerKey[row][col] + " ");
			}
			results.append("\n");
		}

		return results.toString();
	}

}
