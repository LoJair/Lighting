package com.lt.lighting.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class AndroidUtil {

	private static Camera camera;

	/**
	 * 开启闪光灯
	 * 
	 * @throws Exception
	 */
	public static void startLighting() {
		Camera camera = getCameraInstance();
		camera.startPreview();
		Parameters parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
		// MyFlashLight instance;
		// try {
		// instance = MyFlashLight.getInstance();
		// instance.lightOn();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * 关闭闪光灯
	 * 
	 * @throws Exception
	 */
	public static void closeLighting() {
		// MyFlashLight instance;
		// try {
		// instance = MyFlashLight.getInstance();
		// instance.lightOff();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Camera camera = getCameraInstance();
		Parameters parameter = camera.getParameters();
		parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(parameter);
	}

	/**
	 * 或取Camera
	 * 
	 * @return
	 */
	private static Camera getCameraInstance() {
		if (null == camera) {
			camera = Camera.open();
		}
		return camera;
	}

	/**
	 * 启动服务
	 * 
	 * @param c
	 * @param cls
	 */
	public static void startService(Context c, Class<? extends Service> cls) {
		c.startService(new Intent(c, cls));
	}

}
