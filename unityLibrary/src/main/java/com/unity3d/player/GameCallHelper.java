package com.unity3d.player;

import android.util.Log;

public class GameCallHelper {
    //游戏名
    public static String mGameName = "game107";
    //游戏时间
    public static int mGameTime = 600;

    //实际玩游戏时间
    public static int mRealTime;
    //小球运动速度
    public static int mSpeed;


    //Android端设置游戏名字
    public static void setGameName(String gameName) {
        mGameName = gameName;
    }

    //Android端设置游戏时间
    public static void setGameTime(int gameTime) {
        mGameTime = gameTime;
    }

    // Android端调用, 通知游戏玩家操作了:
    // (s2: 发送的消息内容_String)
    public static void playerOperate() {
        UnityPlayer.UnitySendMessage("AndroidMsgCtr", "PlayerOperate", "uselessMsg");
    }

    // 游戏端获取游戏名:
    // (游戏开始时自动调用, 游戏开始前可以修改)
    public static String getGameName() {
        return mGameName;
    }

    // 游戏端获取游戏时间:
    // (游戏开始时自动调用, 游戏开始前可以修改)
    public static int getGameTime() {
        return mGameTime;
    }

    //游戏结束时自动调用1: 设置实际玩游戏的时间:
    public static void setRealGameTime(int gameTime) {
        mRealTime = gameTime;
    }

    //游戏结束时自动调用2: 通知Android端关闭界面:
    public static void closeAcitity() {
        Log.d("", "closeAcitity: closeAcitity123");
    }

    /**
     * 设置小球的运动速度
     * @param speed int值 速度等级
     */
    public static void setSpeed(int speed){
        mSpeed=speed;
    }
}