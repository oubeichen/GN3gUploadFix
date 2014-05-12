package com.g3upfix.main;

import java.util.List;

import com.g3upfix.main.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UpfixControlActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(stopClick);
		if (isServiceRun(UpfixControlActivity.this)) {
			stop.setText("手动停止服务");
		}
		else {
			stop.setText("手动启用服务");
		}
	}

	// 事件响应
	OnClickListener stopClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 如果服务线程存在
			if (isServiceRun(UpfixControlActivity.this)) {
				Intent s = new Intent(UpfixControlActivity.this,
						UpfixService.class);
				UpfixControlActivity.this.stopService(s);
				Button stop = (Button) findViewById(R.id.stop);
				stop.setText("手动启用服务");
			}
			else {
				Intent s = new Intent(UpfixControlActivity.this,
						UpfixService.class);
				UpfixControlActivity.this.startService(s);
				Button stop = (Button) findViewById(R.id.stop);
				stop.setText("手动停止服务");
			}
		}
	};

	// 判断服务线程是否存在
	public static boolean isServiceRun(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(
					"com.g3upfix.main.UpfixService")) {
				return true;
			}
		}
		return false;
	}
}