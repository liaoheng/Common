# Common adapter

- compileOnly deps.fragment
- compileOnly deps.appcompat
- compileOnly deps.recyclerview_flexibledivider
- compileOnly deps.adapterdelegates2
- compileOnly deps.v4swiperefreshlayout

- implementation deps.core
- implementation deps.v7recyclerview
- implementation com.vinaysshenoy:mugen


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