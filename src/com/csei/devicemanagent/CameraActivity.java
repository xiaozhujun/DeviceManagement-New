package com.csei.devicemanagent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;

import com.csei.devicesmanagement.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	private String name = DateFormat.format("yyyyMMdd_hhmmss",
			Calendar.getInstance(Locale.CHINA))
			+ ".jpg";
	private Button commit;
	private Button cancel;

	private String activity;
	private String activityName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		Intent intent1 = this.getIntent();
		activity = (String) intent1.getExtras().get("activity");
		activityName = (String) intent1.getExtras().get("activityName");

		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {

				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/image" + "/" + activityName + "/");
				if (!dir.exists())
					dir.mkdirs();
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(dir, name);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 1);
			} catch (ActivityNotFoundException e) {
				// TODO: handle exception
				Toast.makeText(CameraActivity.this, "没有找到存储目录",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(CameraActivity.this, "没有存储卡", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image/"  + activityName + "/", name);
			try {

				FileInputStream fis = new FileInputStream(
						Environment.getExternalStorageDirectory() + "/image/"
								 + activityName + "/" + name);
				Bitmap bitmap = BitmapFactory.decodeStream(fis);

				// 缩小
				Matrix matrix = new Matrix();
				matrix.postScale(0.2f, 0.2f); // 长和宽放大缩小的比例
				Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				// 旋转
				Matrix matrix2 = new Matrix();
				matrix2.setRotate(90, bitmap2.getWidth(), bitmap2.getHeight());
				Bitmap bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0,
						bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);

				((ImageView) findViewById(R.id.imageViewCamera))
						.setImageBitmap(bitmap3);
				commit = (Button) findViewById(R.id.commit_camera);
				cancel = (Button) findViewById(R.id.cancel_camera);

				commit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (activity.equals("StockinActivity")) {
							Intent intent3 = new Intent(CameraActivity.this,StockinActivity.class);
							intent3.putExtra("image",
									Environment.getExternalStorageDirectory()
											+ "/image/"  + activityName + "/"
											+ name);
							Log.i("image", Environment.getExternalStorageDirectory()
									+ "/image/"  + activityName + "/"
									+ name);
							setResult(0, intent3);
							finish();
						}else if (activity.equals("StockoutActivity")) {
							Intent intent3 = new Intent(CameraActivity.this,StockoutActivity.class);
							intent3.putExtra("image",
									Environment.getExternalStorageDirectory()
											+ "/image/"  + activityName + "/"
											+ name);
							Log.i("image", Environment.getExternalStorageDirectory()
									+ "/image/"  + activityName + "/"
									+ name);
							setResult(0, intent3);
							finish();
						}else if(activity.equals("TransportActivity")){
							Intent intent3 = new Intent(CameraActivity.this,TransportActivity.class);
							intent3.putExtra("image",
									Environment.getExternalStorageDirectory()
											+ "/image/"  + activityName + "/"
											+ name);
							Log.i("image", Environment.getExternalStorageDirectory()
									+ "/image/"  + activityName + "/"
									+ name);
							setResult(0, intent3);
							finish();
						}else if(activity.equals("UninstallActivity")){
							Intent intent3 = new Intent(CameraActivity.this,UninstallActivity.class);
							intent3.putExtra("image",
									Environment.getExternalStorageDirectory()
											+ "/image/"  + activityName + "/"
											+ name);
							Log.i("image", Environment.getExternalStorageDirectory()
									+ "/image/"  + activityName + "/"
									+ name);
							setResult(0, intent3);
							finish();
						}else if(activity.equals("InstallActivity")){
							Intent intent3 = new Intent(CameraActivity.this,InstallActivity.class);
							intent3.putExtra("image",
									Environment.getExternalStorageDirectory()
											+ "/image/"  + activityName + "/"
											+ name);
							Log.i("image", Environment.getExternalStorageDirectory()
									+ "/image/"  + activityName + "/"
									+ name);
							setResult(0, intent3);
							finish();
						}
					}
				});

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (activity.equals("StockinActivity")) {
							Intent intent3 = new Intent(CameraActivity.this,StockinActivity.class);
							intent3.putExtra("image","");
							setResult(0, intent3);
							finish();
						}else if(activity.equals("StockoutActivity")){
							Intent intent3 = new Intent(CameraActivity.this,StockoutActivity.class);
							intent3.putExtra("image", "");
							setResult(0,intent3);
							finish();
						}else if(activity.equals("TransportActivity")){
							Intent intent3 = new Intent(CameraActivity.this,TransportActivity.class);
							intent3.putExtra("image", "");
							setResult(0,intent3);
							finish();
						}else if(activity.equals("UninstallActivity")){
							Intent intent3 = new Intent(CameraActivity.this,UninstallActivity.class);
							intent3.putExtra("image", "");
							setResult(0, intent3);
							finish();
						}else if(activity.equals("InstallActivity")){
							Intent intent3 = new Intent(CameraActivity.this,InstallActivity.class);
							intent3.putExtra("image", "");
							setResult(0, intent3);
							finish();
						}
					}
				});

			} catch (FileNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
