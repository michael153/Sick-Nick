import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class Diseases extends JPanel implements ActionListener
{
	private JLabel title;
	private JPanel p;
	private JScrollPane jsp;
	public JButton home3;

	private JLabel par1;
	private String par1s = "<html>SickNick is a game created to illustrate the importance of disease awareness, and to teach players about pathology (the study of the cause and effects of diseases). Without pathology, doctors will not be able to make accurate decisions about the diagnosis and treatment of their patients. SickNick also shows the significance of organizations such as WHO (World Health Organization), NIH (National Institutes of Health), CDC (Centers for Disease Control and Protection), and others. Without these organizations and facilities, pandemics will be uncontrollable, and people will be living in the world of Sick Nick, where people with extreme diseases such as Ebola or rabies freely walk the streets.</html>";

	public Diseases()
	{
		setLayout(null);
		setPreferredSize(new Dimension(1280, 720));

		title = new JLabel("Diseases", SwingConstants.CENTER);
		title.setFont(new Font("Futura", Font.PLAIN, 75));
		title.setBorder(BorderFactory.createLineBorder(Color.RED));
		title.setPreferredSize(new Dimension(540, 75));

		p = new JPanel();
		p.setLayout(null);
		p.setPreferredSize(new Dimension(640, 640));

		par1 = new JLabel(par1s);

		p.add(par1);
		par1.setBounds(10, 10, 620, 300);

		jsp = new JScrollPane(p);
		jsp.getVerticalScrollBar().setUnitIncrement(10);

		add(title);
		title.setBounds(370, 15, 540, 75);

		add(jsp);
		jsp.setBounds(320, 100, 640, 560);

		//add in the public button that will take the user back to the Homepage
		home3 = new JButton("Back to Homepage");
		add(home3);
		home3.setBounds(20, 650, 160, 30);
		home3.addActionListener(this);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public void actionPerformed(ActionEvent e) {}
}
