package Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import views.GameView;
import views.GameViewListener;
//import engine.* ;
import model.abilities.*;
import model.effects.*;
import model.world.*;

public class Controller  implements ActionListener,AdjustmentListener, GameViewListener/*,KeyListener */ {
	
	private Player modelPlayer1 ;
	private Player modelPlayer2 ;
	private Game model;
	private GameView view;
	private ArrayList<String>imageStrings ;
	private ArrayList<String>selectingChampions;
	private int cntSelectedChampions;
	private boolean showDetails;
	private boolean checkCanPressKey ;
	private PriorityQueue alreadyPlayedChampionsPQ ;
	private JPanel comboBoxPanel;
	private DefaultComboBoxModel defaultComboBoxModel;
	private JComboBox comboBox;
	private Ability selectedAbilityToBeCasted ;
	
	
	
	public Ability getSelectedAbilityToBeCasted() {
		return selectedAbilityToBeCasted;
	}
	public JPanel getComboBoxPanel() {
		return comboBoxPanel;
	}
	public DefaultComboBoxModel getDefaultComboBoxModel() {
		return defaultComboBoxModel;
	}
	public JComboBox getComboBox() {
		return comboBox;
	}
	
	/*private ArrayList<Ability> controllerAvailableAbilities ;
	private ArrayList<Champion> controllerAvailableChampions ;*/
	
	
	
	public PriorityQueue getAlreadyPlayedChampionsPQ() {
		return alreadyPlayedChampionsPQ;
	}
	public boolean isShowDetails() {
		return showDetails;
	}
	public boolean isCheckCanPressKey() {
		return checkCanPressKey;
	}
	public void setCheckCanPressKey(boolean checkCanPressKey) {
		this.checkCanPressKey = checkCanPressKey;
	}
	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}
	public void display() {
		Object [] [] board = model.getBoard() ;
		for(int i=0 ; i< 5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				System.out.print( board[i][j] + "   " );
			}
			System.out.println();
		}
		
	}
	public ArrayList<String> getImageStrings() {
		return imageStrings;
	}
	
	public int getCntSelectedChampions() {
		return cntSelectedChampions;
	}

	public void setCntSelectedChampions(int cntSelectedChampions) {
		this.cntSelectedChampions = cntSelectedChampions;
	}
	
	public Player getModelPlayer1() {
		return modelPlayer1;
	}
	public Player getModelPlayer2() {
		return modelPlayer2;
	}
	public Controller() {
		//model = new Game();
		cntSelectedChampions = 0 ;
		imageStrings = new ArrayList<>() ;
		loadImages();
		selectingChampions = new ArrayList<>() ;	
		view = new GameView();
		view.getStartButton().addActionListener(this);
		showDetails = true;
		checkCanPressKey = false ;
		view.setListener(this);
		alreadyPlayedChampionsPQ = new PriorityQueue(6) ;
		
		
		//this.addKeyListener(this);
	}
	public void loadImages() {
		getImageStrings().add("Captain America.png");
		getImageStrings().add("Deadpool.png");
		getImageStrings().add("Dr Strange.png");
		getImageStrings().add("Electro.png");
		getImageStrings().add("Ghost Rider.png");
		getImageStrings().add("Hela.png");
		getImageStrings().add("Hulk.png");
		getImageStrings().add("Iceman.png");
		getImageStrings().add("Ironman.png");
		getImageStrings().add("Loki.png");
		getImageStrings().add("Quicksilver.png");
		getImageStrings().add("Spiderman.png");
		getImageStrings().add("Thor.png");
		getImageStrings().add("Venom.png");
		getImageStrings().add("Yellow Jacket.png");
	}
	public void loadSelectingChampionsMessages() {
		String name1 = this.getModelPlayer1().getName() ;
		String name2 = this.getModelPlayer2().getName() ;
		selectingChampions.add("Select " + name1 + "'s leader");
		selectingChampions.add("Select " + name2 + "'s leader");
		selectingChampions.add("Select " + name1 + "'s Champion");
		selectingChampions.add("Select " + name2 + "'s Champion");
		selectingChampions.add("Select " + name1 + "'s Champion");
		selectingChampions.add("Select " + name2 + "'s Champion");
	}
	
	public void updateViewGrid() {
		Object [][] gameBoard = model.getBoard() ; 
		JButton [][] viewBoard = view.getViewGrid() ;
		JPanel toBeEmptied = view.getMainView() ;
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				JButton toBeRemoved = viewBoard[i][j] ;
				if(toBeRemoved!=null) {
					toBeEmptied.remove(toBeRemoved);
				}
			}
		}
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				Object cur = gameBoard[i][j] ;
				JButton b = new JButton() ;
				if(cur == null) {
					b.setActionCommand("emptyCell");
				}
				else {
					if(cur instanceof Cover) {
						b.setActionCommand("CoverGrid");
						ImageIcon imIc = new ImageIcon("Cover.png");
						Image img = imIc.getImage() ;
						Image modiefiedPic = img.getScaledInstance(100, 100, 1);
						imIc = new ImageIcon(modiefiedPic) ;
						b.setIcon(imIc);
						b.setText("currentHP= " + ((Cover)cur).getCurrentHP()) ;
						b.setVerticalTextPosition(b.BOTTOM);
						b.setHorizontalTextPosition(b.CENTER);
						b.setIconTextGap(10);
					}
					else {
						if(cur instanceof Champion) {
							//System.out.println("I'm here");
							b.setActionCommand("ChampionGrid");
							int index = model.getAvailableChampions().indexOf((Champion)cur) ;
							String imgStr = getImageStrings().get(index) ;
							ImageIcon imIc = new ImageIcon(imgStr);
							Image img = imIc.getImage() ;
							Image modiefiedPic = img.getScaledInstance(100, 100, 1);
							imIc = new ImageIcon(modiefiedPic) ;
							b.setIcon(imIc);
							String text = "<html>"+((Champion)cur).getName() + "<br>";
							text+= "CurrentHp = " +((Champion)cur).getCurrentHP()+"</html>" ;
							b.setText(text) ;
							b.setFont(new Font("Arial",Font.CENTER_BASELINE,10));
							b.setVerticalTextPosition(b.BOTTOM);
							b.setHorizontalTextPosition(b.CENTER);
							b.setIconTextGap(10);
							Border bo ;
							if(model.getFirstPlayer().getTeam().contains(cur)) {
								bo = BorderFactory.createLineBorder(Color.BLUE, 3) ;
							}
							else {
								bo = BorderFactory.createLineBorder(Color.RED, 3) ;
							}
							b.setBorder(bo);
							if(cur.equals(model.getCurrentChampion())) {
								b.setBackground(Color.GREEN);
							}
						}
					}
				}
				viewBoard[4-i][j] = b ;
				b.addActionListener(this);
				b.setFocusable(false);
			}
		}
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				view.getMainView().add(viewBoard[i][j]) ;
			}
		}
		view.add(view.getMainView()) ;
		
		if(model.checkGameOver() != null) {
			Player winnerP = model.checkGameOver();
			String name = winnerP.getName();
			view.prepareWinningPanel();
			
			String res = "" ;
			res += "\n"+"\n"+"\n"+"\n" ;
			res += "                Congratulations, "+name +"\n"+"\n";
			res += "                       You Won !!!!!";
			view.getWinnerTextArea().setText(res);
			view.getWinnerTextArea().setFont(new Font("MV Boli",Font.ITALIC,50));
		}
	}
	/*public void updateViewGrid() {
		Object [][] gameBoard = model.getBoard() ; 
		JButton [][] viewBoard = view.getViewGrid() ;
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				Object cur = gameBoard[i][j] ;
				JButton b = viewBoard[i][j] ;
				if(cur == null) {
					b.setActionCommand("emptyCell");
					//b.set
				}
				else {
					if(cur instanceof Cover) {
						b.setActionCommand("CoverGrid");
						ImageIcon imIc = new ImageIcon("Cover.png");
						Image img = imIc.getImage() ;
						Image modiefiedPic = img.getScaledInstance(100, 100, 1);
						imIc = new ImageIcon(modiefiedPic) ;
						b.setIcon(imIc);
						b.setText("currentHP= " + ((Cover)cur).getCurrentHP()) ;
						b.setVerticalTextPosition(b.BOTTOM);
						b.setHorizontalTextPosition(b.CENTER);
						b.setIconTextGap(10);
					}
					else {
						if(cur instanceof Champion) {
							//System.out.println("I'm here");
							b.setActionCommand("ChampionGrid");
							int index = model.getAvailableChampions().indexOf((Champion)cur) ;
							String imgStr = getImageStrings().get(index) ;
							ImageIcon imIc = new ImageIcon(imgStr);
							Image img = imIc.getImage() ;
							Image modiefiedPic = img.getScaledInstance(100, 100, 1);
							imIc = new ImageIcon(modiefiedPic) ;
							b.setIcon(imIc);
							String text = "<html>"+((Champion)cur).getName() + "<br>";
							text+= "CurrentHp = " +((Champion)cur).getCurrentHP()+"</html>" ;
							b.setText(text) ;
							b.setFont(new Font("Arial",Font.CENTER_BASELINE,10));
							b.setVerticalTextPosition(b.BOTTOM);
							b.setHorizontalTextPosition(b.CENTER);
							b.setIconTextGap(10);
							Border bo ;
							if(model.getFirstPlayer().getTeam().contains(cur)) {
								bo = BorderFactory.createLineBorder(Color.BLUE, 3) ;
							}
							else {
								bo = BorderFactory.createLineBorder(Color.RED, 3) ;
							}
							b.setBorder(bo);
							if(cur.equals(model.getCurrentChampion())) {
								b.setBackground(Color.GREEN);
							}
						}
					}
				}
				viewBoard[4-i][j] = b ;
				b.addActionListener(this);
				b.setFocusable(false);
			}
		}
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				view.getMainView().add(viewBoard[i][j]) ;
			}
		}
		view.add(view.getMainView()) ;
	}*/
	public void updateLeftArea () {
		String res = "";
		res += "Player1 : " + model.getFirstPlayer().getName()+'\n' ;
		res += "Player2: " + model.getSecondPlayer().getName() + '\n'+'\n' ;
		res += "~~~~~~~~~~~~~~~~" + '\n' +'\n';
		res += model.getFirstPlayer().getName()+"'s leaderAbility:"+'\n';
		String x1;
		if(model.isFirstLeaderAbilityUsed())
			x1 = "Used";
		else
			x1 = "Not Used";
		res += x1 + '\n';
		res += model.getSecondPlayer().getName()+"'s leaderAbility:"+'\n';
		if(model.isSecondLeaderAbilityUsed())
			x1 = "Used";
		else
			x1 = "Not Used";
		res += x1 + '\n'+'\n';
		res += "~~~~~~~~~~~~~~~~" + '\n' +'\n';
		res += "Turn order:"+'\n';
		ArrayList<Champion> temp = new ArrayList<>();
		while(!model.getTurnOrder().isEmpty()) {
			Champion c =(Champion) model.getTurnOrder().remove();
			temp.add(c);
			res += c.getName() + "->" +'\n' ;
		}
		
		for(int i = 0 ;i<temp.size();i++) {
			model.getTurnOrder().insert(temp.get(i));
		}
		
		ArrayList<Champion> temp2 = new ArrayList<>();
		while(!alreadyPlayedChampionsPQ.isEmpty()) {
			Champion c =(Champion) alreadyPlayedChampionsPQ.remove();
			temp2.add(c);
			if(c.getCurrentHP()!=0) {
				res += c.getName() + "->" +'\n' ;
			}
			
		}
		
		for(int i = 0 ;i<temp2.size();i++) {
			alreadyPlayedChampionsPQ.insert(temp2.get(i));
		}
		
		view.getLeftDetails().setText(res);
		//view.add(view.getLeftDetails()) ;
		
	}
	public void updateRightArea () {
		Champion c = model.getCurrentChampion() ;
		String res = "";
		res+= "Current Champion : " + "\n"+ "\n" ;
		res+= "-------------------------" + "\n" + "\n" ;
		res+= "Name:" + c.getName() + "\n" + "\n";
		String type ; 
		if (c instanceof Hero) {
			type = "Hero" ;
		}
		else {
			if (c instanceof AntiHero) {
				type = "AntiHero" ;
			}
			else {
				type = "Villain" ;
			}
		}
		res+= "Type: " + type + "\n" + "\n";
		res+= "Current Hp= " + c.getCurrentHP() + "\n"+ "\n";
		res+= "Mana = " + c.getMana() + "\n"+ "\n";
		res+= "Action Points= " + c.getCurrentActionPoints() + "\n"+ "\n";
		res+= "Attack Damage= " + c.getAttackDamage() + "\n"+ "\n";
		res+= "Attack Range= " + c.getAttackRange() + "\n"+ "\n";
		for(int i =0;i<c.getAbilities().size();i++) {
			Ability a = c.getAbilities().get(i);
			res+= "Ability "+ (i+1) + "{" + a;
			res+= "\n";
		}
		res+= "\n"+ "\n";
		for(int i =0;i<c.getAppliedEffects().size();i++) {
			Effect e = c.getAppliedEffects().get(i);
			res+= "effect"+ (i+1) + " :" + "\n" + "Name: " + e.getName() + "\n" + "Duration: " + e.getDuration() +"\n";
		}		
		view.getRightDetails().setText(res);
		
		
	}
	
	
	public ArrayList<String> getSelectingChampions() {
		return selectingChampions;
	}
	public void actionPerformed(ActionEvent e)  {
		switch(e.getActionCommand()){
			//String name = "";
			//JOptionPane enterFirstName = new JOptionPane() ;
			//enterFirstName.showInputDialog("please enter first player name");
			//Player p1 = new Player(  (String) enterFirstName.getInputValue());
			case "start":String name1 = JOptionPane.showInputDialog( "please enter first player name","Player 1");
						 this.modelPlayer1 = new Player(name1);
						 //System.out.println(p1.getName());
						 String name2 = JOptionPane.showInputDialog("please enter second player name","Player 2" );
						 this.modelPlayer2 = new Player(name2);
						 //System.out.println(p2.getName());
						 //model = new Game(p1,p2) ;
						 loadSelectingChampionsMessages() ;
						 try {
							 Game.initializeAvailableChAb() ;
							 Game.loadAbilities("Abilities.csv");
							 Game.loadChampions("Champions.csv");
						 } catch (IOException e1) {
							 JOptionPane.showMessageDialog(null, "Sorry, there is a technical issue");
						 }
						 view.selectChampions() ;
						 ArrayList<JButton> chToBeSelected = view.getChToBeSelected() ;
						 for(int i = 0;i<Game.getAvailableChampions().size();i++) {
							 JButton b = new JButton();
							 //b.setFocusable(false);
							 //b.setSize(100, 100);
							 b.setFont(new Font("Arial",Font.PLAIN,10));
							 b.setMargin(new Insets(0,0,0,0));
							 b.setText(Game.getAvailableChampions().get(i).toString());
				
							 ImageIcon pic = new ImageIcon(imageStrings.get(i));
							 Image realPic = pic.getImage();
							 Image modiefiedPic = realPic.getScaledInstance(100, 100, 1);
							 pic = new ImageIcon(modiefiedPic);
		
							 b.setIcon(pic);
							 b.setHorizontalTextPosition(b.RIGHT);
							 b.setVerticalTextPosition(b.CENTER);
							 b.setIconTextGap(10);
							 
							 //b.setIconTextGap(10);
							 b.setActionCommand(Game.getAvailableChampions().get(i).getName());
							 b.setFocusable(false);
							 chToBeSelected.add(b);
							 view.getSelectingChampions().add(b);
							 b.addActionListener(this);
						 }
						 view.getSelectingChampions().setVisible(true);
						 view.add(view.getSelectingChampions());
						 JOptionPane.showMessageDialog(null, getSelectingChampions().get(0));
						 //view.addKeyListener(view);
						 view.revalidate();
						 view.repaint();
						 
						 break;
			case "Captain America":
			case "Deadpool":
			case "Dr Strange":
			case "Electro":
			case "Ghost Rider":
			case "Hela":
			case "Hulk":
			case "Iceman":
			case "Ironman":
			case "Loki":
			case "Quicksilver":
			case "Spiderman":
			case "Thor":
			case "Venom":
			case "Yellow Jacket":
				if(getCntSelectedChampions()>5) {
					return ;
				}
				String srcName = e.getActionCommand() ; 
				int index = 0 ;
				JButton b = new JButton() ;
				for(int i=0 ; i<view.getChToBeSelected().size(); i++) {
					JButton bb = view.getChToBeSelected().get(i) ;
					if(bb.getActionCommand().equals(srcName )) {
						index = i ;
						b = bb ;
					}
				}
				Champion c = Game.getAvailableChampions().get(index) ;
				Player p ;
				boolean team1 ;
				if(getCntSelectedChampions()%2 == 0) {
					p = this.getModelPlayer1() ;
					team1 = true ;
				}
				else {
					p = this.getModelPlayer2() ;
					team1 = false ;
				}
				p.getTeam().add(c) ;
				if(team1 && getCntSelectedChampions()==0) {
					p.setLeader(c);
				}
				if(!team1 && getCntSelectedChampions()==1) {
					p.setLeader(c);
				}
				b.setEnabled(false);
				String res = this.getModelPlayer1().getName() + "\n";
				for(int i = 0;i<this.getModelPlayer1().getTeam().size();i++) {
					if(this.getModelPlayer1().getTeam().get(i).equals(this.getModelPlayer1().getLeader()))
						res += "Your Leader is:" ;
					res += this.getModelPlayer1().getTeam().get(i).toString2()  /*"<br>"*/;
					
				}
				res+= "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + '\n' ;
				res += this.getModelPlayer2().getName() + "\n";
				for(int i = 0;i<this.getModelPlayer2().getTeam().size();i++) {
					if(this.getModelPlayer2().getTeam().get(i).equals(this.getModelPlayer2().getLeader()))
						res += "Your Leader is:" ;
					res += this.getModelPlayer2().getTeam().get(i).toString2()  /*"<br>"*/;
					
				}
				view.getPlayersSelectedChampions().setText( res );
				setCntSelectedChampions(getCntSelectedChampions() +1);
				//JScrollPane scroller = view.getScrollBar2();
				//scroller = new JScrollPane(view.getPlayersSelectedChampions(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				//view.add(scroller);
				//view.getPlayersSelectedChampions().setLineWrap(true);
				if( getCntSelectedChampions()<=5) {
					JOptionPane.showMessageDialog(null, getSelectingChampions().get(getCntSelectedChampions()));
				}
				else {
					this.model = new Game ( this.getModelPlayer1(), this.getModelPlayer2()) ;
					
					/*try {
						this.model.loadAbilities("Abilities.csv");
						this.model.loadChampions("Champions.csv");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, there is a technical issue");
						
					}*/
					
					view.getSelectingChampions().setVisible(false);
					view.getPlayersSelectedChampions().setVisible(false);
					view.prepareTutorial();
					view.getContinue1().addActionListener(this);
					//view.getTutorial().setVisible(true);
					/*view.add(view.getTutorial() ) ;
					view.getTutorial().repaint();
					view.getTutorial().revalidate();*/
					
					
				}
				break ;
			case "Continue1" :
				view.getTutorial().setVisible(false);
				updateViewGrid() ;
				view.getMainView().setVisible(true) ;
				view.add(view.getLeftDetails(), BorderLayout.WEST) ;
				view.add(view.getRightDetails(), BorderLayout.EAST) ;
				view.getLeftDetails().setVisible(true);
				view.getRightDetails().setVisible(true);
				
				//view.addKeyListener(view) ;
				
				updateLeftArea () ;
				updateRightArea();
				view.revalidate();
				view.repaint();
				setCheckCanPressKey(true) ;
				
				//System.out.println(view.getKeyListeners());
				break ;
				
			case "ChampionGrid":
				JButton b2 = (JButton)e.getSource();
				JButton[][]Grid = view.getViewGrid();
				Point p1 = new Point(-1,-1) ;
				for(int i = 0;i<5;i++) {
					for(int j = 0 ; j<5 ;j++) {
						if(Grid[i][j].equals(b2)) {
							p1 = new Point(i,j);
							break;
						}
					}
				}
				
				if(isShowDetails()) {
					
					Object [][] gameGrid = model.getBoard();
					Champion c1 = (Champion)gameGrid[4-p1.x][p1.y];	
					String res1 = c1.toString3();
					String type ;
					if (c1 instanceof Hero) {
						type = "Hero" ;
					}
					else {
						if (c1 instanceof AntiHero) {
							type = "AntiHero" ;
						}
						else {
							type = "Villain" ;
						}
					}
					res1 += "Type: " + type + '\n' ;
					String leader ;
					if(c1.equals(model.getFirstPlayer().getLeader()) || c1.equals(model.getSecondPlayer().getLeader())){
						leader = "is a Leader" ;
					}
					else {
						leader = "not a leader" ;
					}
					res1 += leader +'\n';
					for(int i =0;i<c1.getAppliedEffects().size();i++) {
						Effect e1 = c1.getAppliedEffects().get(i);
						res1+= "effect"+ (i+1) + " :" + "\n" + "Name: " + e1.getName() + "\n" + "Duration: " + e1.getDuration() +"\n";
					}	
					JOptionPane.showMessageDialog(null, res1);
				}
				else {
					System.out.println("in SingleTarget !!!!!!!!");
					try {
						System.out.println("tried single target");
						model.castAbility(selectedAbilityToBeCasted, 4-p1.x, p1.y);
						updateViewGrid() ;
						updateLeftArea() ;
						updateRightArea() ;
					}
					catch(NotEnoughResourcesException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					catch(AbilityUseException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
					catch(InvalidTargetException e4) {
						JOptionPane.showMessageDialog(null, e4.getMessage());
					}
					catch(CloneNotSupportedException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					
					finally{
						setShowDetails(true) ;
					}
					
				}
				break;
			
			case "CoverGrid" :
			case "emptyCell":
				JButton b3 = (JButton)e.getSource();
				JButton[][]Grid3 = view.getViewGrid();
				Point p3 = new Point(-1,-1) ;
				for(int i = 0;i<5;i++) {
					for(int j = 0 ; j<5 ;j++) {
						if(Grid3[i][j].equals(b3)) {
							p3 = new Point(i,j);
							break;
						}
					}
				}
				if(! isShowDetails()) {
					System.out.println("in SingleTarget !!!!!!!!");
					try {
						System.out.println("tried single target");
						model.castAbility(selectedAbilityToBeCasted, 4-p3.x, p3.y);
						updateViewGrid() ;
						updateLeftArea() ;
						updateRightArea() ;
					}
					catch(NotEnoughResourcesException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					catch(AbilityUseException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
					catch(InvalidTargetException e4) {
						JOptionPane.showMessageDialog(null, e4.getMessage());
					}
					catch(CloneNotSupportedException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					
					finally{
						setShowDetails(true) ;
					}
				}
				break;
		}	
		
	}
	
	
	/*public ArrayList<Ability> getControllerAvailableAbilities() {
		return controllerAvailableAbilities;
	}
	public ArrayList<Champion> getControllerAvailableChampions() {
		return controllerAvailableChampions;
	}*/
	public static void main(String[] args) /*throws InterruptedException*/ {
	
		Controller c = new Controller();
		//ArrayList<Integer> arr = new ArrayList<>() ;
		//System.out.println(arr.indexOf(5));
		//Thread.sleep(6000);
		//c.display();
		//ArrayList<Champion> test = new ArrayList<>();
	
	}
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		view.repaint();
	}
	/*@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(isCheckCanPressKey()) {
			switch(e.getKeyChar()) {
			case 'w': 
				System.out.println("reached here");
				try {
					model.move(Direction.DOWN);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, There is no enough resources");
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, "Sorry, It's an unallowed movement");
				}
				break ;
			case 's': 
				try {
					model.move(Direction.UP);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, There is no enough resources");
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, "Sorry, It's an unallowed movement");
				}
				break ;
			case 'a': 
				try {
					model.move(Direction.LEFT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, There is no enough resources");
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, "Sorry, It's an unallowed movement");
				}
				break ;
			case 'd': 
				try {
					model.move(Direction.RIGHT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, There is no enough resources");
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, "Sorry, It's an unallowed movement");
				}
				break ;
		}
		}
		
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}*/
	@Override
	public void onPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("I reached controller");
		if(isCheckCanPressKey()) {
			//System.out.println("I can do some change");
			switch(e.getKeyChar()) {
			case 'w': 
				System.out.println("reached here");
				try {
					model.move(Direction.UP);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				break ;
			case 's': 
				try {
					model.move(Direction.DOWN);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				break ;
			case 'a': 
				try {
					model.move(Direction.LEFT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				break ;
			case 'd': 
				try {
					model.move(Direction.RIGHT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(UnallowedMovementException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				break ;
	    	
			case 'e':
				Champion curCH = model.getCurrentChampion() ;
				alreadyPlayedChampionsPQ.insert(curCH);
				if(model.getTurnOrder().size() == 1) {
					while(!alreadyPlayedChampionsPQ.isEmpty()) {
						alreadyPlayedChampionsPQ.remove() ;
					}
				}
				model.endTurn();
				updateViewGrid() ;
				updateLeftArea() ;
				updateRightArea() ;
				/*String res = view.getLeftDetails().getText() ;
				ArrayList<Champion> temp = new ArrayList<>();
				while(!alreadyPlayedChampionsPQ.isEmpty()) {
					Champion c =(Champion) alreadyPlayedChampionsPQ.remove();
					temp.add(c);
					res += c.getName() + "->" +'\n' ;
				}
				
				for(int i = 0 ;i<temp.size();i++) {
					alreadyPlayedChampionsPQ.insert(temp.get(i));
				}
				view.getLeftDetails().setText(res);*/
				break ;
			
			case 'i' :
				System.out.println("You pressed up");
				try {
					model.attack(Direction.UP);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(ChampionDisarmedException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				catch( InvalidTargetException e3) {
					JOptionPane.showMessageDialog(null, e3.getMessage());
				}
				break;
			case 'k' :
				System.out.println("You pressed up");
				try {
					model.attack(Direction.DOWN);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(ChampionDisarmedException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				catch( InvalidTargetException e3) {
					JOptionPane.showMessageDialog(null, e3.getMessage());
				}
				break;
			case 'l' :
				System.out.println("You pressed up");
				try {
					model.attack(Direction.RIGHT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(ChampionDisarmedException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				catch( InvalidTargetException e3) {
					JOptionPane.showMessageDialog(null, e3.getMessage());
				}
				break;
			case 'j' :
				System.out.println("You pressed up");
				try {
					model.attack(Direction.LEFT);
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(NotEnoughResourcesException e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(ChampionDisarmedException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				catch( InvalidTargetException e3) {
					JOptionPane.showMessageDialog(null, e3.getMessage());
				}
				break;
				
			case 'u' :
				try {
					model.useLeaderAbility();
					updateViewGrid() ;
					updateLeftArea() ;
					updateRightArea() ;
				}
				catch(LeaderNotCurrentException e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				catch(LeaderAbilityAlreadyUsedException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				break;
				
			case 'c':
				comboBoxPanel = new JPanel();
				defaultComboBoxModel = new DefaultComboBoxModel();
				comboBoxPanel.setFocusable(false);
				JLabel tmp = new JLabel("Please select the ability you wish to cast");
				tmp.setFocusable(false);
				comboBoxPanel.add(tmp);
				Champion cur = model.getCurrentChampion();
				ArrayList<Ability>abilities = cur.getAbilities();
				for(Ability a : abilities) {
					defaultComboBoxModel.addElement(a.getName());
				}
				comboBox =  new JComboBox(defaultComboBoxModel);
				comboBox.setFocusable(false);
				comboBoxPanel.add(comboBox);
				JOptionPane.showConfirmDialog(null, comboBoxPanel,"",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				String name1 = abilities.get(0).getName();
				String name2 = abilities.get(1).getName();
				String name3 = abilities.get(2).getName();
				Ability curA ;
				if(comboBox.getSelectedItem().toString().equals(name1)) {
					 curA = abilities.get(0);
				}
				else {
					if(comboBox.getSelectedItem().toString().equals(name2)) {
						curA = abilities.get(1);
					}
					else {
						curA = abilities.get(2);
					}
				}
				if(curA.getCastArea().equals( AreaOfEffect.SINGLETARGET)) {
					setShowDetails(false) ;
					selectedAbilityToBeCasted = curA ;
					JOptionPane.showMessageDialog(null, "Please select a cell on the grid to cast your ability on");
				}
				else {
					if(curA.getCastArea().equals( AreaOfEffect.DIRECTIONAL)) {
						comboBoxPanel = new JPanel();
						defaultComboBoxModel = new DefaultComboBoxModel();
						comboBoxPanel.setFocusable(false);
						tmp = new JLabel("Please select a direction");
						tmp.setFocusable(false);
						comboBoxPanel.add(tmp);
						defaultComboBoxModel.addElement("UP") ;
						defaultComboBoxModel.addElement("DOWN");
						defaultComboBoxModel.addElement("RIGHT") ;
						defaultComboBoxModel.addElement("LEFT") ;
						comboBox =  new JComboBox(defaultComboBoxModel);
						comboBox.setFocusable(false);
						comboBoxPanel.add(comboBox);
						JOptionPane.showConfirmDialog(null, comboBoxPanel,"",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
						Direction d ;
						switch(comboBox.getSelectedItem().toString()) {
							case "UP": 
								d = Direction.UP ;
								break;
							case "DOWN":
								d = Direction.DOWN ;
								break;
							case "RIGHT":
								d = Direction.RIGHT ;
								break;
							default:
								d = Direction.LEFT ;
								break;
						}
						try {
							model.castAbility(curA, d);
							updateViewGrid() ;
							updateLeftArea() ;
							updateRightArea() ;
						}
						catch(NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						catch(AbilityUseException e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage());
						}
						catch(CloneNotSupportedException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
					}
					else {
						try {
							model.castAbility(curA);
							updateViewGrid() ;
							updateLeftArea() ;
							updateRightArea() ;
						}
						catch(NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						catch(AbilityUseException e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage());
						}
						catch(CloneNotSupportedException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
					}
				}
				
			}
			/*switch(e.getKeyCode()) {
				case KeyEvent.VK_UP :
					System.out.println("You pressed up");
					try {
						model.attack(Direction.UP);
						updateViewGrid() ;
						updateLeftArea() ;
						updateRightArea() ;
					}
					catch(NotEnoughResourcesException e1){
						JOptionPane.showMessageDialog(null, "Sorry, There is no enough resources");
					}
					catch(ChampionDisarmedException e2) {
						JOptionPane.showMessageDialog(null, "Sorry, Your Champion is disarmed");
					}
					catch( InvalidTargetException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
			}*/
			
			
			
			/*
			    //private JPanel nextView;
				private JPanel mainView;
				private JButton upButton;
				private Champion champ1;
				private JButton middleButton;
				private Champion champ2;
				private JButton downButton;
				private Champion champ3;
				private Game model;
			public quizView()  {
				        this.setTitle("quiz");
						this.setDefaultCloseOperation(3);
						this.setBounds(0,0,1500,3000);
						this.setVisible(true);
						this.setLayout(new BorderLayout());
						
						Player k = new Player("Ahmed");
					    Player b = new Player("Ali");
					    model = new Game(k,b);
					    
					    try {
							Game.loadChampions("Champions.csv");
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					    
						mainView = new JPanel();
						mainView.setLayout(new GridLayout(3,0));
						this.add(mainView);
						
						int r1 = (int) (Math.random()*(model.getAvailableChampions().size()));			    
					    champ1 = model.getAvailableChampions().get(r1);
					    String name1 = champ1.getName() + "\n" + "        "+ champ1.getCurrentHP();
					    upButton = new JButton(name1);
					    mainView.add(upButton);
					    upButton.addActionListener(this);
					    
					    int r2 = (int) (Math.random()*(model.getAvailableChampions().size()));			    
					    champ2 = model.getAvailableChampions().get(r2);
					    String name2 = champ2.getName() + "\n" + "   " + champ2.getCurrentHP();
					    middleButton = new JButton(name2);
					    mainView.add(middleButton);
					    middleButton.addActionListener(this);
					    
					    int r3 = (int) (Math.random()*(model.getAvailableChampions().size()));			    
					    champ3 = model.getAvailableChampions().get(r3);
					    String name3 = champ3.getName() + "\n" + "       " + champ3.getCurrentHP();
					    downButton = new JButton(name3);
					    mainView.add(downButton);
					    downButton.addActionListener(this);
					    
					    champ1=(Champion) champ1.clone();
			            champ2=(Champion) champ2.clone();
			            champ3=(Champion) champ3.clone();
					   	    
					    this.revalidate();
						this.repaint();
			
			
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();
		    	if(b==upButton){
		    		        //mainView.setVisible(false);
		    		       // nextView = new JPanel();
		    		        //JTextArea t = new JTextArea();
		    		        //t.setText("HI");
		    		        //nextView.add(t);
		    		        //this.add(nextView);
		    	            if (champ1.getCurrentHP()==0) {
		    	            	upButton.setEnabled(false);
		    	            }
		    	            else {
		    	            	champ1.setCurrentHP(champ1.getCurrentHP()-500);
		    	            	String res = champ1.getName() + "\n" + "        "+ champ1.getCurrentHP();
		    	            	upButton.setText(res);
		    	            
		    	            }
		    		
		    	}
		    	else if(b==middleButton){
		    		if (champ2.getCurrentHP()==0) {
		            	middleButton.setEnabled(false);
		            }
		            else {
		            	champ2.setCurrentHP(champ2.getCurrentHP()-500);
		            	String res = champ2.getName() + "\n" + "        "+ champ2.getCurrentHP();
		            	middleButton.setText(res);
		            
		            }
		    	}
		    	else {
		    		if (champ3.getCurrentHP()==0) {
		            	downButton.setEnabled(false);
		            }
		            else {
		            	champ3.setCurrentHP(champ3.getCurrentHP()-500);
		            	String res = champ3.getName() + "\n" + "        "+ champ3.getCurrentHP();
		            	downButton.setText(res);
		            
		            }
		    		
		    	}
				
			}
			
			
			public static void main(String [] args) {
				quizView q = new quizView();
				
			}*/
			
		}
	}
}
//}
