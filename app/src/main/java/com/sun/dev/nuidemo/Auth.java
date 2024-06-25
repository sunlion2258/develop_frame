package com.sun.dev.nuidemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.dev.nuidemo.token.AccessToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class Auth {
    // 将鉴权信息打包成json格式
    public static JSONObject getTicket() {
        JSONObject object = new JSONObject();

        //郑重提示:
        //  您的账号信息ak_id和ak_secret一定不可存储在app代码中和移动端侧, 以防帐号信息泄露。
        //  若使用离线功能(离线语音合成), 则必须ak_id、ak_secret、app_key
        //  若使用在线功能(语音合成、实时转写、一句话识别、录音文件转写), 则只需app_key和token

        //账号和项目创建
        //  ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
        object.put("app_key", "pYfegFPoUCid9wzd"); // 必填

        //请使用您的阿里云账号与appkey进行访问, 以下介绍两种方案(不限于两种)
        //方案一(强烈推荐):
        //  首先ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
        //  然后请看 https://help.aliyun.com/document_detail/466615.html 使用其中方案二使用STS获取临时账号
        //  此方案简介: 远端服务器运行STS生成具有有效时限的临时凭证, 下发给移动端进行使用, 保证账号信息ak_id和ak_secret不被泄露
        object.put("ak_id", "STS.LTAI5tD8gxcfScNcUgPQhig8"); // 必填
        object.put("ak_secret", "miyEvQ27AlhfOPcodDYEMLDF7ipP8V"); // 必填
        object.put("sts_token", "95e26ff39e214ff091ef40e54b11b78d"); // 必填
        
        //方案二(泄露风险, 不推荐):
        //  首先ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
        //  此方案简介: 远端服务器存储帐号信息, 加密下发给移动端进行使用, 保证账号信息ak_id和ak_secret不被泄露
//        object.put("ak_id", "<一定不可代码中存储和本地明文存储>"); // 必填
//        object.put("ak_secret", "<一定不可代码中存储和本地明文存储>"); // 必填

        // 特别说明: 鉴权所用的id是由以下device_id，与手机内部的一些唯一码进行组合加密生成的。
        //   更换手机或者更换device_id都会导致重新鉴权计费。
        //   此外, 以下device_id请设置有意义且具有唯一性的id, 比如用户账号(手机号、IMEI等),
        //   传入相同或随机变换的device_id会导致鉴权失败或重复收费。
        //   Utils.getDeviceId() 并不能保证生成不变的device_id，请不要使用
        object.put("device_id", "empty_device_id"); // 必填

        // 离线语音合成sdk_code取值：精品版为software_nls_tts_offline， 标准版为software_nls_tts_offline_standard
        // 离线语音合成账户和sdk_code可用于唤醒
        object.put("sdk_code", "software_nls_tts_offline_standard"); // 必填

        return object;
    }

    // 也可以将鉴权信息以json格式保存至文件，然后从文件里加载（必须包含成员：ak_id/ak_secret/app_key/device_id/sdk_code）
    // 该方式切换账号切换账号比较方便
    public static JSONObject getTicketFromJsonFile(String fileName) {
        try {
            String jsonStr = "";
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getAliYunTicket() {
        JSONObject object = new JSONObject();
        final AccessToken token;

        //郑重提示:
        //  此方法通过ak/sk在此处获取token, 易导致ak/sk泄露, 强烈建议不要使用getAliYunTicket()
        //
        //  您的账号信息ak_id和ak_secret一定不可存储在app代码中和移动端侧, 以防帐号信息泄露。
        //  若使用在线功能(语音合成、实时转写、一句话识别、录音文件转写), 则只需app_key和token

        //账号和项目创建
        //  ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
        String app_key = "pYfegFPoUCid9wzd"; // 必填

        //请使用您的阿里云账号与appkey进行访问,
        //方案二:
        //  首先ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
        //  此方案简介: 远端服务器存储帐号信息, 加密下发给移动端进行使用, 保证账号信息ak_id和ak_secret不被泄露
        String accessKeyId = "<一定不可代码中存储和本地明文存储>"; // 必填
        String accessKeySecret = "<一定不可代码中存储和本地明文存储>"; // 必填

        token = new AccessToken(accessKeyId, accessKeySecret);
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    token.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        th.start();
        try {
            th.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String token_txt = token.getToken();
        // token生命周期超过expired_time, 则需要重新token = new AccessToken()
        long expired_time = token.getExpireTime();

        object.put("app_key", app_key);
        object.put("token", token_txt);
        // Utils.getDeviceId() 并不能保证生成不变的device_id，请不要使用
        object.put("device_id", Utils.getDeviceId());
        return object;
    }
}
