# Common ui

- compileOnly deps.rxlifecycle
- compileOnly deps.rxlifecycle_android
- compileOnly 'com.h6ah4i.android.tablayouthelper:tablayouthelper:0.8.0'

- implementation deps.design
- implementation deps.supportv4
- implementation deps.appcompatv7
- implementation deps.recyclerviewv7

---

```xml
    <!--BottomSheetDialog-->
    <style name="LCU.BottomSheetDialog" parent="Theme.Design.Light.BottomSheetDialog">
        <item name="colorPrimary">@color/lcu_colorPrimary</item>
        <item name="colorPrimaryDark">@color/lcu_colorPrimaryDark</item>
        <item name="colorAccent">@color/lcu_colorAccent</item>
    </style>
```