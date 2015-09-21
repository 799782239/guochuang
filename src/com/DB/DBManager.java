package com.DB;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBConnect db;
	private Context context;
	private SQLiteDatabase sqLiteDatabase;
	private String name;

	public DBManager(Context context) {
		this.context = context;
		db = new DBConnect(this.context);

	}

	public void OnCreate(String name) {
		sqLiteDatabase = db.getWritableDatabase();
		this.name = name;
		String createTable = "CREATE TABLE IF NOT EXISTS MyFriend_" + name
				+ "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "msgData BLOB," + "status INTEGER)";
		sqLiteDatabase.execSQL(createTable);
		sqLiteDatabase.close();
	}

	public void add(User user) {
		sqLiteDatabase = db.getWritableDatabase();
		ContentValues cv = new ContentValues();
		String content = user.getMsgData();
		try {
			byte[] buffer = content.getBytes("UTF-8");
			cv.put("msgData", buffer);
			cv.put("status", user.getStatus());
			sqLiteDatabase.insert("MyFriend_" + name, null, cv);
			Log.i("TAG", "Ìí¼Ó³É¹¦");
			sqLiteDatabase.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<User> serach(String table) {
		List<User> user = new ArrayList<User>();

		sqLiteDatabase = db.getReadableDatabase();
		String serach = "SELECT * FROM MyFriend_" + table;
		Cursor c = sqLiteDatabase.rawQuery(serach, null);
		while (c.moveToNext()) {
			User u = new User();
			byte[] msgData = c.getBlob(c.getColumnIndex("msgData"));
			int status = c.getInt(c.getColumnIndex("status"));
			String msg;
			try {
				msg = new String(msgData, "UTF-8");
				u.setStatus(status);
				u.setMsgData(msg);
				// System.out.println(msg);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// System.out.println(status);
			user.add(u);

		}
		sqLiteDatabase.close();

		return user;
	}

}
