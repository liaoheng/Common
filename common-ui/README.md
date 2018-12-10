# Common ui

- compileOnly deps.rxlifecycle
- compileOnly deps.rxlifecycle_android
- compileOnly deps.rxlifecycle3
- compileOnly deps.rxlifecycle_android3
- compileOnly com.h6ah4i.android.tablayouthelper

- implementation deps.core
- implementation deps.annotations
- implementation deps.appcompat
- implementation deps.recyclerview
- implementation deps.viewpager
- implementation deps.coordinatorlayout
- implementation deps.design
- implementation deps.fragment

---

```xml
    <!--BottomSheetDialog-->
    <style name="LCU.BottomSheetDialog" parent="Theme.Design.Light.BottomSheetDialog">
        <item name="colorPrimary">@color/lcu_colorPrimary</item>
        <item name="colorPrimaryDark">@color/lcu_colorPrimaryDark</item>
        <item name="colorAccent">@color/lcu_colorAccent</item>
    </style>
```