import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class Tester extends JFrame
{
	public static void main(String[] args)
	{
		Tester a = new Tester();
	}

	public Tester()
	{
		super("Sick Nick");
		setSize(1280, 720);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		HomePage b = new HomePage(); //instantiate
		b.setBorder(BorderFactory.createEmptyBorder(-15, 0, 0, 0)); //insets, set top inset to -15 to compensate for default blank space
		setContentPane(b);
		//because some panels use null layout and have "magic numbers",
		//set resizable to false
		setResizable(false);
		setVisible(true);
	}
}