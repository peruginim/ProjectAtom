package cs252.lab6.atoms;

import android.graphics.Color;

public class Atom
{
	private int color;
	private int backColor;
	private int num;
	private double[] elecs;
	private double nuc;
	
	public Atom(int rings)
	{
		color = Color.GRAY;
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		backColor = Color.rgb(red - 50 > 0 ? red - 50 : 0, green - 50 > 0 ? green - 50 : 0, blue - 50 > 0 ? blue - 50 : 0);
		num = 0;
		elecs = new double[rings];
		for(int i=0;i<rings;i++)
			elecs[i] = Math.random() * (2 * Math.PI);
		nuc = Math.random() * (2 * Math.PI);
	}
	
	public int getColor() { return color; }
	public int getBackColor() { return backColor; }
	public int getNum() { return num; }
	public int getRings() { return elecs.length; }
	public double[] getElecs() { return elecs; }
	public double getNuc() { return nuc; }
}