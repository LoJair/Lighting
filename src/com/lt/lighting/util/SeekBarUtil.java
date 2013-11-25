package com.lt.lighting.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lt.lighting.R;

/**
 * 
 * @ClassName: SeekBarUtil
 * @Description: TODO Seekbar的显示和操作改变闪烁频率和次数的工具类
 * @author Dujp
 * @date 2013 2013年11月4日 下午10:23:34
 * 
 */
public class SeekBarUtil {

	/** 开始闪烁 */
	private static final int START_FLICKER_WAHT = 10;
	/** 停止闪烁 */
	private static final int CLOSE_FLICKER_WAHT = 11;

	private static Handler mHanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_FLICKER_WAHT:
				AndroidUtil.startLighting();
				break;
			case CLOSE_FLICKER_WAHT:
				AndroidUtil.closeLighting();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 
	 * @Title: showTimesDialog
	 * @Description: TODO 弹出设置闪光次数的Dialog
	 * @param context
	 *            环境
	 * @param come
	 *            依赖的按钮
	 * @param title
	 *            Dialog的名称
	 */
	public static void showTimesDialog(final Context context,
			final Button come, final int title) {

		// 当前显示的次数
		int cur_times = 0;

		// 根据title判断是来电或者短信或者通知的闪烁次数
		switch (title) {
		case R.string.calling_lighting_times_dialog:// 来电
			cur_times = PreferenceUtil.getInt(
					PreferenceUtil.CALLING_FLICKER_NUMBER, 15);
			break;
		case R.string.message_lighting_times_dialog:// 短信
			cur_times = PreferenceUtil.getInt(
					PreferenceUtil.MESSAGE_FLICKER_NUMBER, 5);
			break;
		case R.string.notice_lighting_times_dialog:// 通知
			cur_times = PreferenceUtil.getInt(
					PreferenceUtil.NOTICE_FLICKER_NUMBER, 5);
			break;
		}

		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.times_dialog, null);
		TextView dialogTitle = (TextView) dialogView
				.findViewById(R.id.tv_times_title);
		dialogTitle.setText(title);
		Button dialogCancel = (Button) dialogView
				.findViewById(R.id.btn_times_dialog_cancel);
		Button dialogSure = (Button) dialogView
				.findViewById(R.id.btn_times_dialog_sure);
		final TextView counts = (TextView) dialogView
				.findViewById(R.id.tv_times_count);
		final SeekBar timesSeekBar = (SeekBar) dialogView
				.findViewById(R.id.sb_times_dialog);
		timesSeekBar.setProgress(PreferenceUtil.getInt(
				PreferenceUtil.CALLING_FLICKER_NUMBER, 0));
		// 设置默认值
		counts.setText(cur_times + "");
		timesSeekBar.setProgress(cur_times);
		final Dialog dialog = new Dialog(context, R.style.TimesDialog);

		// 设置次数Seekbar的监听器
		timesSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				counts.setText(progress + "");
			}
		});
		// 取消
		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 确定
		dialogSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				come.setText(counts.getText().toString()
						+ context.getResources().getString(
								R.string.flicker_number));
				// 根据title判断是来电或者短信或者通知的闪烁次数
				switch (title) {
				case R.string.calling_lighting_times_dialog:// 来电
					PreferenceUtil.putInt(
							PreferenceUtil.CALLING_FLICKER_NUMBER,
							timesSeekBar.getProgress());
					break;
				case R.string.message_lighting_times_dialog:// 短信
					PreferenceUtil.putInt(
							PreferenceUtil.MESSAGE_FLICKER_NUMBER,
							timesSeekBar.getProgress());
					break;
				case R.string.notice_lighting_times_dialog:// 通知
					PreferenceUtil.putInt(PreferenceUtil.NOTICE_FLICKER_NUMBER,
							timesSeekBar.getProgress());
					break;
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 
	 * @Title: showRateDialog
	 * @Description: TODO 弹出设置闪光频率的Dialog
	 * @param context
	 *            环境
	 * @param come
	 *            依赖的按钮
	 * @param title
	 *            Dialog的名称
	 */
	public static void showRateDialog(final Context context, final Button come,
			final int title) {

		// 显示的频率
		int cur_rate = 0;

		// 根据title判断是来电或者短信或者通知的闪烁频率
		switch (title) {
		case R.string.calling_lighting_rate_dialog:// 来电
			cur_rate = PreferenceUtil.getInt(
					PreferenceUtil.CALLING_FLICKER_FREQUENCY, 5);
			break;
		case R.string.message_lighting_rate_dialog:// 短信
			cur_rate = PreferenceUtil.getInt(
					PreferenceUtil.MESSAGE_FLICKER_FREQUENCY, 5);
			break;
		case R.string.notice_lighting_rate_dialog:// 通知
			cur_rate = PreferenceUtil.getInt(
					PreferenceUtil.NOTICE_FLICKER_FREQUENCY, 5);
			break;
		}

		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.rate_dialog, null);
		TextView dialogTitle = (TextView) dialogView
				.findViewById(R.id.tv_rate_title);
		dialogTitle.setText(title);
		Button dialogCancel = (Button) dialogView
				.findViewById(R.id.btn_rate_dialog_cancel);
		Button dialogSure = (Button) dialogView
				.findViewById(R.id.btn_rate_dialog_sure);
		final TextView counts = (TextView) dialogView
				.findViewById(R.id.tv_rate_count);
		final SeekBar rateSeekBar = (SeekBar) dialogView
				.findViewById(R.id.sb_rate_dialog);
		rateSeekBar.setProgress(cur_rate);
		counts.setText(cur_rate * 100 + "");
		final Dialog dialog = new Dialog(context, R.style.TimesDialog);

		// 设置次数Seekbar的监听器
		rateSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				startFlicker(seekBar.getProgress() * 100, 5);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				counts.setText(progress * 100 + "");
			}
		});
		// 取消
		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 确定
		dialogSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				come.setText(counts.getText().toString()
						+ context.getResources().getString(
								R.string.flicker_time));

				// 根据title判断是来电或者短信或者通知的闪烁频率
				switch (title) {
				case R.string.calling_lighting_rate_dialog:// 来电
					PreferenceUtil.putInt(
							PreferenceUtil.CALLING_FLICKER_FREQUENCY,
							rateSeekBar.getProgress());
					break;
				case R.string.message_lighting_rate_dialog:// 短信
					PreferenceUtil.putInt(
							PreferenceUtil.MESSAGE_FLICKER_FREQUENCY,
							rateSeekBar.getProgress());
					break;
				case R.string.notice_lighting_rate_dialog:// 通知
					PreferenceUtil.putInt(
							PreferenceUtil.NOTICE_FLICKER_FREQUENCY,
							rateSeekBar.getProgress());
					break;
				}

				dialog.dismiss();
			}
		});

		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 开始闪烁
	 * 
	 * @param time
	 *            时间
	 * @param number
	 *            次数
	 */
	private static void startFlicker(int time, int number) {
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
}
