package com.github.liaoheng.common.plus.core;

import com.github.liaoheng.common.plus.BaseTest;
import com.github.liaoheng.common.plus.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.SdkConfig;

import static org.junit.Assert.assertEquals;

/**
 * @author liaoheng
 * @version 2016-07-28 15:31
 */
@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = SdkConfig.FALLBACK_SDK_VERSION) public class ListDuplicateHelperTest
        extends BaseTest {

    class D {
        public D(int key, int value) {
            this.key = key;
            this.value = value;
        }

        int key;
        int value;
    }

    ListDuplicateHelper<D> mHelper;
    List<D> ds = new ArrayList<>();

    @Override public void setUp() throws Exception {
        super.setUp();
        mHelper = new ListDuplicateHelper<>(ds, new ListDuplicateHelper.DataOperationListener<D>() {
            @Override public void handle(int index, D data, D item) {
                data.value++;
            }
        });
    }

    @Test public void addDuplicateTest() {
        int[] ints = new int[] { 1, 2, 3, 3, 2, 2, 5, 5, 4, 4, 10 };

        for (int i = 0; i < ints.length; i++) {
            mHelper.addDuplicate(new D(ints[i], 1), ints[i]);
        }

        assertEquals("must run 3 next", ds.get(1).value, 3);

        //for (D d : ds) {
        //    ShadowLog.d(TAG, "k:" + d.key + "  v:" + d.value);
        //}

    }

}