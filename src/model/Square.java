/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 * The smallest unit for a Sudoku board.
 * Contains a value and possible choices
 */
package model;

public class Square {
	private int value;
	private boolean[] possibleChoices;

	public Square(int value, int totalPossible) {
		this.value = value;
		possibleChoices = new boolean[totalPossible];
	}

	public Square() {
		this(0, 9);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/*
	public void addChoice(int choice) {
		possibleChoices[choice + 1] = true;
	}

	public void deleteChoice(int choice) {
		possibleChoices[choice + 1] = false;
	}
	*/

	public void clearChoices() {
		for(int i = 0; i < possibleChoices.length; i++) {
			possibleChoices[i] = false;
		}
	}

	
	public void toggleChoice(int choice) {
		//Subtracts one because an array begins from index 0 but 0 is not a valid choice
		possibleChoices[choice - 1] = !possibleChoices[choice - 1];
	}

	public String getPossibleChoices() {
		StringBuffer results = new StringBuffer();
		for(int i = 0; i < possibleChoices.length; i++) {
			if(possibleChoices[i]) {
				results.append((i + 1) + ", ");
			}
		}
		if(results.length() > 2)
			results.substring(0, results.length() - 2);
		return results.toString();
	}

	public String toString() {
		return String.valueOf(value);
	}

}
