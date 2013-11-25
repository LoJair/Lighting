package com.lt.lighting.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;

import com.lt.lighting.MainActivity;
import com.lt.lighting.R;

/**
 * 
 * @ClassName: ToolsUtil
 * @Description: TODO 工具类
 * @author Dujp
 * @date 2013 2013年11月10日 下午9:53:14
 * 
 */
public class ToolsUtil {

	private ToolsUtil() {
	}

	/**
	 * 
	 * @Title: checkNet
	 * @Description: TODO 检测网络链接
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = con.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @Title: getAppVersion
	 * @Description: TODO 获取应用的版本号
	 * @param context
	 * @return 版本号
	 */

	public static String getAppVersion(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
		}
		return versionName;
	}

	public static void createShortcut(Context context) {
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context,
				R.drawable.ic_launcher);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName component = new ComponentName(context, MainActivity.class);
		i.setComponent(component);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
		context.sendBroadcast(intent);

		PreferenceUtil.putBool(PreferenceUtil.HAS_SHORT, true);
	}

	/**
	 * 
	 * @Title: createShortCut
	 * @Description: TODO 快捷方式
	 * @param context
	 */
	public static void createShortCut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
		ComponentName comp = new ComponentName(context.getPackageName(), "."
				+ ((Activity) context).getLocalClassName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		context.sendBroadcast(shortcut);
	}

	public static boolean hasShortcut(Context context) {
		int version = android.os.Build.VERSION.SDK_INT;
		Uri AUTHORITY = null;
		if (version < 8) {
			AUTHORITY = Uri
					.parse("content://com.android.launcher.settings/favorites");
		} else {
			AUTHORITY = Uri
					.parse("content://com.android.launcher2.settings/favorites");
		}
		final Uri CONTENT_URI = Uri.parse(AUTHORITY + "?notify=true");
		Cursor c = context.getContentResolver().query(
				CONTENT_URI,
				null,
				" title= ? ",
				new String[] { context.getResources().getString(
						R.string.app_name) }, null);
		if (c != null && c.moveToNext()) {
			return true;
		}
		return false;
	}
}
