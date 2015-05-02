import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class Runner extends JFrame
{
	public static void main(String[] args)
	{
		Runner a = new Runner();
	}

	public Runner()
	{
		super("Sick Nick");
		setSize(1280, 720);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		SickNick b = new SickNick(); //instantiate
		setContentPane(b);
		//because some panels use null layout and have "magic numbers",
		//set resizable to false
		setResizable(false);
		setVisible(true);
	}
}

class SickNick extends JPanel implements ActionListener
{
	private CardLayout cards; //use cardlayout
	private JPanel pa, pb, pc, all;
	private Instructions a;
	private GamePanel b;
	public SickNick()
	{
		cards = new CardLayout();
		all = new JPanel();
		all.setLayout(cards);
		pa = new JPanel();
		pb = new JPanel();
		//panel that will hold the Instructions panel
		pa.setPreferredSize(new Dimension(1280, 720));
		//panel that will hold GamePanel panel
		pb.setPreferredSize(new Dimension(1280, 720));
		a = new Instructions();
		b = new GamePanel();
		//add action listener to a specific button in Instructions a
		a.play.addActionListener(this);
		pa.add(a);
		pb.add(b);
		//add the panels to card layout
		all.add(pa, "pa");
		all.add(pb, "pb");
		add(all);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == a.play)
		{
			cards.show(all, "pb");
			pb.requestFocus(); //requestFocus so that the KeyListener can be used
		}
	}
}