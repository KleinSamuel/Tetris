import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
 * 	VIEW CLASS																		**
 *  																				**
 * @author Samuel Klein																**
 * 																					**
 * ***********************************************************************************
 * ***********************************************************************************
 */

public class Tetris_GUI implements KeyListener, ActionListener, MouseListener{
	
	// main frame
	public JFrame frame;
	// panels are like container where other parts are placed in
	public JPanel panelBack, panelBoard, panelStats, panelNextBackground, panelNext, panelNextSquare, panelNextLong, panelScore, panelButtons;
	// labels where score and info are displayed
	public JLabel labelScore, labelInfo;
	// label array for big board where game is played
	public JLabel[][] labelArray;
	// label array for small board where next block is shown
	public JLabel[][] nextArray = new JLabel[4][5];
	public JLabel[][] nextArraySquare = new JLabel[4][4];
	public JLabel[][] nextArrayLong	= new JLabel[5][6];
	// instance of runner object
	public Tetris_Runner runner;
	// different fonts
	public Font fontBig, fontSmall, fontButton, fontScore, fontInfo;
	// restart button
	public JButton restartButton;
	
	/*
	 * constructor receives instance of runner
	 */
	public Tetris_GUI(Tetris_Runner runner){
		this.runner = runner;
	}
	
	/*
	 * build window
	 */
	public void initGUI(){ 
		// create instance of main frame
		frame = new JFrame("TETRIS");
		// set exit operation when "X" button on window is clicked
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create a border for the label array
		Border borderBack = BorderFactory.createLoweredBevelBorder();
		Border borderBevel = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		
		// create different fonts
		fontInfo = new Font("Verdana", Font.PLAIN, 15);
		fontScore = new Font(Font.SANS_SERIF, Font.PLAIN, 150);
		fontButton  = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
		fontSmall = new Font(Font.SANS_SERIF, Font.ITALIC, 40);

		// create instance of panels
		// border layout is used to put content to defined position
		panelBack = new JPanel(new BorderLayout());
		panelScore = new JPanel(new BorderLayout());
		panelButtons = new JPanel(new BorderLayout());
		panelStats = new JPanel(new BorderLayout());
		// card layout is used to display different panels and switch between them
		panelNextBackground = new JPanel(new CardLayout());
		// grid layout is used to split panel into many pieces of the same size
		panelBoard = new JPanel(new GridLayout(18, 0));
		panelNext = new JPanel(new GridLayout(4,5));
		panelNextSquare = new JPanel(new GridLayout(4,4));
		panelNextLong = new JPanel(new GridLayout(5,6));
		
		// add border to each panel
		panelBack.setBorder(borderBack);
		panelBoard.setBorder(borderBack);
		panelStats.setBorder(borderBack);

		// set size of each panel
		panelBoard.setPreferredSize(new Dimension(500, 800));
		panelNextBackground.setPreferredSize(new Dimension(300, 240));
		panelScore.setPreferredSize(new Dimension(300, 300));
		panelButtons.setPreferredSize(new Dimension(300, 200));
		
		// initialize label array for small window
		for (int i = 0; i < nextArray.length; i++) {
			for (int j = 0; j < nextArray[i].length; j++) {
				nextArray[i][j] = new JLabel();
				nextArray[i][j].setBackground(runner.background);
				nextArray[i][j].setOpaque(true);
				nextArray[i][j].setBorder(borderBevel);
				panelNext.add(nextArray[i][j]);
			}
		}
		for (int i = 0; i < nextArraySquare.length; i++) {
			for (int j = 0; j < nextArraySquare[i].length; j++) {
				nextArraySquare[i][j] = new JLabel();
				nextArraySquare[i][j].setBackground(runner.background);
				nextArraySquare[i][j].setOpaque(true);
				nextArraySquare[i][j].setBorder(borderBevel);
				panelNextSquare.add(nextArraySquare[i][j]);
			}
		}
		for (int i = 0; i < nextArrayLong.length; i++) {
			for (int j = 0; j < nextArrayLong[i].length; j++) {
				nextArrayLong[i][j] = new JLabel();
				nextArrayLong[i][j].setBackground(runner.background);
				nextArrayLong[i][j].setOpaque(true);
				nextArrayLong[i][j].setBorder(borderBevel);
				panelNextLong.add(nextArrayLong[i][j]);
			}
		}
		
		// different panels are added to main next panel with a key
		panelNextBackground.add(panelNext, "0");
		panelNextBackground.add(panelNextSquare, "1");
		panelNextBackground.add(panelNextLong, "2");
		
		// initialize score label with 0
		labelScore = new JLabel("0");
		labelScore.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE));
		labelScore.setForeground(Color.WHITE);
		labelScore.setBackground(Color.BLACK);
		labelScore.setOpaque(true);
		labelScore.setHorizontalAlignment(JLabel.CENTER);
		labelScore.setVerticalAlignment(JLabel.CENTER);
		labelScore.setFont(fontScore);
		
		// info text for the info label
		String infoText = "<html> - Press SPACEBAR for pause.<br>"+
				" - You get a point for every deleted row.<br>"+
				" - Change next block to a long one for 5 points by pressing C.<br>"+
				" - Mute sound by pressing M</html>";
		
		// label to display information
		labelInfo = new JLabel(infoText);
		labelInfo.setFont(fontInfo);
		labelInfo.setForeground(runner.foreground);
		labelInfo.setBackground(runner.background);
		labelInfo.setOpaque(true);

		// restart button
		restartButton = new JButton("RESTART");
		// change mouse cursor to hand for better visualization
		restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// no focus for the restart button so key listener is not ignored
		restartButton.setFocusable(false);
		// style button
		restartButton.setForeground(runner.foreground);
		restartButton.setBackground(runner.background);
		restartButton.setPreferredSize(new Dimension(300, 50));
		restartButton.setOpaque(true);
		restartButton.setFont(fontButton);
		Border line = new LineBorder(runner.foreground);
		Border margin = new EmptyBorder(0, 0, 0, 0);
		Border compound = new CompoundBorder(line, margin);
		restartButton.setBorder(compound);
		
		// create instance of main board where game is played
		labelArray = new JLabel[18][10];
		
		// initialize label array for big window
		for (int i = 0; i < labelArray.length; i++) {
			for (int j = 0; j < labelArray[i].length; j++) {
				labelArray[i][j] = new JLabel();
				labelArray[i][j].setBackground(runner.background);
				labelArray[i][j].setOpaque(true);
				labelArray[i][j].setBorder(borderBevel);
				panelBoard.add(labelArray[i][j]);
			}
		}
		
		// add all container from small to biggest
		panelScore.add(labelScore, BorderLayout.CENTER);
		panelButtons.add(restartButton, BorderLayout.CENTER);
		panelButtons.add(labelInfo, BorderLayout.SOUTH);
		
		panelStats.add(panelNextBackground, BorderLayout.NORTH);
		panelStats.add(panelScore, BorderLayout.CENTER);
		panelStats.add(panelButtons, BorderLayout.SOUTH);

		panelBack.add(panelBoard, BorderLayout.WEST);
		panelBack.add(panelStats, BorderLayout.EAST);
		
		frame.add(panelBack);
		
		// add key listener to frame
		frame.addKeyListener(this);
		// add action listener to restart button
		restartButton.addActionListener(this);
		restartButton.addMouseListener(this);
		
		// no focus on button because full focus on frame is needed for key listener
		frame.setFocusable(true);
	    frame.requestFocus();
	    // is used to format the main frame
	    frame.pack();
	    // user is not allowed to resize the frame so everything is in correct format
	    frame.setResizable(false);
	    // set frame visible
		frame.setVisible(true);
	}

	/*
	 * handle key actions as move, rotate, pause and mute
	 */
	public void keyPressed(KeyEvent e) {
		
		// get key code for better comparison with user input
		int keyCode = e.getKeyCode();
		
		// do not accept user input when game is paused
		if(!runner.paused){
		    switch( keyCode ) {
		    	case KeyEvent.VK_UP:
		    		// rotate block
		        	runner.model.rotateBlock();
		        	runner.model.updateWindow();
		            break;
		        case KeyEvent.VK_DOWN:
		        	// move block down
		        	runner.model.moveCurrentBlock("down");
		        	runner.model.updateWindow();
		            break;
		        case KeyEvent.VK_LEFT:
		        	// move block to the left
		            runner.model.moveCurrentBlock("left");
		            runner.model.updateWindow();
		            break;
		        case KeyEvent.VK_RIGHT :
		        	// move block to the right
		        	runner.model.moveCurrentBlock("right");
		        	runner.model.updateWindow();
		            break;
		        case KeyEvent.VK_SPACE:
		        	// pause game
	        		runner.pauseGame();
	        		labelScore.setFont(fontSmall);
	        		labelScore.setText("PAUSED");
		        	runner.paused = !runner.paused;
		        	break;
		        case KeyEvent.VK_C:
		        	// use cheat
		        	runner.model.cheat();
		        	break;
		        case KeyEvent.VK_M:
		        	// mute sound
		        	if(runner.model.isMuted){
		        		runner.model.unmuteSound();
		        	}else{
		        		runner.model.muteSound();
		        	}
		        	break;
		    }
		}
		// only accept space key input when game is paused
		else{
			if(keyCode == KeyEvent.VK_SPACE){
				// resume game
				runner.resumeGame();
				labelScore.setText(String.valueOf(runner.model.score));
        		labelScore.setFont(fontScore);
        		runner.paused = !runner.paused;
			}
		}
	}
	
	/*
	 * handles restart button
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == restartButton){
			runner.newGame(runner.model.isMuted);
		}
	}
	
	/*
	 * used to create hover effect on restart button
	 */
	public void mouseEntered(MouseEvent e) {
		restartButton.setBackground(runner.blocks);
	}
	public void mouseExited(MouseEvent e) {
		restartButton.setBackground(runner.background);
	}
	/*
	 * methods are not used yet
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
