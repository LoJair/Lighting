package com.lt.lighting.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
	/** 开启闪光 */
	public static final String START_FLICKER = "startFlicker";
	/** 开启来电闪光 */
	public static final String START_CALLING_FLICKER = "startCallingFlicker";
	/** 来电闪光频率 */
	public static final String CALLING_FLICKER_FREQUENCY = "callingFlickerFrequency";
	/** 来电闪光次数 */
	public static final String CALLING_FLICKER_NUMBER = "callingFlickerNumber";

	/** 开启短信闪光 */
	public static final String START_MESSAGE_FLICKER = "startMessageFlicker";
	/** 短信闪光频率 */
	public static final String MESSAGE_FLICKER_FREQUENCY = "messageFlickerFrequency";
	/** 短信闪光次数 */
	public static final String MESSAGE_FLICKER_NUMBER = "messageFlickerNumber";

	/** 开启通知闪光 */
	public static final String START_NOTICE_FLICKER = "startNoticeFlicker";
	/** 通知闪光频率 */
	public static final String NOTICE_FLICKER_FREQUENCY = "noticeFlickerFrequency";
	/** 通知闪光次数 */
	public static final String NOTICE_FLICKER_NUMBER = "noticeFlickerNumber";

	/** 在何种模式下进行闪光 -1代表所有模式 其他的直接存储震动或者静音的Integer值 */
	public static final String START_FLICKER_MODE = "startFlickerMode";
	/** 开机启动 */
	public static final String BOOT_START = "bootStart";

	/** 是否创建快捷方式 */
	public static final String HAS_SHORT = "has_short";

	private static SharedPreferences sp = null;

	public static enum Flag {
		START_CALLING_FLICKER(PreferenceUtil.START_CALLING_FLICKER), START_MESSAGE_FLICKER(
				PreferenceUtil.START_MESSAGE_FLICKER);

		private String name;

		private Flag(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public void init(Context c, String name) {
		sp = c.getSharedPreferences(name, 0);
	}

	public static void putBool(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBool(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public static void putInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public static void putLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public static long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public static void putString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public static String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

}
