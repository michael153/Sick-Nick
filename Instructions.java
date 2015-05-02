import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class Instructions extends JPanel implements ActionListener
{
	private CardLayout cards;
	private String str1 = "<html><font size = \"15\"><u>Instructions</u></font><br/>Lore"+
	"m ipsum dolor sit amet, vix vero invenire consequuntur no, inermis tibique est ex."+
	"Qui ne modo facilis, est et malorum vivendo. Ut erat abhorreant concludaturque cum."+
	"Ei electram patrioque intellegebat vix. Duo utamur gloriatur complectitur an, an"+
	"sonet deterruisset vis. At mei detracto maluisset prodesset, ei eum decore"+
	"tacimates reprehendunt. Eu iriure commune eum. No pri torquatos dissentias, eam an"+
	"quis fastidii gloriatur. His an modo putent abhorreant. Errem albucius sapientem"+
	" ne mei. Cu graeco nemore nam, harum iuvaret abhorreant ea nam. Euismod qualisque"+
	" has ex, vim prompta officiis reprimique et, virtute referrentur ex per. At nam "+
	"iisque impetus apeirian, paulo saepe at eam. Sea alia porro ex, vix oblique"+
	"nominavi oporteat cu, sed stet inermis an. Mei veri virtute te, at sonet luptatum"+
	" usu, vidisse labores sea ei. Te meis error dictas cum, et volutpat evertitur "+
	"elaboraret nec. Dolor inciderint contentiones est eu, pri timeam salutandi "+
	"evertitur ut. Ea vel imperdiet omittantur reprehendunt. Oblique dolores invenire "+
	"ne sit, has ne civibus dignissim liberavisse. Atqui graece cu has, possim "+
	"maiestatis reprehendunt in nec. Cu etiam habemus pro. Usu ut magna omnis, sea "+
	"epicuri propriae et. Consequat delicatissimi sea cu. Ea ius ipsum suscipit "+
	"conclusionemque, an discere aliquid mel. Qualisque signiferumque sea et, est "+
	"elaboraret consequuntur at.</html>";
	private JLabel title1, title2, instr1, instr2;
	private JPanel all, page1, ph1, page2, ph2;
	private JButton quit1, next, quit2, back;
	public JButton play;

	public Instructions()
	{
		cards = new CardLayout();
		
		//all will hold everything, and will use cardlayout
		all = new JPanel();
		all.setLayout(cards);
		
		//page1 will be the JPanel containing everything in the 1st page of the instructions
		page1 = new JPanel();
		page1.setPreferredSize(new Dimension(1280, 720));
		page1.setLayout(new BorderLayout());
		
		//page2 will be the JPanel containing everything in the 1st page of the instructions
		page2 = new JPanel();
		page2.setPreferredSize(new Dimension(1280, 720));
		page2.setLayout(new BorderLayout());
		
		//initialize the panel containing the buttons in FlowLayout form for page1
		ph1 = new JPanel();
		ph1.setPreferredSize(new Dimension(1280, 80));
		ph1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//instantiate the buttons & description area, and add them in the appropriate layout
		quit1 = new JButton("Quit");
		next = new JButton("Next");
		instr1 = new JLabel(str1);
		
		//instantiate the jlabel of the title of this page
		title1 = new JLabel("Sick Nick", SwingConstants.CENTER);
		title1.setFont(new Font("Futura", Font.PLAIN, 75));
		
		//the panel that holds everything in the center
		JPanel C1 = new JPanel();
		
		//C1.setPreferredSize(new Dimension(700, 700));
		instr1.setPreferredSize(new Dimension(700, 500));
		C1.add(instr1);
		page1.add(title1, BorderLayout.NORTH);
		page1.add(C1, BorderLayout.CENTER);
		ph1.add(quit1);
		ph1.add(next);
		page1.add(ph1, BorderLayout.SOUTH);

		//initialize the panel containing the buttons in FlowLayout form for page2
		ph2 = new JPanel();
		ph2.setPreferredSize(new Dimension(1280, 80));
		ph2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//instantiate the buttons for page2, and add them using BorderLayout.SOUTH
		quit2 = new JButton("Quit");
		back = new JButton("Back");
		play = new JButton("Play Game");
		
		//title for 2nd page
		title2 = new JLabel("Sick Nick", SwingConstants.CENTER);
		title2.setFont(new Font("Futura", Font.PLAIN, 75));

		//the center panel #2
		JLabel title = new JLabel("<html><font size = \"15\"><u>Instructions (Cont'd)</u></font></html>");
		JLabel pu = new JLabel("<html><u><b>Power Ups</b></u><br/>These can be bought in the in-game store to help the player survive longer.</html>");
		JLabel d = new JLabel("<html><u><b>Diseases</b></u><br/>The other humans that Nick has to dodge are sick to different extents. Some are greener/sicker than the others. The following shows the levels of sickness.</html>");
		JLabel tissue = new JLabel("Tissues: Increases health x15%", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Tissue.png")), JLabel.LEFT);
		JLabel shoes = new JLabel("Sturdy Shoes: Increases speed", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Shoes.png")), JLabel.LEFT);
		JLabel bottle = new JLabel("Hand Sanitizer: Increases health x20%", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Bottle.png")), JLabel.LEFT);
		JLabel gloves = new JLabel("Gloves: Increases health x25%", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Gloves.png")), JLabel.LEFT);
		JLabel glasses = new JLabel("Glasses: Increases vision, see areas of sickness", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Glasses.png")), JLabel.LEFT);
		JLabel mask = new JLabel("Face Mask: Increases health x50%", new ImageIcon(Toolkit.getDefaultToolkit().getImage("Mask.png")), JLabel.LEFT);
		JLabel b1 = new JLabel("<html>Healthy</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("B1.jpg")), JLabel.LEFT);
		JLabel g1 = new JLabel("<html>Level 1 Sickness: Most common sicknesses, such as the common cold, or norovirus</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G1.jpg")), JLabel.LEFT);
		JLabel g2 = new JLabel("<html>Level 2 Sickness: Common ilnesses that are relatively benign, such as Flu (Influenza), or Seasonal Flu </html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G2.jpg")), JLabel.LEFT);
		JLabel g3 = new JLabel("<html>Level 3 Sickness: Very contagious (e.g Rotavirus, Mumps)</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G3.jpg")), JLabel.LEFT);
		JLabel g4 = new JLabel("<html>Level 4 Sickness: Somewhat deadly (e.g Swine Flu, Salmonella, or E.Coli)</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G4.jpg")), JLabel.LEFT);
		JLabel g5 = new JLabel("<html>Level 5 Sickness: Very contagious diseases (e.g Malaria, Measles, and Whooping Cough)</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G5.jpg")), JLabel.LEFT);
		JLabel g6 = new JLabel("<html>Level 6 Sickness: Deadly diseases, such as SARS (Severe Acute Respiratory Syndrome), Typhoid, Polio</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G6.jpg")), JLabel.LEFT);
		JLabel g7 = new JLabel("<html>Level 7 Sickness: Very deadly diseases, such as Ebola (fatality rate of 70%), HIV (80% fatality), TB, & Rabies (100% fatality rate)</html>", new ImageIcon(Toolkit.getDefaultToolkit().getImage("G7.jpg")), JLabel.LEFT);
		JPanel C2 = new JPanel();
		
		C2.setLayout(null); //gives more flexibility, add jlabels, images for powerups to this
		C2.setPreferredSize(new Dimension(700, 750));
		C2.add(title);		title.setBounds(290, 5, 600, 40);
		C2.add(pu);			pu.setBounds(290, 65, 600, 40);
		C2.add(tissue); 	tissue.setBounds(290, 105, 600, 32);
		C2.add(shoes); 		shoes.setBounds(290, 140, 600, 32);
		C2.add(bottle); 	bottle.setBounds(290, 175, 600, 32);
		C2.add(gloves); 	gloves.setBounds(290, 210, 600, 32);
		C2.add(glasses);	glasses.setBounds(290, 245, 600, 32);
		C2.add(mask);		mask.setBounds(290, 280, 600, 32);
		C2.add(d);			d.setBounds(290, 340, 700, 50);
		C2.add(b1);			b1.setBounds(290, 395, 700, 32); 
		C2.add(g1);			g1.setBounds(290, 435, 700, 32); 
		C2.add(g2);			g2.setBounds(290, 475, 700, 32); 
		C2.add(g3);			g3.setBounds(290, 515, 700, 32); 
		C2.add(g4);			g4.setBounds(290, 555, 700, 32); 
		C2.add(g5);			g5.setBounds(290, 595, 700, 32); 
		C2.add(g6);			g6.setBounds(290, 635, 700, 32); 
		C2.add(g7);			g7.setBounds(290, 675, 700, 32); 

		//since C2 is very large, add it to a scrollpane so that the entire thing can be
		//viewed on a 1280x720 window
		JScrollPane jsp = new JScrollPane(C2);
		jsp.getVerticalScrollBar().setUnitIncrement(10);
		
		page2.add(title2, BorderLayout.NORTH);
		page2.add(jsp, BorderLayout.CENTER);
		ph2.add(quit2);
		ph2.add(back);
		ph2.add(play);
		page2.add(ph2, BorderLayout.SOUTH);

		//add ActionListeners to the buttons
		quit1.addActionListener(this);
		next.addActionListener(this);
		quit2.addActionListener(this);
		play.addActionListener(this);
		back.addActionListener(this);
		//add the page panels to the panel holding everything (all), which uses cardpanel
		all.add(page1, "page1");
		all.add(page2, "page2");
		add(all);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//sets the font to be very smooth, and not jagged
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == next || e.getSource() == back)
		{
			cards.next(all);
			repaint(); //render the graphics properly even for the next panels
		}
		if (e.getSource() == quit1 || e.getSource() == quit2)
			System.exit(0);
	}
}
