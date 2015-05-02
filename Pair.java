import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Pair implements Comparator<Pair>, Comparable<Pair>
{
	//This is a class that makes the data structure called Pair, adopted from C++'s pair
	//This "pairs" together an integer (f / first) and a string (s / second)
	int f;
	String s;
	
	Pair() {}
	
	Pair(int f, String s)
	{
		//For initialization
		this.f = f;
		this.s = s;
	}

	//These following functions are to allow Collections.sort sort pairs appropriately
	public int compareTo(Pair d)
	{
		return (int)(this.s).compareTo(d.s);
	}
	
	public int compare(Pair a, Pair d)
	{
		return (-1)*(a.f - d.f);
	}
}