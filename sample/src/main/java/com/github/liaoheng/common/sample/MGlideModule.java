package com.github.liaoheng.common.sample;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.github.liaoheng.common.network.CNGlideModule;

import androidx.annotation.NonNull;

/**
 * @author liaoheng
 * @version 2018-04-19 11:08
 */
@GlideModule
public class MGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        CNGlideModule.applyOptions(context, builder);
    }
}
