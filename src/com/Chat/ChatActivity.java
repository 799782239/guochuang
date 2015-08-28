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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.talk);
		Intent intent = getIntent();
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
		// Intent intent2 = new Intent(ChatActivity.this, ChatService.class);
		// intent.putExtra("sender", sender);
		// startService(intent2);
		// Log.i("TAG", "1");
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("reciver");
		// registerReceiver(broadcastReceiver, filter);
		connect();
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				send();
			}
		});

	}

	private Socket socket = null;
	private BufferedWriter writer = null;
	private BufferedReader reader = null;

	public void connect() {
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					socket = new Socket(ip, 12345);
					writer = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					reader = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					publishProgress("@success");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("state", "add");
					jsonObject.put("Id", sender);
					writer.write(jsonObject.toString() + "\n");
					writer.flush();
				} catch (UnknownHostException e1) {
					Toast.makeText(ChatActivity.this, "无法建立链接",
							Toast.LENGTH_SHORT).show();
				} catch (IOException e1) {
					Toast.makeText(ChatActivity.this, "无法建立链接",
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					String line;
					while ((line = reader.readLine()) != null) {
						publishProgress(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				if (values[0].equals("@success")) {
					Toast.makeText(ChatActivity.this, "链接成功！",
							Toast.LENGTH_SHORT).show();

				}
				DataC dataC = new DataC(values[0], DataC.RECIVER);
				list.add(dataC);
				textAdpter.notifyDataSetChanged();
				// text.append("别人说：" + values[0] + "\n");
				super.onProgressUpdate(values);
			}
		};
		read.execute();
	}

	public void send() {
		try {
			DataC c = new DataC(editText.getText().toString(), DataC.SEND);
			list.add(c);
			textAdpter.notifyDataSetChanged();
			// text.append("我说：" + editText.getText().toString() + "\n");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("state", "chat");
			jsonObject.put("send", reciver);
			jsonObject.put("content", editText.getText().toString());
			writer.write(jsonObject.toString() + "\n");
			writer.flush();
			editText.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	// public void onReceive(Context context, Intent intent) {
	// if (intent.getAction() == "reciver") {
	// String content = intent.getStringExtra("content");
	// DataC dataC = new DataC(content, DataC.RECIVER);
	// list.add(dataC);
	// textAdpter.notifyDataSetChanged();
	//
	// }
	// }
	// };
	@Override
	protected void onPause() {
		super.onPause();
	}

}
