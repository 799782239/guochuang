package com.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ChatDBThread extends Thread {
	private ChatDataCallBack callBack;
	private String name;
	private Context context;

	public ChatDBThread(String name, ChatDataCallBack callBack, Context context) {
		this.callBack = callBack;
		this.context = context;
		this.name = name;
	}

	@Override
	public void run() {
		List<User> user = new ArrayList<User>();
		DBManager dbManager = new DBManager(context);
		user = dbManager.serach(name);
		callBack.result(user);
	}

}
