package com.Login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;

import com.CommonData.StringData;
import com.FriendData.DataFromServerToXML;
import com.Map.Mapactivity;
import com.example.notification.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends Activity {

	static StringData myData = new StringData();
	// �ؼ�����
	private EditText username, password;
	private CheckBox rem_pw, Auto_login;
	private ImageButton btn_login;
	private ProgressDialog dialog;
	private SharedPreferences sharedPreferences;
	private String userNameValue, passwordValue;
	private String loginState = null;

	// �ӷ������˻������
	static StringData mydata = new StringData();

	// �õ���������ַ
	private static String PATH = myData.getLoginPath();

	// URLλ����Ϣ
	private StringData stringData = new StringData();
	private String RefreshFriendInfoPath = stringData
			.getRefreshFriendInfoPath();;

	// ������Ϣ,ֻ�ᴫ�ݻ��ü�����Ϣ��IP��ַ����������
	private String MyName;
	private String MyIP = null;
	private Double MyLocal_longitude = 0.0;
	private Double MyLocal_latitude = 0.0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �������������
		Typeface fontFace = Typeface.createFromAsset(getAssets(),
				"fonts/Kim's GirlType.ttf");
		// ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		sharedPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		username = (EditText) findViewById(R.id.username_ed);
		password = (EditText) findViewById(R.id.password_ed);
		rem_pw = (CheckBox) findViewById(R.id.remember_cb);
		Auto_login = (CheckBox) findViewById(R.id.auto_login_cb);
		btn_login = (ImageButton) findViewById(R.id.login_bt);
		dialog = new ProgressDialog(this);
		dialog.setMessage("���ڵ�½....");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		username.setTypeface(fontFace);
		password.setTypeface(fontFace);
		// �жϼ�ס�����ѡ���״̬
		// getBoolean����һΪ�ļ��е�xmlֵ��������λȱʡֵ
		if (sharedPreferences.getBoolean("ISCHECK", false)) {

			// ����Ĭ���Ǽ�¼����״̬
			rem_pw.setChecked(true);
			username.setText(sharedPreferences.getString("USER_NAME", ""));
			password.setText(sharedPreferences.getString("PASSWORD", ""));

			// �ж��Զ���¼��ѡ��״̬
			if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {

				new Thread(new getFriendInfoThread()).start();
				// ����Ĭ�����Զ���½״̬
				Auto_login.setChecked(true);
				// ��ת����
			}
		}

		// ��½�����¼� ����Ĭ��Ϊ�û���Ϊ��fuhao ���룺fuhao
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��½�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ
				new Thread(new getFriendInfoThread()).start();
				new AuthenticationTask().execute(PATH);

			}
		});

		// ������ס�����ѡ��ť�¼�
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {
					sharedPreferences.edit().putBoolean("ISCHECK", true)
							.commit();
				} else {
					sharedPreferences.edit().putBoolean("ISCHECK", false)
							.commit();
				}
			}
		});

		// �����Զ���½��ѡ���¼�
		Auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (Auto_login.isChecked()) {
					sharedPreferences.edit().putBoolean("AUTO_ISCHECK", true)
							.commit();
				} else {
					sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false)
							.commit();
				}
			}
		});

	}

	/**
	 * @author hao ʹ���첽����Ĺ��� 1������һ���̳�AsyncTask ��ע��������������
	 *         2����һ��������ʾҪִ�е�����ͨ���������·�����ڶ���������ʾ���ȵĿ̶� ��������������ʾ����ִ�еķ��ؽ��
	 * 
	 */
	public class AuthenticationTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String loginResult = "";
			try {
				URL url = new URL(params[0]);
				Map<String, String> userInfo = new HashMap<String, String>();
				userNameValue = username.getText().toString();
				passwordValue = password.getText().toString();
				userInfo.put("USER_NAME", userNameValue);
				userInfo.put("PASSWORD", passwordValue);

				StringBuffer buffer = new StringBuffer();
				if (userInfo != null && !userInfo.isEmpty()) {
					for (Map.Entry<String, String> entry : userInfo.entrySet()) {
						buffer.append(entry.getKey())
								.append("=")
								.append(URLEncoder.encode(entry.getValue(),
										"utf-8")).append("&");
					}
					buffer.deleteCharAt(buffer.length() - 1);
				}
				System.out.println("-->>" + buffer.toString());
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				urlConnection.setRequestMethod("POST");
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				urlConnection.setConnectTimeout(3000);
				byte[] mydata = buffer.toString().getBytes();
				urlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				urlConnection.setRequestProperty("Content-Length",
						String.valueOf(mydata.length));
				OutputStream outputStream = urlConnection.getOutputStream();
				outputStream.write(mydata, 0, mydata.length);
				outputStream.close();

				int responseCode = urlConnection.getResponseCode();

				if (responseCode == 200) {
					loginResult = changeInputStream(
							urlConnection.getInputStream(), "utf-8");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return loginResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loginState = result;
			if (rem_pw.isChecked()) {
				// ��ס�û���������
				Editor editor = sharedPreferences.edit();
				editor.putString("USER_NAME", userNameValue);
				editor.putString("PASSWORD", passwordValue);
				editor.commit();
			}
			if (loginState.equals("pass")) {
				Toast.makeText(LoginActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(LoginActivity.this,
						Mapactivity.class);
				intent.putExtra("user", username.getText());
				LoginActivity.this.startActivity(intent);

				// ��ת����
			} else if (loginState.equals("not pass")) {
				Toast.makeText(LoginActivity.this, "�û�����������������µ�½",
						Toast.LENGTH_LONG).show();
			} else if (loginState.equals("not find")) {
				Toast.makeText(LoginActivity.this, "���û�������", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(LoginActivity.this, "������������״̬",
						Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}

	}

	public class getFriendInfoThread implements Runnable {
		// ˢ����Ϣ���
		String uploadingResult = null;
		// �ӷ������õ���XML input��
		InputStream input = null;

		@Override
		public void run() {
			// TODO д����ֹѭ��
			try {
				// //���Լ�����Ϣ���ݵ���������
				// URL RefreshMyDataUrl = new URL(RefreshMyDataPath);
				// Map<String,String> mydata = new HashMap<String,
				// String>();
				// mydata.put("IP", value)

				// �������û������ݵ��������У���������ô��û�������������Ϣ���ڷ���������XML�ĵ���

				// TODO �ڴ˴�������IP�����Ͱٶȵ�ͼSDK��ȡ������IP��ַ��λ������
				// MyIP = ��������������;
				// MyLocal_longitude = ��������������;
				// MyLocal_latitude = ����������;

				// ��ñ���IP��ַ
				Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces();
				while (en.hasMoreElements()) {
					NetworkInterface networkInterface = (NetworkInterface) en
							.nextElement();
					Enumeration<InetAddress> inet = networkInterface
							.getInetAddresses();
					while (inet.hasMoreElements()) {
						InetAddress ip = (InetAddress) inet.nextElement();
						if (!ip.isLoopbackAddress()
								&& InetAddressUtils.isIPv4Address(ip
										.getHostAddress())) {
							MyIP = ip.getHostAddress();
						}
					}
				}

				URL url = new URL(RefreshFriendInfoPath);
				Map<String, String> userInfo = new HashMap<String, String>();
				MyName = username.getText().toString();
				userInfo.put("USER_NAME", MyName);
				userInfo.put("IP", MyIP);
				userInfo.put("Local_longitude", MyLocal_longitude.toString());
				userInfo.put("Local_latitude", MyLocal_latitude.toString());

				StringBuffer buffer = new StringBuffer();
				if (userInfo != null && !userInfo.isEmpty()) {
					for (Map.Entry<String, String> entry : userInfo.entrySet()) {
						buffer.append(entry.getKey())
								.append("=")
								.append(URLEncoder.encode(entry.getValue(),
										"utf-8")).append("&");
					}
					buffer.deleteCharAt(buffer.length() - 1);
				}
				System.out.println("-->>-->>" + buffer.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				urlConnection.setConnectTimeout(3000);
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				urlConnection.setRequestMethod("POST");
				byte[] mydata = buffer.toString().getBytes();

				urlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				urlConnection.setRequestProperty("Content-Length",
						String.valueOf(mydata.length));
				OutputStream outputStream = urlConnection.getOutputStream();
				outputStream.write(mydata, 0, mydata.length);
				outputStream.close();

				int responseCode = urlConnection.getResponseCode();

				if (responseCode == 200) {
					uploadingResult = changeInputStream(
							urlConnection.getInputStream(), "utf-8");
				}

				String FriendXMlPath = "http://172.23.191.1:8080/NoticeServer/"
						+ username.getText().toString() + "'friend.xml";
				URL FriendXMlURL = new URL(FriendXMlPath);
				HttpURLConnection GetXMLconnection = (HttpURLConnection) FriendXMlURL
						.openConnection();
				GetXMLconnection.setConnectTimeout(3000);
				GetXMLconnection.setDoInput(true);
				GetXMLconnection.setRequestMethod("GET");
				int GetXMLresponseCode = GetXMLconnection.getResponseCode();
				if (GetXMLresponseCode == 200) {
					input = GetXMLconnection.getInputStream();
				}

				DataFromServerToXML dataFromServerToXML = new DataFromServerToXML(
						input);
				dataFromServerToXML.ServerDataToList();
				dataFromServerToXML.ListDataToXML();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public String changeInputStream(InputStream inputStream, String encode) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), encode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
