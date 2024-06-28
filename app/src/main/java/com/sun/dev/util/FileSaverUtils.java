package com.sun.dev.util;

import android.content.Context;

import com.sun.dev.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fengwj on 2024/3/27.
 */
public class FileSaverUtils {
    public static File createFile(Context context, String fileName) {
        File fileDir = context.getExternalFilesDir(null); // 默认的外部存储位置
        if (fileDir != null) {
            File file = new File(fileDir, fileName);
            try {
                boolean wasSuccessful = file.createNewFile();
                if (wasSuccessful) {
                    return file;
                }
            } catch (IOException e) {
                // Handle the exception
            }
        }
        return null;
    }

    public static void saveInputStreamToFile(Context context, InputStream inputStream, String fileNmae) {
        File file = createFile(context, fileNmae + ".aac");

        SharedHelper.INSTANCE.getEdit(editor -> {
            editor.putString(Constants.SP.VIP_FILE_NAME, fileNmae + ".aac");
            return editor;
        });

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
