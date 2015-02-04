package com.csei.devicemanagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import com.csei.application.MyApplication;
import com.csei.client.CasClient;
import com.csei.devicesmanagement.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GridView gridView;
	int[] images = { R.drawable.img_stock_in, R.drawable.img_stock_out,
			R.drawable.img_install, R.drawable.img_uninstall,
			R.drawable.img_transport, R.drawable.img_history };
	String[] functions = { "设备入库", "设备出库", "设备安装", "设备卸载", "设备运输", "历史记录" };
	private String userId;
	private RelativeLayout account;
	private String userName;
	
	/** 
	 * 菜单、返回键响应 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK)  {   
			exitBy2Click();
		}  
		return false;  
	}  
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  

	private void exitBy2Click() {  
		Timer tExit = null;  
		if (isExit == false) {  
			isExit = true; // 准备退出  
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
			tExit = new Timer();  
			tExit.schedule(new TimerTask() {  
				@Override  
				public void run() {  
					isExit = false; // 取消退出  
				}  
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  

		} else {  
			MyApplication.getInstance().exit();
		}  
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent11=getIntent();
		userId=intent11.getStringExtra("id");
		userName=intent11.getStringExtra("name");
		Log.i("userName", userName);
		Log.i("id", userId);
		account=(RelativeLayout) findViewById(R.id.tv_topbar_right_map_layout_main);
		account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle(userName).setItems(new String[]{"切换账户","退出"}, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							Builder alertDialog_switch = new AlertDialog.Builder(MainActivity.this);
							alertDialog_switch.setTitle("提示").setMessage("是否切换账户？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									CasClient.getInstance().reset();
									final Intent it = getPackageManager().getLaunchIntentForPackage(getPackageName());
									it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(it);
								}
							}).setNegativeButton("取消", null).show();
							break;
						case 1:
							Builder alertDialog_exit = new AlertDialog.Builder(MainActivity.this);
							alertDialog_exit.setTitle("提示").setMessage("是否退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									MyApplication.getInstance().exit();
								}
							}).setNegativeButton("取消", null).show();
							break;
						}
					}

				}).show();
			}
		});
		gridView = (GridView) findViewById(R.id.function_choose);
		gridView.setAdapter(getGridAdapter(functions, images));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:

					Intent it1 = new Intent(MainActivity.this,
							StockinActivity.class);
					it1.putExtra("userId", userId);
					it1.putExtra("userName", userName);
					startActivity(it1);

					break;
				case 1:

					Intent it2 = new Intent(MainActivity.this,
							StockoutActivity.class);
					it2.putExtra("userId", userId);
					it2.putExtra("userName", userName);
					startActivity(it2);

					break;
				case 2:

					Intent it3 = new Intent(MainActivity.this,
							InstallActivity.class);
					it3.putExtra("userId", userId);
					it3.putExtra("userName", userName);
					startActivity(it3);

					break;
				case 3:

					Intent it4 = new Intent(MainActivity.this,
							UninstallActivity.class);
					it4.putExtra("userId", userId);
					it4.putExtra("userName", userName);
					startActivity(it4);

					break;
				case 4:

					Intent it5 = new Intent(MainActivity.this,
							TransportActivity.class);
					it5.putExtra("userId", userId);
					it5.putExtra("userName", userName);
					startActivity(it5);

					break;
//				case 5:
//					Intent it6=new Intent(MainActivity.this,HistoryActivity.class);
//					it6.putExtra("userId", userId);
//					it6.putExtra("userName", userName);
//					startActivity(it6);
				}
			}
		});
	}

	private ListAdapter getGridAdapter(String[] functions, int[] images) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < functions.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemText", functions[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.grid_item_main,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.iv_icon, R.id.tv_function }) {
		};
		return simperAdapter;

	}
}
