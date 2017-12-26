package com.Eudy.PacMan.GamePlay;

import java.awt.*;
import javax.swing.ImageIcon;

import com.Eudy.PacMan.Graphics.ScreenManager;

public abstract class GameCore {
	
	protected static final int FONT_SIZE = 24;
	
	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(800, 600, 16, 0),
			new DisplayMode(800, 600, 32, 0),
			new DisplayMode(800, 600, 24, 0),
			new DisplayMode(640, 480, 16, 0),
			new DisplayMode(640, 480, 32, 0),
			new DisplayMode(640, 480, 24, 0),
			new DisplayMode(1024, 768, 16, 0),
			new DisplayMode(1024, 768, 32, 0),
			new DisplayMode(1024, 768, 24, 0)
	};
	
	private boolean isRunning;
	private boolean isPaused;
	protected ScreenManager screen;
	
	public void stop(){
		isRunning = false;
	}
	
	public void run(){
		try{
			init();
			gameLoop();
		}finally{
			screen.restoreScreen();
			lazilyExit();
		}
	}
	
	public void lazilyExit(){
		Thread thread = new Thread(){
			public void run(){
				try{
					Thread.sleep(3000);
				}catch(InterruptedException ex){}
				System.exit(0);
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public void init(){
		screen = new ScreenManager();
		DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
		screen.setFullScreen(displayMode);
		
		Window window = screen.getFullScreenWindow();
		window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		window.setBackground(Color.black);
		window.setForeground(Color.white);
		
		isPaused = false;
		isRunning = true;
	}
	
	public Image loadImage(String fileName){
		return new ImageIcon(fileName).getImage();
	}
	
	public void gameLoop(){
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		while (isRunning){
			Graphics2D g = screen.getGraphics();
			
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			update(elapsedTime);
			draw(g);
			g.dispose();
			screen.update();
		}
	}
	
	public void update(long elapsedTime){
		//do nothing 
	}
	
	public abstract void checkInput(long elapsedTime);
	
	public abstract void draw(Graphics2D g);
	public abstract void message(Graphics2D g);

}
