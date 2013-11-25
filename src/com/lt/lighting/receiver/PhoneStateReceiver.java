package com.lt.lighting.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lt.lighting.App;
import com.lt.lighting.util.PreferenceUtil;

/**
 * 
 * @ClassName: PhoneStateReceiver
 * @Description: TODO 监听手机状态的广播
 * @author Dujp
 * @date 2013 2013年11月7日 下午9:18:15
 * 
 */
public class PhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if (arg1.getAction() == Intent.ACTION_REBOOT) {
			if (PreferenceUtil.getBool(PreferenceUtil.BOOT_START, true)) {// 手机重启,是否需要启动服务
				App.startService();
			}
		}
	}

}
