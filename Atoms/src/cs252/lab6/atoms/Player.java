package cs252.lab6.atoms;

import android.graphics.Color;

public class Player
{
	private String name;
	private int color;
	private int backColor;
	private boolean bot;
	
	public Player(String name, int color, boolean bot)
	{
		this.name = name;
		this.color = color;
		this.bot = bot;
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		backColor = Color.rgb(red - 50 > 0 ? red - 50 : 0, green - 50 > 0 ? green - 50 : 0, blue - 50 > 0 ? blue - 50 : 0);
	}
	
	public String getName() { return name; }
	public int getColor() { return color; }
	public int getBackColor() { return backColor; }
	public boolean isBot() { return bot; }
}