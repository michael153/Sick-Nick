import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ScoreHandler
{
	public boolean hi; //this will be true if the score is a hi score
	public int times = 0;
	//this is -1 if the score isn't in top 5, otherwise, it is the place of the new score
	public int pos = -1;
	public Pair[] scores = new Pair[5]; //this will keep track of top 5 scores

	private int cur;
	//a dynamic array of Pairs, which "pairs" a int and a string
	private ArrayList<Pair> inds = new ArrayList<Pair>();
	private File fin = new File("Scores.txt");
	private Scanner rdr = null;
	private PrintWriter wtr = null;

	public ScoreHandler(int s, String n)
	{
		//set the int s to cur
		cur = s;
		int t = 0;
		String st = "";
		try
		{
			rdr = new Scanner(fin);
			//read in scores, and add it to the inds arraylist, which will be sorted
			while (rdr.hasNext())
			{
				Pair a = new Pair(0, "0"); //for sake of initialization
				t = rdr.nextInt();
				st = rdr.nextLine();
				//get rid of the space
				//because there is a space between the int and the string in Scores.txt,
				//the string will be " ...", so we have to get rid of the space
				st = st.substring(1);
				a.f = t;
				a.s = st;
				inds.add(a);
			}
			//using Collections.sort,
			//we custom sort our pairs by using our comparator in the Pair class
			Collections.sort(inds, new Pair());
			
			//times is the number of previous plays + 1
			times = inds.size()+1;
			
			//if there's less than 5 scores, it is automatically a high score
			if (inds.size() < 5)
			{
				hi = true;
				int num = inds.size();
				//find the position of the new score, so we know what place it got
				boolean fnd = false;
				for (int i = num-1; i >= 0; i--)
				{
					if (cur > inds.get(i).f)
					{
						pos = i;
						fnd = true;
					}
				}
				//this score doesn't compare greater than the current ones,
				//so this score will go behind all the others, thus it's position is num
				if (!fnd)
					pos = num;
			}
			else
			{
				//Check if the score is one of the high scores
				if (cur > inds.get(4).f)
				{
					hi = true;
					//find the position of the new score
					for (int i = 4; i >= 0; i--)
						if (cur > inds.get(i).f)
							pos = i;
				}
				else pos = -1;
				//the score isn't in the top 5,
				//so set pos to -1 to indicate that it isn't a high score
			}
			
			//add the new score into the list, so that we can reprint this into Scores.txt
			// and keep track of how many times we played
			inds.add(new Pair(s, n));
			
			//re-print into the same file, so it's updated next time
			wtr = new PrintWriter(fin);
			for (Pair a : inds)
				wtr.println(a.f + " " + a.s);
			wtr.close();
			
			//Sort again to incorporate the new high score
			Collections.sort(inds, new Pair());
			
			//This will update the array of high scores, so that we can pass on the list
			//of top 5 scores to other classes 
			for (int i = 0; i < 5; i++)
			{
				//if there are less than 5 scores,
				//then the rest of the scores will (-1, "NULL"),
				//which will symbolize that there is no score
				if (i >= inds.size())
					scores[i] = new Pair(-1, "NULL");
				else
					scores[i] = inds.get(i);
			}
		}
		catch (FileNotFoundException e) {}
	}

}