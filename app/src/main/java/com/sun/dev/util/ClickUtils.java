package com.sun.dev.util;

public class ClickUtils {
    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime = 0L;

    /**
     * 是否为快速操作
     *
     * @return true 不是快速点击
     */
    public static boolean isNotFastClick() {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = true;
            lastClickTime = currentClickTime;
        }
        return flag;
    }


    public static boolean isNotFastTimeClick(int time) {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= time) {
            flag = true;
            lastClickTime = currentClickTime;
        }
        return flag;
    }

}
