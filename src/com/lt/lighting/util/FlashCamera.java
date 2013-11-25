package com.lt.lighting.util;

import java.util.List;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

public class FlashCamera {

	public static int RingMode = 0;
	public static final int RingMode_Call = 1;
	public static final int RingMode_Notification = 3;
	public static final int RingMode_SMS = 2;
	public static final int RingMode_Test = 4;
	private static String TAG;
	public static Camera camera;
	private static String flashMode = "";
	private static List<String> flashModes;
	public static Camera.Parameters parameters;
	public static SurfaceTexture surfaceTexture;

	static {
		TAG = "FlashRingCamera";
	}

	public static void InitialCamera() {
		if (camera == null)
			;
		try {
			camera = Camera.open();
			Camera localCamera = camera;
			if (localCamera != null)
				
			return;
		} catch (Exception localException2) {
			if (parameters == null)
				;
			try {
				Camera.Parameters localParameters;
				do {
					parameters = camera.getParameters();
					localParameters = parameters;
				} while (localParameters == null);
				try {
					camera.setPreviewTexture(surfaceTexture);
					return;
				} catch (Exception localIOException) {
					return;
				}
			} catch (Exception localException1) {
			}
		}
	}

	public static void InitialSurface() {
		if (surfaceTexture != null)
			return;
		surfaceTexture = new SurfaceTexture(0);
	}

	public static void ReleaseCamera(int paramInt) {
		if ((paramInt > RingMode) && (RingMode != 0))
			;
//		do {
//			return;
//			if (RingMode == 0)
//				continue;
//			RingMode = 0;
//		} while (camera == null);
		try {
			camera.stopPreview();
			try {
				camera.release();
				camera = null;
				return;
			} catch (Exception localException2) {
				return;
			}
		} catch (Exception localException1) {
		}
	}

	public static void SetFlashModeOff() {
		parameters.setFlashMode("off");
	}

	public static void SetFlashModeOn() {
		List localList = parameters.getSupportedFlashModes();
		if (!("torch".equals(parameters.getFlashMode()))) {
			label41 : if (!(localList.contains("torch")))
				break label41;
			parameters.setFlashMode("torch");
		}
		label41: parameters.setFlashMode("on");
		return;
	}

	public static void turnLEDOff(int paramInt) {
		if ((paramInt > RingMode) && (RingMode != 0))
			return;
		RingMode = paramInt;
		InitialSurface();
		try {
			do {
				if ((camera != null) && (parameters != null))
					continue;
				InitialCamera();
			} while ((camera == null) || (parameters == null));
			SetFlashModeOff();
			camera.setParameters(parameters);
			camera.startPreview();
			return;
		} catch (Exception localException) {
		}
	}

	public static void turnLEDOn(int paramInt) {
		if ((paramInt > RingMode) && (RingMode != 0))
			return;
		RingMode = paramInt;
		InitialSurface();
		try {
			do {
				if ((camera != null) && (parameters != null))
					continue;
				InitialCamera();
			} while ((camera == null) || (parameters == null));
			SetFlashModeOn();
			camera.setParameters(parameters);
			camera.startPreview();
			return;
		} catch (Exception localException) {
		}
	}
}
