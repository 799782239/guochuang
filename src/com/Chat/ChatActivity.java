package com.Chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.DB.ChatDBAddThread;
import com.DB.ChatDBThread;
import com.DB.ChatDataCallBack;
import com.DB.DBManager;
import com.DB.User;
import com.example.notification.R;

public class ChatActivity extends Activity {
	private String sender = null;
	private String reciver = null;
	private EditText editText;
	private ListView listView;
	private List<DataC> list;
	private TextAdpter textAdpter;
	private TextView textView;
	private Button send;
	private ChatDBThread thread;
	private ChatDBAddThread addThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.talk);
		Intent intent = getIntent();
		// ʵ�������
		sender = intent.getStringExtra(ChatConfig.SENDER);
		reciver = intent.getStringExtra(ChatConfig.RECIVER);
		send = (Button) this.findViewById(R.id.send);
		editText = (EditText) this.findViewById(R.id.edit);
		listView = (ListView) this.findViewById(R.id.lv);
		textView = (TextView) this.findViewById(R.id.ip);
		// ����Ƿ���������
		DBManager dbManager = new DBManager(this);
		dbManager.OnCreate(reciver);
		textView.setText(reciver);
		list = new ArrayList<DataC>();
		textAdpter = new TextAdpter(list, this);
		listView.setAdapter(textAdpter);

		thread = new ChatDBThread(reciver, new ChatDataCallBack() {

			@Override
			public void result(List<User> list) {
				textAdpter.addAll(list);
				listView.setSelection(listView.getCount() - 1);
				Log.i("TAG", "upload");
			}
		}, this);
		thread.start();
		// ��������
		Intent intent2 = new Intent(ChatActivity.this, ChatService.class);
		intent2.putExtra(ChatConfig.SENDER, sender);
		intent2.putExtra(ChatConfig.RECIVER, reciver);
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent2);
		Log.i("TAG", "1");

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ������Ϣ
				send();
			}
		});

	}

	// ������Ϣ
	public void send() {
		try {
			DataC c = new DataC(editText.getText().toString(), DataC.SEND);
			list.add(c);
			textAdpter.notifyDataSetChanged();
			addThread = new ChatDBAddThread(reciver, this, editText.getText()
					.toString(), DataC.SEND);
			Log.i("TAG", "save");
			addThread.start();
			// �����͵���Ϣ
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("state", "chat");
			jsonObject.put("send", reciver);// sendΪ������Ϣ����
			jsonObject.put("sender", sender);// senderΪ������Ϣ����
			jsonObject.put("content", editText.getText().toString());
			// �Թ㲥��ʽ����
			Intent intent = new Intent();
			intent.setAction(ChatConfig.ACTION_SEND);
			intent.putExtra(ChatConfig.CONTENT, jsonObject.toString());
			sendBroadcast(intent);
			Log.i("TAG", jsonObject.toString());
			editText.setText("");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			// ���ý��չ㲥���ܶԷ�����Ϣ
			if (intent.getAction() == ChatConfig.ACTION_RECIVER) {
				String obj_reciver = intent.getStringExtra("reciver");
				String content = intent.getStringExtra(ChatConfig.CONTENT);
				if (obj_reciver.equals(reciver)) {

					DataC dataC = new DataC(content, DataC.RECIVER);
					list.add(dataC);
					textAdpter.notifyDataSetChanged();
					// �õ���˭���͵���Ϣ
					addThread = new ChatDBAddThread(reciver,
							getApplicationContext(), content, DataC.RECIVER);
					addThread.start();
				} else {
					addThread = new ChatDBAddThread(obj_reciver,
							getApplicationContext(), content, DataC.RECIVER);
					addThread.start();
				}

			}
		}
	};

	@Override
	protected void onPause() {
		// ע������
		unregisterReceiver(broadcastReceiver);
		// ��֪service�����̨
		Intent intent = new Intent();
		intent.setAction(ChatConfig.ACTION_STATE);
		sendBroadcast(intent);
		super.onPause();
	}

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ChatConfig.ACTION_RECIVER);
		// �ָ�������ע��㲥����
		registerReceiver(broadcastReceiver, filter);
		Intent intent = new Intent();
		intent.setAction(ChatConfig.ACTION_CREAT);
		sendBroadcast(intent);
		super.onStart();
	}

	@Override
	protected void onStop() {
		stopService(new Intent(ChatActivity.this, ChatService.class));
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
