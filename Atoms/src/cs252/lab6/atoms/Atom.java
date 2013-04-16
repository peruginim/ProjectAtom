package cs252.lab6.atoms;

import android.graphics.Color;

public class Atom
{
	private int color;
	private int backColor;
	private int num;
	private double[] elecs;
	private int[] speeds;
	private double nuc;
	
	public Atom(int rings)
	{
		color = Color.GRAY;
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		backColor = Color.rgb(red - 50 > 0 ? red - 50 : 0, green - 50 > 0 ? green - 50 : 0, blue - 50 > 0 ? blue - 50 : 0);
		num = rings;
		elecs = new double[rings];
		speeds = new int[rings];
		for(int i=0;i<rings;i++)
		{
			elecs[i] = Math.random() * (2 * Math.PI);
			speeds[i] = (int)(Math.random() * 4) + 1;
		}
		nuc = Math.random() * (2 * Math.PI);
	}
	
	public void moveElectrons()
	{
		for(int i=0;i<elecs.length;i++)
			elecs[i] = (elecs[i] + (Math.PI / 180) * speeds[i]) % (2 * Math.PI);
	}
	
	public int getColor() { return color; }
	public int getBackColor() { return backColor; }
	public int getNum() { return num; }
	public int getRings() { return elecs.length; }
	public double[] getElecs() { return elecs; }
	public double getNuc() { return nuc; }
}