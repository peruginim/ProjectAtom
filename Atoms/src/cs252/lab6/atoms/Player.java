package cs252.lab6.atoms;

import android.graphics.Color;

public class Player
{
	private int player_id;
	private String name;
	private int games_played;
	private int games_won;
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
		games_won = 0;
		games_played = 0;
	}
	
	public String getName() { return name; }
	public int getColor() { return color; }
	public int getBackColor() { return backColor; }
	public int getGamesPlayed() { return games_played; }
	public int getGamesWon() { return games_won; }
	public int getPlayerID() { return player_id; }
	public boolean isBot() { return bot; }
	
	public void setName(String name){ this.name = name; }
	public void setColor(int color){ this.color = color; }
	public void setBackColor(int color){ this.backColor = color; }
	public void setBot(boolean bot){ this.bot = bot; }
	public void setID(int player_id){ this.player_id = player_id; }
	public void incrementGamesPlayed(){ games_played++; }
	public void incrementGamesWon(){ games_won++; }
	
	
}