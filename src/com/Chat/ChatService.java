package com.Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ChatService extends Service {

	private String sender = null;
	private String reciver = null;
	private Socket socket = null;
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	private String ip = "192.168.1.109";
	private Boolean state = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		sender = intent.getStringExtra("sender");
		new Thread(new Runnable() {
			@Override
			public void run() {
				connectSocket();
				register();

				readMessage();

			}
		}).start();

		return super.onStartCommand(intent, flags, startId);
	}

	protected void readMessage() {
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (state) {
					Intent intent2 = new Intent();
					intent2.setAction("reciver");
					intent2.putExtra("content", line);
					sendBroadcast(intent2);
					Log.i("TAG", "4");
				}else
				{
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void register() {
		try {
			Log.i("TAG", "3");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("state", "add");
			jsonObject.put("Id", sender);
			writer.write(jsonObject.toString() + "\n");
			writer.flush();
			Log.i("TAG", jsonObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void connectSocket() {
		try {
			socket = new Socket(ip, 12345);
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			Log.i("TAG", "2");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
