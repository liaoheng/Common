package com.leng.common;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.leng.common.util.BitmapUtils;
import com.leng.common.util.Utils;

/**
 * @author liaoheng
 * @version 2016-03-31 11:29
 */
@RunWith(RobolectricTestRunner.class)
public class BitmapUtilsTest {

    @Test
    public void getImageExtensionTest(){
        String file="/opt/1.jpg";
        Bitmap.CompressFormat imageExtension = BitmapUtils.getImageExtension(file);
        Assert.assertEquals("not ", imageExtension.name(), "JPEG");
    }

    @Test
    public void appendUrlParameterTest(){
        String url="http://117.172.59.127:8011/api/?area_code=&access_token=86f3059b228c8acf99e69734b6bb32cc";
        url= Utils.appendUrlParameter(url,"area_code","YA");
        System.out.println(url);
    }
}
