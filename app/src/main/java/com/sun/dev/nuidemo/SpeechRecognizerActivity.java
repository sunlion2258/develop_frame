package com.sun.dev.nuidemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.nui.AsrResult;
import com.alibaba.idst.nui.CommonUtils;
import com.alibaba.idst.nui.Constants;
import com.alibaba.idst.nui.INativeNuiCallback;
import com.alibaba.idst.nui.INativeTtsCallback;
import com.alibaba.idst.nui.KwsResult;
import com.alibaba.idst.nui.NativeNui;
import com.google.gson.Gson;
import com.sun.dev.R;
import com.sun.dev.entity.RecordVoiceBean;
import com.sun.dev.entity.RecordVoiceResultBean;
import com.sun.dev.fragment.mine.MineRepository;
import com.sun.dev.util.ToastUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// 本样例展示在线一句话语音识别使用方法
// Android SDK 详细说明：https://help.aliyun.com/document_detail/173115.html
public class SpeechRecognizerActivity extends Activity implements INativeNuiCallback {
    private static final String TAG = "SpeechRecognizerActivity";

    NativeNui nui_instance = new NativeNui();
    final static int WAVE_FRAM_SIZE = 20 * 2 * 1 * 16000 / 1000; //20ms audio for 16k/16bit/mono
    public final static int SAMPLE_RATE = 16000;
    private AudioRecord mAudioRecorder;

    private Button startButton;
    private Button cancelButton;

    private AtomicBoolean vadMode = new AtomicBoolean(false);

    private Switch mVadSwitch;

    private TextView asrView;
    private TextView kwsView;

    private HandlerThread mHanderThread;

    private boolean mInit = false;
    private Handler mHandler;
    private MediaPlayer mp;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean isSpeakState = true;
    NativeNui nui_tts_instance = new NativeNui(Constants.ModeType.MODE_TTS);
    boolean initialized = false;
    String asset_path;
    private OutputStream output_file = null;
    private boolean b_savewav = false;
    //  AudioPlayer默认采样率是16000
    private AudioPlayer mAudioTrack = new AudioPlayer(new AudioPlayerCallback() {
        @Override
        public void playStart() {
            Log.i(TAG, "start play");
        }

        @Override
        public void playOver() {
            Log.i(TAG, "play over");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                this.requestPermissions(permissions, 321);
            }
            while (true) {
                i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i == PackageManager.PERMISSION_GRANTED)
                    break;
            }
        }

        String version = NativeNui.GetInstance().GetVersion();
        Log.i(TAG, "current sdk version: " + version);
        final String version_text = "内部SDK版本号:" + version;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(SpeechRecognizerActivity.this, version_text, Toast.LENGTH_LONG).show();
            }
        });

        initUIWidgets();

        mHanderThread = new HandlerThread("process_thread");
        mHanderThread.start();
        mHandler = new Handler(mHanderThread.getLooper());

        // 这里获得当前nuisdk.aar中assets路径
        String path = CommonUtils.getModelPath(this);
        Log.i(TAG, "workpath = " + path);
        asset_path = path;

        //这里主动调用完成SDK配置文件的拷贝
        if (CommonUtils.copyAssetsData(this)) {
            Log.i(TAG, "copy assets data done");
        } else {
            Log.i(TAG, "copy assets failed");
            return;
        }

        if (Constants.NuiResultCode.SUCCESS == Initialize(path)) {
            initialized = true;
        } else {
            Log.e(TAG, "init failed");
        }


    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        doInit();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        nui_instance.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUIWidgets() {
        asrView = findViewById(R.id.textView);
        kwsView = findViewById(R.id.kws_text);
        startButton = findViewById(R.id.button_start);
        cancelButton = findViewById(R.id.button_cancel);
        mVadSwitch = findViewById(R.id.vad_switch);

        mVadSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.i(TAG, "vad mode onCheckedChanged b=" + b);
            vadMode.set(b);
        });


        setButtonState(startButton, true);
        setButtonState(cancelButton, false);
        startButton.setOnClickListener(v -> {
            Log.i(TAG, "start!!!");

            setButtonState(startButton, false);
            setButtonState(cancelButton, true);

            showText(asrView, "");
            showText(kwsView, "");
            startDialog();
        });

        cancelButton.setOnClickListener(v -> {
            Log.i(TAG, "cancel");
            setButtonState(startButton, true);
            setButtonState(cancelButton, false);

            if (!checkNotInitToast()) {
                return;
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    long ret = nui_instance.stopDialog();
                    Log.i(TAG, "cancel dialog " + ret + " end");
                }
            });

        });
    }

    private void doInit() {
        showText(asrView, "");
        showText(kwsView, "");

        setButtonState(startButton, true);
        setButtonState(cancelButton, false);

        //获取工作路径, 这里获得当前nuisdk.aar中assets路径
        String asset_path = CommonUtils.getModelPath(this);
        Log.i(TAG, "use workspace " + asset_path);

        String debug_path = getExternalCacheDir().getAbsolutePath() + "/debug_" + System.currentTimeMillis();
        Utils.createDir(debug_path);

        //录音初始化，录音参数中格式只支持16bit/单通道，采样率支持8K/16K
        //使用者请根据实际情况选择Android设备的MediaRecorder.AudioSource
        //录音麦克风如何选择,可查看https://developer.android.google.cn/reference/android/media/MediaRecorder.AudioSource
        mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, WAVE_FRAM_SIZE * 4);

        //这里主动调用完成SDK配置文件的拷贝
        if (CommonUtils.copyAssetsData(this)) {
            Log.i(TAG, "copy assets data done");
        } else {
            Log.i(TAG, "copy assets failed");
            return;
        }

        //初始化SDK，注意用户需要在Auth.getAliYunTicket中填入相关ID信息才可以使用。
        int ret = nui_instance.initialize(this, genInitParams(asset_path, debug_path), Constants.LogLevel.LOG_LEVEL_VERBOSE, true);
        Log.i(TAG, "result = " + ret);
        if (ret == Constants.NuiResultCode.SUCCESS) {
            mInit = true;
        } else {
            final String msg_text = Utils.getMsgWithErrorCode(ret, "init");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(SpeechRecognizerActivity.this, msg_text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private String genParams() {
        String params = "";
        try {
            JSONObject nls_config = new JSONObject();
            nls_config.put("enable_intermediate_result", true);

            //参数可根据实际业务进行配置
            //接口说明可见: https://help.aliyun.com/document_detail/173298.html
            //查看 2.开始识别

            //由于对外的SDK不带有本地VAD模块(仅带有唤醒功能的SDK具有VAD模块)，
            //若要使用VAD模式，则需要设置nls_config参数启动在线VAD模式(见genParams())
            if (vadMode.get()) {
                nls_config.put("enable_voice_detection", true);
                nls_config.put("max_start_silence", 10000);
                nls_config.put("max_end_silence", 3000);
            }

            //nls_config.put("enable_punctuation_prediction", true);
            //nls_config.put("enable_inverse_text_normalization", true);
            //nls_config.put("enable_voice_detection", true);
            //nls_config.put("customization_id", "test_id");
            //nls_config.put("vocabulary_id", "test_id");
            //nls_config.put("max_start_silence", 10000);
            //nls_config.put("max_end_silence", 800);
            //nls_config.put("sample_rate", 16000);
            //nls_config.put("sr_format", "opus");

            JSONObject parameters = new JSONObject();

            parameters.put("nls_config", nls_config);
            parameters.put("service_type", Constants.kServiceTypeASR); // 必填

            //如果有HttpDns则可进行设置
            //parameters.put("direct_ip", Utils.getDirectIp());

            params = parameters.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void startDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //由于对外的SDK不带有本地VAD模块(仅带有唤醒功能的SDK具有VAD模块)，
                //若要使用VAD模式，则需要设置nls_config参数启动在线VAD模式(见genParams())
                Constants.VadMode vad_mode = Constants.VadMode.TYPE_P2T;
                if (vadMode.get()) {
                    //TYPE_VAD: SDK自动判断句尾结束识别。(此功能仅存在于<029>带唤醒功能的SDK)
                    //vad_mode = Constants.VadMode.TYPE_VAD;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(SpeechRecognizerActivity.this, "使用Voice Active Detection模式", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    //TYPE_P2T: 有用户主动stop()以告知识别完成
                    //vad_mode = Constants.VadMode.TYPE_P2T;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(SpeechRecognizerActivity.this, "使用Push To Talk模式", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //设置相关识别参数，具体参考API文档
                //  initialize()之后startDialog之前调用
                nui_instance.setParams(genParams());

                int ret = nui_instance.startDialog(vad_mode, genDialogParams());
                Log.i(TAG, "start done with " + ret);
                if (ret != 0) {
                    final String msg_text = Utils.getMsgWithErrorCode(ret, "start");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SpeechRecognizerActivity.this, msg_text, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private String genInitParams(String workpath, String debugpath) {
        String str = "";
        try {
            //获取token方式：

            JSONObject object = new JSONObject();

            //账号和项目创建
            //  ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
            object.put("app_key", "pYfegFPoUCid9wzd"); // 必填

            //方法1：
            //  首先ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
            //  然后请看 https://help.aliyun.com/document_detail/466615.html 使用其中方案一获取临时凭证
            //  此方案简介: 远端服务器生成具有有效时限的临时凭证, 下发给移动端进行使用, 保证账号信息ak_id和ak_secret不被泄露
            //  获得Token方法(运行在APP服务端): https://help.aliyun.com/document_detail/450255.html?spm=a2c4g.72153.0.0.79176297EyBj4k
            object.put("token", "95e26ff39e214ff091ef40e54b11b78d"); // 必填

            //方法2：
            //  STS获取临时凭证方法暂不支持

            //方法3：（强烈不推荐，存在阿里云账号泄露风险）
            //  参考Auth类的实现在端上访问阿里云Token服务获取SDK进行获取。请勿将ak/sk存在本地或端侧环境。
            //  此方法优点: 端侧获得Token, 无需搭建APP服务器。
            //  此方法缺点: 端侧获得ak/sk账号信息, 极易泄露。
//            JSONObject object = Auth.getAliYunTicket();

            object.put("device_id", Utils.getDeviceId()); // 必填, 推荐填入具有唯一性的id, 方便定位问题
            object.put("url", "wss://nls-gateway.cn-shanghai.aliyuncs.com:443/ws/v1"); // 默认
            object.put("workspace", workpath); // 必填, 且需要有读写权限

            object.put("sample_rate", "16000");
            object.put("format", "opus");

            //当初始化SDK时的save_log参数取值为true时，该参数生效。表示是否保存音频debug，该数据保存在debug目录中，需要确保debug_path有效可写。
//            object.put("save_wav", "true");
            //debug目录，当初始化SDK时的save_log参数取值为true时，该目录用于保存中间音频文件。
            object.put("debug_path", debugpath);

            // FullMix = 0   // 选用此模式开启本地功能并需要进行鉴权注册
            // FullCloud = 1
            // FullLocal = 2 // 选用此模式开启本地功能并需要进行鉴权注册
            // AsrMix = 3    // 选用此模式开启本地功能并需要进行鉴权注册
            // AsrCloud = 4
            // AsrLocal = 5  // 选用此模式开启本地功能并需要进行鉴权注册
            object.put("service_mode", Constants.ModeAsrCloud); // 必填
            str = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "InsideUserContext:" + str);
        return str;
    }

    private String genDialogParams() {
        String params = "";
        try {
            JSONObject dialog_param = new JSONObject();
            //运行过程中可以在startDialog时更新参数，尤其是更新过期token
//            dialog_param.put("app_key", "");
//            dialog_param.put("token", "");
            params = dialog_param.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "dialog params: " + params);
        return params;
    }

    private boolean checkNotInitToast() {
        if (!mInit) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SpeechRecognizerActivity.this, "SDK未成功初始化.", Toast.LENGTH_LONG).show();
                }
            });
            return false;
        } else {
            return true;
        }
    }

    private void setButtonState(final Button btn, final boolean state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "setBtn state " + btn.getText() + " state=" + state);
                btn.setEnabled(state);
            }
        });
    }

    private void showText(final TextView who, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "showText text=" + text);
                if (TextUtils.isEmpty(text)) {
                    Log.w(TAG, "asr text is empty");
                    if (who == kwsView) {
                        who.setText("激活词");
                    } else {
                        who.setText("识别文本");
                    }
                } else {
                    who.setText(text);
                }
            }
        });
    }

    private void appendText(final TextView who, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "append text=" + text);
                if (TextUtils.isEmpty(text)) {
                    return;
                } else {
                    String orign = who.getText().toString();
                    who.setText(orign + "\n---\n" + text);
                }
            }
        });
    }

    //当回调事件发生时调用
    @Override
    public void onNuiEventCallback(Constants.NuiEvent event, final int resultCode, final int arg2, KwsResult kwsResult,
                                   AsrResult asrResult) {
        Log.i(TAG, "event=" + event);
        switch (event) {
            case EVENT_ASR_RESULT:
                showText(asrView, asrResult.asrResult);
                setButtonState(startButton, true);
                setButtonState(cancelButton, false);
                break;
            case EVENT_ASR_PARTIAL_RESULT:
                if (isSpeakState) {
                    isSpeakState = false;
                    showText(asrView, asrResult.asrResult);

                    RecordVoiceBean recordVoiceBean = new Gson().fromJson(asrResult.asrResult, RecordVoiceBean.class);
                    String result = recordVoiceBean.getPayload().getResult();

                    new MineRepository().getRecordResultData(result)
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<RecordVoiceResultBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(RecordVoiceResultBean bean) {
                                    ToastUtils.INSTANCE.show(SpeechRecognizerActivity.this, bean.getMsg());
                                    isSpeakState = true;

                                    setButtonState(startButton, true);
                                    setButtonState(cancelButton, false);
                                    mHandler.post(() -> {
                                        nui_instance.stopDialog();
                                    });

                                    if (!initialized) {
                                        Log.i(TAG, "init tts");
                                        Initialize(asset_path);
                                    }

                                    // 支持一次性合成300字符以内的文字，其中1个汉字、1个英文字母或1个标点均算作1个字符，
                                    // 超过300个字符的内容将会截断。所以请确保传入的text小于300字符(不包含ssml格式)。
                                    // 长短文本语音合成收费不同，须另外开通长文本语音服务，请注意。
                                    // 不需要长文本语音合成功能则无需考虑以下操作。
                                    int charNum = nui_tts_instance.getUtf8CharsNum(bean.getMsg());
                                    if (charNum > 300) {
                                        Log.w(TAG, "text exceed 300 chars.");
                                        // 超过300字符设置成 长文本语音合成 模式
                                        nui_tts_instance.setparamTts("tts_version", "1");
                                    } else {
                                        // 未超过300字符设置成 短文本语音合成 模式
                                        nui_tts_instance.setparamTts("tts_version", "0");
                                    }

                                    // 每个instance一个task，若想同时处理多个task，请启动多instance
                                    nui_tts_instance.startTts("1", "", bean.getMsg());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtils.INSTANCE.show(SpeechRecognizerActivity.this, e.toString());
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }


                break;
            case EVENT_ASR_ERROR:
                showText(asrView, asrResult.asrResult);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SpeechRecognizerActivity.this, "ERROR with " + resultCode,
                                Toast.LENGTH_LONG).show();
                    }
                });
                final String msg_text = Utils.getMsgWithErrorCode(resultCode, "start");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SpeechRecognizerActivity.this, msg_text, Toast.LENGTH_LONG).show();
                    }
                });

                showText(kwsView, "");
                setButtonState(startButton, true);
                setButtonState(cancelButton, false);
                break;
            case EVENT_DIALOG_EX:  /* unused */
                Log.i(TAG, "dialog extra message = " + asrResult.asrResult);
                break;
        }
    }

    //当调用NativeNui的start后，会一定时间反复回调该接口，底层会提供buffer并告知这次需要数据的长度
    //返回值告知底层读了多少数据，应该尽量保证return的长度等于需要的长度，如果返回<=0，则表示出错
    @Override
    public int onNuiNeedAudioData(byte[] buffer, int len) {
        int ret = 0;
        if (mAudioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "audio recorder not init");
            return -1;
        }
        ret = mAudioRecorder.read(buffer, 0, len);
        return ret;
    }

    //当录音状态发送变化的时候调用
    @Override
    public void onNuiAudioStateChanged(Constants.AudioState state) {
        Log.i(TAG, "onNuiAudioStateChanged");
        if (state == Constants.AudioState.STATE_OPEN) {
            Log.i(TAG, "audio recorder start");
            mAudioRecorder.startRecording();
            Log.i(TAG, "audio recorder start done");
        } else if (state == Constants.AudioState.STATE_CLOSE) {
            Log.i(TAG, "audio recorder close");
            mAudioRecorder.release();
        } else if (state == Constants.AudioState.STATE_PAUSE) {
            Log.i(TAG, "audio recorder pause");
            mAudioRecorder.stop();
        }
    }

    @Override
    public void onNuiAudioRMSChanged(float val) {
//        Log.i(TAG, "onNuiAudioRMSChanged vol " + val);
    }

    @Override
    public void onNuiVprEventCallback(Constants.NuiVprEvent event) {
        Log.i(TAG, "onNuiVprEventCallback event " + event);
    }

    private int Initialize(String path) {
        int ret = nui_tts_instance.tts_initialize(new INativeTtsCallback() {
            @Override
            public void onTtsEventCallback(INativeTtsCallback.TtsEvent event, String task_id, int ret_code) {
                Log.i(TAG, "tts event:" + event + " task id " + task_id + " ret " + ret_code);
                if (event == INativeTtsCallback.TtsEvent.TTS_EVENT_START) {
                    mAudioTrack.play();
                    Log.i(TAG, "start play");
                } else if (event == INativeTtsCallback.TtsEvent.TTS_EVENT_END) {
                    /*
                     * 提示: TTS_EVENT_END事件表示TTS已经合成完并通过回调传回了所有音频数据, 而不是表示播放器已经播放完了所有音频数据。
                     */
                    Log.i(TAG, "play end");

                    // 表示推送完数据, 当播放器播放结束则会有playOver回调
                    mAudioTrack.isFinishSend(true);

                    // 调试使用, 若希望存下音频文件, 如下
                    if (b_savewav) {
                        try {
                            output_file.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (event == TtsEvent.TTS_EVENT_PAUSE) {
                    mAudioTrack.pause();
                    Log.i(TAG, "play pause");
                } else if (event == TtsEvent.TTS_EVENT_RESUME) {
                    mAudioTrack.play();
                } else if (event == TtsEvent.TTS_EVENT_ERROR) {
                    // 表示推送完数据, 当播放器播放结束则会有playOver回调
                    mAudioTrack.isFinishSend(true);

                    String error_msg = nui_tts_instance.getparamTts("error_msg");
                    Log.e(TAG, "TTS_EVENT_ERROR error_code:" + ret_code + " errmsg:" + error_msg);
                }
            }

            @Override
            public void onTtsDataCallback(String info, int info_len, byte[] data) {
                if (info.length() > 0) {
                    Log.i(TAG, "info: " + info);
                }
                if (data.length > 0) {
                    mAudioTrack.setAudioData(data);
                    Log.i(TAG, "write:" + data.length);
                    if (b_savewav) {
                        try {
                            output_file.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onTtsVolCallback(int vol) {
                Log.i(TAG, "tts vol " + vol);
            }
        }, genTicket(path), Constants.LogLevel.LOG_LEVEL_VERBOSE, true);

        if (Constants.NuiResultCode.SUCCESS != ret) {
            Log.i(TAG, "create failed");
        }

        // 在线语音合成发音人可以参考阿里云官网
        // https://help.aliyun.com/document_detail/84435.html
        nui_tts_instance.setparamTts("font_name", "siqi");

        // 详细参数可见: https://help.aliyun.com/document_detail/173642.html
        nui_tts_instance.setparamTts("sample_rate", "16000");
        // 模型采样率设置16K，则播放器也得设置成相同采样率16K.
        mAudioTrack.setSampleRate(16000);

        nui_tts_instance.setparamTts("enable_subtitle", "1");
        // 调整语速
//        nui_tts_instance.setparamTts("speed_level", "1");
        // 调整音调
//        nui_tts_instance.setparamTts("pitch_level", "0");
        // 调整音量
//        nui_tts_instance.setparamTts("volume", "1.0");

        if (b_savewav) {
            try {
                output_file = new FileOutputStream("/sdcard/mit/tmp/test.pcm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private String genTicket(String workpath) {
        String str = "";
        try {
            //获取token方式：

            JSONObject object = new JSONObject();

            //账号和项目创建
            //  ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
            object.put("app_key", "pYfegFPoUCid9wzd"); // 必填

            //方法1：
            //  首先ak_id ak_secret app_key如何获得,请查看https://help.aliyun.com/document_detail/72138.html
            //  然后请看 https://help.aliyun.com/document_detail/466615.html 使用其中方案一获取临时凭证
            //  此方案简介: 远端服务器生成具有有效时限的临时凭证, 下发给移动端进行使用, 保证账号信息ak_id和ak_secret不被泄露
            //  获得Token方法(运行在APP服务端): https://help.aliyun.com/document_detail/450255.html?spm=a2c4g.72153.0.0.79176297EyBj4k
            object.put("token", "95e26ff39e214ff091ef40e54b11b78d"); // 必填

            //方法2：
            //  STS获取临时凭证方法暂不支持

            //方法3：（强烈不推荐，存在阿里云账号泄露风险）
            //  参考Auth类的实现在端上访问阿里云Token服务获取SDK进行获取。请勿将ak/sk存在本地或端侧环境。
            //  此方法优点: 端侧获得Token, 无需搭建APP服务器。
            //  此方法缺点: 端侧获得ak/sk账号信息, 极易泄露。
//            JSONObject object = Auth.getAliYunTicket();

            object.put("device_id", "empty_device_id"); // 必填, 推荐填入具有唯一性的id, 方便定位问题

            object.put("url", "wss://nls-gateway.cn-shanghai.aliyuncs.com:443/ws/v1"); // 默认
            //工作目录路径，SDK从该路径读取配置文件
            object.put("workspace", workpath); // 必填, 且需要有读写权限

            // 设置为在线合成
            //  Local = 0,
            //  Mix = 1,  // init local and cloud
            //  Cloud = 2,
            object.put("mode_type", "2");
            str = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "UserContext:" + str);
        return str;
    }
}


