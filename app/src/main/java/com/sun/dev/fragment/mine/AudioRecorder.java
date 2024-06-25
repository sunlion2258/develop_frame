package com.sun.dev.fragment.mine;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by fengwj on 2024/1/3.
 */
public class AudioRecorder {
    private MediaRecorder mediaRecorder;
    private String outputFile;

    public void startRecording() {
        // 创建 MediaRecorder 实例
        mediaRecorder = new MediaRecorder();

        // 设置音频源为麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置输出格式为默认的三星声音
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

        // 设置输出文件的路径
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        mediaRecorder.setOutputFile(outputFile);


        // 设置音频编码器为默认的AMR编码器
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            // 准备录制
            mediaRecorder.prepare();
            // 开始录制
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            // 停止录制
            mediaRecorder.stop();
            // 释放资源
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
