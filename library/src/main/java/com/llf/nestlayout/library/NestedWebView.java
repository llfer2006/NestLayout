/*
 *
 *  * Copyright 2015 llfer2006@gmail.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.llf.nestlayout.library;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.webkit.WebView;

/**
 * Created by llf on 2015/7/8.
 */
public class NestedWebView extends WebView implements NestedScrollingChild {
    private int mLastX, mLastY, mTouchY, mTouchSlop;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedOffsetY;

    private boolean mTouchCancel;
    private NestedScrollingChildHelper mChildHelper;

    public NestedWebView(Context context) {
        this(context, null);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
            int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private static final String TAG = "NestedWebView";

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (!isNestedScrollingEnabled())
            return super.onTouchEvent(event);
        boolean rs = false;
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0;
            super.onTouchEvent(event);
        }
        MotionEvent ne = MotionEvent.obtain(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rs = super.onTouchEvent(ne);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                mTouchCancel = false;
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                mTouchY = mLastY;
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) event.getY();
                if (Math.abs(mTouchY - y) >= mTouchSlop) {
                    //cancel click,Long click
                    if(!mTouchCancel){
                        mTouchCancel = true;
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(event);
                    }

                    int x = (int) event.getX();
                    int dx = mLastX - x;
                    int dy = mLastY - y;
                    int oldY = getScrollY();
                    Log.i(TAG, "PreScroll pre: dy :" + dy + ",lastY:" + mLastY + ",Y:" + y + ",no:" + mNestedOffsetY);
                    if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                        dy -= mScrollConsumed[1];
                        mNestedOffsetY += mScrollOffset[1];
                        Log.i(TAG, "PreScroll consume y:" + mScrollConsumed[1] + ",offset:" + mScrollOffset[1] + ",y:" +
                                ne.getY());
                    }

                    //
                    overScrollBy(dx, dy, getScrollX(), oldY, computeHorizontalScrollRange(),
                            computeVerticalScrollRange(), computeHorizontalScrollExtent(),
                            computeVerticalScrollExtent(), true);
                    mLastX = x;
                    mLastY = y - mScrollOffset[1];
                    int scrollDelta = getScrollY() - oldY;
                    dy -= scrollDelta;
                    Log.i(TAG, "Scroll pre oldScrollY:" + oldY + ",cScrollY:" + getScrollY());
                    if (dispatchNestedScroll(0, scrollDelta, 0, dy, mScrollOffset)) {
                        Log.i(TAG, "Scroll offset:" + mScrollOffset[1]);
                        mNestedOffsetY += mScrollOffset[1];
                        mLastY -= mScrollOffset[1];
                    }
                    rs = true;
                } else {
                    rs = super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(!mTouchCancel)
                    rs = super.onTouchEvent(ne);
                stopNestedScroll();
                break;
        }
        return rs;
    }
}
