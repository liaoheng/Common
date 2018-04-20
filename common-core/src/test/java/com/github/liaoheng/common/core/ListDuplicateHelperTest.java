package com.github.liaoheng.common.core;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author liaoheng
 * @version 2016-07-28 15:31
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ListDuplicateHelperTest extends BaseTest {

    class D {
        D(int key, int value) {
            this.key = key;
            this.value = value;
        }

        int key;
        int value;
    }

    private ListDuplicateHelper<D> mHelper;
    private List<D> ds = new ArrayList<>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mHelper = new ListDuplicateHelper<>(ds, new ListDuplicateHelper.DataOperationListener<D>() {
            @Override
            public void handle(int index, D data, D item) {
                data.value++;//重复记数
            }
        });
    }

    @Test
    public void addDuplicateTest() {
        int[] ints = new int[] { 1, 2, 3, 3, 2, 2, 5, 5, 4, 4, 10 };

        for (int anInt : ints) {
            mHelper.addDuplicate(new D(anInt, 1), anInt);
        }

        assertEquals("must duplicate count 3", ds.get(1).value, 3);

        //for (D d : ds) {
        //    log("key:" + d.key + "  count:" + d.value);
        //}

    }

}