package com.leng.common.helper;

import android.os.CountDownTimer;

/**
 * 倒计时帮助类
 *
 * @author zhaokaiqiang
 * @see  <a href=" http://blog.csdn.net/zhaokaiqiang1992">csdn</a>
 */
public class CountDownHelper {

    // 倒计时timer  
    private CountDownTimer   countDownTimer;
    // 计时结束的回调接口
    private OnCountDownListener listener;

    /**
     *
     * @param max
     *            需要进行倒计时的最大值,单位是秒 
     * @param interval
     *            倒计时的间隔，单位是秒 
     */
    public CountDownHelper(int max, int interval, OnCountDownListener downListener) {
        this.listener=downListener;
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。  
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常  
        countDownTimer = new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s  
                listener.onTick((int) ((time + 15) / 1000));
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.stop();
                }
            }
        };
    }

    /**
     * 开始倒计时 
     */
    public void start() {
        listener.start();
        countDownTimer.start();
    }

    /**
     * 结束倒计时
     */
    public void stop(){
        listener.stop();
        countDownTimer.cancel();
    }

    /**
     * 设置倒计时结束的监听器 
     *
     * @param listener
     */
    public void setOnFinishListener(OnCountDownListener listener) {
        this.listener = listener;
    }

    /**
     * 计时结束的回调接口 
     *
     * @author zhaokaiqiang
     *
     */
    public interface OnCountDownListener {
        void start();

        void onTick(int time);

        void stop();
    }

}
