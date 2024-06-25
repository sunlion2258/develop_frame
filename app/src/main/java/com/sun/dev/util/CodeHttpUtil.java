package com.sun.dev.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by SunLion on 2019/12/13.
 * <p>
 * 网络请求
 */
public class CodeHttpUtil {

    public static String getData(String urlPath) {
        StringBuilder sb = null;
        String str;
        try {
            URL url = new URL(urlPath);
            InputStream in = url.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            bufferedReader.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert sb != null;
        return sb.toString();
    }
}
