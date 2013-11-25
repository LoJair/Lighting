package com.lt.lighting.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.lt.lighting.App;
import com.lt.lighting.MainActivity;
import com.lt.lighting.util.AndroidUtil;
import com.lt.lighting.util.FlashCamera;
import com.lt.lighting.util.PreferenceUtil;

/**
 * 
 * @ClassName: LTService
 * @Description: TODO 长期运行, 监听手机状态
 * @author Dujp
 * @date 2013 2013年11月7日 下午9:14:51
 * 
 */
public class LTService extends Service {

	/** 开始闪烁 */
	private static final int START_FLICKER_WAHT = 10;
	/** 停止闪烁 */
	private static final int CLOSE_FLICKER_WAHT = 11;
	private Handler mHanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_FLICKER_WAHT:
				 AndroidUtil.startLighting();
//				FlashCamera.turnLEDOn(FlashCamera.RingMode_Call);
				break;
			case CLOSE_FLICKER_WAHT:
				 AndroidUtil.closeLighting();
//				FlashCamera.turnLEDOff(FlashCamera.RingMode_Call);
				break;
			}
			super.handleMessage(msg);
		}
	};

	/** 电话状态管理者 */
	private TelephonyManager tm;
	/** 铃声模式管理者 */
	private AudioManager mAudioManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		init();
		super.onCreate();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 创建电话状态监听
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(new CallPhoneListener(), CallPhoneListener.LISTEN_CALL_STATE);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// FlashCamera.InitialCamera();
		// 创建短信状态监听
		// ContentObserver co = new SmsReceiver(new Handler());
		// this.getContentResolver().registerContentObserver(
		// Uri.parse("content://sms/"), true, co);
		IntentFilter localIntentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		localIntentFilter.setPriority(1000);
		registerReceiver(new SmsReceiver(), localIntentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
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
		if (0 < time && 0 < number) {
			for (int i = 0; i < totalNum; i++) {
				AndroidUtil.startLighting();
				if (start) {
					start = false;
					mHanlder.sendEmptyMessageDelayed(CLOSE_FLICKER_WAHT,
							(i + 1) * time);
				} else {
					start = true;
					mHanlder.sendEmptyMessageDelayed(START_FLICKER_WAHT,
							(i + 1) * time);
				}
			}
		}
	}

	/**
	 * 监听来电事件
	 * 
	 * @author Lo
	 */
	private class CallPhoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			FlashCamera.RingMode = 1;
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 来电
				flickerStart(PreferenceUtil.Flag.START_CALLING_FLICKER);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接听
				closeFlicker();
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 挂断
				closeFlicker();
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 关闭闪光灯
	 */
	private void closeFlicker() {
		// 清除队列信息
		mHanlder.removeMessages(START_FLICKER_WAHT);
		mHanlder.removeMessages(CLOSE_FLICKER_WAHT);

		// 发送关闭信息
		mHanlder.sendEmptyMessage(CLOSE_FLICKER_WAHT);
	}

	/**
	 * 判断是否开启(来电,短信)闪光 并判断手机是还否为(震动,静音)模式
	 * 
	 * @param flag
	 */
	private void flickerStart(PreferenceUtil.Flag flag) {
		if (PreferenceUtil.getBool(PreferenceUtil.START_FLICKER, true)) {// 如果开启闪光
			if (PreferenceUtil.getBool(flag.toString(), true)) {// 如果开启来电,短信闪光
				// 获取当前模式
				int ringerMode = mAudioManager.getRingerMode();
				int startFlickerMode = PreferenceUtil
						.getInt(PreferenceUtil.START_FLICKER_MODE,
								App.RING_MODE_NORMAL);
				// 当前模式等于存储的模式比如静音或者震动模式时才去闪光,如果是短信则不需要判断模式
				if (startFlickerMode == AudioManager.RINGER_MODE_NORMAL
						|| flag.toString().equals(
								PreferenceUtil.START_MESSAGE_FLICKER)) {
					startToFlicker(flag);
				} else if (startFlickerMode == AudioManager.RINGER_MODE_VIBRATE) {
					startToFlicker(flag);
				} else if (startFlickerMode == App.RING_MODE_RING
						&& ringerMode == AudioManager.RINGER_MODE_NORMAL
						&& mAudioManager
								.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
					// 如果是仅铃声模式, 判断当前是否是正常模式并且没有开启震动
					startToFlicker(flag);
				}
			}
		}
	}

	private void startToFlicker(PreferenceUtil.Flag flag) {
		int flickerFrequency = PreferenceUtil.getInt(
				PreferenceUtil.CALLING_FLICKER_FREQUENCY,
				MainActivity.CALLING_FLICKER_FREQUENCY) * 100;
		int flickerNumber;
		if (PreferenceUtil.START_CALLING_FLICKER.equals(flag.toString())) {
			flickerNumber = PreferenceUtil.getInt(
					PreferenceUtil.CALLING_FLICKER_NUMBER,
					MainActivity.CALLING_FLICKER_NUMBER);
		} else {
			flickerNumber = PreferenceUtil.getInt(
					PreferenceUtil.CALLING_FLICKER_NUMBER,
					MainActivity.MESSAGE_FLICKER_NUMBER);
		}
		if (0 < flickerFrequency && 0 < flickerNumber) {
			startFlicker(flickerFrequency, flickerNumber);
		}
	}

	/**
	 * 短信监听
	 * 
	 * @author Lo
	 */
	public class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 来短信的了
			if (intent.getAction().equals(
					"android.provider.Telephony.SMS_RECEIVED")) {
				flickerStart(PreferenceUtil.Flag.START_MESSAGE_FLICKER);
			}
		}

	}

	// /**
	// * 短信监听
	// *
	// * @author Lo
	// */
	// public class SmsReceiver extends ContentObserver {
	//
	// public SmsReceiver(Handler handler) {
	// super(handler);
	// }
	//
	// @Override
	// public void onChange(boolean selfChange) {
	// super.onChange(selfChange);
	// flickerStart(PreferenceUtil.Flag.START_MESSAGE_FLICKER);
	// }
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
		App.startService();
	}

}
