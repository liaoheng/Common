package com.github.liaoheng.common.core;

import android.os.CountDownTimer;

import com.github.liaoheng.common.util.L;

/**
 * 倒计时帮助类
 *
 * @author liaoheng
 * @version 2017-06-13
 */
public class CountDownHelper {

    private final String TAG = CountDownHelper.class.getSimpleName();
    // 倒计时timer
    private CountDownTimer mCountDownTimer;
    // 计时结束的回调接口
    private CountDownListener mListener;

    //当前倒计时间,单位是毫秒
    private long mCurTime;

    //需要倒计的时长
    private long mFutureTime;
    //间隔时长
    private long mIntervalTime;
    //当前是否暂停
    private boolean pause;

    public void setFutureTime(long futureTime) {
        mFutureTime = futureTime * 1000;
    }

    public long getFutureTime() {
        return mFutureTime;
    }

    public boolean isPause() {
        return pause;
    }

    /**
     * @param futureTime 需要进行倒计时的最大值,单位是秒
     * @param interval 倒计时的间隔，单位是秒
     */
    public CountDownHelper(long futureTime, long interval, CountDownListener listener) {
        mListener = listener;
        //author zhaokaiqiang
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        mIntervalTime = interval * 1000 - 10;
        setFutureTime(futureTime);
    }

    /**
     * @param interval 倒计时的间隔，单位是秒
     */
    public CountDownHelper(int interval, CountDownListener listener) {
        this(0, interval, listener);
    }

    /**
     * @param futureTime 需要进行倒计时的最大值,单位是毫秒
     */
    private void create(long futureTime) {
        L.Log.d(TAG, String.format("create futureTime : %s  IntervalTime : %s", mFutureTime, mIntervalTime));
        mCurTime = 0;
        mFutureTime = futureTime;
        stopCountDownTimer();
        mCountDownTimer = createCountDownTimer(mFutureTime, mIntervalTime);
    }

    private void create() {
        create(mFutureTime);
    }

    private CountDownTimer createCountDownTimer(long max, long interval) {
        return new CountDownTimer(max, interval) {

            @Override
            public void onTick(long time) {
                mCurTime = time;
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if (mListener != null) {
                    mListener.onTick((time + 15) / 1000);
                }
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
    }

    /**
     * 暂停倒计时
     */
    public void pause() {
        L.Log.d(TAG, "pause");
        pause = true;
        stopCountDownTimer();
        mCountDownTimer = createCountDownTimer(mCurTime, mIntervalTime);
        if (mListener != null) {
            mListener.pause(mCurTime);
        }
    }

    /**
     * 开始倒计时
     */
    public void start() {
        start(mFutureTime);
    }

    /**
     * 开始倒计时
     * @param futureTime 需要进行倒计时的最大值,单位是毫秒
     */
    public void start(long futureTime) {
        L.Log.d(TAG, "start");
        if (!pause) {
            create(futureTime);
        }
        mCountDownTimer.start();
        pause = false;
        if (mListener != null) {
            mListener.start();
        }
    }

    /**
     * 结束倒计时
     */
    public void stop() {
        L.Log.d(TAG, "stop");
        stopCountDownTimer();
        pause = false;
        if (mListener != null) {
            mListener.stop();
        }
    }

    public void stopCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 完成倒计时
     */
    public void finish() {
        L.Log.d(TAG, "finish");
        pause = false;
        if (mListener != null) {
            mListener.finish();
        }
    }

    /**
     * 计时状态回调接口
     */
    public interface CountDownListener {
        void start();

        /**
         * @param time 毫秒
         */
        void pause(long time);

        /**
         * @param time 秒
         */
        void onTick(long time);

        void stop();

        void finish();
    }

}
