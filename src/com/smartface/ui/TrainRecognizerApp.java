package com.smartface.ui;

import com.smartface.camera.FaceTrainer;

public class TrainRecognizerApp {
    public static void main(String[] args) {
        FaceTrainer trainer = new FaceTrainer();

        try {
            System.out.println("Initializing face training...");
            trainer.train();  // Updated method call to match the revised FaceTrainer
            System.out.println("Face training completed successfully!");
            System.out.println("The 'face_trainer.yml' file has been generated in the 'face_images' directory.");
        } catch (Exception e) {
            System.err.println("Error occurred during face training: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
