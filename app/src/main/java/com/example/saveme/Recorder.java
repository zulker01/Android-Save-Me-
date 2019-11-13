package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.IOException;


public class Recorder {

    final static MediaRecorder recorder = new MediaRecorder();
    public static  String path;

    public Recorder(String path) {
        this.path = sanitizePath(path);
    }

    public static String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + path;
    }

    public static void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    public static void stop() throws IOException {
        recorder.stop();
        recorder.release();
    }

    public static void playarcoding(String path) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(path);
        mp.prepare();
        mp.start();
        mp.setVolume(10, 10);
    }
}
