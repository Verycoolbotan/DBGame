package ru.samsung.itschool.dbgame;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	/*
	 * TABLES: ------- RESULTS SCORE INTEGER USER VARCHAR
	 * --------------- USERS USERID INTEGER PRIMARY KEY ASC NAME TEXT PIC TEXT
	 */
	private Context context;
	private String DB_NAME = "game.db";
	String userPics_path="";

	private SQLiteDatabase db;

	private static DBManager dbManager;

	public static DBManager getInstance(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager(context);
		}
		return dbManager;
	}

	private DBManager(Context context) {
		this.context = context;
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		createTablesIfNeedBe(); 
	}

	
	
	void addResult(String username, int score) {
		db.execSQL("INSERT INTO RESULTS VALUES ('" + username + "', " + score
				+ ");");
	}

	ArrayList<Result> getAllResults() {

		ArrayList<Result> data = new ArrayList<Result>();
		Cursor cursor = db.rawQuery("SELECT * FROM RESULTS ;", null);
		boolean hasMoreData = cursor.moveToFirst();

		while (hasMoreData) {
			String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
			int score = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("SCORE")));
			data.add(new Result(name, score));
			hasMoreData = cursor.moveToNext();
		}

		return data;
	}
	
	public int sumScores(){
		String query = "SELECT SUM (SCORE) AS SCORE FROM RESULTS;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String score = cursor.getString(cursor.getColumnIndex("SCORE"));
		return Integer.parseInt(score);
	}
	
	public int maxScore(){
		String query = "SELECT MAX (SCORE) AS SCORE FROM RESULTS;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String score = cursor.getString(cursor.getColumnIndex("SCORE"));
		return Integer.parseInt(score);
	}
	
	public int numOfPlayers(){
		String query = "SELECT COUNT (DISTINCT USERNAME)  AS P FROM RESULTS;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("P"));
		return Integer.parseInt(count);
	}
	
	public int numOfGames(){
		String query = "SELECT COUNT (SCORE) AS Q FROM RESULTS;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("Q"));
		return Integer.parseInt(count);
	}
	
	public int numOfEven(){
		String query = "SELECT COUNT (SCORE) AS E FROM RESULTS WHERE SCORE%2=0;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("E"));
		return Integer.parseInt(count);
	}
	
	public void clearResults(){
		db.execSQL("DROP TABLE RESULTS");
		createTablesIfNeedBe();
	}

	private void createTablesIfNeedBe() {
		db.execSQL("CREATE TABLE IF NOT EXISTS RESULTS (USERNAME TEXT, SCORE INTEGER);");
	}

	private boolean dbExist() {
		File dbFile = context.getDatabasePath(DB_NAME);
		return dbFile.exists();
	}

}
