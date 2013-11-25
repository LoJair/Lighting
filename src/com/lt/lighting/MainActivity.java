package com.lt.lighting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.lighting.http.DoHttpGet;
import com.lt.lighting.util.DialogUtil;
import com.lt.lighting.util.PreferenceUtil;
import com.lt.lighting.util.SeekBarUtil;
import com.lt.lighting.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 
 * @ClassName: MainActivity
 * @Description: TODO 主界面
 * @author Dujp
 * @date 2013 2013年11月1日 下午9:24:07
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	/** 默认来电闪光频率 */
	public static int CALLING_FLICKER_FREQUENCY = 5;
	/** 默认来电闪光次数 */
	public static int CALLING_FLICKER_NUMBER = 10;
	/** 默认短信闪光频率 */
	public static int MESSAGE_FLICKER_FREQUENCY = 5;
	/** 默认短信闪光次数 */
	public static int MESSAGE_FLICKER_NUMBER = 5;
	/** 默认通知闪光频率 */
	public static int NOTICE_FLICKER_FREQUENCY = 5;
	/** 默认通知闪光次数 */
	public static int NOTICE_FLICKER_NUMBER = 5;

	private RelativeLayout mCallingLayoutTitle;
	private RelativeLayout mMessageLayoutTitle;
	private RelativeLayout mNoticeLayoutTitle;
	private LinearLayout mCallingLayout;
	private LinearLayout mMessageLayout;
	private LinearLayout mNoticeLayout;
	private LinearLayout mStopLayout;
	private RelativeLayout mStopLayoutTitle;
	private LinearLayout mModeLayout;
	private RelativeLayout mModeLayoutTitle;

	private Button mCallingLightingTimes;
	private Button mMessageLightingTimes;
	private Button mNoticeLightingTimes;
	private Button mCallingLightingRate;
	private Button mMessageLightingRate;
	private Button mNoticeLightingRate;

	private TextView mAboutUs;
	private TextView mHelp;

	/** 启动闪光 */
	private CheckBox mLightingCheckBoxStartApp;
	/** 启动来电闪光 */
	private CheckBox mLightingCheckBoxCalling;
	/** 开启开机自动启动 */
	private CheckBox mStartAsBegin;
	/** 在任何模式时闪光 */
	private CheckBox mLightingCheckBoxAny;
	/** 仅在震动模式时闪光 */
	private CheckBox mLightingCheckBoxShake;
	/** 仅在静音模式时闪光 */
	private CheckBox mLightingCheckBoxRing;
	/** 开启短信闪光 */
	private CheckBox mLightingCheckBoxMsg;
	/** 用户反馈 */
	private View mLightingFeedback;
	/** 向好友推荐 */
	private View mLightingShare;

	/** 友盟意见反馈 */
	private FeedbackAgent agent;
	final UMSocialService mController = UMServiceFactory.getUMSocialService(
			"com.hunk.lock", RequestType.SOCIAL);
	// final UMSocialService mController = UMServiceFactory.getUMSocialService(
	// "com.umeng.share", RequestType.SOCIAL);
	private TextView mShortCur;
	private TextView mCheckUpdate;
	private View mZan;
	private ImageView mCallingArrow;
	private ImageView mMessageArrow;
	private ImageView mNoticeArrow;
	private ImageView mModeArrow;
	private ImageView mStopArrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		MobclickAgent.setDebugMode(true);
		initViews();
		initData();
		setListener();
		if (!PreferenceUtil.getBool(PreferenceUtil.HAS_SHORT, false)) {
			ToolsUtil.createShortcut(this);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (PreferenceUtil.getBool(PreferenceUtil.START_FLICKER, true)) {
			App.startService();
		}
		agent = new FeedbackAgent(this);
		// 设置开发者信息反馈
		agent.getDefaultConversation().sync(null);

		// 设置分享内容
		mController.setShareContent("来电闪光灯...........");
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx967daebe835fbeac";
		// 微信图文分享必须设置一个url
		String contentUrl = "http://www.umeng.com/social";
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		mController.openShare(this, false);

		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		mController.getConfig().supportWXPlatform(this, appID, contentUrl);
		// 支持微信朋友圈
		mController.getConfig()
				.supportWXCirclePlatform(this, appID, contentUrl);

		mLightingCheckBoxStartApp.setChecked(PreferenceUtil.getBool(
				PreferenceUtil.START_FLICKER, true));
		mLightingCheckBoxCalling.setChecked(PreferenceUtil.getBool(
				PreferenceUtil.START_CALLING_FLICKER, true));
		setFlickerMode(PreferenceUtil.getInt(PreferenceUtil.START_FLICKER_MODE,
				AudioManager.RINGER_MODE_NORMAL));
		mLightingCheckBoxMsg.setChecked(PreferenceUtil.getBool(
				PreferenceUtil.START_MESSAGE_FLICKER, true));
		// 默认500ms
		mCallingLightingRate.setText(PreferenceUtil.getInt(
				PreferenceUtil.CALLING_FLICKER_FREQUENCY,
				CALLING_FLICKER_FREQUENCY)
				* 100 + getResources().getString(R.string.flicker_time));
		// 来电默认15次
		mCallingLightingTimes.setText(PreferenceUtil.getInt(
				PreferenceUtil.CALLING_FLICKER_NUMBER, CALLING_FLICKER_NUMBER)
				+ getResources().getString(R.string.flicker_number));
		// 短信默认500ms
		mMessageLightingRate.setText(PreferenceUtil.getInt(
				PreferenceUtil.MESSAGE_FLICKER_FREQUENCY,
				MESSAGE_FLICKER_FREQUENCY)
				* 100 + getResources().getString(R.string.flicker_time));
		// 短信默认5次
		mMessageLightingTimes.setText(PreferenceUtil.getInt(
				PreferenceUtil.MESSAGE_FLICKER_NUMBER, MESSAGE_FLICKER_NUMBER)
				+ getResources().getString(R.string.flicker_number));
		// 通知500ms
		mNoticeLightingRate.setText(PreferenceUtil.getInt(
				PreferenceUtil.NOTICE_FLICKER_FREQUENCY,
				NOTICE_FLICKER_FREQUENCY)
				* 100 + getResources().getString(R.string.flicker_time));
		// 通知默认5次
		mNoticeLightingTimes.setText(PreferenceUtil.getInt(
				PreferenceUtil.NOTICE_FLICKER_NUMBER, NOTICE_FLICKER_NUMBER)
				+ getResources().getString(R.string.flicker_number));
	}

	/**
	 * 初始化闪光模式
	 * 
	 * @param mode
	 */
	private void setFlickerMode(int mode) {
		switch (mode) {
		case AudioManager.RINGER_MODE_NORMAL:
			mLightingCheckBoxAny.setChecked(true);
			mLightingCheckBoxShake.setChecked(false);
			mLightingCheckBoxRing.setChecked(false);
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			mLightingCheckBoxAny.setChecked(false);
			mLightingCheckBoxShake.setChecked(true);
			mLightingCheckBoxRing.setChecked(false);
			break;
		case AudioManager.RINGER_MODE_SILENT:
			mLightingCheckBoxAny.setChecked(false);
			mLightingCheckBoxShake.setChecked(false);
			mLightingCheckBoxRing.setChecked(true);
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		mCallingLightingTimes = (Button) findViewById(R.id.btn_calling_setting_times);
		mMessageLightingTimes = (Button) findViewById(R.id.btn_message_setting_times);
		mNoticeLightingTimes = (Button) findViewById(R.id.btn_notice_setting_times);
		mCallingLightingRate = (Button) findViewById(R.id.btn_calling_setting_rate);
		mMessageLightingRate = (Button) findViewById(R.id.btn_message_setting_rate);
		mNoticeLightingRate = (Button) findViewById(R.id.btn_notice_setting_rate);
		mLightingCheckBoxStartApp = (CheckBox) findViewById(R.id.cb_main_start_app);
		mStartAsBegin = (CheckBox) findViewById(R.id.cb_main_start_as_begin);
		mLightingCheckBoxCalling = (CheckBox) findViewById(R.id.cb_calling_setting_checked);
		mLightingCheckBoxAny = (CheckBox) findViewById(R.id.cb_lighting_mode_setting_anytime);
		mLightingCheckBoxShake = (CheckBox) findViewById(R.id.cb_lighting_mode_setting_shake);
		mLightingCheckBoxRing = (CheckBox) findViewById(R.id.cb_lighting_mode_setting_ring);
		mLightingCheckBoxMsg = (CheckBox) findViewById(R.id.cb_message_setting_checked);
		// 关闭和收起布局
		mCallingLayoutTitle = (RelativeLayout) findViewById(R.id.layout_main_calling_title);
		mCallingArrow = (ImageView) findViewById(R.id.iv_main_arrow_call);
		mCallingLayout = (LinearLayout) findViewById(R.id.layout_main_calling_setting);
		mMessageLayoutTitle = (RelativeLayout) findViewById(R.id.layout_main_message_title);
		mMessageArrow = (ImageView) findViewById(R.id.iv_main_arrow_message);
		mMessageLayout = (LinearLayout) findViewById(R.id.layout_main_message_setting);
		mNoticeLayoutTitle = (RelativeLayout) findViewById(R.id.layout_main_notice_title);
		mNoticeArrow = (ImageView) findViewById(R.id.iv_main_arrow_notice);
		mNoticeLayout = (LinearLayout) findViewById(R.id.layout_main_notice_setting);
		mModeLayoutTitle = (RelativeLayout) findViewById(R.id.layout_lighting_mode_title);
		mModeArrow = (ImageView) findViewById(R.id.iv_main_arrow_mode);
		mModeLayout = (LinearLayout) findViewById(R.id.layout_lighting_mode_setting);
		mStopLayoutTitle = (RelativeLayout) findViewById(R.id.layout_lighting_stop_title);
		mStopArrow = (ImageView) findViewById(R.id.iv_main_arrow_stop);
		mStopLayout = (LinearLayout) findViewById(R.id.layout_stop_lighting_setting);

		// 其他设置
		mAboutUs = (TextView) findViewById(R.id.tv_about_us);
		mHelp = (TextView) findViewById(R.id.tv_help);
		mLightingFeedback = findViewById(R.id.tv_feel_back);
		mLightingShare = findViewById(R.id.tv_share);
		mZan = findViewById(R.id.layout_other_zan);
		mShortCur = (TextView) findViewById(R.id.tv_short_cut);
		mCheckUpdate = (TextView) findViewById(R.id.tv_check_update);
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		mCallingLightingTimes.setOnClickListener(this);
		mMessageLightingTimes.setOnClickListener(this);
		mNoticeLightingTimes.setOnClickListener(this);
		mCallingLightingRate.setOnClickListener(this);
		mMessageLightingRate.setOnClickListener(this);
		mNoticeLightingRate.setOnClickListener(this);
		mCallingLayoutTitle.setOnClickListener(this);
		mMessageLayoutTitle.setOnClickListener(this);
		mNoticeLayoutTitle.setOnClickListener(this);
		mModeLayoutTitle.setOnClickListener(this);
		mStopLayoutTitle.setOnClickListener(this);

		// 其他
		mAboutUs.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mLightingFeedback.setOnClickListener(this);
		mLightingShare.setOnClickListener(this);
		mShortCur.setOnClickListener(this);
		mCheckUpdate.setOnClickListener(this);
		mZan.setOnClickListener(this);

		checkBoxListener();
	}

	/**
	 * 
	 * @Title: checkBoxListener
	 * @Description: TODO 选择框的监听
	 */
	private void checkBoxListener() {
		// 开启应用
		mLightingCheckBoxStartApp
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						PreferenceUtil.putBool(PreferenceUtil.START_FLICKER,
								isChecked);
						App.startService();
					}
				});
		// 开启开机自启动
		mStartAsBegin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				PreferenceUtil.putBool(PreferenceUtil.BOOT_START, isChecked);
			}
		});
		// 开启来电
		mLightingCheckBoxCalling
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						PreferenceUtil
								.putBool(PreferenceUtil.START_CALLING_FLICKER,
										isChecked);
					}
				});

		// 在任何模式时闪光
		mLightingCheckBoxAny
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							PreferenceUtil.putInt(
									PreferenceUtil.START_FLICKER_MODE,
									App.RING_MODE_NORMAL);
							mLightingCheckBoxShake.setChecked(false);
							mLightingCheckBoxRing.setChecked(false);
						} else if (!mLightingCheckBoxShake.isChecked()
								&& !mLightingCheckBoxRing.isChecked()) {
							mLightingCheckBoxAny.setChecked(true);
						}
					}
				});

		// 仅在震动模式时闪光
		mLightingCheckBoxShake
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							PreferenceUtil.putInt(
									PreferenceUtil.START_FLICKER_MODE,
									App.RING_MODE_VIBRATE);
							mLightingCheckBoxAny.setChecked(false);
							mLightingCheckBoxRing.setChecked(false);
						} else if (!mLightingCheckBoxAny.isChecked()
								&& !mLightingCheckBoxRing.isChecked()) {
							mLightingCheckBoxAny.setChecked(true);
						}
					}
				});

		// 仅在铃声模式时闪光
		mLightingCheckBoxRing
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							PreferenceUtil.putInt(
									PreferenceUtil.START_FLICKER_MODE,
									App.RING_MODE_RING);
							mLightingCheckBoxAny.setChecked(false);
							mLightingCheckBoxShake.setChecked(false);
						} else if (!mLightingCheckBoxAny.isChecked()
								&& !mLightingCheckBoxShake.isChecked()) {
							mLightingCheckBoxAny.setChecked(true);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_calling_setting_times:// 设置来电闪光次数
			SeekBarUtil.showTimesDialog(this, mCallingLightingTimes,
					R.string.calling_lighting_times_dialog);
			break;
		case R.id.btn_message_setting_times:// 设置短信闪电次数
			SeekBarUtil.showTimesDialog(this, mMessageLightingTimes,
					R.string.message_lighting_times_dialog);
			break;
		case R.id.btn_notice_setting_times:// 设置通知闪电次数
			SeekBarUtil.showTimesDialog(this, mNoticeLightingTimes,
					R.string.notice_lighting_times_dialog);
			break;
		case R.id.btn_calling_setting_rate: // 设置来电闪光频率
			SeekBarUtil.showRateDialog(this, mCallingLightingRate,
					R.string.calling_lighting_rate_dialog);
			break;
		case R.id.btn_message_setting_rate:// 设置短信闪光频率
			SeekBarUtil.showRateDialog(this, mMessageLightingRate,
					R.string.message_lighting_rate_dialog);
			break;
		case R.id.btn_notice_setting_rate:// 设置通知闪光频率
			SeekBarUtil.showRateDialog(this, mNoticeLightingRate,
					R.string.notice_lighting_rate_dialog);
			break;
		case R.id.layout_main_calling_title:// 打开和收起来电设置的布局
			if (mCallingLayout.getVisibility() == View.VISIBLE) {
				mCallingLayout.setVisibility(View.GONE);
				mCallingArrow.setBackgroundResource(R.drawable.shousuo);
			} else {
				mCallingLayout.setVisibility(View.VISIBLE);
				mCallingArrow.setBackgroundResource(R.drawable.zhankai);
			}
			break;
		case R.id.layout_main_message_title:// 打开和收起短信设置的布局
			if (mMessageLayout.getVisibility() == View.VISIBLE) {
				mMessageLayout.setVisibility(View.GONE);
				mMessageArrow.setBackgroundResource(R.drawable.shousuo);
			} else {
				mMessageLayout.setVisibility(View.VISIBLE);
				mMessageArrow.setBackgroundResource(R.drawable.zhankai);
			}
			break;
		case R.id.layout_main_notice_title:// 打开和收起通知设置的布局
			if (mNoticeLayout.getVisibility() == View.VISIBLE) {
				mNoticeLayout.setVisibility(View.GONE);
				mNoticeArrow.setBackgroundResource(R.drawable.shousuo);
			} else {
				mNoticeLayout.setVisibility(View.VISIBLE);
				mNoticeArrow.setBackgroundResource(R.drawable.zhankai);
			}
			break;
		case R.id.layout_lighting_mode_title:// 打开和收起模式设置的布局
			if (mModeLayout.getVisibility() == View.VISIBLE) {
				mModeLayout.setVisibility(View.GONE);
				mModeArrow.setBackgroundResource(R.drawable.shousuo);
			} else {
				mModeLayout.setVisibility(View.VISIBLE);
				mModeArrow.setBackgroundResource(R.drawable.zhankai);
			}
			break;
		case R.id.layout_lighting_stop_title:// 打开和收起停止设置的布局
			if (mStopLayout.getVisibility() == View.VISIBLE) {
				mStopLayout.setVisibility(View.GONE);
				mStopArrow.setBackgroundResource(R.drawable.shousuo);
			} else {
				mStopLayout.setVisibility(View.VISIBLE);
				mStopArrow.setBackgroundResource(R.drawable.zhankai);
			}
			break;
		case R.id.tv_about_us:// 关于
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.tv_help:// 使用帮助
			startActivity(new Intent(this, HelpActivity.class));
			break;
		case R.id.tv_feel_back:// 用户反馈
			agent.startFeedbackActivity();
			break;
		case R.id.tv_share:
			// 打开平台选择面板，参数2为打开分享面板时是否强制登录,false为不强制登录
			mController.openShare(this, false);
			break;
		case R.id.tv_short_cut:// 快捷方式
			ToolsUtil.createShortcut(this);
			break;
		case R.id.tv_check_update:// 检查更新
			checkUpdate();
			break;
		case R.id.tv_zan_ge:// 打分
			Uri uri = Uri.parse("market://details?id=" + "com.hunk.lock");
			// Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 
	 * @Title: checkUpdate
	 * @Description: TODO 检查更新
	 */
	private void checkUpdate() {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				String result = "";
				DoHttpGet httpGet = new DoHttpGet();
				try {
					result = httpGet.doGetWork(params[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return result;
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				try {
					JSONObject resultObj = new JSONObject(result);
					int state = resultObj.getInt("status");
					switch (state) {
					case 1:// chenggong
						JSONObject dataObj = new JSONObject(
								resultObj.getString("data"));

						String updateTitle = dataObj.getString("soft_title");
						String version = dataObj.getString("soft_bate");
						String softUrl = dataObj.getString("soft_file");
						String softTsup = dataObj.getString("soft_tsup");
						String softType = dataObj.getString("soft_type");
						String softContent = dataObj.getString("soft_content");

						String appVersion = ToolsUtil
								.getAppVersion(getApplicationContext());
						// if (Double.valueOf(appVersion) < Double
						// .valueOf(version)) {
						// showDialog
						Map<String, String> map = new HashMap<String, String>();
						map.put("updateTitle", updateTitle);
						map.put("version", version);
						map.put("softUrl", softUrl);
						map.put("softTsup", softTsup);
						map.put("softType", softType);
						map.put("softContent", softContent);

						DialogUtil.showUpdateDialog(MainActivity.this, map);
						// }

						break;
					case 0:// 失败
						Toast.makeText(getApplicationContext(), "最新版本", 0)
								.show();
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}.execute("http://ltapp.3gwawa.net/index.php/index/updatelog.html?appid=3");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		if (PreferenceUtil.getBool(PreferenceUtil.START_FLICKER, true)) {
			App.startService();
		}
		super.onResume();
	}
}
