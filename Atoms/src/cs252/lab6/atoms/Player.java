package cs252.lab6.atoms;

import android.graphics.Color;

public class Player
{
	private int player_id;
	private String name;
	public int games_played = 0;
	public int games_won = 0;
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
	
	//Used for loading players from SQL DB
	public Player(int player_id, String name, int bot, int color, int bg_color, int games_played, int games_won)
	{
		this.player_id = player_id;
		this.name = name;
		if(bot == 1)
		{
			this.bot = true;
		}
		else
		{
			this.bot = false;
		}
		this.color = color;
		this.backColor = bg_color;
		this.games_played = games_played;
		this.games_won = games_won;
	}
	
	public String getName() { return name; }
	public int getColor() { return color; }
	public int getBackColor() { return backColor; }
	public int getGamesPlayed() { return games_played; }
	public int getPlayerID() { return player_id; }
	public boolean isBot() { return bot; }
	
	public void setName(String name){ this.name = name; }
	public void setColor(int color){ this.color = color; }
	public void setBackColor(int color){ this.backColor = color; }
	public void setBot(boolean bot){ this.bot = bot; }
	public void setID(int player_id){ this.player_id = player_id; }
	
	public String toString()
	{
		return "ID: " + player_id + ", Name: " + name + ", Bot: " + bot + ", Color: " + color + ", Back Color: " + backColor + ", Games Won: " + games_won + ", Games Played: " + games_played;
	}
	
	public boolean equals(Object o)
	{
		Player other = (Player)o;
		return this.player_id == other.getPlayerID();
	}
	
	
}