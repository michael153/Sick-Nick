import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ScorePanel extends JPanel
{
	private Image hisc;
	private ScoreHandler sh;
	private JLabel game, numplays, llabel;
	private JPanel list1, list2, list3;

	public ScorePanel(int s, String curname)
	{	
		//name will be passed in from GamePanel,
		//there will be a Jtextfield asking for name in the gamepanel
		//null layout will be used so that ScorePanel can be more customizable
		setLayout(null);
		setPreferredSize(new Dimension(640, 640));
		
		//create the instance of the scorehandler, which will find the top 5
		//as well as other information
		sh = new ScoreHandler(s, curname);
		
		//game is the header/title of this panel
		//SwingConstants.CENTER makes the label in the center everytime
		game = new JLabel("Game Over!", SwingConstants.CENTER);
		game.setFont(new Font("Impact", Font.PLAIN, 45));
		
		//hisc is the image that shows the user that there is a new highscore
		hisc = Toolkit.getDefaultToolkit().getImage("img/HighScore.png");
		
		//llabel is the label that explains what the list of highscores is for
		llabel = new JLabel("----- List of Highscores -----", SwingConstants.CENTER);
		llabel.setForeground(Color.gray); //set the text color to gray
		
		//numplays is the label that shows the user how much times this game was played
		if (sh.times == 1) //if only 1 game is played, "game" should be singular tense!
			numplays = new JLabel(sh.times + " game played", SwingConstants.CENTER);
		else
			numplays = new JLabel(sh.times + " games played", SwingConstants.CENTER);
		numplays.setForeground(Color.gray); //set the text color to gray

		//list1, 2, 3 are panels that will contain all the list of scores in a
		//320x320 box, each high score is 320x64 large  
		list1 = new JPanel();
		list1.setPreferredSize(new Dimension(50, 320));
		list1.setLayout(null);
		
		list2 = new JPanel();
		list2.setPreferredSize(new Dimension(150, 320));
		list2.setLayout(null);
		
		list3 = new JPanel();
		list3.setPreferredSize(new Dimension(120, 320));
		list3.setLayout(null);

		//for loop to add the 5 top scores into list, the panel that holds the scores in
		//a 320x320 box
		for (int i = 0; i < 5; i++)
		{
			JLabel ind = new JLabel("");
			JLabel name = new JLabel("");
			JLabel score = new JLabel("");
			
			ind.setPreferredSize(new Dimension(50, 64));
			name.setPreferredSize(new Dimension(180, 64));
			score.setPreferredSize(new Dimension(90, 64));
			
			ind = new JLabel("" + (i+1), SwingConstants.CENTER);
			//don't write anything if there isn't a i'th score
			if (sh.scores[i].f == -1 && (sh.scores[i].s).equals("NULL"))
			{
				name = new JLabel("");
				score = new JLabel("");
			}
			else
			{
				//The label can fit around 45 characters
				//add 45 - (length of name + length of score) to the middle
				
				name = new JLabel(sh.scores[i].s, SwingConstants.CENTER);
				score = new JLabel("" + sh.scores[i].f, SwingConstants.CENTER);
				
				if (sh.pos == i)
				{
					//Highlight and show this current round's position within the rankings
					ind.setOpaque(true);
					ind.setBackground(Color.YELLOW);
					ind.setBorder(BorderFactory.createLineBorder(Color.black));
					name.setOpaque(true);
					name.setBackground(Color.YELLOW);
					name.setBorder(BorderFactory.createLineBorder(Color.black));
					score.setOpaque(true);
					score.setBackground(Color.YELLOW);
					score.setBorder(BorderFactory.createLineBorder(Color.black));
				}
			}
			list1.add(ind);
			list2.add(name);
			list3.add(score);
			ind.setBounds(0, 64*i, 50, 64);
			name.setBounds(0, 64*i, 180, 64);
			score.setBounds(0, 64*i, 90, 64);
		}
		
		//add in the appropriate containers/labels
		add(game);
		game.setPreferredSize(new Dimension(640, 70));
		game.setBounds(0, 0, 640, 70);
		
		add(list1);
		add(list2);
		add(list3);
		
		//if there is a highscore, set the list lower so the high score image can fit
		if (sh.hi)
		{
			list1.setBounds(160, 230, 50, 320);
			list2.setBounds(210, 230, 180, 320);
			list3.setBounds(390, 230, 90, 320);
		}
		else
		{
			list1.setBounds(160, 120, 50, 320);
			list2.setBounds(210, 120, 180, 320);
			list3.setBounds(390, 120, 90, 320);
		}
		
		list1.setBorder(BorderFactory.createLineBorder(Color.black));
		list2.setBorder(BorderFactory.createLineBorder(Color.black));
		list3.setBorder(BorderFactory.createLineBorder(Color.black));
		
		add(llabel);
		llabel.setPreferredSize(new Dimension(320, 40));
		
		add(numplays);
		numplays.setPreferredSize(new Dimension(320, 40));
		
		//if there is a highscore, set numplays lower so the high score image can fit
		if (sh.hi)
		{
			llabel.setBounds(160, 195, 320, 40);
			numplays.setBounds(160, 550, 320, 40);
		}
		else
		{
			llabel.setBounds(160, 90, 320, 40);
			numplays.setBounds(160, 460, 320, 40);
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//sets the font to be very smooth, and not jagged
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (sh.hi) //draw hiscore image only if it actually is a high score
			g.drawImage(hisc, 259, 60, 122, 122, this);
	}
}


		//|---------------------------------
		//|          game label            |
		//|                                |
		//|           hisc png             |
		//|         list panel(s)          |
		//|        --------------          |
		//|        |            |          |
		//|        |            |          |
		//|        |            |          |
		//|        --------------          |
		//|        numplays label          |
		//|                                |
		//|         bpanel panel           |
		//|---------------------------------
		//||                              ||
		//||    quit button replay button ||
		//|---------------------------------
