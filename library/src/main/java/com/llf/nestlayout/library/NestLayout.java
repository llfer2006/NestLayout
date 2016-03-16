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
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by llf on 2015/7/8.
 */
public class NestLayout extends ViewGroup implements NestedScrollingParent {

    public interface OnSectionChangedListener {
        void onSectionChanged(CharSequence old, CharSequence current);
    }

    private Scroller mScroller;
    /**
     * indicate which is current nested view,may be not this ViewGroup direct child
     */
    private View mNestedView;
    /**
     * Section Change Lisnter
     */
    private OnSectionChangedListener mSectionChangeListener;
    private int[] mTmpCoord;
    public NestLayout(Context context) {
        this(context, null);
    }

    public NestLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mTmpCoord = new int[2];
    }

    public void setSectionChangeListener(OnSectionChangedListener listener) {
        mSectionChangeListener = listener;
    }

    @Override public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (nestedScrollAxes != ViewCompat.SCROLL_AXIS_VERTICAL) {
            return false;
        }
        return true;
    }

    @Override public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {

    }

    @Override public void onStopNestedScroll(View target) {
        target = mNestedView;
        mNestedView = null;
        if (target == null)
            return;
        int pY = getScrollY() + getPaddingTop();
        View child = getChildFromTarget(target);
        //found section view from nested view;
        View section = findSectionView(indexOfChild(child));
        int dy = 0;
        //whether the section view needs scroll to really position
        if (pY == section.getTop()) {
            return;
        }
        getLocationOnScreen(mTmpCoord);
        int parentOffset = mTmpCoord[1];
        if (section.getTop() < pY) {
            View nextSection = findNextSectionView(indexOfChild(section));
            if (nextSection != null && section != nextSection) {
                nextSection.getLocationOnScreen(mTmpCoord);
                mTmpCoord[1] -= parentOffset;
                if (mTmpCoord[1] < getHeight() * 4 / 5) {
                    performSectionChange(section, nextSection);
                    dy = mTmpCoord[1];
                } else {
                    dy = mTmpCoord[1] - getHeight();
                }
            }
        } else {
            View preSection = findSectionView(indexOfChild(section) - 1);
            if (preSection != null && section != preSection) {
                preSection.getLocationOnScreen(mTmpCoord);
                mTmpCoord[1] -= parentOffset;
                if (mTmpCoord[1] > getHeight()/5) {
                    performSectionChange(section, preSection);
                    dy = mTmpCoord[1] - getHeight();
                } else {
                    dy = mTmpCoord[1];
                }
            }
        }
        if (dy != 0) {
            mScroller.startScroll(0, pY, 0, dy);
            invalidate();
        }
    }

    protected void performSectionChange(View section, View next) {
        if (mSectionChangeListener != null) {
            CharSequence old = ((LayoutParams) section.getLayoutParams()).mSectionTag;
            CharSequence current = ((LayoutParams) next.getLayoutParams()).mSectionTag;
            mSectionChangeListener.onSectionChanged(old, current);
        }
    }

    @Override public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        mNestedView = target;
        int pY = getScrollY() + getPaddingTop();
        View child = getChildFromTarget(target);
        View section = findSectionView(indexOfChild(child));
        if (section.getTop() == pY)
            return;
        if ((section.getTop() < pY && dy < 0) || (section.getTop() > pY && dy > 0)) {
            scrollByIfNeed(dy);
            consumed[1] = getScrollY() - pY;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        mNestedView = target;
        scrollByIfNeed(dyUnconsumed);
    }

    @Override public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return !mScroller.isFinished();
    }

    @Override public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    @Override protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onStopNestedScroll(null);
            //block the child view fling
            if (!mScroller.isFinished())
                ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        } else {
            super.computeScroll();
        }
    }

    void scrollByIfNeed(int dy) {
        int scrollY = getScrollY();
        if (scrollY <= 0 && dy < 0)
            return;
        int childCount = getChildCount();
        if (childCount == 0)
            return;
        View lastChild = getChildAt(childCount - 1);
        if (scrollY + getHeight() >= lastChild.getTop() + lastChild.getMeasuredHeight() && dy > 0)
            return;
        int delta;
        if (dy > 0) {
            delta = Math.min(Math.max(lastChild.getTop() + lastChild.getMeasuredHeight() - scrollY, 0), dy);
        } else {
            delta = Math.max(dy, -scrollY);
        }
        scrollBy(0, delta);
    }

    View getChildFromTarget(View t) {
        while (t.getParent() != null && t.getParent() != this)
            t = (View) t.getParent();
        return t;
    }

    View findSectionView(int end) {
        int childCount = getChildCount();
        end = end < 0 ? childCount - 1 : Math.min(end, childCount - 1);
        View child;
        for (int i = end; i >= 0; i--) {
            child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            if (isSectionView(child))
                return child;
        }
        return null;
    }

    View findNextSectionView(int start) {
        int childCount = getChildCount();
        View child;
        for (int i = start + 1; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            if (isSectionView(child))
                return child;
        }
        return null;
    }

    boolean isSectionView(View v) {
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        if (lp.isSection())
            return true;
        return indexOfChild(v) == 0;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(
                getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int padLeft = getPaddingLeft();
        int padTop = getPaddingTop();
        int childCount = getChildCount();
        View child;
        LayoutParams lp;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            lp = (LayoutParams) child.getLayoutParams();
            padTop += lp.topMargin;
            child.layout(padLeft + lp.leftMargin, padTop, padLeft + lp.leftMargin + child.getMeasuredWidth(),
                    padTop + child.getMeasuredHeight());
            padTop += child.getMeasuredHeight() + lp.bottomMargin;
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private boolean mSection;
        private CharSequence mSectionTag;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.NestLayout);
            mSection = a.getBoolean(R.styleable.NestLayout_section, false);
            mSectionTag = a.getText(R.styleable.NestLayout_sectionTag);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams p) {
            super(p);
            mSection = p.mSection;
            mSectionTag = p.mSectionTag;
        }

        public boolean isSection() {
            return mSection;
        }

        public CharSequence getSectionTag() {
            return mSectionTag;
        }

        public void setSection(boolean enable) {
            mSection = enable;
        }

        public void setSection(boolean enable, String tag) {
            mSection = enable;
            mSectionTag = tag;
        }
    }
}
