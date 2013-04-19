package cs252.lab6.atoms;

import java.util.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class SQLHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "atoms.db";
	private static final String PLAYER_TABLE = "players";
	private static final int DB_VERSION = 1;
	
	//Keys for database fields:
	private static final String PLAYER_ID = "player_id";
	private static final String PLAYER_NAME = "player_name";
	private static final String BOT = "bot";
	private static final String PLAYER_COLOR = "player_color";
	private static final String PLAYER_BGCOLOR = "player_bgcolor";
	private static final String GAMES_PLAYED = "games_played";
	private static final String GAMES_WON = "games_won";
	
	public SQLHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	//Create new table
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE " + PLAYER_TABLE + " (" + PLAYER_ID + " INTEGER AUTOINCREMENT PRIMARY KEY NOT NULL, " + PLAYER_NAME + " TEXT NOT NULL, " + BOT + " INTEGER DEFAULT '0', " + PLAYER_COLOR + " INTEGER UNSIGNED NOT NULL, " + PLAYER_BGCOLOR + " INTEGER UNSIGNED NOT NULL, " + GAMES_PLAYED + " INTEGER UNSIGNED, " + GAMES_WON + " INTEGER UNSIGNED);");
	}
	
	//Delete old table and create a new table
	public void onUpgrade(SQLiteDatabase database, int oldversion, int newversion)
	{
		database.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE);
		onCreate(database);
	}
	
	public void createPlayer(Player player)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PLAYER_ID, 0);
		values.put(PLAYER_NAME, player.getName());
		values.put(BOT, player.isBot());
		values.put(PLAYER_COLOR, player.getColor());
		values.put(PLAYER_BGCOLOR, player.getBackColor());
		values.put(GAMES_PLAYED, player.getGamesPlayed());
		values.put(GAMES_WON, player.getGamesWon());
		database.insert(PLAYER_TABLE, null, values);
		database.close();
	}
	
	public Player getPlayer(int player_id)
	{
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.query(PLAYER_TABLE, new String[] {PLAYER_NAME, BOT, PLAYER_COLOR, PLAYER_BGCOLOR, GAMES_PLAYED, GAMES_WON}, PLAYER_ID + " = " + String.valueOf(player_id), null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		Player player = new Player(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
		database.close();
		return player;
	}
	
	public void updatePlayer(Player player)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PLAYER_ID, 0);
		values.put(PLAYER_NAME, player.getName());
		values.put(BOT, player.isBot());
		values.put(PLAYER_COLOR, player.getColor());
		values.put(PLAYER_BGCOLOR, player.getBackColor());
		values.put(GAMES_PLAYED, player.getGamesPlayed());
		values.put(GAMES_WON, player.getGamesWon());
		database.update(PLAYER_TABLE, values, PLAYER_ID + " = ?", new String[] { String.valueOf(player.getPlayerID()) });
		database.close();
	}
	
	public void deletePlayer(Player player)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(PLAYER_TABLE, PLAYER_ID + " = ?", new String[] { String.valueOf(player.getPlayerID()) });
		database.close();
	}
	
}
