package com.gaoshoubang.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PermanentBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			boolean isServiceRunning = false;
			ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				// Log.i("-----------", service.service.getClassName());
				if ("com.gaoshoubang.receiver.JpushReceiver".equals(service.service.getClassName())) {
					isServiceRunning = true;
				}

			}
			if (!isServiceRunning) {
				Intent i = new Intent(context, JpushReceiver.class);
				context.startService(i);
			}
		}
	}
}
