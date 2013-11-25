package com.lt.lighting.service;

import com.lt.lighting.MainActivity;
import com.lt.lighting.util.AndroidUtil;
import com.lt.lighting.util.FlashRingCamera;
import com.lt.lighting.util.PreferenceUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class LightingService extends Service {

	private final String TAG = "LightingService";

	/** 开始闪烁 */
	private static final int START_FLICKER_WAHT = 1000;
	/** 停止闪烁 */
	private static final int CLOSE_FLICKER_WAHT = 1001;

	private static final int RELEASE_FLICKER_WHAT = 1002;

	private CallPhoneListener callPhoneListener;
	private TelephonyManager tm;

	private Handler mHanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_FLICKER_WAHT:
				FlashRingCamera.turnLEDOn(FlashRingCamera.RingMode_Call);
				break;
			case CLOSE_FLICKER_WAHT:
				FlashRingCamera.turnLEDOff(FlashRingCamera.RingMode_Call);
				break;
			case RELEASE_FLICKER_WHAT:
				FlashRingCamera.ReleaseCamera(FlashRingCamera.RingMode_Call);
				break;
			}

		}
	};

	private class CallPhoneListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String paramString) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 来电
				startFlicker(PreferenceUtil.getInt(
						PreferenceUtil.CALLING_FLICKER_FREQUENCY,
						MainActivity.CALLING_FLICKER_FREQUENCY) * 100, 5);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接听
				closeFlicker();
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 挂断
				closeFlicker();
				break;
			}
		}
	}

	@Override
	public void onCreate() {
		registerPhoneStateReceiver();
	}

	private void registerPhoneStateReceiver() {
		callPhoneListener = new CallPhoneListener();
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callPhoneListener, CallPhoneListener.LISTEN_CALL_STATE);
	}

	private void unregisterPhoneStateReceiver() {

	}

	/**
	 * 开始闪烁
	 * 
	 * @param time
	 *            时间
	 * @param number
	 *            次数
	 */
	private void startFlicker(int time, int number) {
		boolean start = true;
		int totalNum = number * 2 - 1;
		time = 700;
		if (0 < time && 0 < number) {
			for (int i = 0; i < totalNum; i++) {
				FlashRingCamera.turnLEDOn(FlashRingCamera.RingMode_Call);
				if (start) {
					start = false;
					mHanlder.sendEmptyMessageDelayed(CLOSE_FLICKER_WAHT,
							(i + 1) * time);
				} else {
					start = true;
					mHanlder.sendEmptyMessageDelayed(START_FLICKER_WAHT,
							(i + 1) * time);
				}
				Log.i(TAG, i + "");
			}
		}
	}

	/**
	 * 关闭闪光灯并释放资源
	 */
	private void closeFlicker() {
		// 清除队列信息
		mHanlder.removeMessages(START_FLICKER_WAHT);
		mHanlder.removeMessages(CLOSE_FLICKER_WAHT);

		// 发送关闭信息
		mHanlder.sendEmptyMessage(CLOSE_FLICKER_WAHT);
		mHanlder.sendEmptyMessage(RELEASE_FLICKER_WHAT);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		unregisterPhoneStateReceiver();
	}
}
