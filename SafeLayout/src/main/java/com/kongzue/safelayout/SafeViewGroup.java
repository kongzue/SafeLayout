package com.kongzue.safelayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;

import androidx.core.view.ViewCompat;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/12/16 16:00
 */
public class SafeViewGroup extends ViewGroup {
    
    private OnSafeInsetsChangeListener onSafeInsetsChangeListener;
    private boolean autoPaddingUnsafePath = false;
    
    public SafeViewGroup(Context context) {
        super(context);
        init(context, null);
    }
    
    public SafeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public SafeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    public SafeViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SafeRelativeLayout);
            autoPaddingUnsafePath = a.getBoolean(R.styleable.SafeRelativeLayout_safePadding, false);
            a.recycle();
        }
    }
    
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        paddingView(insets.left, insets.top, insets.right, insets.bottom);
        return super.fitSystemWindows(insets);
    }
    
    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paddingView(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }
        return super.dispatchApplyWindowInsets(insets);
    }
    
    public void paddingView(WindowInsets insets) {
        if (insets == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paddingView(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ViewParent parent = getParent();
        
        ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));
        ViewCompat.requestApplyInsets(this);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isInEditMode()) {
            ((Activity) getContext()).getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(decorViewLayoutListener);
        }
    }
    
    private ViewTreeObserver.OnGlobalLayoutListener decorViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                paddingView(getRootWindowInsets());
            }
        }
    };
    
    @Override
    protected void onDetachedFromWindow() {
        if (decorViewLayoutListener != null) {
            ((Activity) getContext()).getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(decorViewLayoutListener);
        }
        super.onDetachedFromWindow();
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    
    }
    
    protected Rect unsafePlace;
    
    private void paddingView(int left, int top, int right, int bottom) {
        unsafePlace = new Rect(left, top, right, bottom);
        if (onSafeInsetsChangeListener != null) {
            if (onSafeInsetsChangeListener.onChange(unsafePlace)) {
                if (autoPaddingUnsafePath) setPadding(left, top, right, bottom);
            }
        } else {
            if (autoPaddingUnsafePath) setPadding(left, top, right, bottom);
        }
    }
    
    public Rect getUnsafePlace() {
        return unsafePlace;
    }
    
    public OnSafeInsetsChangeListener getOnSafeInsetsChangeListener() {
        return onSafeInsetsChangeListener;
    }
    
    public SafeViewGroup setOnSafeInsetsChangeListener(OnSafeInsetsChangeListener onSafeInsetsChangeListener) {
        this.onSafeInsetsChangeListener = onSafeInsetsChangeListener;
        if (unsafePlace != null) {
            onSafeInsetsChangeListener.onChange(unsafePlace);
        }
        return this;
    }
    
    public boolean isAutoPaddingUnsafePath() {
        return autoPaddingUnsafePath;
    }
    
    public SafeViewGroup setAutoPaddingUnsafePath(boolean autoPaddingUnsafePath) {
        this.autoPaddingUnsafePath = autoPaddingUnsafePath;
        return this;
    }
    
    public interface OnSafeInsetsChangeListener {
        boolean onChange(Rect unsafeRect);
    }
    
}
