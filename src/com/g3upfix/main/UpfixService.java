package com.g3upfix.main;

import java.io.DataOutputStream;
import java.io.IOException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.content.Context;

public class UpfixService extends Service {

	private BroadcastReceiver receiver = null;
	private ConnectivityManager connectivityManager = null;
	private NetworkInfo networkInfo = null;
	Process process = null;
	DataOutputStream out = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		try {
			process = Runtime.getRuntime().exec("su");
			out = new DataOutputStream(process.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (networkInfo != null && networkInfo.isConnected())
					try {
						out.writeBytes("echo 1444 > /sys/class/net/cdma_rmnet4/mtu" + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub	
		super.onDestroy();
		unregisterReceiver(receiver);
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.destroy();
		
	}

}