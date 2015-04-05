package ru.samsung.itschool.dbgame;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
		int uid = this.getPlayerIDByName(username);
		if(uid == -1){
			ContentValues cv = new ContentValues();
			cv.put("NAME", username);
			db.insert("USERS", null, cv);
			uid = this.getPlayerIDByName(username);
		}
			ContentValues cv = new ContentValues();
			cv.put("USERID", uid);
			cv.put("SCORE", score);
			db.insert("RESULTS", null, cv);
	}
	
	/*void addResult(String username, int score) {
		db.execSQL("INSERT INTO RESULTS VALUES ('" + username + "', " + score
				+ ");");
	}*/
	
	ArrayList<Result> getAllResults() {

		ArrayList<Result> data = new ArrayList<Result>();
		Cursor cursor = db.rawQuery("SELECT NAME, SCORE FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS.USERID ORDER BY SCORE DESC LIMIT 100", null);
		boolean hasMoreData = cursor.moveToFirst();

		while (hasMoreData) {
			String name = cursor.getString(cursor.getColumnIndex("NAME"));
			int score = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("SCORE")));
			data.add(new Result(name, score));
			hasMoreData = cursor.moveToNext();
		}
		return data;
	}

	/*ArrayList<Result> getAllResults() {

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
	}*/
	
	public int sumScores(){
		String query = "SELECT SUM (SCORE) AS SCORE FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS.USERID ORDER BY SCORE DESC;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String score = cursor.getString(cursor.getColumnIndex("SCORE"));
		return Integer.parseInt(score);
	}
	
	public int maxScore(){
		String query = "SELECT MAX (SCORE) AS SCORE FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS.USERID ORDER BY SCORE DESC;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String score = cursor.getString(cursor.getColumnIndex("SCORE"));
		return Integer.parseInt(score);
	}
	
	public int maxScoreByPlayer(int uid){
		String query = "SELECT MAX (SCORE) AS SCORE FROM RESULTS WHERE USERID='" + uid + "'";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String score = cursor.getString(cursor.getColumnIndex("SCORE"));
		return Integer.parseInt(score);
	}
	
	public int numOfPlayers(){
		String query = "SELECT COUNT (DISTINCT NAME) AS P FROM USERS;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("P"));
		return Integer.parseInt(count);
	}
	
	public int numOfGames(){
		String query = "SELECT COUNT (SCORE) AS Q FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS.USERID ORDER BY SCORE DESC;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("Q"));
		return Integer.parseInt(count);
	}
	
	public int numOfGamesByPlayer(int uid){
		String query = "SELECT COUNT (SCORE) AS Q FROM RESULTS WHERE USERID='" + uid + "'";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("Q"));
		return Integer.parseInt(count);
	}
	
	public int numOfEven(){
		String query = "SELECT COUNT (SCORE) AS Q FROM RESULTS WHERE SCORE % 2 = 0;";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(cursor.getColumnIndex("Q"));
		return Integer.parseInt(count);
	}
	
	public void deletePData(int uid){
		db.execSQL("DELETE FROM RESULTS WHERE USERID='" + uid + "'");
		db.execSQL("DELETE FROM USERS WHERE USERID='" + uid + "'");
	}
	
	public void clearResults(){
		db.execSQL("DROP TABLE RESULTS");
		db.execSQL("DROP TABLE USERS");
		createTablesIfNeedBe();
	}

	private void createTablesIfNeedBe() {
		db.execSQL("CREATE TABLE IF NOT EXISTS RESULTS (USERID INTEGER, SCORE INTEGER);");
		db.execSQL("CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY ASC, NAME TEXT, PIC TEXT);");
	}

	public boolean dbExist() {
		File dbFile = context.getDatabasePath(DB_NAME);
		return dbFile.exists();
	}
	
	//========================================================================================================================//
	
	int getPlayerIDByName(String username) {
		Cursor cursor = db.rawQuery("SELECT USERID FROM USERS WHERE NAME='"
				+ username + "'", null);
		if (!cursor.moveToFirst()) {
			return -1;
		}
		return cursor.getInt(cursor.getColumnIndex("USERID"));
	}
	
	String getUserName(int userid) {
		Cursor cursor = db.rawQuery("SELECT NAME FROM USERS WHERE USERID='"
				+ userid + "'", null);
		if (cursor.moveToFirst()) return cursor.getString(0);
		return "";
	}
	
	String getUserPicName(int userid) {
		Cursor cursor = db.rawQuery("SELECT PIC FROM USERS WHERE USERID='"
				+ userid + "'", null);
		cursor.moveToFirst();
		//Если нет фото - возвращаем пустую строку
		if (!cursor.moveToFirst() || cursor.isNull(0)) return "";  
		else return cursor.getString(0);
	}
	
	Bitmap getUserPic(int userid){
		String playerPic = this.getUserPicName(userid);	
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(userPics_path+"/"+playerPic+".png", options);
		
		return bitmap;
	}
	
	void userUpdate(int userid, String username, String pic)
	{
		//напишите запрос правильно
		//db.execSQL("UPDATE USERS SET NAME = 'NameStub' WHERE USERID = -1;");
		ContentValues cv = new ContentValues();
		cv.put("NAME", username);
		cv.put("PIC", pic);
		db.update("USERS", cv, "USERID=?", new String[]{userid+""});
	}

}
