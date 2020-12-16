# SafeLayout
用来取代android:fitsSystemWindows="true"的组件，默认实现了SafeFrameLayout、SafeLinearLayout、SafeRelativeLayout和SafeViewGroup

fitsSystemWindows愚蠢在它是消费型的，在一个Activity界面里，设置多个fitsSystemWindows是无效的，只有第一个生效，但这个组件可以帮你忙，除了就可以生效无限个安全区布局外，还可以自行监听处理非安全区大小。

例如你一个Activity带多个Fragment，那么Fragment里的布局设置fitsSystemWindows是无效的。

# Demo
![基础对话框 MessageDialog和 输入对话框 InputDialog](https://github.com/kongzue/SafeLayout/raw/master/README_RES/1.png)

使用`app:safePadding="true"`开启自动padding非安全区。

```
<com.kongzue.safelayout.SafeRelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:safePadding="true">
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#80BEEC89"
        android:text="这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区这里是安全区" />
</com.kongzue.safelayout.SafeRelativeLayout>
```
若你想自己处理非安全区，或做一些炫酷的动画，你可以自行设置监听器处理：
```
.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
    @Override
    public boolean onChange(Rect unsafeRect) {
        //处理 unsafeRect：unsafeRect.left、top、bottom、right
        return false;
    }
})
```
