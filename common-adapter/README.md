# Common adapter

- compileOnly deps.recyclerview_flexibledivider
- compileOnly deps.adapterdelegates2
- compileOnly deps.appcompatv7

- implementation deps.supportv4
- implementation deps.recyclerviewv7
- implementation 'com.vinaysshenoy:mugen:1.0.3'

---

```xml
<com.github.liaoheng.common.adapter.widget.FixTouchSlopSwipeRefreshLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@id/lca_list_swipe_container"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <android.support.v7.widget.RecyclerView
      android:id="@id/lca_list_recycler_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>
</com.github.liaoheng.common.adapter.widget.FixTouchSlopSwipeRefreshLayout>
```