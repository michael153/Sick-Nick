import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.text.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener
{
	private int fieldpos, ms, x, y, health, score, offset;
	private boolean start, end, w, a, s, d; //start marks the game has started, end marks if the game has ended, w is true if w is pressed, a is true if a is pressed, etc.
	private boolean tissue, shoes, bottle, gloves, glasses, mask; //will be true if the power up is currently being used
	private String name; //this will be the name of the player playing right now
	private ArrayList<Item> powerloc = new ArrayList<Item>();

	private JTextField njt; //stands for name jtextfield, is the first jtextfield that asks for the players name
	private JLabel caption; //caption is the jlabel associated with the text field that explains what it's for
	private JPanel game; //the actual panel that holds the game
	private Image bg;
	private javax.swing.Timer jtimer, player, master, powergen;
	private ScorePanel after;

	//WIDTH and HEIGHT are the width and height of the character
	private final int SPEED = 2;
	private final int WIDTH = 15;
	private final int HEIGHT = 15;
	private final int LANE_WIDTH = 54; //the width of each "row" in game
	
	public GamePanel()
	{
		setLayout(null);
		setPreferredSize(new Dimension(1280, 720));	
		jtimer = new javax.swing.Timer(1000/60, this); //60 FPS
		player = new javax.swing.Timer(1000/60, this); //60 FPS
		master = new javax.swing.Timer(1000/30, this); //30 FPS
		//powergen = new javax.swing.Timer(40000, this); //Refreshes every 2/3rds of a minute
		powergen = new javax.swing.Timer(400, this); //Refreshes every 2/3rds of a minute
		start = false;
		end = false;
		health = 100; //set initial health to 100
		fieldpos = 345; //initial button position
		bg = Toolkit.getDefaultToolkit().getImage("background.jpg");

		//the game will be split into "10" rows, 7 of them playable, 3 of them are the background
		//since the game is 960x540, each row is 54px wide. Sick Nick will start in the middle playable row,
		//which will be the 3 + 4 = 7th row
		
		//initial position of Sick Nick (x, y)
		x = 50;
		y = 6*LANE_WIDTH + LANE_WIDTH/2 - 5; //it is pass 6 rows, and half way through one row, but we must subtract 5 because this is the top right corner

		game = new JPanel()
		{
			//for some reason, if WIDTH and HEIGHT are not redeclared in this nested class, then the value of WIDTH and HEIGHT will be 1 and 2
			private final int WIDTH = 24;
			private final int HEIGHT = 43;
			private final int LANE_WIDTH = 54;
			private GradientPaint gp = new GradientPaint(10, 15, Color.RED, 110, 15, Color.GREEN); //gradient for the health bar
			private DecimalFormat df = new DecimalFormat("0000000000");
			Image nick = Toolkit.getDefaultToolkit().getImage("SickNick.png");
			Color grass = new Color(124, 252, 0); //Color of the foreground, which will look like grass
			//create game's own paintComponent method so that things can also be drawn in game
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Smooth out the text
				if (start)
				{
					//draw the grass foreground first, so the other things can layer on top of it
					g.setColor(grass);
					g.fillRect(0, 3*LANE_WIDTH, 960, 7*LANE_WIDTH);
					//draw the sick nick character
					g.drawImage(nick, x, y, x+24, y+43, 0, 0, 120, 215, this);
					//repeatedly draw the background
					//240x200
					for (int i = 0; i <= 540/240+3; i++)
						g.drawImage(bg, i*200 + offset, 0, i*200 + 240 + offset, 162, 0, 38, 240, 200, this);
					for (int i = 0; i < powerloc.size(); i++)
						g.drawImage(powerloc.get(i).img, powerloc.get(i).x, powerloc.get(i).y + 3*LANE_WIDTH, 32, 32, this);
					
					//Using DecimalFormat, format the score so that it has 10 places no matter what
					g.setColor(Color.BLACK);
					String scoretext = df.format(score);
					g.drawString(scoretext, 875, 15); 
					
					//set the paint (have to cast g to Graphics2d) as the gradient paint, which will be the health bar
					((Graphics2D)g).setPaint(gp);
					g.fillRect(10, 15, health, 15);
				}

			}
		};

		caption = new JLabel("Enter Your Name: ");
		njt = new JTextField("");
		game.setPreferredSize(new Dimension(960, 540)); //the size of the game is 
		add(caption);
		add(njt);
		caption.setBounds(490, 345, 120, 30);
		njt.setBounds(610, 345, 180, 30);
		njt.addActionListener(this);
		addKeyListener(this); //add this to the Panel so that the keys can be used to move the character around
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Smooth out the text
	}

	public void actionPerformed(ActionEvent e)
	{
		//button falling timer code
		if (!start && e.getSource() == njt) //only once
			jtimer.start();
		else if (!start && e.getSource() == jtimer)
		{
			if (fieldpos >= 820) //this means that the button is out of the screen, so we can remove it
			{
				name = njt.getText();
				jtimer.stop();
				remove(njt);
				add(game);
				game.setBounds(160, 90, 960, 540);
				game.setBorder(BorderFactory.createLineBorder(Color.black));
				start = true;
			}
			else //otherwise keep animating using the formula fieldpos
			{
				ms++;
				fieldpos = (int)((-15)*(ms) + 0.5*5.8*ms*ms) + 345; //calculate the yposition of the button using the animation
				caption.setBounds(490, fieldpos, 120, 30);
				njt.setBounds(610, fieldpos, 180, 30); //redraw the button using setBounds
			}
			//redraw and revalidate everything
			this.repaint();
			requestFocus();
			master.start();
			player.start();
			powergen.start();
		}
		
		//entire panel moving over (it's a moving frame)
		if (e.getSource() == master)
		{
			//shift everything over by 1 since it's a moving frame
			boolean changed = false;
			x--;
			score += 1; //add values to the score, because the user lasts this long
			offset--; //background has to move
			if (offset < -200)
				offset += 200;
			for (int i = 0; i < powerloc.size(); i++) //set the value to a new Item with an x value -1 of the original one
			{
				//System.out.println(powerloc.get(i).x + ", " + powerloc.get(i).y + " -- " + x + ", " + y);
				powerloc.set(i, new Item(powerloc.get(i).x-1, powerloc.get(i).y, powerloc.get(i).t));
				//close enough to the power up
				if (Math.abs((powerloc.get(i).x) + 16 - x) <= 16 && Math.abs((powerloc.get(i).y) + 16 + 3*LANE_WIDTH - y) <= 16) //32 by 32 box 
				{
					if (powerloc.get(i).t == 0) tissue = true;
					else if (powerloc.get(i).t == 1) shoes = true;
					else if (powerloc.get(i).t == 2) bottle = true;
					else if (powerloc.get(i).t == 3) gloves = true;
					else if (powerloc.get(i).t == 4) glasses = true;
					else if (powerloc.get(i).t == 5) mask = true;
					powerloc.set(i, new Item(-100, -100, -1)); //set it to unvisible, do not delete b/c deleting will mess up indexing
					changed = true;
				}
			}
			changed = true;
			if (changed)
				Collections.sort(powerloc, new Item());
			while (powerloc.size() > 0)
			{
				if (powerloc.get(0).x + 32 < 0)
					powerloc.remove(0);
				else break;
			}
			//call the repaint() in game so that the stuff being drawn is inside game
			game.repaint();
		}
		
		//character animation timer code
		if (e.getSource() == player)
		{
			if (w) y -= SPEED; //inverted, y goes "up"
			if (a) x -= SPEED; //move x left (-) by SPEED amount
			if (s) y += SPEED; //inverted, y goes "down"
			if (d) x += SPEED; //move x right (+) by SPEED amount
			if (x > 960 - WIDTH) //out of bounds (too far to the right)
				x = 960 - WIDTH; //leave 5 spaces for the circle because it is the position of the top right corner
			if (y < 162) //out of bounds (too far up). this isn't 0 because the player can't go into the background
				y = 162;
			if (y > 540 - HEIGHT) //out of bounds (too far down)
				y = 540 - HEIGHT;
			if (x < -1*WIDTH - SPEED/2) //you lose, too far to the left, the SPEED/2 serves as a leniency
			{
				System.out.println("Out of Screen, YOU LOSE!");
				end = true;
				master.stop();
				player.stop();
				game.setVisible(false);
				after = new ScorePanel(score, name);
				after.setPreferredSize(new Dimension(640, 640));
				//TOO SUDDEN, ADD SOMETHING IN BETWEEN
				add(after);
				after.replay.addActionListener(this);
				after.setBounds(320, 40, 640, 640);
			}
			game.repaint();
		}

		//generate power up
		if (e.getSource() == powergen)
		{
			if (powerloc.size() < 3) //after this, maximum of 3 at all times
			{
				int rx, ry, ri;
				//0: Tissues, 1: Sturdy Shoes, 2: Hand Sanitizer, 3: Gloves, 4: Glasses, 5: Face Mask
				int power_ind[] = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4, 5}; //power_ind is weighted so easier to get 0 than 5
				rx = (int)(Math.random()*(960 - 32)) + 960; //out of the screen so the user can't see it being created
				ry = (int)(Math.random()*(540 - 3*LANE_WIDTH - 32)); //32 so there is room for the entire icon
				ri = (int)(Math.random()*power_ind.length);
				powerloc.add(new Item(rx, ry, power_ind[ri]));
				Collections.sort(powerloc, new Item());
			}
			//~~ 
		}
		
		//piece of code if the replay button is hit
		if (end)
		{
			//WORKING ON THIS PART~~
			if (e.getSource() == after.replay)
				new GamePanel();
		}
		
		this.revalidate();
	}
	
	public void keyPressed(KeyEvent e)
	{
		//w, s, d, a are booleans that represent if up is pressed, down is pressed, right is pressed, or if left is pressed
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
			w = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
			s = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			d = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			a = true;
	}
	
	public void keyReleased(KeyEvent e)
	{
		//here, the key being released is what's associated, so mark the correct released keys as false to reset
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
			w = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
			s = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			d = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			a = false;
	}
	
	public void keyTyped(KeyEvent e) {}
	

}

class Item implements Comparator<Item>, Comparable<Item>
{
	//This is a class that makes the data structure called Item, adopted from C++'s pair
	//This "pairs" together two integers to make a Iteminate
	int x, y, t;
	Image img;
	Item() {}
	Item(int x, int y, int t)
	{
		//For initialization
		this.x = x;
		this.y = y;
		this.t = t;
		if (t == 0) img = Toolkit.getDefaultToolkit().getImage("Tissue.png");
		else if (t == 1) img = Toolkit.getDefaultToolkit().getImage("Shoes.png");
		else if (t == 2) img = Toolkit.getDefaultToolkit().getImage("Bottle.png");
		else if (t == 3) img = Toolkit.getDefaultToolkit().getImage("Gloves.png");
		else if (t == 4) img = Toolkit.getDefaultToolkit().getImage("Glasses.png");
		else if (t == 5) img = Toolkit.getDefaultToolkit().getImage("Mask.png");
	}

	public int compareTo(Item d)
	{
		return (this.y < d.y ? 1 : 0);
	}
	
	public int compare(Item a, Item d)
	{
		return (a.x - d.x);
	}
}

