/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 *
 */
package model;

public class CheckValidTask implements Runnable {
	private Square[][] board;
	private int x, y, typeOfTest, width, height, valueToCheck;
	private ValidLock check;

	/**
	 *  @param board: the 2D array containing the squares of the Sudoku board
	 *  @param typeOfTest: 0 is check row, 1 is check column, 2 is check square
	 *  @param check: Whether or not a valid value has been found. Will short-circuit if not valid
	 *  @param x: Column of the square
	 *  @param y: Row of the square
	 *  @param width: Width of a box
	 *  @param height: Height of a box
	 *  @param valueToCheck: Value to check if valid. If valueToCheck is -1, then will take the square's value
	 */
	public CheckValidTask(Square[][] board, int typeOfTest, ValidLock check, int x, int y, int width, int height, int valueToCheck) {
		this.board = board;
		this.typeOfTest = typeOfTest;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.check = check;
		this.valueToCheck = valueToCheck;
	}	
	
	public CheckValidTask(Square[][] board, int typeOfTest, ValidLock check, int x, int y, int width, int height) {
		this(board, typeOfTest, check, x, y, width, height, -1);
	}

	@Override
	public void run() {
		int value;
		
		if(valueToCheck == -1) {
			value = board[x][y].getValue();
		} else {
			value = valueToCheck;
		}
		
		if(typeOfTest == 0) {
			checkRow(value);
		} else if(typeOfTest == 1) {
			checkColumn(value);
		} else {
			checkBox(value);
		}

	}

	public void checkRow(int value) {
		//Check row
		for(int i = 0; i < board.length; i++) {
			if(check.isValid()) {
				boolean alreadyExists = value != board[i][y].getValue();
				synchronized(check) {
					check.update(alreadyExists);
				}
			} else {
				break;
			}
		}
	}

	public void checkColumn(int value) {
		for(int j = 0; j < board[0].length; j++) {
			if(check.isValid()) {
				boolean alreadyExists = value != board[x][j].getValue();
				synchronized(check) {
					check.update(alreadyExists);
				}
			} else {
				break;
			}
		}
	}

	public void checkBox(int value) {
		//Check box it is in
		int lowerBound = (y / height) * height;
		int upperBound = lowerBound + height;
		int leftBound = (x / width) * width;
		int rightBound = leftBound + width;


		//row is y, col is x
		for(int row = lowerBound; row < upperBound; row++) {
			if(check.isValid()) {
				for(int col = leftBound; col < rightBound; col++) {
					if(check.isValid()){
						boolean alreadyExists = value != board[col][row].getValue();
						synchronized(check) {
							check.update(alreadyExists);
						}
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
	}

}
