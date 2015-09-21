package com.DB;

import android.content.Context;

public class ChatDBAddThread extends Thread {
	private String name;
	private Context context;
	private String content;
	private int status;

	public ChatDBAddThread(String name, Context context, String content,
			int status) {
		this.context = context;
		this.name = name;
		this.content = content;
		this.status = status;
	}

	@Override
	public void run() {
		User user = new User();
		user.setMsgData(content);
		user.setStatus(status);
		DBManager dbManager = new DBManager(context);
		dbManager.OnCreate(name);
		dbManager.add(user);
	}
}
