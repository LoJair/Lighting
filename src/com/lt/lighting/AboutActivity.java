package com.lt.lighting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.lt.lighting.util.ToolsUtil;

/**
 * 
 * @ClassName: AboutActivity
 * @Description: TODO 关于界面
 * @author Dujp
 * @date 2013 2013年11月10日 下午9:45:42
 * 
 */
public class AboutActivity extends Activity {

	private TextView mVersionTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);

		initView();
		initData();
	}

	private void initView() {
		mVersionTV = (TextView) findViewById(R.id.tv_about_version);
	}

	private void initData() {
		mVersionTV.setText("v" + ToolsUtil.getAppVersion(this));
	}
}
