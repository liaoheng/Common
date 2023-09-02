package com.github.liaoheng.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

import com.github.liaoheng.common.thread.IWorkProcessThread;
import com.github.liaoheng.common.thread.WorkProcessQueueHelper;
import com.github.liaoheng.common.thread.WorkProcessThread;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author liaoheng
 * @version 2019-11-12 13:12
 */
public class TtsHelper {
    private final String TAG = TtsHelper.class.getSimpleName();
    private TextToSpeech mTTS;
    private WorkProcessQueueHelper<String> mQueueHelper;

    public TtsHelper(Context context, Consumer<Boolean> initCallback) {
        this(context, initCallback, null);
    }

    public TtsHelper(Context context, Consumer<Boolean> initCallback, Callback<String> callback) {
        this(context, "", initCallback, callback);
    }

    /**
     * @param engine3rd packageName
     */
    public TtsHelper(Context context, String engine3rd, Consumer<Boolean> initCallback, Callback<String> callback) {
        init(context, engine3rd, initCallback);
        mQueueHelper = new WorkProcessQueueHelper<>(
                new WorkProcessThread(new IWorkProcessThread.BaseHandler(L.alog(), TAG) {
                    @Override
                    public void onHandler() {
                        String msg = mQueueHelper.takeQueue();
                        if (msg == null) {
                            return;
                        }
                        if (callback == null) {
                            speak(msg);
                        } else {
                            callback.onSuccess(msg);
                        }
                    }
                }));
        mQueueHelper.start();
    }

    public void addMsg(String msg) {
        mQueueHelper.putQueue(msg);
    }

    public TextToSpeech getTextToSpeech() {
        return mTTS;
    }

    /**
     * @param engine3rd packageName
     */
    private void init(Context context, String engine3rd, Consumer<Boolean> callback) {
        String ttsPackage = null;
        if (!TextUtils.isEmpty(engine3rd) && getEngineInfo(context, engine3rd) != null) {
            ttsPackage = engine3rd;
        }
        mTTS = new TextToSpeech(context, status -> {
            callback.accept(status == TextToSpeech.SUCCESS);
        }, ttsPackage);
        mTTS.setSpeechRate(0.7f);
        L.alog().d(TAG, "init tts : %s", ttsPackage);
    }

    public void speak(String text) {
        if (mTTS == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text,
                    TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString());
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void destroy() {
        if (mQueueHelper != null) {
            mQueueHelper.destroy();
        }
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }

    /**
     * Returns the engine info for a given engine name. Note that engines are
     * identified by their package name.
     */
    private TextToSpeech.EngineInfo getEngineInfo(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE);
        intent.setPackage(packageName);
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        // Note that the current API allows only one engine per
        // package name. Since the "engine name" is the same as
        // the package name.
        if (resolveInfos != null && resolveInfos.size() == 1) {
            return getEngineInfo(resolveInfos.get(0), pm);
        }

        return null;
    }

    private TextToSpeech.EngineInfo getEngineInfo(ResolveInfo resolve, PackageManager pm) {
        ServiceInfo service = resolve.serviceInfo;
        if (service != null) {
            TextToSpeech.EngineInfo engine = new TextToSpeech.EngineInfo();
            // Using just the package name isn't great, since it disallows having
            // multiple engines in the same package, but that's what the existing API does.
            engine.name = service.packageName;
            CharSequence label = service.loadLabel(pm);
            engine.label = TextUtils.isEmpty(label) ? engine.name : label.toString();
            engine.icon = service.getIconResource();
            return engine;
        }
        return null;
    }
}
