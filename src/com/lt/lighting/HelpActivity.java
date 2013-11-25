package com.lt.lighting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 
 * @ClassName: HelpActivity
 * @Description: TODO 使用帮助
 * @author Dujp
 * @date 2013 2013年11月10日 下午9:56:33
 *
 */
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help);
		
	}
}
