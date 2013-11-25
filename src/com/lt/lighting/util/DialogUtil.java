package com.lt.lighting.util;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lt.lighting.R;

/**
 * 
 * @ClassName: DialogUtil
 * @Description: TODO
 * @author Dujp
 * @date 2013 2013年11月23日 下午3:23:22
 * 
 */
public class DialogUtil {

	private DialogUtil() {

	}

	/**
	 * 
	 * @Title: showUpdateDialog
	 * @Description: TODO
	 * @param context
	 * @param map
	 */
	public static void showUpdateDialog(final Context context,
			final Map<String, String> map) {

		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.update_dialog, null);
		TextView updateContent = (TextView) dialogView
				.findViewById(R.id.tv_update_content);
		updateContent.setText(map.get("softContent"));
		Button dialogCancel = (Button) dialogView
				.findViewById(R.id.btn_update_dialog_cancel);
		Button dialogSure = (Button) dialogView
				.findViewById(R.id.btn_update_dialog_sure);

		final Dialog dialog = new Dialog(context, R.style.TimesDialog);

		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialogSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Uri uri = Uri.parse(map.get("softUrl"));
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
				dialog.dismiss();
			}
		});

		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
