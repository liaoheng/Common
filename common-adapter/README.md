# Common adapter

- compileOnly deps.fragment
- compileOnly deps.appcompat
- compileOnly deps.adapterdelegates4


---

```xml
<com.github.liaoheng.common.adapter.widget.FixTouchSlopSwipeRefreshLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@id/lca_list_swipe_container"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <androidx.recyclerview.widget.RecyclerView
      android:id="@id/lca_list_recycler_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>
</com.github.liaoheng.common.adapter.widget.FixTouchSlopSwipeRefreshLayout>
```