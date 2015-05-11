import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.text.*;
import java.awt.image.BufferedImage;

public class HomePage extends JPanel implements ActionListener, MouseListener
{
	private JPanel master, ctrs0, ctrs1, ctrs2;
	private CardLayout cards; //Runner will display a HomePage, which will contain all the other classes
	private JPanel home;
	private Instructions instr;
	private GamePanel gme;
	private boolean b1, b2, b3, b4, rx, inhome = true;

	//these images are used for the backgrounds
	private Image bkg, btnbkg, btnbkgclk, redx, redxclk;

	public HomePage()
	{
		cards = new CardLayout();
		master = new JPanel();
		master.setLayout(cards);

		ctrs0 = new JPanel(); //ctrs1 will be the container that holds the Homepage panel
		ctrs1 = new JPanel(); //ctrs1 will be the container that holds the Instructions panel
		ctrs2 = new JPanel(); //ctrs2 will be the container that holds the Game panel

		//make sure that the size of the containers is 1280x720
		ctrs0.setPreferredSize(new Dimension(1280, 720));
		ctrs1.setPreferredSize(new Dimension(1280, 720));
		ctrs2.setPreferredSize(new Dimension(1280, 720));

		bkg = Toolkit.getDefaultToolkit().getImage("img/homepage.png");
		btnbkg = Toolkit.getDefaultToolkit().getImage("img/Effects/button_texture.png");
		btnbkgclk = Toolkit.getDefaultToolkit().getImage("img/Effects/button_texture_clicked.png");
		redx = Toolkit.getDefaultToolkit().getImage("img/Effects/redx.png");
		redxclk = Toolkit.getDefaultToolkit().getImage("img/Effects/redx_clicked.png");

		home = new JPanel()
		{
			private GradientPaint gp1 = new GradientPaint(452,280, Color.BLACK, 452+375, 280, Color.GRAY);
			private GradientPaint gp2 = new GradientPaint(0,0, Color.BLACK, 180, 0, Color.GRAY);

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(new Color(230, 230, 230));
				g.fillRect(370, 60, 540, 110); //Title

				g.drawImage(bkg, 0, 0, 1280, 720, 0, 0, 1920, 1200, this); //draw the background
				//draw 1st button using appropriate image
				if (b1) g.drawImage(btnbkgclk, 452, 280, 452+376, 280+35, 0, 0, 376, 35, this);
				else g.drawImage(btnbkg, 452, 280, 452+376, 280+35, 0, 0, 376, 35, this);
				//draw 2nd button using appropriate image
				if(b2) g.drawImage(btnbkgclk, 452, 280+35+10, 452+376, 280+35+10+35, 0, 0, 376, 35, this);
				else g.drawImage(btnbkg, 452, 280+35+10, 452+376, 280+35+10+35, 0, 0, 376, 35, this);
				//draw 3rd button using appropriate image
				if (b3) g.drawImage(btnbkgclk, 452, 280+35+10+35+50, 452+180, 280+35+10+35+50+35, 0, 0, 180, 35, this);
				else g.drawImage(btnbkg, 452, 280+35+10+35+50, 452+180, 280+35+10+35+50+35, 0, 0, 180, 35, this);
				//draw 4th button using appropriate image
				if (b4) g.drawImage(btnbkgclk, 452+180+14, 280+35+10+35+50, 452+180+14+180, 280+35+10+35+50+35, 0, 0, 180, 35, this);
				else g.drawImage(btnbkg, 452+180+14, 280+35+10+35+50, 452+180+14+180, 280+35+10+35+50+35, 0, 0, 180, 35, this);

				if (rx) g.drawImage(redxclk, 1280-5-15, 710-15-12, 1280-5, 710-15, 0, 0, 512, 512, this);
				else g.drawImage(redx, 1280-5-15, 710-15-12, 1280-5, 710-15, 0, 0, 512, 512, this);
			}
		};

		instr = new Instructions();
		gme = new GamePanel();

		gme.home0.addActionListener(this); //add an action listener to this button to know when to go back to homepage

		instr.play.addActionListener(this); //this will tell us when to show the gme panel
		instr.home1.addActionListener(this); //add an action listener to this button to know when to go back to homepage
		instr.home2.addActionListener(this); //add an action listener to this button to know when to go back to homepage

		//create the home panel
		home.setLayout(null);
		home.setPreferredSize(new Dimension(1280, 720));
		
		//initialize the labels that will correspond with the rects/buttons
		JLabel title = new JLabel("Sick Nick", SwingConstants.CENTER);
		JLabel btn1 = new JLabel("Play Game", SwingConstants.CENTER);
		JLabel btn2 = new JLabel("Instructions", SwingConstants.CENTER);
		JLabel btn3 = new JLabel("Diseases", SwingConstants.CENTER);
		JLabel btn4 = new JLabel("More", SwingConstants.CENTER);

		title.setFont(new Font("Futura", Font.PLAIN, 75));
		btn1.setFont(new Font("Futura", Font.PLAIN, 28));
		btn2.setFont(new Font("Futura", Font.PLAIN, 28));
		btn3.setFont(new Font("Futura", Font.PLAIN, 15));
		btn4.setFont(new Font("Futura", Font.PLAIN, 15));
		
		btn1.setForeground(new Color(236, 236, 236)); //Set the font color to off-white so that it's contrasting
		btn2.setForeground(new Color(236, 236, 236)); //Set the font color to off-white so that it's contrasting
		btn3.setForeground(new Color(236, 236, 236)); //Set the font color to off-white so that it's contrasting
		btn4.setForeground(new Color(236, 236, 236)); //Set the font color to off-white so that it's contrasting

		//add the buttons to the home panel
		home.add(title);
		home.add(btn1);
		home.add(btn2);
		home.add(btn3);
		home.add(btn4);

		//because it is a null layout panel, have to use setbounds
		title.setBounds(370, 60, 540, 110);
		btn1.setBounds(452, 280, 376, 35);
		btn2.setBounds(452, 280+35+10, 376, 35);
		btn3.setBounds(452, 280+35+10+35+50, 180, 35);
		btn4.setBounds(452+180+14, 280+35+10+35+50, 180, 35);

		//add the Homepage panel, Instructions panel and GamePanel to their containers
		ctrs0.add(home);
		ctrs1.add(instr);
		ctrs2.add(gme);
		//set names for the containers so we can access them later
		ctrs0.setName("ctrs0");
		ctrs1.setName("ctrs1");
		ctrs2.setName("ctrs2");
		//add the containers to master panel, along with their strings
		master.add(ctrs0, "ctrs0");
		master.add(ctrs1, "ctrs1");
		master.add(ctrs2, "ctrs2");
		add(master);
		addMouseListener(this); //add mouselistener to see if user clicks on "buttons"/rectangles
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == instr.play)
			cards.show(master, "ctrs2"); //display the game
		if (e.getSource() == gme.home0 || e.getSource() == instr.home1 || e.getSource() == instr.home2)
		{
			cards.show(master, "ctrs0"); //display homepage since that is what's being requested
			inhome = true; //back to homepage, set this to true
		}
	}

	//mousePressed and mouseReleased are used to "animate"/decorate the button when it is clicked for aesthetics
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (x >= 452 && x <= 452+376 && y >= 280 && y <= 280+35 && inhome) //Clicked on first button
			b1 = true;
		if (x >= 452 && x <= 452+376 && y >= 280+35+10 && y <= 280+35+10+35 && inhome) //2nd button
			b2 = true;
		if (x >= 452 && x <= 452+180 && y >= 280+35+10+35+50 && y <= 280+35+10+35+50+35 && inhome) //3rd button
			b3 = true;
		if (x >= 452+180+14 && x <= 452+180+14+180 && y >= 280+35+10+35+50 && y <= 280+35+10+35+50+35 && inhome) //4th button
			b4 = true;
		if (x >= 1280-5-15 && x <= 1280-5 && y >= 710-15-12 && y <= 710-15 && inhome)
			rx = true;
		home.repaint();
	}

	public void mouseReleased(MouseEvent e)
	{
		//since the mouse is released, none of the buttons are still being held, so set all to false
		b1 = false;
		b2 = false;
		b3 = false;
		b4 = false;
		rx = false;
		home.repaint();
	}


	public void mouseClicked(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (x >= 452 && x <= 452+376 && y >= 280 && y <= 280+35 && inhome) //Clicked on first button
		{
			cards.show(master, "ctrs2");
			inhome = false; //the panel has changed, so change inhome to false
		}
		if (x >= 452 && x <= 452+376 && y >= 280+35+10 && y <= 280+35+10+35 && inhome) //2nd button
		{
			cards.show(master, "ctrs1");
			inhome = false;
		}
		if (x >= 1280-5-12 && x <= 1280-5 && y >= 710-15-12 && y <= 710-15 && inhome) //Quit the game
		{
			System.exit(0);
			inhome = false;
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}