/**
 * 
 * @author Stephan Loh
 * Last updated as of 2/6/17
 * Things to add:
 * 	Highlighting of invalid values
 * 	An intro screen 
 * 	Allow user to create a board of any size
 * 	Possible other types of sudoku
 */ 
package gui;

import model.Sudoku;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.*;

@SuppressWarnings("restriction")


public class GUIRunner extends Application{
	private static Label allSquares[][];
	private static Sudoku sudoku;
	private static int curX, curY, prevX, prevY, width, height;
	private static int sceneWidth = 500, sceneHeight = 500;

	private static Background blank = new Background(new BackgroundFill(Color.TRANSPARENT, null, null));
	private static Background highlighted = new Background(new BackgroundFill(Color.GRAY, null, null));
	private static Background choices = new Background(new BackgroundFill(Color.LIGHTSKYBLUE, null, null));
	private static Background invalid = new Background(new BackgroundFill(Color.RED, null, null));
	private static Font valueFont = new Font("Times New Roman", 34);
	private static Font choicesFont = new Font("Times New Roman", 12);

	private static boolean choiceSelect = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		width = 3;
		height = 3;
		int verSpaceBetweenNodes = 0, horSpaceBetweenNodes = 0;
		int paneBorderTop = 20, paneBorderRight = 20;
		int paneBorderBottom = 20, paneBorderLeft = 20;

		curX = curY = prevX = prevY = 0;

		sudoku = new Sudoku(width, height);
		allSquares = new Label[width * height][width * height];

		/* Setting pane properties */
		GridPane pane = createGrid(width, height);
		pane.setHgap(horSpaceBetweenNodes);
		pane.setVgap(verSpaceBetweenNodes);
		pane.setPadding(new Insets(paneBorderTop, paneBorderRight, 
				paneBorderBottom, paneBorderLeft));
		pane.setStyle("-fx-border-style: solid inside;" +
				"-fx-border-color: black;");

		/* Display the stage */
		Scene scene = new Scene(pane, sceneWidth, sceneHeight);

		//Add possible keys
		//Note the special S key! It updates all possible choices
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {	
			int numToAdd = -1;
			switch(keyEvent.getCode()) {
			case LEFT: if(curX > 0) 
				curX--;
			break;
			case RIGHT: if(curX < width * height - 1) 
				curX++;
			break;
			case DOWN: if(curY < width * height - 1)
				curY++;
			break;
			case UP: if(curY > 0)
				curY--;
			break;
			case ENTER:
				choiceSelect = !choiceSelect;
				break;
			case S:
				updateAllChoices();
				break;
			case DIGIT0:
				numToAdd = 0;
				break;
			case DIGIT1:
				numToAdd = 1;
				break;
			case DIGIT2:
				numToAdd = 2;
				break;
			case DIGIT3:
				numToAdd = 3;
				break;
			case DIGIT4:
				numToAdd = 4;
				break;
			case DIGIT5:
				numToAdd = 5;
				break;
			case DIGIT6:
				numToAdd = 6;
				break;
			case DIGIT7:
				numToAdd = 7;
				break;
			case DIGIT8:
				numToAdd = 8;
				break;
			case DIGIT9:
				numToAdd = 9;
				break;
			default:
				break;
			}
			updateSquare(numToAdd);
		});

		primaryStage.setTitle("Sudoku");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static GridPane createGrid(int width, int height) {
		GridPane board = new GridPane();
		for(int x = 0; x < width * height; x++) {
			for(int y = 0; y < width * height; y++) {
				Label square = createSquare(x, y);
				allSquares[x][y] = square;
				board.add(square, x, y);
			}
		}

		return board;
	}

	public static Label createSquare(int x, int y) {
		StringBuffer style = new StringBuffer("-fx-border-style: solid;"
				+ "-fx-border-color: black;"
				+ "-fx-alignment: center;"
				+ "-fx-border-width: ");
		int top = 1, right = 1, bottom = 1, left = 1; 
		if(x % width == 0) {
			left = 3;
		}

		if(x == width * height - 1) {
			right = 3;
		}

		if(y % height == 0) {
			top = 3;
		}

		if(y == width * height - 1) {
			bottom = 3;
		}
		style.append(top + " " + right + " " + bottom + " " + left + ";");

		Label square = new Label();
		square.setPrefWidth(sceneWidth / width * height);
		square.setPrefHeight(sceneHeight / width * height);
		square.setStyle(style.toString());
		square.setWrapText(true);
		square.setFont(valueFont);

		square.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			System.out.println("clicked");
			curX = x;
			curY = y;
			updateSquare(-1);
		});



		return square;
	}
	
	public static void updateAllChoices() {
		sudoku.updateAllChoices();
		for(int x = 0; x < allSquares.length; x++) {
			for(int y = 0; y < allSquares[x].length; y++) {
				if(sudoku.getNumber(x, y) == 0) {
					allSquares[x][y].setFont(choicesFont);
					allSquares[x][y].setText(sudoku.getPossibleChoices(x, y));
				}
				
			}
		}
	}

	public static void updateSquare(int numToAdd) {
		Label curLabel = allSquares[curX][curY];
		if(prevX != curX || prevY != curY) {
			allSquares[prevX][prevY].setBackground(blank);
			prevX = curX;
			prevY = curY;
		}

		if(choiceSelect) {
			curLabel.setBackground(choices);
		} else {
			curLabel.setBackground(highlighted);
		}	

		if(numToAdd != -1) {
			if(choiceSelect && sudoku.getNumber(curX, curY) == 0) {
				sudoku.toggleChoice(numToAdd, curX, curY);
				curLabel.setFont(choicesFont);
				curLabel.setText(sudoku.getPossibleChoices(curX, curY));
			} else if(!choiceSelect && numToAdd != 0) {
				sudoku.enterNumber(numToAdd, curX, curY);
				curLabel.setFont(valueFont);
				curLabel.setText(sudoku.getNumberString(curX, curY));
			} else if(!choiceSelect && numToAdd == 0) {
				sudoku.enterNumber(numToAdd, curX, curY);
				curLabel.setFont(choicesFont);
				curLabel.setText(sudoku.getPossibleChoices(curX, curY));
			}
		} 
		
		

	}
	
	public static void checkValid() {
		if(!sudoku.isValid(curX, curY)) {
			allSquares[curX][curY].setBackground(invalid);
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
