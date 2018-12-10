package com.github.liaoheng.mugen;

import android.widget.AbsListView;

import com.github.liaoheng.mugen.attachers.AbsListViewAttacher;
import com.github.liaoheng.mugen.attachers.RecyclerViewAttacher;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Helper class to detect whenever an {@link android.widget.AbsListView} has to given a Load
 * More implementation.
 *
 * @author vinaysshenoy 31/10/14
 */
public class Mugen {

    private static final String TAG = "Mugen";

    private Mugen() {
        //Default constructor to prevent initialization
    }

    /**
     * Creates a Attacher for AbsListView implementations
     *
     * @param absListView The List for which load more functionality is needed
     * @param callbacks   The callbacks which will receive the Load more events
     */
    public static AbsListViewAttacher with(final AbsListView absListView, final MugenCallbacks callbacks) {
        return new AbsListViewAttacher(absListView, callbacks);
    }

    /**
     * Creates a Attacher for RecyclerView implementations
     *
     * @param recyclerView The List for which load more functionality is needed
     * @param callbacks    The callbacks which will receive the Load more events
     */
    public static RecyclerViewAttacher with(final RecyclerView recyclerView, final MugenCallbacks callbacks) {
        return new RecyclerViewAttacher(recyclerView, callbacks);
    }

}
