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

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ChatService extends Service {

	private String sender = null;
	private Socket socket = null;
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	private String ip = "192.168.1.109";
	private Boolean state = true;
	private NotificationManager manager;
	private Builder builder;
	private Boolean first = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		first = true;
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		IntentFilter filter = new IntentFilter();
		filter.addAction("send");
		registerReceiver(broadcastReceiver, filter);
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("state");
		registerReceiver(broadcastReceiver, filter2);
		if (first) {
			sender = intent.getStringExtra("sender");
			System.out.println(sender);
			new Thread(new Runnable() {
				@Override
				public void run() {
					connectSocket();
					register();
					readMessage();
				}
			}).start();
		}
		first = false;
		flags = START_STICKY;
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
				} else {
					Intent intent2 = new Intent(this, ChatActivity.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(
							this, 0, intent2, 0);
					manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					builder = new Notification.Builder(this);
					builder.setContentTitle("message");
					builder.setContentText(line);
					builder.setSmallIcon(com.example.notification.R.drawable.ic_launcher);
					builder.setContentIntent(pendingIntent);
					manager.notify(1003, builder.build());
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

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == "send") {
				String content = intent.getStringExtra("content");
				try {
					Log.i("TAG", "5");
					writer.write(content + "\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (intent.getAction() == "state") {
				Log.i("TAG", "7");
				state = false;
			}
		}
	};

}
