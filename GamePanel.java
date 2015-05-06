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
	private int fieldpos, ms, x, y, health, score, offset, count, clock, wave;
	private boolean start, end, w, a, s, d; //start marks the game has started, end marks if the game has ended, w is true if w is pressed, a is true if a is pressed, etc.
	private boolean tissue, shoes, bottle, gloves, glasses, mask, player = false; //will be true if the power up is currently being used
	private String name; //this will be the name of the player playing right now
	private ArrayList<Item> powerloc = new ArrayList<Item>();
	private ArrayList<People> people = new ArrayList<People>();
	private Stack<Integer> notify = new Stack<Integer>();

	private JTextField njt; //stands for name jtextfield, is the first jtextfield that asks for the players name
	private JLabel caption; //caption is the jlabel associated with the text field that explains what it's for
	private JPanel game; //the actual panel that holds the game
	private Image bg;
	private javax.swing.Timer master, pplgen, powergen;
	private javax.swing.Timer[] powertime = new javax.swing.Timer[6];
	
	private int[] pclock = new int[6];
	
	private ScorePanel after;
	
	//WIDTH and HEIGHT are the width and height of the character
	private final int SPEED = 2;
	private final int WIDTH = 24;
	private final int HEIGHT = 43;
	private final int LANE_WIDTH = 54; //the width of each "row" in game	

	private Thread masterT = new Thread(new masterLoop());//<-- the loop/timer thread
	private boolean running;
	
	//<--use this to start the loop/new timer
	public void startLoop() { masterT.start(); }
	//<-- use this to stop it.
	public void stopLoop() { running = false; }
	
	public GamePanel()
	{
		setLayout(null);
		setPreferredSize(new Dimension(1280, 720));
		master = new javax.swing.Timer(1000/40, this); //40 FPS
		//powergen = new javax.swing.Timer(40000, this); //Refreshes every 2/3rds of a minute
		powergen = new javax.swing.Timer(400, this); //Refreshes every 2/3rds of a minute
		pplgen = new javax.swing.Timer(6000, this); //Refreshes every 6 seconds
		
		for (int i = 0; i < 6; i++)
			powertime[i] = new javax.swing.Timer(1000/10, this); //10 FPS, each power up lasts 10 seconds, so 10*10 frames
		
		start = false;
		end = false;
		health = 100; //set initial health to 100
		fieldpos = 345; //initial button position
		bg = Toolkit.getDefaultToolkit().getImage("img/background.jpg");

		//the game will be split into "10" rows, 7 of them playable, 3 of them are the background
		//since the game is 960x540, each row is 54px wide. Sick Nick will start in the middle playable row,
		//which will be the 3 + 4 = 7th row
		//initial position of Sick Nick (x, y)
		x = 450;
		y = 6*LANE_WIDTH + LANE_WIDTH/2 - 5; //it is pass 6 rows, and half way through one row, but we must subtract 5 because this is the top right corner

		game = new JPanel()
		{
			//for some reason, if WIDTH and HEIGHT are not redeclared in this nested class, then the value of WIDTH and HEIGHT will be 1 and 2
			private final int WIDTH = 24;
			private final int HEIGHT = 43;
			private final int LANE_WIDTH = 54;
			private GradientPaint gp = new GradientPaint(10, 15, Color.RED, 110, 15, Color.GREEN); //gradient for the health bar
			private DecimalFormat df = new DecimalFormat("0000000000");
			Image nick = Toolkit.getDefaultToolkit().getImage("img/SickNick.png");
			Image sick = Toolkit.getDefaultToolkit().getImage("img/SickMan.png");
			Color grass = new Color(124, 252, 0); //Color of the foreground, which will look like grass
			//create game's own paintComponent method so that things can also be drawn in game
			long tick = System.currentTimeMillis();
			int fps = 60, curFps = 60;
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Smooth out the text
				if (start)
				{
					if (System.currentTimeMillis() - tick >= 1000)
					{
						tick = System.currentTimeMillis();
						fps = curFps;
						System.out.println(fps + " fps");
						curFps = 0;
					}
					curFps++;
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
					
					g.setColor(Color.BLACK);
					g.drawString(health + "% health", 115, 27);
					
					for (int i = 0; i < people.size(); i++) //as time goes on, move sick people to the left x pixels
					{
						g.drawImage(people.get(i).img, people.get(i).x, people.get(i).y, people.get(i).x + 20, people.get(i).y + 30, 0, 0, 190, 240, this); //draw the image, keeping all the information
						if (glasses)
						{
							g.setColor(Color.BLACK);
							g.drawOval(people.get(i).x+10 - people.get(i).radius, people.get(i).y+15 - people.get(i).radius, 2*people.get(i).radius, 2*people.get(i).radius);
						}
					}
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
	}

	public void actionPerformed(ActionEvent e)
	{
		//button falling timer code
		if (!start && e.getSource() == njt) //only once
			startLoop();	

		//entire panel moving over (it's a moving frame)
		if (e.getSource() == master)
		{
			//add to clock
			clock++;
			//shift everything over by 1 since it's a moving frame
			x--;
			score += 1; //add values to the score, because the user lasts this long
			offset--; //background has to move
			if (offset < -200)
				offset += 200;
			if(clock % 50 == 0)
				health = Math.min(health+1, 100);
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
			game.repaint();
			//~~
		}

		//spawning, draw a new wave of sick ppl every 5 seconds (60 presses on keyboard up or down for now)
		if (e.getSource() == pplgen)
		{
			Random r = new Random((long)Math.random()*1000);
			int[] weighted = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7};
			int rval = 8 - 8/(clock+1);
			int rnum = (int)(Math.random()*(rval + score/10000)); //# of possible sick people will depend of score and time played (to make it harder)
			int ry = -1;
			for (int i = 0; i < rnum; i++) //draw sick people depending on random number
			{
				if(people.size() > 0)
				{
					//random y value 0
					do { ry = (int)((Math.random() * (7*LANE_WIDTH-30)) + 3*LANE_WIDTH); }
					while (Math.abs(ry - people.get(people.size()-1).y) <= 30);
				}
				else 
					ry = (int)((Math.random() * (7*LANE_WIDTH-30)) + 3*LANE_WIDTH);
				int sign[] = {-1, 1};
				people.add(new People(1300 + sign[(int)(Math.random()*2)]*r.nextInt(300), ry, weighted[r.nextInt(weighted.length)]));
			}
		}
		
		for (int i = 0; i < 6; i++)
		{
			if (e.getSource() == powertime[i])
			{
				pclock[i]++;
				//System.out.println(i + ": " + pclock[i]);
				if (pclock[i] >= 100) //it has already been 10 seconds, and the power up should've disappeared
				{
					if (i == 0) tissue = false;
					else if (i == 1) shoes = false;
					else if (i == 2) bottle = false;
					else if (i == 3) gloves = false;
					else if (i == 4) glasses = false;
					else if (i == 5) mask = false;
					powertime[i].stop();					
				}
			}
		}

		//piece of code if the replay button is hit
		if (end)
		{
			//WORKING ON THIS PART~~
			if (e.getSource() == after.replay)
				new GamePanel();
		}
	}
	
	//the new timer will trigger this method, running at around 60fps
	public void update()
	{
		if(player)
		{
			if (w) //inverted, y goes "up"
			{
				if (shoes) y -= (SPEED + 1); //1.5 times faster b/c of powerup
				else y -= SPEED;
			}
			if (a) //move x left (-) by SPEED amount
			{
				if (shoes) x -= (SPEED + 1); //1.5 times faster b/c of powerup
				else x -= SPEED;
			}
			if (s) //inverted, y goes "down" {i_did_it}
			{
				if (shoes) y += (SPEED + 1); // 1.5 times faster b/c of powerup
				else y += SPEED;
			}
			if (d) //move x right (+) by SPEED amount
			{
				if (shoes) x += (SPEED + 1); //1.5 times faster b/c of powerup
				else x += SPEED;
			}
			if (x > 960 - WIDTH) //out of bounds (too far to the right)
				x = 960 - WIDTH; //leave 5 spaces for the circle because it is the position of the top right corner
			if (y < 162) //out of bounds (too far up). this isn't 0 because the player can't go into the background
				y = 162;
			if (y > 540 - HEIGHT) //out of bounds (too far down)
				y = 540 - HEIGHT;
			if ((x < -1*WIDTH - SPEED/2) || health <= 0) //you lose, too far to the left, the SPEED/2 serves as a leniency
			{
				System.out.println("Out of Screen, YOU LOSE!");
				end = true;
				master.stop();
				player = false;
				game.setVisible(false);
				after = new ScorePanel(score, name);
				after.setPreferredSize(new Dimension(640, 640));
				//TOO SUDDEN, ADD SOMETHING IN BETWEEN
				add(after);
				after.replay.addActionListener(this);
				after.setBounds(320, 40, 640, 640);
			}

			boolean changed = false;
			for (int i = 0; i < powerloc.size(); i++)
			{
				powerloc.get(i).x--;
				if (Math.abs(x - (powerloc.get(i).x)) <= 20 && Math.abs(y-(powerloc.get(i).y+3*LANE_WIDTH)) <= 32)
				{
					int type = powerloc.get(i).t;
					if (type == 0)
					{
						tissue = true;
						health = Math.min((int)(Math.ceil(health*1.15)), 100); //increase health by 15%, but make sure it doesn't go over 100
						notify.add(0);
					}
					else if (type == 1)
					{
						shoes = true;
						notify.add(1);
					}
					else if (type == 2)
					{
						bottle = true;
						health = Math.min((int)(Math.ceil(health*1.2)), 100); //increase health by 20%, make sure doesn't top 100
						notify.add(2);
					}
					else if (type == 3)
					{
						gloves = true;
						health = Math.min((int)(Math.ceil(health*1.25)), 100); //increase health by 25%
						notify.add(3);
					}
					else if (type == 4)
					{
						glasses = true;
						notify.add(4);
					}
					else if (type == 5)
					{
						mask = true;
						health = Math.min((int)(Math.ceil(health*1.5)), 100); //increase healthy by 50%
						notify.add(5);
					}
					//while (notify.size() > 6)
						//notify.remove();
					powerloc.set(i, new Item(-100, -100, type)); //set it to unvisible, do not delete b/c deleting will mess up indexing
					health = Math.min(health+10, 100); //ambigous effect no matter which one
					changed = true;
					pclock[powerloc.get(i).t] = 0;
					powertime[powerloc.get(i).t].start();
				}
			}
			if (changed)
				Collections.sort(powerloc, new Item());
			changed = false;

			while (powerloc.size() > 0)
			{
				if (powerloc.get(0).x + 32 < 0)
					powerloc.remove(0);
				else break;
			}

			for (int i = 0; i < people.size(); i++)
			{
				//people.get(i).x -= 2; //update people's position
				people.get(i).x -= people.get(i).speed; //update people's position
				int d = dist(x + WIDTH/2, y + HEIGHT/2, people.get(i).x + 20/2, people.get(i).y + 30/2); //save the distance between player and sick man, add WIDTH/2 and HEIGHT/2 to find the true center
				if (clock % 4 == 0) //Make it only 15fps so that ways it's not an automatic death
				{
					if (d < people.get(i).radius)
						health -= (int)(Math.ceil(Math.sqrt((people.get(i).radius - d)*people.get(i).sicklevel/4))); //Decrease the health since the player is within the sick range
				}
				if (Math.abs(x - people.get(i).x) < 23 && Math.abs(y - people.get(i).y + 10) < 30)
				{
					people.get(i).x = -100;
					health -= (people.get(i).sicklevel + 1)*4;
					changed = true;
				}
			}

			if (changed)
				Collections.sort(people, new People());
			while (people.size() > 0)
			{
				if (people.get(0).x < 0)
					people.remove(0);
				else break;
			}
		}
		if (!start)
		{
			name = njt.getText(); //get the text in the textbox
			remove(njt); //remove the textfield
			add(game); //add the gamePanel
			game.setBounds(160, 90, 960, 540);
			game.setBorder(BorderFactory.createLineBorder(Color.black));
			start = true;
			//redraw and revalidate everything
			requestFocus();
			master.start();
			powergen.start();
			pplgen.start();
			player = true;
		}
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
	
	//Find the distance between points (x1, y1) and (x2, y2)
	int dist(int x1, int y1, int x2, int y2) { return (int)(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))); }

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
			if (t == 0) img = Toolkit.getDefaultToolkit().getImage("img/Tissue.png");
			else if (t == 1) img = Toolkit.getDefaultToolkit().getImage("img/Shoes.png");
			else if (t == 2) img = Toolkit.getDefaultToolkit().getImage("img/Bottle.png");
			else if (t == 3) img = Toolkit.getDefaultToolkit().getImage("img/Gloves.png");
			else if (t == 4) img = Toolkit.getDefaultToolkit().getImage("img/Glasses.png");
			else if (t == 5) img = Toolkit.getDefaultToolkit().getImage("img/Mask.png");
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


	class People extends Object implements Comparator<People>, Comparable<People>
	{
		int x, y, sicklevel, speed, radius;
		Image img;

		public People() {}
		
		public People(int x, int y, int sicklevel)
		{
			this.x = x;
			this.y = y;
			this.sicklevel = sicklevel;
			this.speed = (int)((Math.log(8-sicklevel)+5)/3);
			this.radius = 5*(sicklevel)+(int)(Math.random()*sicklevel);
			if (sicklevel == 1) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan1.png");
			else if (sicklevel == 2) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan2.png");
			else if (sicklevel == 3) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan3.png");
			else if (sicklevel == 4) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan4.png");
			else if (sicklevel == 5) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan5.png");
			else if (sicklevel == 6) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan6.png");
			else if (sicklevel == 7) img = Toolkit.getDefaultToolkit().getImage("img/SickPeople/SickMan7.png");
		}

		public int compareTo(People d)
		{
			return (this.y < d.y ? 1 : 0);
		}

		public int compare(People a, People d)
		{
			return (a.x - d.x);
		}
	}

	class masterLoop implements Runnable
	{
		private double ups = 60, fps = 60, lastUpdate = System.nanoTime(), lastRender = System.nanoTime();
		private int maxRenderDelay = 2;
		public void run()
		{
			double tPerFps = 1000000000 / fps, tPerUps = 1000000000 / ups, curTime;
			running = true;
			while (running)
			{
				curTime = System.nanoTime();  //update current time
				int updateNum = 0;    //keeps in track of skipeped updates
				while (curTime - lastUpdate > tPerUps && updateNum < maxRenderDelay)
				{
					update();   //all collision, spawning, movment calculations in this update method.
					updateNum++; //keep track of the updateNum
					lastUpdate += tPerUps;   
				}
				curTime = System.nanoTime();   //update cur time again
				game.repaint();   //rerender
				revalidate();
				lastRender = curTime;
				while (curTime - lastRender < tPerFps && curTime - lastUpdate < tPerUps)
				{   //sleep untill the next closest update time
					Thread.yield();
					try { Thread.sleep(0, 999999); }
					catch (Exception e) {}
					curTime = System.nanoTime();
				}
			}
		}
	}
}

