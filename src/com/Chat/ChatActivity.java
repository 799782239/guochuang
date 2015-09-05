package com.Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.notification.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	private String sender = null;
	private String reciver = null;
	private String ip = "192.168.1.109";
	private EditText editText;
	private ListView listView;
	private List<DataC> list;
	private TextAdpter textAdpter;
	private TextView textView;
	private Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.talk);
		Intent intent = getIntent();
		// ʵ�������
		sender = intent.getStringExtra("sender");
		reciver = intent.getStringExtra("reciver");
		send = (Button) this.findViewById(R.id.send);
		editText = (EditText) this.findViewById(R.id.edit);
		listView = (ListView) this.findViewById(R.id.lv);
		textView = (TextView) this.findViewById(R.id.ip);
		textView.setText(reciver);
		list = new ArrayList<DataC>();
		textAdpter = new TextAdpter(list, this);
		listView.setAdapter(textAdpter);
		// ��������
		Intent intent2 = new Intent(ChatActivity.this, ChatService.class);
		intent2.putExtra("sender", sender);
		intent2.putExtra("reciver", reciver);
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

	public void send() {
		try {
			DataC c = new DataC(editText.getText().toString(), DataC.SEND);
			list.add(c);
			textAdpter.notifyDataSetChanged();
			// �����͵���Ϣ
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("state", "chat");
			jsonObject.put("send", reciver);
			jsonObject.put("content", editText.getText().toString());
			// �Թ㲥��ʽ����
			Intent intent = new Intent();
			intent.setAction("send");
			intent.putExtra("content", jsonObject.toString());
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
			if (intent.getAction() == "reciver") {
				String content = intent.getStringExtra("content");
				DataC dataC = new DataC(content, DataC.RECIVER);
				list.add(dataC);
				textAdpter.notifyDataSetChanged();

			}
		}
	};

	@Override
	protected void onPause() {
		// ע������
		unregisterReceiver(broadcastReceiver);
		// ��֪service�����̨
		Intent intent = new Intent();
		intent.setAction("state");
		sendBroadcast(intent);
		super.onPause();
	}

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("reciver");
		// �ָ�������ע��㲥����
		registerReceiver(broadcastReceiver, filter);
		Intent intent = new Intent();
		intent.setAction("create");
		sendBroadcast(intent);
		super.onStart();
	}

}
