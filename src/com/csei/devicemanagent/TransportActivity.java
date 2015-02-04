package com.csei.devicemanagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.csei.adapter.DeviceListAdapter;
import com.csei.application.MyApplication;
import com.csei.client.CasClient;
import com.csei.devicesmanagement.R;
import com.csei.entity.Device;
import com.csei.util.JSONUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TransportActivity extends Activity{
	
	private Button scanButton;
	private ImageView left_back;
	private Button upload_btn;
	private ListView deviceListView;
	private ArrayList<Device> deviceList;
	private DeviceListAdapter myAdapter;
	
	private String userId;
	private String userName;
	
	private String driverName;
	private String telephone;
	private String destination;
	private String address;
	private String image = "";
	
	private ArrayList<Integer> isOpen;
	
	//各种dialog
	private AlertDialog.Builder driverNameDialog;
	private AlertDialog.Builder telephoneDialog;
	private AlertDialog.Builder destinationDialog;
	private AlertDialog.Builder addressDialog;
	private AlertDialog.Builder takephotoDialog;
	private AlertDialog.Builder uploadDialog;
	private ProgressDialog uploadProgressDialog;
	
	private LayoutInflater layoutInflater;
	
	private View driverNameView;
	private View telephoneView;
	private View destinationView;
	private View addressView;
	
	private EditText driverNameEdt;
	private EditText telephoneEdt;
	private EditText destinationEdt;
	private EditText addressEdt;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transportactivity);
		MyApplication.getInstance().addActivity(this);
		
		Intent intent11 = getIntent();
		userId = intent11.getStringExtra("userId");
		userName = intent11.getStringExtra("userName");
		
		layoutInflater = LayoutInflater.from(getApplicationContext());
		
		scanButton = (Button) findViewById(R.id.scan_transport);
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_transport);
		upload_btn= (Button) findViewById(R.id.btn_topbar_upload_transport);
		deviceListView = (ListView) findViewById(R.id.devicelist_transport);
		
		deviceList = new ArrayList<Device>();
		isOpen = new ArrayList<Integer>();
		
		myAdapter = new DeviceListAdapter(TransportActivity.this, deviceList,isOpen);
		deviceListView.setAdapter(myAdapter);
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 2:
					uploadProgressDialog.dismiss();
					Toast.makeText(TransportActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					uploadProgressDialog.dismiss();
					Toast.makeText(TransportActivity.this, "记录和图片均上传成功", Toast.LENGTH_SHORT).show();
					deviceList.clear();
					myAdapter.notifyDataSetChanged();
					break;
				case 4:
					uploadProgressDialog.dismiss();
					Toast.makeText(TransportActivity.this, "图片上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				}
		};
		
		//各种dialog声明的地方
		driverNameDialog = new AlertDialog.Builder(TransportActivity.this);
		driverNameView = layoutInflater.inflate(R.layout.view_drivername, null);
		driverNameEdt = (EditText) driverNameView.findViewById(R.id.view_drivername_edt);
		driverNameDialog.setTitle("提示").setMessage("请输入司机姓名").setView(driverNameView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				driverName = driverNameEdt.getText().toString();
				if("".equals(driverName)){
					Toast.makeText(TransportActivity.this, "司机姓名不能为空", Toast.LENGTH_SHORT).show();
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);
						 
						} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}else{
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
						} catch (Exception e) {
						e.printStackTrace();
						}
					ViewGroup p = (ViewGroup) telephoneView.getParent(); 
			        if (p != null) { 
			        	p.removeAllViewsInLayout(); 
			        }
					telephoneDialog.show();
				}
			}
		}).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
					} catch (Exception e) {
					e.printStackTrace();
					}
				driverName = "";
				ViewGroup p = (ViewGroup) driverNameView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
			}
		});
		
		telephoneDialog = new AlertDialog.Builder(TransportActivity.this);
		telephoneView = layoutInflater.inflate(R.layout.view_telephone, null);
		telephoneEdt = (EditText) telephoneView.findViewById(R.id.view_telephone_edt);
		telephoneDialog.setTitle("提示").setMessage("请输入司机电话").setView(telephoneView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				telephone = telephoneEdt.getText().toString();
				if("".equals(telephone)){
					Toast.makeText(TransportActivity.this, "司机电话不能为空", Toast.LENGTH_SHORT).show();
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);
						 
						} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}else{
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
						} catch (Exception e) {
						e.printStackTrace();
					}
					ViewGroup p = (ViewGroup) addressView.getParent(); 
			        if (p != null) { 
			        	p.removeAllViewsInLayout(); 
			        }
					addressDialog.show();
				}
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
					} catch (Exception e) {
					e.printStackTrace();
					}
				telephone = "";
				ViewGroup p = (ViewGroup) driverNameView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				driverNameDialog.show();
			}
		});
		
		destinationDialog = new AlertDialog.Builder(TransportActivity.this);
		destinationView = layoutInflater.inflate(R.layout.view_destination, null);
		destinationEdt = (EditText) destinationView.findViewById(R.id.view_destination_edt);
		destinationDialog.setTitle("提示").setMessage("请输入目的地").setView(destinationView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				destination = destinationEdt.getText().toString();
				if("".equals(destination)){
					Toast.makeText(TransportActivity.this, "目的地不能为空", Toast.LENGTH_SHORT).show();
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);
						} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}else{
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
						} catch (Exception e) {
						e.printStackTrace();
					}
					takephotoDialog.show();
				}
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
					} catch (Exception e) {
					e.printStackTrace();
					}
				destination = "";
				ViewGroup p = (ViewGroup) addressView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				addressDialog.show();
			}
		});
		
		addressDialog = new AlertDialog.Builder(TransportActivity.this);
		addressView = layoutInflater.inflate(R.layout.view_address, null);
		addressEdt = (EditText) addressView.findViewById(R.id.view_address_edt);
		addressDialog.setTitle("提示").setMessage("请输入出发地").setView(addressView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				address = addressEdt.getText().toString();
				if("".equals(address)){
					Toast.makeText(TransportActivity.this, "出发地不能为空", Toast.LENGTH_SHORT).show();
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);
						 
						} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}else{
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
						} catch (Exception e) {
						e.printStackTrace();
					}
					ViewGroup p = (ViewGroup) destinationView.getParent(); 
			        if (p != null) { 
			        	p.removeAllViewsInLayout(); 
			        }
					destinationDialog.show();
				}
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
					} catch (Exception e) {
					e.printStackTrace();
					}
				address = "";
				ViewGroup p = (ViewGroup) telephoneView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				telephoneDialog.show();
			}
		});
		
		takephotoDialog = new AlertDialog.Builder(TransportActivity.this);
		takephotoDialog.setTitle("提示").setMessage("是否拍照上传？").setPositiveButton("拍照", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TransportActivity.this,CameraActivity.class);
				intent.putExtra("activity", "TransportActivity");
				intent.putExtra("activityName", "transport");
				startActivityForResult(intent, 1);
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ViewGroup p = (ViewGroup) addressView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				addressDialog.show();
			}
		}).setNeutralButton("不拍照", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				uploadDialog.show();
			}
		});
		
		uploadDialog = new AlertDialog.Builder(TransportActivity.this);
		uploadDialog.setTitle("提示").setMessage("现在上传？").setPositiveButton("上传", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				uploadProgressDialog.show();
				new Thread(new UploadThread()).start();
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				takephotoDialog.show();
			}
		});
		
		uploadProgressDialog = new ProgressDialog(TransportActivity.this);
		uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		uploadProgressDialog.setTitle("提示");
		uploadProgressDialog.setMessage("正在上传……");
		
		left_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Back();
			}
		});
		
		scanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(TransportActivity.this,ScanCodeActivity.class);
				startActivityForResult(it, 0);	
			}
		});
		
		deviceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				isOpen = myAdapter.getIsOpen();
				if(isOpen.get(position)==0)
					isOpen.set(position, 1);
				else 
					isOpen.set(position, 0);
				myAdapter.notifyDataSetChanged();
			}
		});
		
		upload_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(myAdapter.getDeviceList().isEmpty()){
					Toast.makeText(TransportActivity.this, "请先扫描设备二维码", Toast.LENGTH_SHORT).show();
				}else{
					driverNameDialog.show();
				}
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0){
			String code = data.getStringExtra("Code");
			Log.i("code", code);
			if (code != null) {
				if (!code.substring(0, 4).equals("rent")) {
					Toast.makeText(TransportActivity.this, "请扫设备专用二维码！", Toast.LENGTH_SHORT).show();
					return;
				} else {
					Device device = new Device();
					device.setId(Integer.parseInt(code.split(",")[1]));
					device.setNumber(code.split(",")[2]);
					device.setBatchId(code.split(",")[3]);
					device.setBachNumber(code.split(",")[4]);
					device.setTypeId(code.split(",")[5]);
					device.setDeviceType(code.split(",")[6]);
					device.setIsMainDevice(Integer.parseInt(code.split(",")[7]));
					deviceList = myAdapter.getDeviceList();
					deviceList.add(device);
					isOpen = myAdapter.getIsOpen();
					isOpen.add(0);
					myAdapter.notifyDataSetChanged();
				}
			}
		}else if(requestCode==1){
			image = data.getExtras().getString("image");
			if (data.getExtras().getString("image") != null) {
				Toast.makeText(TransportActivity.this, "照片保存路径为："+image, Toast.LENGTH_SHORT).show();
				Log.i("image", data.getExtras().getString("image"));
				uploadDialog.show();
			}
		}else {
			Toast.makeText(TransportActivity.this, "error", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void Back(){
		if(myAdapter.getDeviceList().isEmpty()){
			finish();
		}else{
			Builder alertDialog = new AlertDialog.Builder(TransportActivity.this);
			alertDialog.setTitle("提示").setMessage("是否放弃此次操作？").setNegativeButton("取消", null).setPositiveButton("放弃", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					deviceList = myAdapter.getDeviceList();
					deviceList.clear();
					myAdapter.setDeviceList(deviceList);
					myAdapter.notifyDataSetChanged();
					finish();
				}
			}).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	class UploadThread implements Runnable{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			String deviceId = new String(",");
			deviceList = myAdapter.getDeviceList();
			HashMap<String, String> uploadMap = new HashMap<String, String>();
			//注意注意
			uploadMap.put("driver", driverName);
			uploadMap.put("telephone", telephone);
			uploadMap.put("destination", destination);
			uploadMap.put("address", address);
			
			for(Device device:deviceList){
				deviceId += device.getId()+",";
			}
			
			uploadMap.put("deviceId", deviceId);
			
			int id = 0;
			try {
				id = JSONUtils.UploadTransport(getResources().getString(R.string.TRANSPORT_ADD),uploadMap);
				if(id!=0){
					msg.what = 2;
				} else {
					msg.what = 0;
				}
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (id != 0 && image != null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("id", id + "");
				try {
					String result = CasClient.getInstance().doSendImage(getResources().getString(R.string.TRANSPORT_UPLOAD), image,params);
					Log.i("asdasd","dasdasdasdasddddddddddddddddddddddddddddddddddddddddddd" + result);
					int code = Integer.parseInt((new JSONObject(result).getString("code")));
					Log.i("code", Integer.toString(code));
					if (code == 200) {
						msg.what = 3;
					} else {
						msg.what = 4;
					}
				} catch (Exception e) {
					msg.what = 4;
					e.printStackTrace();
				}
			}
			handler.sendMessage(msg);
//		}
	}
	
	}
}

