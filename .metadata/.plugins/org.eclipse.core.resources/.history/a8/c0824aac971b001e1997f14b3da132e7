package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
public class GameView extends JFrame implements KeyListener {
	    private ImageIcon firstBackground;
	    private JLabel firstBackgroundLabel;
	    private JButton startButton;
	    private JPanel selectingChampions ;
	    private ArrayList<JButton> chToBeSelected ;
	    private JTextArea playersSelectedChampions; 
	    private JPanel tutorial;
	    private JButton continue1;
	    private JPanel mainView;	    
	    private JTextArea leftDetails;
	    private JTextArea rightDetails;
	    private JButton[][] viewGrid;
	    private JScrollPane scrollBar2;
	    private GameViewListener listener ;
	   // private JPanel winningPanel;
	    private JTextArea winnerTextArea;
	    
	    
	    
	    
	    public JTextArea getWinnerTextArea() {
			return winnerTextArea;
		}

		

		public void setListener(GameViewListener listener) {
			this.listener = listener;
		}

		public JScrollPane getScrollBar2() {
			return scrollBar2;
		}

		public JButton[][] getViewGrid() {
			return viewGrid;
		}

		public JPanel getMainView() {
			return mainView;
		}

		public JTextArea getLeftDetails() {
			return leftDetails;
		}

		public JTextArea getRightDetails() {
			return rightDetails;
		}

	    
	public GameView() {
		
		
		this.setDefaultCloseOperation(3);
		this.setBounds(0, 0, 1000, 1000);
		this.setVisible(true);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setTitle("Marvel Game");
		this.getContentPane().setBackground(Color.BLACK);
		//this.setResizable(false);
		
		//first background
		firstBackground = new ImageIcon("marvelStartBackground.JPEG");
	    Image marvelImg = firstBackground.getImage();
	    this.setIconImage(marvelImg);
	    Image Modifiedimg = marvelImg.getScaledInstance(this.getWidth(), this.getHeight()-200, 1);
	    firstBackground = new ImageIcon(Modifiedimg);
	    firstBackgroundLabel = new JLabel(firstBackground);
	    firstBackgroundLabel.setVisible(true);
	    this.add(firstBackgroundLabel);
	    
	    
	    startButton = new JButton("START !");
	    startButton.setActionCommand("start");
	    startButton.setBounds(700,720,100,50);
	    startButton.setVisible(true);
	    startButton.setFocusable(false);
	    firstBackgroundLabel.add(startButton);
	    
	    this.addKeyListener(this);
	    
	    //this.add(tutorial);
	    
	    mainView = new JPanel();
	    mainView.setVisible(false);
	    mainView.setLayout(new GridLayout(5,5));
	    leftDetails = new JTextArea();
	    leftDetails.setEditable(false);
	    leftDetails.setFocusable(false);
	    rightDetails = new JTextArea();
	    rightDetails.setEditable(false);
	    rightDetails.setFocusable(false);
	    viewGrid = new JButton[5][5];
	    //this.add(mainView) ;
	    
	    
	    
	    
	    
	    //this.pack();
	    this.revalidate();
	    this.repaint();
	}
	
	public JButton getContinue1() {
		return continue1;
	}

	public JPanel getTutorial() {
		return tutorial;
	}

	public JTextArea getPlayersSelectedChampions() {
		return playersSelectedChampions;
	}

	public ArrayList<JButton> getChToBeSelected() {
		return chToBeSelected;
	}
	
	public void selectChampions() {
		getFirstBackgroundLabel().setVisible(false);
		selectingChampions = new JPanel() ;
		selectingChampions.setLayout(new GridLayout(3,5)); 
		chToBeSelected = new ArrayList<>() ;
		
		this.add(selectingChampions,BorderLayout.CENTER);
		selectingChampions.setVisible(true);
		
		
		
		playersSelectedChampions = new JTextArea();
		playersSelectedChampions.setFocusable(false);
		this.add(playersSelectedChampions,BorderLayout.EAST);
		playersSelectedChampions.setBackground(Color.WHITE);
		playersSelectedChampions.setSize(300,300) ;
		playersSelectedChampions.setVisible(true) ;
		
		this.revalidate();
		this.repaint();
		
	}
	public void prepareTutorial() {
		this.tutorial = new JPanel();
	    this.tutorial.setVisible(true);
	    this.tutorial.setLayout( new BorderLayout());
	    this.continue1 = new JButton();
	    continue1.setText("Start Game!");
	    continue1.setActionCommand("Continue1") ;
	    continue1.setFocusable(false);
	    continue1.setBounds(700,720,100,50) ;
	    continue1.setVisible(true);
	    tutorial.add(continue1);
	    ImageIcon tutImgIc = new ImageIcon("tut pic.jpg"); 
	    Image tutImg = tutImgIc.getImage();
	    tutImg.getScaledInstance(this.getWidth(), this.getHeight()-200, 1);
	    tutImgIc = new ImageIcon(tutImg);
	    JLabel toBeAdded = new JLabel(tutImgIc);
	    tutorial.add(toBeAdded);
	    this.add(this.getTutorial() ) ;
		this.getTutorial().repaint();
		this.getTutorial().revalidate();
	}
	
	public void prepareWinningPanel() {
		   
			mainView.setVisible(false) ;
			leftDetails.setVisible(false);
			rightDetails.setVisible(false);
		    //winningPanel = new JPanel();
		   // winningPanel.setVisible(true);
		   // winningPanel.setFocusable(false);
		    winnerTextArea = new JTextArea();
		    winnerTextArea.setVisible(true);
		    winnerTextArea.setFocusable(false);
		    winnerTextArea.setBackground(Color.GREEN);
		    this.add(winnerTextArea,BorderLayout.CENTER);
		    
		    
	}
	
	public ImageIcon getFirstBackground() {
		return firstBackground;
	}

	public JLabel getFirstBackgroundLabel() {
		return firstBackgroundLabel;
	}

	public JButton getStartButton() {
		return startButton;
	}

	public JPanel getSelectingChampions() {
		return selectingChampions;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODOt Auto-generated method stub
		//System.out.println("I reached GameView");
		if(listener != null ) {
			listener.onPressed(e); 
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("I'm now here!!!!!!!");
		/*if(listener != null ) {
			listener.onPressed(e); 
		}*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("I'm now here!!!!!!!!!!!!!!!!!!!!");
		/*if(listener != null ) {
			listener.onPressed(e); 
		}*/
	}
	
		
	/*public static void main(String[] args) {
		new GameView();
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("START !")) {
			JOptionPane.showInputDialog(null,"please enter your name");
		}
		
	}*/
	
	
	
	
	
	/*public class quizView  extends JFrame implements ActionListener{
		private JPanel mainView;
		private JButton Upleft;
		private JButton Upright;
		private JButton Downleft;
		private JButton Downright;
		//private JTextArea Text;
		private Game model;
	public quizView()  {
		        this.setTitle("quiz");
				this.setDefaultCloseOperation(3);
				this.setBounds(0,0,1500,3000);
				this.setVisible(true);
				this.setLayout(new BorderLayout());
				
				mainView = new JPanel();
				//mainView.setPreferredSize(new Dimension(1100,3000));
			    mainView.setLayout(new GridLayout(2,2));
			    this.add(mainView, BorderLayout.CENTER);
			    
			    //Text = new JTextArea();
			    //Text.setPreferredSize(new Dimension(400,3000));
			    //Text.setText("Hello World");
			    //this.add(Text, BorderLayout.EAST);
			    
			    Player k = new Player("Ahmed");
			    Player b = new Player("Ali");
			    model = new Game(k,b);
			    try {
					Game.loadAbilities("Abilities.csv");
				} catch (IOException e) {
					e.printStackTrace();
				}
			    System.out.print(model.getAvailableAbilities().size());
				int r = (int) (Math.random()*(model.getAvailableAbilities().size()));
			    Ability a= model.getAvailableAbilities().get(r);
			    String abilityName = a.getName();			    
			    Upleft = new JButton(abilityName);
			    mainView.add(Upleft);
			    
			    if(a instanceof CrowdControlAbility) 
			        Upright = new JButton("CrowdControl");
			    if(a instanceof DamagingAbility) 
			        Upright = new JButton("Damaging");
			    if(a instanceof HealingAbility) 
			        Upright = new JButton("Healing");
			    mainView.add(Upright);
			    
			    
			    Downleft = new JButton(""+r);
			    mainView.add(Downleft);
			    
			    Downright = new JButton("Next");
			    //Downright.setActionCommand("next");
			    Downright.addActionListener(this);
			    mainView.add(Downright);
			    			    
				this.revalidate();
				this.repaint();
		    }
	    public void actionPerformed(ActionEvent e)  {	
	    	JButton b = (JButton) e.getSource();
	    	if(b==Downright){
	    	        updateview();    		
	    	}
	    	//if(e.getActionCommand()=="next") {
	    		//updateview();
	    	//}
	    }
	    
	    public void updateview() {
	    	int r = (int) (Math.random()*(model.getAvailableAbilities().size()));
		    Ability a= model.getAvailableAbilities().get(r);
		    String abilityName = a.getName();			    
		    Upleft.setText(abilityName);
		   	    
		    if(a instanceof CrowdControlAbility) 
		        Upright.setText("CrowdControl");
		    if(a instanceof DamagingAbility) 
		        Upright.setText("Damaging");
		    if(a instanceof HealingAbility) 
		        Upright.setText("Healing");
		    	    
		    Downleft.setText(""+r);
		   
		    //Text.setText(" "+r);
		 	    
			this.revalidate();
			this.repaint();
	    	
	    	
	    }
	 			
		
		
		
		
		public static void main(String[] args) throws IOException {
			new quizView();
			
		}
}*/
	
	
}
