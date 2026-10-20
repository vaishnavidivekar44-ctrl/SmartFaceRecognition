package com.smartface.camera;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import java.io.File;
import java.nio.IntBuffer;

public class FaceTrainer {

    public void train() {
        try {
            File faceImagesDir = new File("face_images");
            File[] faceFiles = faceImagesDir.listFiles((dir, name) -> 
                name.endsWith(".png") && !name.equals("temp_face.png") && name.matches("\\d+_\\w+\\.png")
            );

            // Check for no valid face images
            if (faceFiles == null || faceFiles.length == 0) {
                System.out.println("No valid face images found for training.");

                // Attempt to delete the model if no valid faces are found
                File modelFile = new File("face_images/face_trainer.yml");
                if (modelFile.exists() && modelFile.delete()) {
                    System.out.println("Model file 'face_trainer.yml' deleted due to lack of valid face data.");
                } else {
                    System.out.println("No model file found to delete.");
                }
                return;
            }

            MatVector trainingImages = new MatVector(faceFiles.length);
            Mat labels = new Mat(faceFiles.length, 1, opencv_core.CV_32SC1);
            IntBuffer labelsBuffer = labels.createBuffer();

            int counter = 0;
            for (File faceFile : faceFiles) {
                String[] nameParts = faceFile.getName().split("_");
                int empId = Integer.parseInt(nameParts[0]);

                // Read the face image as grayscale
                Mat faceImage = opencv_imgcodecs.imread(faceFile.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);

                if (faceImage.empty()) {
                    System.out.println("Skipping invalid image file: " + faceFile.getName());
                    continue;
                }

                trainingImages.put(counter, faceImage);
                labelsBuffer.put(counter, empId);
                counter++;
            }

            if (counter == 0) {
                System.out.println("No valid face data available for training.");
                return;
            }

            LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
            recognizer.train(trainingImages, labels);
            recognizer.save("face_images/face_trainer.yml");

            System.out.println("Face recognizer trained successfully. Model saved to 'face_trainer.yml'.");
        } catch (Exception e) {
            System.out.println("Error during face training: " + e.getMessage());
        }
    }
}
