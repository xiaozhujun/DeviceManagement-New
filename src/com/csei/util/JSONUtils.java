package com.csei.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

import com.csei.client.CasClient;

public class JSONUtils {
	
	private static String msg;
	

//	public static Boolean UploadStockIn(List<HashMap<String, String>> list)
//			throws JSONException {
//
//		JSONObject jsonObject = new JSONObject();
//		JSONArray historys = new JSONArray();
//		HashMap<String, String> map = new HashMap<String, String>();
//		for (int i = 0; i < list.size(); i++) {
//			map = list.get(i);
//			JSONObject history = new JSONObject();
//			history.put("deviceId", map.get("deviceId"));
//			history.put("userId", map.get("userId"));
//			history.put("storeId", map.get("storeId"));
//			history.put("time", map.get("time"));
//			historys.put(history);
//		}
//		jsonObject.put("historys", historys);
//		if (CasClient.getInstance().doSendHistorys(service, jsonObject) != null) {
//			return true;
//		} else {
//			return false;
//		}
//
//	}

	public static int UploadTransport(String service,HashMap<String, String> map)
			throws JSONException {

		
			JSONObject history = new JSONObject();
			history.put("deviceId", map.get("deviceId"));
			history.put("driver", map.get("driver"));
			history.put("telephone", map.get("telephone"));
			history.put("destination", map.get("destination"));
			history.put("address", map.get("address"));
		

		
		msg = CasClient.getInstance()
				.doSendHistorys(service, history);
		
		Log.i("ceshiceshi", msg);

		int id = (new JSONObject(msg)).getInt("data");

		if ((new JSONObject(msg)).getInt("code") == 200) {
			return id;
		} else {
			return 0;
		}

	}
	
	public static int UploadStock(String service,HashMap<String, String> map)
			throws JSONException {

		
			JSONObject history = new JSONObject();
			history.put("storehouseId", map.get("storeId"));
			history.put("number", map.get("number"));
			history.put("contractId", map.get("contractId"));
			history.put("driver", map.get("driverName"));
			history.put("carNumber", map.get("carNum"));
			history.put("description", map.get("description"));
			history.put("deviceId", map.get("deviceId"));
		

		Log.i("history", String.valueOf(history));
		msg = CasClient.getInstance()
				.doSendHistorys(service, history);
		
		Log.i("ceshiceshi", msg+"ddd");

		int id = (new JSONObject(msg)).getInt("data");

		Log.i("id", String.valueOf(id));
		if ((new JSONObject(msg)).getInt("code") == 200) {
			return id;
		} else {
			return 0;
		}

	}
	
//	public static int UploadStockOut(String service,HashMap<String, String> map)
//			throws JSONException {
//
//		
//			JSONObject history = new JSONObject();
//			history.put("deviceId", map.get("deviceId"));
//			history.put("driver", map.get("driverName"));
//			history.put("telephone", map.get("driverTel"));
//			history.put("destination", map.get("destination"));
//			history.put("address", map.get("address"));
//		
//
//		
//		msg = CasClient.getInstance()
//				.doSendHistorys(service, history);
//		
//		Log.i("ceshiceshi", msg);
//
//		int id = (new JSONObject(msg)).getInt("data");
//
//		if ((new JSONObject(msg)).getInt("code") == 200) {
//			return id;
//		} else {
//			return 0;
//		}
//
//	}
	
	public static int UploadInstall(String service,HashMap<String, String> map)
			throws JSONException {

		
			JSONObject history = new JSONObject();
			history.put("contractId", map.get("contractId"));
			history.put("type", map.get("type"));
			history.put("installMan", map.get("installMan"));
			history.put("installStatus", map.get("installStatus"));
			history.put("deviceId", map.get("deviceId"));
		

		
		msg = CasClient.getInstance()
				.doSendHistorys(service, history);
		
		Log.i("ceshiceshi", msg);

		int id = (new JSONObject(msg)).getInt("data");

		Log.i("id", id+"");
		if ((new JSONObject(msg)).getInt("code") == 200) {
			return id;
		} else {
			return 0;
		}

	}
	
	public static int UploadUninstall(String service,HashMap<String, String> map)
			throws JSONException {

		
			JSONObject history = new JSONObject();
			history.put("contractId", map.get("contractId"));
			history.put("removeMan", map.get("removeMan"));
			history.put("removeStatus", map.get("removeStatus"));
			history.put("deviceId", map.get("deviceId"));
		

		Log.i("history", String.valueOf(history));
		msg = CasClient.getInstance()
				.doSendHistorys(service, history);
		
		Log.i("ceshiceshi", msg);

		int id = (new JSONObject(msg)).getInt("data");

		if ((new JSONObject(msg)).getInt("code") == 200) {
			return id;
		} else {
			return 0;
		}

	}

}
