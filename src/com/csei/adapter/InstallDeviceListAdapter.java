package com.csei.adapter;

import java.util.ArrayList;

import com.csei.devicesmanagement.R;
import com.csei.entity.Device;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class InstallDeviceListAdapter extends BaseAdapter{

	private ArrayList<Device> deviceList;
	private ArrayList<Integer> isOpen;
	private Context context;
	private LayoutInflater inflater = null;
	private int hasMainDevice;
	
	public InstallDeviceListAdapter(Context context,ArrayList<Device> deviceList,ArrayList<Integer> isOpen,int hasMainDevice){
		this.context = context;
		this.deviceList = deviceList;
		this.isOpen = isOpen;
		inflater = LayoutInflater.from(context);
		this.hasMainDevice = hasMainDevice;
	}
	
	public void setHasMainDevice(int hasMainDevice) {
		this.hasMainDevice = hasMainDevice;
	}
	
	public int getHasMainDevice() {
		return hasMainDevice;
	}
	
	public void setDeviceList(ArrayList<Device> deviceList){
		this.deviceList = deviceList;
	}

	public ArrayList<Device> getDeviceList() {
		return deviceList;
	}

	public ArrayList<Integer> getIsOpen() {
		return isOpen;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Device device = deviceList.get(position);
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_devicelist, null);
			holder.numbertv = (TextView) convertView.findViewById(R.id.listitem_devicelist_number);
			holder.bachnumbertv = (TextView) convertView.findViewById(R.id.listitem_devicelist_bachnumber);
			holder.devicetypetv = (TextView) convertView.findViewById(R.id.listitem_devicelist_devicetype);
			holder.ismaindevicetv = (TextView) convertView.findViewById(R.id.listitem_devicelist_ismaindevice);
			holder.button = (Button) convertView.findViewById(R.id.listitem_button);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.numbertv.setText("设备编号："+device.getNumber()+"");
		holder.bachnumbertv.setText("设备批次："+device.getBachNumber());
		holder.devicetypetv.setText("设备类型："+device.getDeviceType());

		if(device.getIsMainDevice()==1){
			holder.ismaindevicetv.setText("是否是主设备：是");
		}else{ 
			holder.ismaindevicetv.setText("是否是主设备：否");
		}

		if(isOpen.get(position)==1){
			holder.button.setVisibility(View.VISIBLE);
			this.notifyDataSetChanged();
		}else{ 
			holder.button.setVisibility(View.GONE);
			this.notifyDataSetChanged();
		}
		holder.button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(context==null)
					Log.i("log", "nimeide");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

				alertDialog.setTitle("提示").setMessage("是否删除该设备信息").setNegativeButton("取消", null).setPositiveButton("删除", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(hasMainDevice==1&&position==0){
							hasMainDevice = 0;
						}
						deviceList.remove(position);
						isOpen.remove(position);
						InstallDeviceListAdapter.this.notifyDataSetChanged();
					}
				}).show();
			}
		});
	return convertView;
}

public static class ViewHolder{
	TextView numbertv;
	TextView bachnumbertv;
	TextView devicetypetv;
	TextView ismaindevicetv;
	Button button;
}

}
