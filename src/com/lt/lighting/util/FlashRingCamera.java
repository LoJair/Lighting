package com.lt.lighting.util;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

public class FlashRingCamera {
	
	private final static String TAG = "FlashRingCamera";
	
	public static int RingMode = 0;
	public static final int RingMode_Call = 1;
	public static final int RingMode_SMS = 2;
	public static final int RingMode_Notification = 3;
	public static Camera camera;
	public static Camera.Parameters parameters;
	public static SurfaceTexture surfaceTexture;

	public static void InitialCamera() {
		if (camera == null) {
			camera = Camera.open();
		}
		if (parameters == null) {
			parameters = camera.getParameters();
			Camera.Parameters localParameters = parameters;
			if (localParameters != null) {
				camera.setPreviewTexture(surfaceTexture);
				return;
			}
		}
	}

	public static void InitialSurface() {
		if (surfaceTexture == null)
			surfaceTexture = new SurfaceTexture(0);
	}

	public static void ReleaseCamera(int paramInt) {
		if (camera != null) {
			if (RingMode != 0) {
				RingMode = 0;
			}
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	public static void SetFlashModeOff() {
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
	}

	public static void SetFlashModeOn() {
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
	}

	public static void turnLEDOff(int paramInt) {
		Log.i(TAG, "turnoff");
		RingMode = paramInt;
		InitialSurface();
		if ((camera == null) || (parameters == null)) {
			InitialCamera();
		}
		SetFlashModeOff();
		camera.setParameters(parameters);
		camera.startPreview();
	}

	public static void turnLEDOn(int paramInt) {
		Log.i(TAG, "turnon");
		RingMode = paramInt;
		InitialSurface();
		if ((camera == null) || (parameters == null))
			InitialCamera();
		SetFlashModeOn();
		camera.setParameters(parameters);
		camera.startPreview();
	}
}
