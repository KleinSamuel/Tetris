import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;


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
 * 	RUNNER CLASS																	**
 *  																				**
 * @author Samuel Klein																**
 * 																					**
 * ***********************************************************************************
 * ***********************************************************************************
 */
public class Tetris_Runner {
	
	// timer is used to move blocks down
	Timer timer;
	// timer task is needed for timer
	TimerTask task;
	// boolean value used to control user input
	boolean paused;
	// delay rate is time before timer refreshes for the first time
	int DELAY_RATE = 1000;
	// refresh rate is time between the timer refreshes
	int REFRESH_RATE = 500;
	// instance of other classes from MVC Model
	Tetris_Model model;
	Tetris_GUI gui = new Tetris_GUI(this);
	// set colors
	Color background = Color.BLACK;
	Color foreground = Color.CYAN;
	Color blocks = Color.DARK_GRAY;
	Color restartButtonColor = Color.WHITE;
    
	/*
	 * used when game is first executed
	 */
	public void startGame(){
		paused = false;
		model = new Tetris_Model(this);
		model.initVariables();
		gui.initGUI();
		model.updateWindow();
		model.startSpawnBlock();
		model.spawnNewBlock();
		model.startSoundtrack();
		resumeGame();
	}
	
	/*
	 * generates a task for the timer
	 * run method is called in timer intervals
	 */
	public void generateTask(){
		task = new TimerTask(){
			public void run() {
				model.moveCurrentBlock("down");
				model.updateWindow();
			}
		};
	}
	
	/*
	 * used to pause the timer
	 */
	public void pauseGame(){
		model.pauseSoundtrack();
		this.timer.cancel();
	}
	
	/*
	 * used to resume the timer
	 */
	public void resumeGame(){
		model.startSoundtrack();
		this.timer = new Timer();
		generateTask();
		this.timer.schedule(task, DELAY_RATE, REFRESH_RATE);
	}
	
	/*
	 * used when game is lost
	 */
	public void endGame(){
		model.pauseSoundtrack();
		model.playLostSound();
		this.timer.cancel();
		model.isGameOver = true;
		paused = true;
		gui.frame.removeKeyListener(gui);
		gui.labelScore.setFont(gui.fontSmall);
		this.gui.labelScore.setText("<html>Score:\t\t"+
				model.score+"<br>Blocks:\t"+model.countGeneratedBlocks+
				"<br>Cheats:\t"+model.countCheats+"</html>");
	}
	
	
	/*
	 *used when restart button is pressed
	 */
	public void newGame(boolean isMuted){
		
		if(model.isGameOver){
			gui.frame.addKeyListener(gui);	
		}
		
		model.initVariables();
		model.isMuted = isMuted;
		gui.labelScore.setFont(gui.fontScore);

		if(model.isMuted){
			gui.labelScore.setText("<html>"+model.score+"<br><a style='font-size:10px'>MUTED</a></html>");
		}else{
			gui.labelScore.setText(String.valueOf(model.score));				
		}
		
		pauseGame();
		paused = false;
		resumeGame();
		model.startSpawnBlock();
		model.spawnNewBlock();
		model.updateWindow();
	}
	
	
	public static void main(String[] args) {
		
		Tetris_Runner runner = new Tetris_Runner();
		runner.startGame();
		
	}

}
