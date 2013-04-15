package cs252.lab6.atoms;

import android.graphics.Color;

public class Atom
{
	private int color;
	private int num;
	private double[] elecs;
	
	public Atom(int rings)
	{
		color = Color.GRAY;
		num = 0;
		elecs = new double[rings];
		for(int i=0;i<rings;i++)
			elecs[i] = Math.random() * (2 * Math.PI);
	}
	
	public int getColor() { return color; }
}