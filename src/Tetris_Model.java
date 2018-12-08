import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * ***********************************************************************************
 * ***********************************************************************************
 *   ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄ 	**
 *	▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌	**
 *	 ▀▀▀▀█░█▀▀▀▀ ▐░█▀▀▀▀▀▀▀▀▀  ▀▀▀▀█░█▀▀▀▀ ▐░█▀▀▀▀▀▀▀█░▌ ▀▀▀▀█░█▀▀▀▀ ▐░█▀▀▀▀▀▀▀▀▀ 	**
 *	     ▐░▌     ▐░▌               ▐░▌     ▐░▌       ▐░▌     ▐░▌     ▐░▌          	**
 *	     ▐░▌     ▐░█▄▄▄▄▄▄▄▄▄      ▐░▌     ▐░█▄▄▄▄▄▄▄█░▌     ▐░▌     ▐░█▄▄▄▄▄▄▄▄▄ 	**
 *	     ▐░▌     ▐░░░░░░░░░░░▌     ▐░▌     ▐░░░░░░░░░░░▌     ▐░▌     ▐░░░░░░░░░░░▌	**
 *		 ▐░▌     ▐░█▀▀▀▀▀▀▀▀▀      ▐░▌     ▐░█▀▀▀▀█░█▀▀      ▐░▌      ▀▀▀▀▀▀▀▀▀█░▌	**
 *		 ▐░▌     ▐░▌               ▐░▌     ▐░▌     ▐░▌       ▐░▌               ▐░▌	**
 *		 ▐░▌     ▐░█▄▄▄▄▄▄▄▄▄      ▐░▌     ▐░▌      ▐░▌  ▄▄▄▄█░█▄▄▄▄  ▄▄▄▄▄▄▄▄▄█░▌	**
 *		 ▐░▌     ▐░░░░░░░░░░░▌     ▐░▌     ▐░▌       ▐░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌	**	
 *		  ▀       ▀▀▀▀▀▀▀▀▀▀▀       ▀       ▀         ▀  ▀▀▀▀▀▀▀▀▀▀▀  ▀▀▀▀▀▀▀▀▀▀▀ 	**
 *  **********************************************************************************
 *  																				**
 * 	MODEL CLASS																		**
 * 	TODO: 	- use better font														**
 * 			- change button because it's ugly af									**
 * 			- change label to display score											**
 *  																				**
 * @author Samuel Klein																**
 * 																					**
 * ***********************************************************************************
 * ***********************************************************************************
 */
public class Tetris_Model {

	public int[][] board;
	// array where colors of blocks are stored
	public Color[][] colorBoard;
	
	public ArrayList<int[]> nextBlock;
	public ArrayList<int[]> currentBlock;
	public int rotation;
	public int score;
	public boolean isGameOver;
	public boolean isMuted;
	
	// colors for each block is stored
	public HashMap<Blocks, Color> colorRelations;
	
	public int countGeneratedBlocks;
	public int countCheats;
			
	Tetris_Runner runner;
	
	ArrayList<Integer> filledLines;
	
	public enum Blocks{
		SQUARE, LONG, LBLOCK, REVERSELBLOCK, ZBLOCK, REVERSEZBLOCK, TBLOCK;
	}
	
	public Blocks currentBlockENUM;
	public Blocks nextBlockENUM;
	
	// import sound track and set up player
	JFXPanel jfxpanel = new javafx.embed.swing.JFXPanel();
	// url to local sound files
	URL mainThemeDubstep = Tetris_Model.class.getClassLoader().getResource("SoundEffects/Tetris_MainTheme_Dubstep.wav");
	URL lostPath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_GameOver.wav");
	URL rotatePath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_PieceRotateLR.wav");
	URL rotateFailedPath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_PieceRotateFail.wav");
	URL dropPath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_PieceLockdown.wav");
	URL cheaterPath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/cheater_sound.wav");
	URL deletedRowPath = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_SpecialLineClearSingle.wav");
	URL deletedRow2Path = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_SpecialLineClearDouble.wav");
	URL deletedRow3Path = Tetris_Model.class.getClassLoader().getResource("SoundEffects/SFX_SpecialLineClearTriple.wav");
//	
//	URL tetrisFont = Tetris_Model.class.getClassLoader().getResource("Fonts/TETRIS.ttf");
	
	// media and player for different sounds
	// TODO: find better way to solve this problem without this many different Objects
//    Media mainThemeDubstepMedia = new Media(mainThemeDubstep.toString());
//    MediaPlayer mainThemeDubstepPlayer = new MediaPlayer(mainThemeDubstepMedia);
//    Media lostMedia = new Media(lostPath.toString());
//    MediaPlayer lostPlayer = new MediaPlayer(lostMedia);
//    Media rotateMedia = new Media(rotatePath.toString());
//    MediaPlayer rotatePlayer = new MediaPlayer(rotateMedia);
//    Media rotateFailedMedia = new Media(rotateFailedPath.toString());
//    MediaPlayer rotateFailedPlayer = new MediaPlayer(rotateFailedMedia);
//    Media dropMedia = new Media(dropPath.toString());
//    MediaPlayer dropPlayer = new MediaPlayer(dropMedia);
//    Media deletedMedia = new Media(deletedRowPath.toString());
//    MediaPlayer deletedPlayer = new MediaPlayer(deletedMedia);
//    Media deleted2Media = new Media(deletedRow2Path.toString());
//    MediaPlayer deleted2Player = new MediaPlayer(deleted2Media);
//    Media deleted3Media = new Media(deletedRow3Path.toString());
//    MediaPlayer deleted3Player = new MediaPlayer(deleted3Media);
//    Media cheaterMedia = new Media(cheaterPath.toString());
//    MediaPlayer cheaterPlayer = new MediaPlayer(cheaterMedia);
	AudioPlayer playerMainTheme = new AudioPlayer(mainThemeDubstep);
	AudioPlayer playerLost = new AudioPlayer(lostPath);
	AudioPlayer playerRotate = new AudioPlayer(rotatePath);
	AudioPlayer playerRotateFailed = new AudioPlayer(rotateFailedPath);
	AudioPlayer playerDrop = new AudioPlayer(dropPath);
	AudioPlayer playerCheater = new AudioPlayer(cheaterPath);
	AudioPlayer playerDeletedRow = new AudioPlayer(deletedRowPath);
	AudioPlayer playerDeletedRow2 = new AudioPlayer(deletedRow2Path);
	AudioPlayer playerDeletedRow3 = new AudioPlayer(deletedRow3Path);
	
	public Tetris_Model(Tetris_Runner runner){
		this.runner = runner;
	}
	
	/*
	 * initialize variables for first and every new game
	 */
	public void initVariables(){
		board = new int[18][10];
		colorBoard = new Color[18][10];
		
		nextBlock = new ArrayList<int[]>();
		currentBlock = new ArrayList<int[]>();
		rotation = 0;
		score = 0;
		isGameOver = false;
		isMuted = false;
		countGeneratedBlocks = 0;
		countCheats = 0;
		
		colorRelations = new HashMap<Blocks, Color>();
		colorRelations.put(Blocks.SQUARE, Color.GREEN);
		colorRelations.put(Blocks.LONG, Color.BLUE);
		colorRelations.put(Blocks.LBLOCK, Color.RED);
		colorRelations.put(Blocks.REVERSELBLOCK, Color.YELLOW);
		colorRelations.put(Blocks.ZBLOCK, Color.MAGENTA);
		colorRelations.put(Blocks.REVERSEZBLOCK, Color.ORANGE);
		colorRelations.put(Blocks.TBLOCK, Color.LIGHT_GRAY);
	}
	
	/*
	 * check if direction is possible
	 */
	public boolean checkIfLegalMove(String direction){
		for(int[] array : currentBlock){
			
			int x = array[0];
			int y = array[1];
			
			switch (direction) {
			case "left":
				if(array[1] == 0){
					return false;
				}
				if(board[x][y-1] == 1){
					return false;
				}
				
				break;
			case "right":
				if(array[1] == board[0].length-1){
					return false;
				}
				if(board[x][y+1] == 1){
					return false;
				}
				
				break;
			case "down":
				if(array[0] == board.length-1 || board[x+1][y] == 1){
					putCurrentBlockToBoard();
					checkIfGameOver();
					checkIfFilled();
					return false;
				}
				break;
			}
			
		}
		
		return true;
	}
	
	/*
	 * when block is blocked set it permanent and spawn new block
	 */
	public void putCurrentBlockToBoard(){	
		Color tmpColor = colorRelations.get(currentBlockENUM);
		
		for(int[] array : currentBlock){
			int x = array[0];
			int y = array[1];
			
			board[x][y] = 1;	
			colorBoard[x][y] = tmpColor; 
		}	
		
		spawnNewBlock();
		
		playDropSound();
	}
	
	/*
	 * move current block in given direction if legal
	 */
	public void moveCurrentBlock(String direction){
		
		boolean canMove = checkIfLegalMove(direction);
		
		if(canMove){
			for(int[] array : currentBlock){
				if(direction.equals("left")){
					array[1] -= 1;
				}
				if(direction.equals("right")){
					array[1] += 1;
				}
				if(direction.equals("down")){
					array[0] += 1;
				}
			}
		}
	}
	
	/*
	 * generates a random new block
	 */
	public void generateNewBlock(Blocks whichBlock){
		
		ArrayList<int[]> tmp = new ArrayList<>();
		
		int[] tmpArray;

		switch (whichBlock) {
		case SQUARE:
			// square block
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,5};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.SQUARE;
			
			break;
		case LONG:
			// long block
			tmpArray = new int[] {0,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,6};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.LONG;
			
			break;
		case LBLOCK:
			// L block
			tmpArray = new int[] {0,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,3};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.LBLOCK;
			
			break;
		case REVERSELBLOCK:
			// reversed L block
			tmpArray = new int[] {0,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,5};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.REVERSELBLOCK;
			
			break;
		case ZBLOCK:
			// Z block
			tmpArray = new int[] {0,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,5};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.ZBLOCK;
			
			break;
		case REVERSEZBLOCK:
			// reverse Z block
			tmpArray = new int[] {1,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.REVERSEZBLOCK;
			
			break;
		case TBLOCK:
			// T block
			tmpArray = new int[] {0,3};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,4};
			tmp.add(tmpArray);
			tmpArray = new int[] {0,5};
			tmp.add(tmpArray);
			tmpArray = new int[] {1,4};
			tmp.add(tmpArray);
			
			nextBlockENUM = Blocks.TBLOCK;
			
			break;
		}
		
		nextBlock = tmp;
		
	}
	
	/*
	 * generates new random block enum
	 */
	public Blocks getRandomBlock(){
		
		Random rand = new Random();
		int randomNum = rand.nextInt((6) + 1);

		return Blocks.values()[randomNum];
	}
	
	/*
	 * generate first block
	 */
	public void startSpawnBlock(){
		
		generateNewBlock(getRandomBlock());
	}
	
	/*
	 * spawn new block
	 */
	public void spawnNewBlock(){
		
		countGeneratedBlocks += 1;
		currentBlock = nextBlock;
		currentBlockENUM = nextBlockENUM;
		generateNewBlock(getRandomBlock());
		updateWindowNext();
		rotation = 0;
	}
	
	
	
	/*
	 * check if line is filled
	 */
	public void checkIfFilled(){
		
		filledLines = new ArrayList<Integer>();
		boolean flag = true;
		
		for (int i = 0; i < board.length; i++) {
			
			flag = true;
		
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j] == 0){
					flag = false;
				}
			}
			if(flag){
				filledLines.add(i);
			}
		}
		moveBlocksDown();
	}
	
	/*
	 * when line is filled remove it and let all upper blocks fall down
	 */
	public void moveBlocksDown(){
		
		Collections.sort(filledLines);
		score += filledLines.size();
		
		// different sounds for more lines
		switch (filledLines.size()) {
		case 1:
			playDeleteRowSound();
			break;
		case 2:
			playDeleteRowSound();
			break;
		case 3:
			playDeleteRow2Sound();
			break;
		case 4:
			playDeleteRow3Sound();
			break;
		}
				
		for(int i : filledLines){
			
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = 0;
			}
			for (int j = i-1; j >= 0; j--){
				for (int j2 = 0; j2 < board[j].length; j2++) {
					board[j+1][j2] = board[j][j2];
				}
			}
			for (int j = 0; j < board[0].length; j++) {
				board[0][j] = 0;
			}
			updateWindow();
		}
		updateWindow();
	}
	
	/*
	 * change next block to LONG for 5 points
	 */
	public void cheat(){
		
		if(score >= 5){
			countCheats += 1;
			score -= 5;
			generateNewBlock(Blocks.LONG);
			updateWindowNext();
			playCheaterSound();
		}
		
	}
	
	/*
	 * check if game is lost
	 */
	public boolean checkIfGameOver(){
		for (int i = 0; i < board[0].length; i++) {
			if(board[0][i] == 1){
				runner.endGame();
				return true;
			}
		}
		return false;
	}
	
	/*
	 * play sound track
	 */
	public void startSoundtrack(){
		// set player to infinite loop+

	    // who doesnt like dubstep?
//	    mainThemeDubstepPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//	    mainThemeDubstepPlayer.setVolume(0.15);
//	    mainThemeDubstepPlayer.play();
		playerMainTheme.playSound();
	}
	
	/*
	 * pause sound track
	 */
	public void pauseSoundtrack(){
		playerMainTheme.pauseSound();
	}
	
	/*
	 * play different sounds
	 */
	public void playLostSound(){
		if(!isMuted){
			playerLost.playSoundOnce();
		}
	}
	public void playDropSound(){
		if(!isMuted){
			playerDrop.playSoundOnce();
		}
	}
	public void playRotateSound(){
		if(!isMuted){
			playerRotate.playSoundOnce();
		}
	}
	public void playRotateFailSound(){
		if(!isMuted){
			playerRotateFailed.playSoundOnce();
		}
	}
	public void playDeleteRowSound(){
		if(!isMuted){
			playerDeletedRow.playSoundOnce();
		}
	}
	public void playDeleteRow2Sound(){
		if(!isMuted){
			playerDeletedRow2.playSoundOnce();
		}
	}
	public void playDeleteRow3Sound(){
		if(!isMuted){
			playerDeletedRow3.playSoundOnce();
		}
	}
	public void playCheaterSound(){
		if(!isMuted){
			playerCheater.playSoundOnce();
		}
	}
	/*
	 * mute all sounds
	 */
	public void muteSound(){
		
		isMuted = true;
		
		playerMainTheme.pauseSound();

	}
	/*
	 * unmute all sounds
	 */
	public void unmuteSound(){
		
		isMuted = false;
		
		playerMainTheme.resumeSound();
	}
	
	/*
	 * updates the window and board
	 */
	public void updateWindow(){
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				boolean flag = false;
				
				for(int[] a : currentBlock){
					if(a[0] == i && a[1] == j){
						runner.gui.labelArray[i][j].setBackground(colorRelations.get(currentBlockENUM));
						flag = true;
					}
				}
				if(!flag){
					if(board[i][j] == 0){
						runner.gui.labelArray[i][j].setBackground(runner.background);
					}else{
						runner.gui.labelArray[i][j].setBackground(colorBoard[i][j]);
					}
				}
			}
		}
		if(!isGameOver){
			
			if(isMuted){
				runner.gui.labelScore.setText("<html>"+score+"<br><a style='font-size:10px'>MUTED</a></html>");
			}else{
				runner.gui.labelScore.setText(String.valueOf(score));				
			}
			
		}
	}
	
	/*
	 * updates the label where the next block is shown
	 */
	public void updateWindowNext(){
		
		// temp variable where key of panel is stored
		String tmp;
		
		if(nextBlockENUM == Blocks.SQUARE){
			tmp = "1";
			for (int i = 0; i < runner.gui.nextArraySquare.length; i++) {
				for (int j = 0; j < runner.gui.nextArraySquare[i].length; j++) {
					runner.gui.nextArraySquare[i][j].setBackground(runner.background);
				}
			}			

			Color tmpColor = colorRelations.get(Blocks.SQUARE);			
			runner.gui.nextArraySquare[1][1].setBackground(tmpColor);
			runner.gui.nextArraySquare[1][2].setBackground(tmpColor);
			runner.gui.nextArraySquare[2][1].setBackground(tmpColor);
			runner.gui.nextArraySquare[2][2].setBackground(tmpColor);
			
		}else if(nextBlockENUM == Blocks.LONG){
			tmp = "2";
			for (int i = 0; i < runner.gui.nextArrayLong.length; i++) {
				for (int j = 0; j < runner.gui.nextArrayLong[i].length; j++) {
					runner.gui.nextArrayLong[i][j].setBackground(runner.background);
				}
			}			

			Color tmpColor = colorRelations.get(Blocks.LONG);
			runner.gui.nextArrayLong[2][1].setBackground(tmpColor);
			runner.gui.nextArrayLong[2][2].setBackground(tmpColor);
			runner.gui.nextArrayLong[2][3].setBackground(tmpColor);
			runner.gui.nextArrayLong[2][4].setBackground(tmpColor);

		}else{
			tmp = "0";
			for (int i = 0; i < runner.gui.nextArray.length; i++) {
				for (int j = 0; j < runner.gui.nextArray[i].length; j++) {
					
					runner.gui.nextArray[i][j].setBackground(runner.background);
					for(int[] a : nextBlock){
						if(a[0] == i-1 && a[1]-2 == j){
							runner.gui.nextArray[i][j].setBackground(colorRelations.get(nextBlockENUM));
						}
					}
				}
			}	
		}
		
		// switch between different panels
		CardLayout cl = (CardLayout)(runner.gui.panelNextBackground.getLayout());
		cl.show(runner.gui.panelNextBackground, tmp);
		
	}
	
	/*
	 * rotate current block
	 */
	public void rotateBlock(){

		ArrayList<int[]> tempList = new ArrayList<int[]>();
		int[] tempArray;
		
		// check if rotation is legal
		boolean flag = true;
		
		switch (currentBlockENUM) {
		
		case SQUARE:
			flag = false;
			break;
			
		case LONG:
			if(rotation == 0 || rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-2,currentBlock.get(3)[1]-2};
				tempList.add(tempArray);
			}
			else{
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+2,currentBlock.get(3)[1]+2};
				tempList.add(tempArray);
			}
			break;
			
		case LBLOCK:
			if(rotation == 0){
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}else if(rotation == 1){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]+2};
				tempList.add(tempArray);
			}else if(rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+2,currentBlock.get(3)[1]};
				tempList.add(tempArray);		
			}else{
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]-2};
				tempList.add(tempArray);
			}
			break;
			
		case REVERSELBLOCK:
			if(rotation == 0){
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]-2};
				tempList.add(tempArray);
			}else if(rotation == 1){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}else if(rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]+2};
				tempList.add(tempArray);
			}else{
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}	
			break;
			
		case ZBLOCK:
			if(rotation == 0){
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]-2};
				tempList.add(tempArray);
			}else if(rotation == 1){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}else if(rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]+2};
				tempList.add(tempArray);
			}else{
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}
			break;
			
		case REVERSEZBLOCK:
			if(rotation == 0){
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}else if(rotation == 1){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]-2};
				tempList.add(tempArray);
			}else if(rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-2,currentBlock.get(3)[1]};
				tempList.add(tempArray);
			}else{
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0],currentBlock.get(3)[1]+2};
				tempList.add(tempArray);
			}
			break;
			
		case TBLOCK:
			if(rotation == 0){
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-1,currentBlock.get(3)[1]-1};
				tempList.add(tempArray);
			}else if(rotation == 1){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]+1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]-1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]-1,currentBlock.get(3)[1]+1};
				tempList.add(tempArray);				
			}else if(rotation == 2){
				tempArray = new int[] {currentBlock.get(0)[0]+1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]-1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+1,currentBlock.get(3)[1]+1};
				tempList.add(tempArray);
			}else{	
				tempArray = new int[] {currentBlock.get(0)[0]-1,currentBlock.get(0)[1]-1};
				tempList.add(tempArray);
				tempList.add(currentBlock.get(1));
				tempArray = new int[] {currentBlock.get(2)[0]+1,currentBlock.get(2)[1]+1};
				tempList.add(tempArray);
				tempArray = new int[] {currentBlock.get(3)[0]+1,currentBlock.get(3)[1]-1};
				tempList.add(tempArray);	
			}
			break;
		}
		
		// check if rotation is possible
		for(int[] array : tempList){
			// block is at the edge of the board
			if(array[0] >= 0 && array[0] < board.length && array[1] >= 0 && array[1] < board[0].length){
				// block hits another existing block
				if(board[array[0]][array[1]] == 1){
					flag = false;
				}
			}else{
				flag = false;
			}
		}
		
		// if rotation is possible
		if(flag){
			// set rotated block as current block
			currentBlock = tempList;
			rotation += 1;
	    	rotation = rotation%4;
	    	playRotateSound();
		}else{
			playRotateFailSound();
		}
		
	}
	
	/*
	 * AudioPlayer for better game experience
	 */
	public class AudioPlayer{
		private Clip clip;
		
		public AudioPlayer(URL url){
			
			AudioInputStream in;
			try {
				
				in = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				clip.open(in);
				
				
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
		}
		
		public void playSound(){
			clip.loop(100);
		}
		
		public void stopSound(){
			clip.stop();
			clip.setFramePosition(0);
		}
		
		public void playSoundOnce(){
			clip.loop(0);
			clip.setFramePosition(0);
		}
		
		public void pauseSound(){
			clip.stop();
		}
		
		public void resumeSound(){
			clip.start();
		}
	}
	
	/*
	 * print board to console
	 * NOT USED ANYMORE
	 */
	public void printBoard(){
		
		// clear console
		for (int i = 0; i < 200; i++) {
			System.out.println();
		}
		
		// print board to console
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				boolean flag = false;
				
				for(int[] a : currentBlock){
					if(a[0] == i && a[1] == j){
						System.out.print("██");
						flag = true;
					}
				}
				if(!flag){
					if(board[i][j] == 0){
						System.out.print("░░");
					}else{
						System.out.print("██");
					}
				}
			}
			System.out.println();
		}
	}
	
}
