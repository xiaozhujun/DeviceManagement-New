package com.csei.devicemanagent;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.csei.client.CasClient;
import com.csei.devicesmanagement.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button button;
	private EditText passwords;
	private EditText username;
	private String uname;
	private String pswords;
	private CheckBox show_passwords, remember_passwords;
	private JSONObject jsonObject;
	private String role1;
	private String name1;
	private String id1;
	private String image1;
	private String sex1;
	private String status1;
	private String appId1;
	private String appName1;

	private Handler handler;

	// private
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		passwords = (EditText) findViewById(R.id.input_passwords);
		username = (EditText) findViewById(R.id.input_username);

		username.setText("xiaozhujun");
		passwords.setText("123456");

		passwords.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

		passwords.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					uname = username.getText().toString().trim();
					pswords = passwords.getText().toString().trim();
					new Thread(new MyThread()).start();
				}
				return false;
			}
		});

		show_passwords = (CheckBox) findViewById(R.id.show_passwords);
		show_passwords
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (show_passwords.isChecked()) {
							passwords
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {
							passwords
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});

		button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				uname = username.getText().toString().trim();
				pswords = passwords.getText().toString().trim();

				new Thread(new MyThread()).start();
				// Intent intent = new
				// Intent(LoginActivity.this,MainActivity.class);
				// intent.putExtra("id", "6");
				// intent.putExtra("name", "xiaozhujun");
				// startActivity(intent);

			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					Toast.makeText(getApplicationContext(), "登录成功",
							Toast.LENGTH_SHORT).show();
					Intent intent1 = new Intent(LoginActivity.this,
							MainActivity.class);
					HashMap<String, String> map = new HashMap<String, String>();
					map = (HashMap<String, String>) msg.obj;

					intent1.putExtra("id", map.get("id"));
					intent1.putExtra("name", map.get("name"));
					startActivity(intent1);
					finish();
					break;
				case 2:
					String errorString = "用户名或密码错误";
					Toast.makeText(getApplicationContext(), errorString,
							Toast.LENGTH_SHORT).show();
					break;
				case 3:
					String noNetWorkString = "请检查网络连接";
					Toast.makeText(getApplicationContext(), noNetWorkString,
							Toast.LENGTH_SHORT).show();
					break;
				}

			}
		};

	}

	class MyThread implements Runnable {
		public void run() {

			final boolean loginresult = CasClient.getInstance().login(uname,
					pswords,
					getResources().getString(R.string.LOGIN_SECURITY_CHECK));

			if (loginresult) {
				String msg1 = CasClient.getInstance().doGet(
						getResources().getString(R.string.USER_GETIMF));
				try {
					jsonObject = (new JSONObject(msg1)).getJSONObject("data");
					id1 = jsonObject.getString("id");
					name1 = jsonObject.getString("name");
					sex1 = jsonObject.getString("sex");
					role1 = jsonObject.getString("role");
					status1 = jsonObject.getString("status");
					appId1 = jsonObject.getString("appId");
					appName1 = jsonObject.getString("appName");
					image1 = jsonObject.getString("image");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", name1);
				map.put("id", id1);
				Message msg = Message.obtain();

				msg.obj = map;
				msg.what = 1;
				handler.sendMessage(msg);
			} else {

				Message msg = Message.obtain();
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}

	}

}
