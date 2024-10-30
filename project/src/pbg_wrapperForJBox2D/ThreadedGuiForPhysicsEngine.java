package pbg_wrapperForJBox2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ThreadedGuiForPhysicsEngine {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Modified by Moganaselvan Ramamoorthy
	 */
	
	public ThreadedGuiForPhysicsEngine() {
	}

	//Level data
	private static int currentLevelID = 1;
	private static int MaxNumberOfLevels = Constants.getMaxNumberOfLevels();
	
	//UI Components
	private static JButton jButton_go;
	private static JLabel levelIdLabel;

	//References
	private static Thread theThread;
	private static BasicView view;
	
	public static void main(String[] args) throws Exception {
		BasicPhysicsEngineUsingBox2D game = new BasicPhysicsEngineUsingBox2D (Constants.getDefaultLevel());
		view = new BasicView(game);
		JComponent mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(view, BorderLayout.CENTER);
		JPanel sidePanel=new JPanel();
		sidePanel.setLayout(new FlowLayout());
		jButton_go=new JButton("Go");
		//sidePanel.add(jButton_go);
		mainPanel.add(sidePanel, BorderLayout.NORTH);
		// add any new buttons or textfields to side panel here...

		JComponent topPanel=new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("Game"));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		topPanel.add(new JLabel(" | "));
		
		levelIdLabel = new JLabel();
		//levelIdLabel.setPreferredSize(new Dimension(50, 24));
		
		JButton previousLevelButton = new JButton("<");
		topPanel.add(previousLevelButton);
		
		topPanel.add(levelIdLabel);
		updateLevelIDField();
		
		JButton nextLevelButton = new JButton(">");
		topPanel.add(nextLevelButton);
		
		topPanel.add(jButton_go);
		
		JEasyFrame frame = new JEasyFrame(mainPanel, "Game");
		
		if(Constants.AUTO_MAXIMIZE_WINDOW)
		{			
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}
		
		view.addKeyListener(new InputListener());

		ActionListener listener=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==jButton_go) {
					startGame(view);
				}
			}
		};
		
		jButton_go.addActionListener(listener);
		
		previousLevelButton.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource()==previousLevelButton) {
							handlePreviousLevelButtonClicked();						}
					}
				});
		
		nextLevelButton.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource()==nextLevelButton) {
							handleNextLevelButtonClicked();						}
					}
				});
		
	}
	
	private static void startGame(BasicView view)
	{
		try {
			
			if(currentLevelID < 1 || currentLevelID > MaxNumberOfLevels)
			{
				return;
			}
			
			// recreate all particles in their original positions:
			final BasicPhysicsEngineUsingBox2D game2 = new BasicPhysicsEngineUsingBox2D (Constants.getLevel(currentLevelID));
			// Tell the view object to start displaying this new Physics engine instead:
			view.updateGame(game2);
			view.requestFocus();// needed for keyboard listener to work - it would be
			// better off to rewrite using Swing's "Key Bindings" apparently as this
			// will remove the need for focus.
			//
			startThread(game2, view); // start a new thread for the new game object:
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void startThread(final BasicPhysicsEngineUsingBox2D game, final BasicView view) throws InterruptedException {
	    Runnable r = new Runnable() {
	         public void run() {
	        	 
	        	view.initializeBasicView();
	        	// this while loop will exit any time this method is called for a second time, because 
	    		while (theThread==Thread.currentThread()) {
    				game.update();
    				view.repaint();
    				Toolkit.getDefaultToolkit().sync();
	    			try {
						Thread.sleep(BasicPhysicsEngineUsingBox2D.DELAY);
					} catch (InterruptedException e) {
					}
	    		}
	         }
	     };

	     theThread=new Thread(r);// this will cause any old threads running to self-terminate
	     theThread.start();
	}
	
	
	private static void handlePreviousLevelButtonClicked()
	{
		if(currentLevelID > 1)
		{
			currentLevelID -= 1;
			updateLevelIDField();
		}		
	}
	
	
	private static void handleNextLevelButtonClicked()
	{
		if(currentLevelID < MaxNumberOfLevels)
		{
			currentLevelID += 1;
			updateLevelIDField();
		}	
	}

	private static void updateLevelIDField()
	{
		levelIdLabel.setText("Level : " + currentLevelID + " / " + MaxNumberOfLevels);
	}
	
	public static void RestartLevel()
	{
		System.out.println("Restarting level : " + currentLevelID);
		startGame(view);
	}
	
	public static void HandleBallDied()
	{
		JOptionPane.showMessageDialog(null, "You lose! Restarting level..");	
		RestartLevel();
	}
	
	public static void HandleBallCompletedLevel()
	{
		if(currentLevelID < MaxNumberOfLevels)
		{
			JOptionPane.showMessageDialog(null, "Level complete!");	
			
			currentLevelID += 1;
			updateLevelIDField();
			startGame(view);
		}
		else {
			JOptionPane.showMessageDialog(null, "Game over, Thanks for playing!");
			System.exit(0);
		}
	}
}


