package cs252.lab6.atoms;

import android.graphics.Color;

public class Atom
{
	private Player player;
	public int num;
	private double[] elecs;
	private int[] speeds;
	private double nuc;
	
	public Atom(int rings)
	{
		player = new Player("null", Color.GRAY);
		num = 0;
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
	
	public void addElectron() { num++; }
	public void setPlayer(Player player) { this.player = player; }
	
	public Player getPlayer() { return player; }
	public int getColor() { return player.getColor(); }
	public int getBackColor() { return player.getBackColor(); }
	public int getNum() { return num; }
	public int getRings() { return elecs.length; }
	public double[] getElecs() { return elecs; }
	public double getNuc() { return nuc; }
}