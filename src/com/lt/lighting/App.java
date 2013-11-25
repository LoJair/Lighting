package com.lt.lighting;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.lt.lighting.service.LTService;
import com.lt.lighting.util.AndroidUtil;
import com.lt.lighting.util.PreferenceUtil;

public class App extends Application {
	/** 保存数据的sp的文件名 */
	private static final String APP_NAME = "lighting";
	/** 保存数据的sp工具 */
	public static PreferenceUtil sp = new PreferenceUtil();

	/** 正常模式 */
	public static final int RING_MODE_NORMAL = AudioManager.RINGER_MODE_NORMAL;
	/** 震动模式 */
	public static final int RING_MODE_VIBRATE = AudioManager.RINGER_MODE_VIBRATE;
	/** 响铃模式 */
	public static final int RING_MODE_RING = AudioManager.RINGER_MODE_NORMAL
			+ AudioManager.RINGER_MODE_VIBRATE;

	private static Context context = null;

	@Override
	public void onCreate() {
		super.onCreate();
		sp.init(getApplicationContext(), APP_NAME);
		context = this;
	}

	public static void startService() {
		AndroidUtil.startService(context, LTService.class);
	}

	public static void stopService() {
		context.stopService(new Intent(context, LTService.class));
	}

}
