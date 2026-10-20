package com.smartface.camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CameraHandler {
    private VideoCapture camera;

    public CameraHandler() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Load OpenCV library
        camera = new VideoCapture(0);  // 0 for default camera
    }

    public boolean isCameraOpen() {
        return camera.isOpened();
    }

    public Mat captureFrame() {
        Mat frame = new Mat();
        if (camera.isOpened()) {
            camera.read(frame);
        }
        return frame;
    }

    public void releaseCamera() {
        if (camera.isOpened()) {
            camera.release();
        }
    }
}
