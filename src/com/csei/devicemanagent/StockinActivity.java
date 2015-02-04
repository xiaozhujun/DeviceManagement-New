package com.csei.devicemanagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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

public class StockinActivity extends Activity{
	
	private Button scanButton;
	private ImageView left_back;
	private Button upload_btn;
	private ListView deviceListView;
	private ArrayList<Device> deviceList;
	private DeviceListAdapter myAdapter;
	
	private String userId;
	private String userName;
	
	private int contractSlected = 0;
	private int storehouseSlected = 0;
	private String stockinNum;
	private String driverName;
	private String carNum;
	private String image;
	
	private int[] contractId;
	private int[] storehouseId;
	private String[] contractString;
	private String[] storehouseString;
	
	//各种dialog
	private AlertDialog.Builder storehouseDialog;
	private AlertDialog.Builder contractDialog;
	private AlertDialog.Builder stockinNumDialog;
	private AlertDialog.Builder driverNameDialog;
	private AlertDialog.Builder carNumDialog;
	private AlertDialog.Builder takephotoDialog;
	private AlertDialog.Builder uploadDialog;
	private ProgressDialog uploadProgressDialog;
	
	//菜单是否展开的标志位
	private ArrayList<Integer> isOpen;
	
	private LayoutInflater layoutInflater;
	
	private View stockinNumView;
	private View driverNameView;
	private View carNumView;
	
	private EditText stockinNumEdt;
	private EditText driverNameEdt;
	private EditText carNumEdt;
	
	private Handler handler;
	
	//测试专用代码段
	
//	private int hasMainDevice = 0;
	//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stockinactivity);
		MyApplication.getInstance().addActivity(this);
		
		Intent intent11 = getIntent();
		userId = intent11.getStringExtra("userId");
		userName = intent11.getStringExtra("userName");
		
		layoutInflater = LayoutInflater.from(getApplicationContext());
		
		scanButton = (Button) findViewById(R.id.scan_stockin);
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_stockin);
		upload_btn= (Button) findViewById(R.id.btn_topbar_upload_stockin);
		deviceListView = (ListView) findViewById(R.id.devicelist_stockin);
		
		deviceList = new ArrayList<Device>();
		isOpen = new ArrayList<Integer>();
		
		myAdapter = new DeviceListAdapter(StockinActivity.this, deviceList,isOpen);
		deviceListView.setAdapter(myAdapter);
		
		new Thread(new DownloadThread()).start();
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					Toast.makeText(StockinActivity.this, "获取仓库及合同列表成功", Toast.LENGTH_SHORT).show();
					storehouseDialog = new AlertDialog.Builder(StockinActivity.this);
					storehouseDialog.setTitle("请选择仓库").setSingleChoiceItems(storehouseString, storehouseSlected, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							storehouseSlected = which;
						}
					}).setPositiveButton("下一步", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							contractDialog.show();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							storehouseSlected = 0;
						}
					});
					contractDialog = new AlertDialog.Builder(StockinActivity.this);
					contractDialog.setTitle("请选择合同").setSingleChoiceItems(contractString, contractSlected, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							contractSlected = which;
						}
					}).setPositiveButton("下一步", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
					        ViewGroup p = (ViewGroup) stockinNumView.getParent(); 
					        if (p != null) { 
					        	p.removeAllViewsInLayout(); 
					        }
					        stockinNumDialog.show(); 
						}
					}).setNegativeButton("上一步", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							storehouseDialog.show();
							contractSlected = 0;
						}
					});
					break;
				case 2:
					uploadProgressDialog.dismiss();
					Toast.makeText(StockinActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					uploadProgressDialog.dismiss();
					Toast.makeText(StockinActivity.this, "记录和图片均上传成功", Toast.LENGTH_SHORT).show();
					deviceList.clear();
					myAdapter.notifyDataSetChanged();
					break;
				case 4:
					uploadProgressDialog.dismiss();
					Toast.makeText(StockinActivity.this, "图片上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				}
		};
		
		//各种dialog声明的地方
		
		stockinNumDialog = new AlertDialog.Builder(StockinActivity.this);
		stockinNumView = layoutInflater.inflate(R.layout.view_stockinnum, null);
		stockinNumEdt = (EditText) stockinNumView.findViewById(R.id.view_stockinnum_edt);
		stockinNumDialog.setTitle("提示").setMessage("请输入入库编号").setView(stockinNumView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				stockinNum = stockinNumEdt.getText().toString();
				if("".equals(stockinNum)){
					Toast.makeText(StockinActivity.this, "入库编号不能为空", Toast.LENGTH_SHORT).show();
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
					ViewGroup p = (ViewGroup) driverNameView.getParent(); 
			        if (p != null) { 
			        	p.removeAllViewsInLayout(); 
			        }
			        driverNameDialog.show();
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
				stockinNum = "";
				contractDialog.show();
			}
		});
		
		driverNameDialog = new AlertDialog.Builder(StockinActivity.this);
		driverNameView = layoutInflater.inflate(R.layout.view_drivername, null);
		driverNameEdt = (EditText) driverNameView.findViewById(R.id.view_drivername_edt);
		driverNameDialog.setTitle("提示").setMessage("请输入司机姓名").setView(driverNameView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				driverName = driverNameEdt.getText().toString();
				if("".equals(driverName)){
					Toast.makeText(StockinActivity.this, "司机姓名不能为空", Toast.LENGTH_SHORT).show();
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
					ViewGroup p = (ViewGroup) carNumView.getParent(); 
			        if (p != null) { 
			        	p.removeAllViewsInLayout(); 
			        }
					carNumDialog.show();
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
				driverName = "";
				ViewGroup p = (ViewGroup) stockinNumView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				stockinNumDialog.show();
			}
		});
		
		carNumDialog = new AlertDialog.Builder(StockinActivity.this);
		carNumView = layoutInflater.inflate(R.layout.view_carnum, null);
		carNumEdt = (EditText) carNumView.findViewById(R.id.view_carnum_edt);
		carNumDialog.setTitle("提示").setMessage("请输入车牌号").setView(carNumView).setPositiveButton("下一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				carNum = carNumEdt.getText().toString();
				if("".equals(carNum)){
					Toast.makeText(StockinActivity.this, "车牌号不能为空", Toast.LENGTH_SHORT).show();
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
				carNum = "";
				ViewGroup p = (ViewGroup) driverNameView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				driverNameDialog.show();
			}
		});
		
		takephotoDialog = new AlertDialog.Builder(StockinActivity.this);
		takephotoDialog.setTitle("提示").setMessage("是否拍照上传？").setPositiveButton("拍照", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StockinActivity.this,CameraActivity.class);
				intent.putExtra("activity", "StockinActivity");
				intent.putExtra("activityName", "stockin");
				startActivityForResult(intent, 1);
			}
		}).setNegativeButton("上一步", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ViewGroup p = (ViewGroup) carNumView.getParent(); 
		        if (p != null) { 
		        	p.removeAllViewsInLayout(); 
		        }
				carNumDialog.show();
			}
		}).setNeutralButton("不拍照", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		uploadDialog = new AlertDialog.Builder(StockinActivity.this);
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
		
		uploadProgressDialog = new ProgressDialog(StockinActivity.this);
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
				Intent it = new Intent(StockinActivity.this,ScanCodeActivity.class);
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
					Toast.makeText(StockinActivity.this, "请先扫描设备二维码", Toast.LENGTH_SHORT).show();
				}else{
					storehouseDialog.show();
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
					Toast.makeText(StockinActivity.this, "请扫设备专用二维码！", Toast.LENGTH_SHORT).show();
					return;
				} else {
					final Device device = new Device();
					device.setId(Integer.parseInt(code.split(",")[1]));
					device.setNumber(code.split(",")[2]);
					device.setBatchId(code.split(",")[3]);
					device.setBachNumber(code.split(",")[4]);
					device.setTypeId(code.split(",")[5]);
					device.setDeviceType(code.split(",")[6]);
					device.setIsMainDevice(Integer.parseInt(code.split(",")[7]));
					//测试专用代码段
//					if(device.getIsMainDevice()==1){
//						if(hasMainDevice==0){
//							deviceList = myAdapter.getDeviceList();
//							deviceList.add(0, device);
//							isOpen = myAdapter.getIsOpen();
//							isOpen.add(0,0);
//							hasMainDevice = 1;
//							myAdapter.notifyDataSetChanged();
//						}else{
//							AlertDialog.Builder alertDialog = new AlertDialog.Builder(StockinActivity.this);
//							alertDialog.setTitle("提示").setMessage("是否替换该主设备？").setPositiveButton("替换", new OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									deviceList = myAdapter.getDeviceList();
//									deviceList.remove(0);
//									deviceList.add(0, device);
//									isOpen = myAdapter.getIsOpen();
//									isOpen.add(0, 0);
//									myAdapter.notifyDataSetChanged();
//								}
//							}).setNegativeButton("取消", new OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									
//								}
//							}).show();
//						}
//					}else{
						deviceList = myAdapter.getDeviceList();
						deviceList.add(device);
						isOpen = myAdapter.getIsOpen();
						isOpen.add(0);
						myAdapter.notifyDataSetChanged();
//					}
				}
			}
		}else if(requestCode==1){
			image = data.getExtras().getString("image");
			if (data.getExtras().getString("image") != null) {
				Toast.makeText(StockinActivity.this, "照片保存路径为："+image, Toast.LENGTH_SHORT).show();
				Log.i("image", data.getExtras().getString("image"));
				uploadDialog.show();
			}
		}else {
			Toast.makeText(StockinActivity.this, "error", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void Back(){
		if(myAdapter.getDeviceList().isEmpty()){
			finish();
		}else{
			Builder alertDialog = new AlertDialog.Builder(StockinActivity.this);
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
			uploadMap.put("storeId", storehouseId[storehouseSlected]+"");
			uploadMap.put("contractId", contractId[contractSlected]+"");
			uploadMap.put("number", stockinNum+"");
			uploadMap.put("driverName", driverName);
			uploadMap.put("carNum", carNum);
			uploadMap.put("description", "可以不填的是吧！");
			
			for(Device device:deviceList){
				deviceId += device.getId()+",";
			}
			
			uploadMap.put("deviceId", deviceId);
			
			int id = 0;
			try {
				id = JSONUtils.UploadStock(getResources().getString(R.string.STOCK_IN_ADD),uploadMap);
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
					String result = CasClient.getInstance().doSendImage(getResources().getString(R.string.STOCK_IN_UPLOAD), image,params);
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
		}
	}
	
	class DownloadThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			InputStream inputStream = CasClient.getInstance().getStorehouseList("http://www.cseicms.com/rentManagement/rs/storeHouse/list");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			
			try {      
				while ((line = reader.readLine()) != null) {      
					sb.append(line + "\n");      
				    }      
			} catch (IOException e) {      
				    e.printStackTrace();      
			} finally {      
				try {      
				    inputStream.close();      
				    } catch (IOException e) {      
				    	e.printStackTrace();      
				        }      
			}
			Log.i("json", sb.toString());
			
			try {
				JSONObject root = new JSONObject(sb.toString());
				Log.i("message", root.getString("message"));
				JSONArray array = root.getJSONArray("data");
				storehouseId = new int[array.length()];
				storehouseString = new String[array.length()];
					for(int i=0;i<array.length();i++){
						JSONObject item = array.getJSONObject(i);
						Log.i("id", item.getInt("id")+"");
						storehouseId[i] = item.getInt("id");
						Log.i("name", item.getString("name"));
						storehouseString[i] = item.getString("name");
					}
				Log.i("code",root.getString("code"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			inputStream = CasClient.getInstance().getStorehouseList("http://www.cseicms.com/rentManagement/rs/contract/list");
			reader = new BufferedReader(new InputStreamReader(inputStream));
			sb = new StringBuilder();
			line = null;
			
			try {      
				while ((line = reader.readLine()) != null) {      
					sb.append(line + "\n");      
				    }      
			} catch (IOException e) {      
				    e.printStackTrace();      
			} finally {      
				try {      
				    inputStream.close();      
				    } catch (IOException e) {      
				    	e.printStackTrace();      
				        }      
			}
			Log.i("json", sb.toString());
			
			try {
				JSONObject root = new JSONObject(sb.toString());
				Log.i("message", root.getString("message"));
				JSONArray array = root.getJSONArray("data");
				contractId = new int[array.length()];
				contractString = new String[array.length()];
					for(int i=0;i<array.length();i++){
						JSONObject item = array.getJSONObject(i);
						Log.i("id", item.getInt("id")+"");
						contractId[i] = item.getInt("id");
						Log.i("name", item.getString("name"));
						contractString[i] = item.getString("name");
					}
				Log.i("code",root.getString("code"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.what = 1;
			handler.sendMessage(msg);
		}
	}
}
