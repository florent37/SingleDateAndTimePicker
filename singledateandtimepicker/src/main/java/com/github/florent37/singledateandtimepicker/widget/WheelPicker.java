package com.github.florent37.singledateandtimepicker.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.LocaleHelper;
import com.github.florent37.singledateandtimepicker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public abstract class WheelPicker<V> extends View {

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SCROLLING = 2;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int MAX_ANGLE = 90;
    protected final static String FORMAT = "%1$02d"; // two digits
    protected DateHelper dateHelper = new DateHelper(); // Overwritten from Single..Picker
    private final Handler handler = new Handler();
    protected V defaultValue;
    protected int lastScrollPosition;
    protected Listener<WheelPicker, V> listener;
    protected Adapter<V> adapter = new Adapter<>();
    private Locale customLocale;
    private Paint paint;
    private Scroller scroller;
    private VelocityTracker tracker;
    private OnItemSelectedListener onItemSelectedListener;
    private OnWheelChangeListener onWheelChangeListener;
    private final Rect rectDrawn = new Rect();
    private final Rect rectIndicatorHead = new Rect();
    private final Rect rectIndicatorFoot = new Rect();
    private final Rect rectCurrentItem = new Rect();
    private final Camera camera = new Camera();
    private final Matrix matrixRotate = new Matrix();
    private final Matrix matrixDepth = new Matrix();
    private String maxWidthText;

    private int mVisibleItemCount, mDrawnItemCount;
    private int mHalfDrawnItemCount;
    private int mTextMaxWidth, mTextMaxHeight;
    private int mItemTextColor, mSelectedItemTextColor;
    private int mItemTextSize;
    private int mIndicatorSize;
    private int mIndicatorColor;
    private int mCurtainColor;
    private int mItemSpace;
    private int mMaxAngle = MAX_ANGLE;
    private int mItemAlign;
    private int mItemHeight, mHalfItemHeight;
    private int mHalfWheelHeight;
    private int selectedItemPosition;
    private int currentItemPosition;
    private int minFlingY, maxFlingY;
    private int minimumVelocity = 50, maximumVelocity = 8000;
    private int wheelCenterX, wheelCenterY;
    private int drawnCenterX, drawnCenterY;
    private int scrollOffsetY;
    private int textMaxWidthPosition;
    private int lastPointY;
    private int downPointY;
    private int touchSlop = 8;

    private boolean hasSameWidth;
    private boolean hasIndicator;
    private boolean hasCurtain;
    private boolean hasAtmospheric;
    private boolean isCyclic;
    private boolean isCurved;
    private boolean showOnlyFutureDate;

    private boolean isClick;
    private boolean isForceFinishScroll;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (null == adapter) return;
            final int itemCount = adapter.getItemCount();
            if (itemCount == 0) return;
            if (scroller.isFinished() && !isForceFinishScroll) {
                if (mItemHeight == 0) return;
                int position = (-scrollOffsetY / mItemHeight + selectedItemPosition) % itemCount;
                position = position < 0 ? position + itemCount : position;
                currentItemPosition = position;
                onItemSelected();
                if (null != onWheelChangeListener) {
                    onWheelChangeListener.onWheelSelected(position);
                    onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }
            if (scroller.computeScrollOffset()) {
                if (null != onWheelChangeListener) {
                    onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_SCROLLING);
                }

                scrollOffsetY = scroller.getCurrY();

                int position = (-scrollOffsetY / mItemHeight + selectedItemPosition) % itemCount;
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onCurrentItemOfScroll(WheelPicker.this, position);
                }
                onItemCurrentScroll(position, adapter.getItem(position));

                postInvalidate();
                handler.postDelayed(this, 16);
            }
        }
    };

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker);

        mItemTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_text_size, getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        mVisibleItemCount = a.getInt(R.styleable.WheelPicker_wheel_visible_item_count, 7);
        selectedItemPosition = a.getInt(R.styleable.WheelPicker_wheel_selected_item_position, 0);
        hasSameWidth = a.getBoolean(R.styleable.WheelPicker_wheel_same_width, false);
        textMaxWidthPosition = a.getInt(R.styleable.WheelPicker_wheel_maximum_width_text_position, -1);
        maxWidthText = a.getString(R.styleable.WheelPicker_wheel_maximum_width_text);
        mSelectedItemTextColor = a.getColor(R.styleable.WheelPicker_wheel_selected_item_text_color, -1);
        mItemTextColor = a.getColor(R.styleable.WheelPicker_wheel_item_text_color, 0xFF888888);
        mItemSpace = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_space, getResources().getDimensionPixelSize(R.dimen.WheelItemSpace));
        isCyclic = a.getBoolean(R.styleable.WheelPicker_wheel_cyclic, false);
        hasIndicator = a.getBoolean(R.styleable.WheelPicker_wheel_indicator, false);
        mIndicatorColor = a.getColor(R.styleable.WheelPicker_wheel_indicator_color, 0xFFEE3333);
        mIndicatorSize = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_indicator_size, getResources().getDimensionPixelSize(R.dimen.WheelIndicatorSize));
        hasCurtain = a.getBoolean(R.styleable.WheelPicker_wheel_curtain, false);
        mCurtainColor = a.getColor(R.styleable.WheelPicker_wheel_curtain_color, 0x88FFFFFF);
        hasAtmospheric = a.getBoolean(R.styleable.WheelPicker_wheel_atmospheric, false);
        isCurved = a.getBoolean(R.styleable.WheelPicker_wheel_curved, false);
        mItemAlign = a.getInt(R.styleable.WheelPicker_wheel_item_align, ALIGN_CENTER);
        a.recycle();

        updateVisibleItemCount();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(mItemTextSize);

        scroller = new Scroller(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            ViewConfiguration conf = ViewConfiguration.get(getContext());
            minimumVelocity = conf.getScaledMinimumFlingVelocity();
            maximumVelocity = conf.getScaledMaximumFlingVelocity();
            touchSlop = conf.getScaledTouchSlop();
        }

        init();
        defaultValue = initDefault();
        adapter.setData(generateAdapterValues(showOnlyFutureDate));
        currentItemPosition = adapter.getItemPosition(defaultValue);
        selectedItemPosition = currentItemPosition;
    }

    protected abstract void init();

    protected abstract V initDefault();

    public void updateAdapter() {
        adapter.setData(generateAdapterValues(showOnlyFutureDate));
        notifyDatasetChanged();
    }

    protected abstract List<V> generateAdapterValues(boolean showOnlyFutureDates);

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAdapter(adapter);
        setDefault(this.defaultValue);
    }

    private void updateVisibleItemCount() {
        if (mVisibleItemCount < 2) {
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");
        }

        if (mVisibleItemCount % 2 == 0) mVisibleItemCount += 1;
        mDrawnItemCount = mVisibleItemCount + 2;
        mHalfDrawnItemCount = mDrawnItemCount / 2;
    }

    private void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (hasSameWidth) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(0));
        } else if (isPosInRang(textMaxWidthPosition)) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(textMaxWidthPosition));
        } else if (!TextUtils.isEmpty(maxWidthText)) {
            mTextMaxWidth = (int) paint.measureText(maxWidthText);
        } else {
            final int itemCount = adapter.getItemCount();
            for (int i = 0; i < itemCount; ++i) {
                String text = adapter.getItemText(i);
                int width = (int) paint.measureText(text);
                mTextMaxWidth = Math.max(mTextMaxWidth, width);
            }
        }
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void updateItemTextAlign() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case ALIGN_RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    protected void updateDefault() {
        setSelectedItemPosition(getDefaultItemPosition());
    }

    public void setDefault(V defaultValue) {
        this.defaultValue = defaultValue;
        updateDefault();
    }

    public void setDefaultDate(Date date) {
        if (adapter != null && adapter.getItemCount() > 0) {
            final int indexOfDate = findIndexOfDate(date);
            if (indexOfDate >= 0) {
                this.defaultValue = adapter.getData().get(indexOfDate);
                setSelectedItemPosition(indexOfDate);
            }
        }
    }

    public void selectDate(Date date) {
        setSelectedItemPosition(findIndexOfDate(date));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCustomLocale(Locale customLocale) {
        this.customLocale = customLocale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Correct sizes of original content
        int resultWidth = mTextMaxWidth;
        int resultHeight = mTextMaxHeight * mVisibleItemCount + mItemSpace * (mVisibleItemCount - 1);

        // Correct view sizes again if curved is enable
        if (isCurved) {
            // The text is written on the circle circumference from -mMaxAngle to mMaxAngle.
            // 2 * sinDegree(mMaxAngle): Height of drawn circle
            // Math.PI: Circumference of half unit circle, `mMaxAngle / 90f`: The ratio of half-circle we draw on
            resultHeight = (int) (2 * sinDegree(mMaxAngle) / (Math.PI * mMaxAngle / 90f) * resultHeight);
        }

        // Consideration padding influence the view sizes
        resultWidth += getPaddingLeft() + getPaddingRight();
        resultHeight += getPaddingTop() + getPaddingBottom();

        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth);
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight);

        setMeasuredDimension(resultWidth, resultHeight);
    }

    private int measureSize(int mode, int sizeExpect, int sizeActual) {
        int realSize;
        if (mode == MeasureSpec.EXACTLY) {
            realSize = sizeExpect;
        } else {
            realSize = sizeActual;
            if (mode == MeasureSpec.AT_MOST) realSize = Math.min(realSize, sizeExpect);
        }
        return realSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set content region
        rectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        // Get the center coordinates of content region
        wheelCenterX = rectDrawn.centerX();
        wheelCenterY = rectDrawn.centerY();

        // Correct item drawn center
        computeDrawnCenter();

        mHalfWheelHeight = rectDrawn.height() / 2;

        mItemHeight = rectDrawn.height() / mVisibleItemCount;
        mHalfItemHeight = mItemHeight / 2;

        // Initialize fling max Y-coordinates
        computeFlingLimitY();

        // Correct region of indicator
        computeIndicatorRect();

        // Correct region of current select item
        computeCurrentItemRect();
    }

    private void computeDrawnCenter() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                drawnCenterX = rectDrawn.left;
                break;
            case ALIGN_RIGHT:
                drawnCenterX = rectDrawn.right;
                break;
            default:
                drawnCenterX = wheelCenterX;
                break;
        }
        drawnCenterY = (int) (wheelCenterY - ((paint.ascent() + paint.descent()) / 2));
    }

    private void computeFlingLimitY() {
        int currentItemOffset = selectedItemPosition * mItemHeight;
        minFlingY = isCyclic ? Integer.MIN_VALUE
                : -mItemHeight * (adapter.getItemCount() - 1) + currentItemOffset;
        maxFlingY = isCyclic ? Integer.MAX_VALUE : currentItemOffset;
    }

    private void computeIndicatorRect() {
        if (!hasIndicator) return;
        int halfIndicatorSize = mIndicatorSize / 2;
        int indicatorHeadCenterY = wheelCenterY + mHalfItemHeight;
        int indicatorFootCenterY = wheelCenterY - mHalfItemHeight;
        rectIndicatorHead.set(rectDrawn.left, indicatorHeadCenterY - halfIndicatorSize, rectDrawn.right,
                indicatorHeadCenterY + halfIndicatorSize);
        rectIndicatorFoot.set(rectDrawn.left, indicatorFootCenterY - halfIndicatorSize, rectDrawn.right,
                indicatorFootCenterY + halfIndicatorSize);
    }

    private void computeCurrentItemRect() {
        if (!hasCurtain && mSelectedItemTextColor == -1) return;
        rectCurrentItem.set(rectDrawn.left, wheelCenterY - mHalfItemHeight, rectDrawn.right,
                wheelCenterY + mHalfItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != onWheelChangeListener) onWheelChangeListener.onWheelScrolled(scrollOffsetY);
        if (mItemHeight - mHalfDrawnItemCount <= 0) {
            return;
        }
        int drawnDataStartPos = -scrollOffsetY / mItemHeight - mHalfDrawnItemCount;
        for (int drawnDataPos = drawnDataStartPos + selectedItemPosition,
             drawnOffsetPos = -mHalfDrawnItemCount;
             drawnDataPos < drawnDataStartPos + selectedItemPosition + mDrawnItemCount;
             drawnDataPos++, drawnOffsetPos++) {
            String data = "";
            if (isCyclic) {
                final int itemCount = this.adapter.getItemCount();
                int actualPos = drawnDataPos % itemCount;
                actualPos = actualPos < 0 ? (actualPos + itemCount) : actualPos;
                data = adapter.getItemText(actualPos);
            } else {
                if (isPosInRang(drawnDataPos)) {
                    data = adapter.getItemText(drawnDataPos);
                }
            }
            paint.setColor(mItemTextColor);
            paint.setStyle(Paint.Style.FILL);
            int mDrawnItemCenterY = drawnCenterY + (drawnOffsetPos * mItemHeight) +
                    scrollOffsetY % mItemHeight;

            float distanceToCenter = 0;
            if (isCurved) {
                // Correct ratio of item's drawn center to wheel center
                float ratio = (drawnCenterY - Math.abs(drawnCenterY - mDrawnItemCenterY) -
                        rectDrawn.top) * 1.0F / (drawnCenterY - rectDrawn.top);

                // Correct unit
                int unit = 0;
                if (mDrawnItemCenterY > drawnCenterY) {
                    unit = 1;
                } else if (mDrawnItemCenterY < drawnCenterY) unit = -1;

                float degree = clamp((-(1 - ratio) * mMaxAngle * unit), -mMaxAngle, mMaxAngle);
                distanceToCenter = computeYCoordinateAtAngle(degree);

                float transX = wheelCenterX;
                switch (mItemAlign) {
                    case ALIGN_LEFT:
                        transX = rectDrawn.left;
                        break;
                    case ALIGN_RIGHT:
                        transX = rectDrawn.right;
                        break;
                }
                float transY = wheelCenterY - distanceToCenter;

                camera.save();
                camera.rotateX(degree);
                camera.getMatrix(matrixRotate);
                camera.restore();
                matrixRotate.preTranslate(-transX, -transY);
                matrixRotate.postTranslate(transX, transY);

                camera.save();
                camera.translate(0, 0, computeDepth((int) degree));
                camera.getMatrix(matrixDepth);
                camera.restore();
                matrixDepth.preTranslate(-transX, -transY);
                matrixDepth.postTranslate(transX, transY);

                matrixRotate.postConcat(matrixDepth);
            }
            if (hasAtmospheric) {
                int alpha =
                        (int) ((drawnCenterY - Math.abs(drawnCenterY - mDrawnItemCenterY)) * 1.0F / drawnCenterY
                                * 255);
                alpha = alpha < 0 ? 0 : alpha;
                paint.setAlpha(alpha);
            }
            // Correct item's drawn centerY base on curved state
            float drawnCenterY = isCurved ? this.drawnCenterY - distanceToCenter : mDrawnItemCenterY;

            // Judges need to draw different color for current item or not
            if (mSelectedItemTextColor != -1) {
                canvas.save();
                if (isCurved) canvas.concat(matrixRotate);
                canvas.clipRect(rectCurrentItem, Region.Op.DIFFERENCE);
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();

                paint.setColor(mSelectedItemTextColor);
                canvas.save();
                if (isCurved) canvas.concat(matrixRotate);
                canvas.clipRect(rectCurrentItem);
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(rectDrawn);
                if (isCurved) canvas.concat(matrixRotate);
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();
            }
        }
        // Need to draw curtain or not
        if (hasCurtain) {
            paint.setColor(mCurtainColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectCurrentItem, paint);
        }
        // Need to draw indicator or not
        if (hasIndicator) {
            paint.setColor(mIndicatorColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectIndicatorHead, paint);
            canvas.drawRect(rectIndicatorFoot, paint);
        }
    }

    private boolean isPosInRang(int position) {
        return position >= 0 && position < adapter.getItemCount();
    }

    private float computeYCoordinateAtAngle(float degree) {
        // Compute y-coordinate for item at degree. mMaxAngle is at mHalfWheelHeight
        return sinDegree(degree) / sinDegree(mMaxAngle) * mHalfWheelHeight;
    }

    private float sinDegree(float degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

    private float computeDepth(float degree) {
        return (float) (mHalfWheelHeight - Math.cos(Math.toRadians(degree)) * mHalfWheelHeight);
    }

    private float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (null != getParent()) getParent().requestDisallowInterceptTouchEvent(true);
                    if (null == tracker) {
                        tracker = VelocityTracker.obtain();
                    } else {
                        tracker.clear();
                    }
                    tracker.addMovement(event);
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                        isForceFinishScroll = true;
                    }
                    downPointY = lastPointY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(downPointY - event.getY()) < touchSlop
                            && computeDistanceToEndPoint(scroller.getFinalY() % mItemHeight) > 0) {
                        isClick = true;
                        break;
                    }
                    isClick = false;
                    tracker.addMovement(event);
                    if (null != onWheelChangeListener) {
                        onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_DRAGGING);
                    }

                    // Scroll WheelPicker's content
                    float move = event.getY() - lastPointY;
                    if (Math.abs(move) < 1) break;
                    scrollOffsetY += move;
                    lastPointY = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (null != getParent()) getParent().requestDisallowInterceptTouchEvent(false);
                    if (isClick) break;
                    tracker.addMovement(event);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                        tracker.computeCurrentVelocity(1000, maximumVelocity);
                    } else {
                        tracker.computeCurrentVelocity(1000);
                    }

                    // Judges the WheelPicker is scroll or fling base on current velocity
                    isForceFinishScroll = false;
                    int velocity = (int) tracker.getYVelocity();
                    if (Math.abs(velocity) > minimumVelocity) {
                        scroller.fling(0, scrollOffsetY, 0, velocity, 0, 0, minFlingY, maxFlingY);
                        scroller.setFinalY(
                                scroller.getFinalY() + computeDistanceToEndPoint(scroller.getFinalY() % mItemHeight));
                    } else {
                        scroller.startScroll(0, scrollOffsetY, 0,
                                computeDistanceToEndPoint(scrollOffsetY % mItemHeight));
                    }
                    // Correct coordinates
                    if (!isCyclic) {
                        if (scroller.getFinalY() > maxFlingY) {
                            scroller.setFinalY(maxFlingY);
                        } else if (scroller.getFinalY() < minFlingY) scroller.setFinalY(minFlingY);
                    }
                    handler.post(runnable);
                    if (null != tracker) {
                        tracker.recycle();
                        tracker = null;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (null != getParent()) getParent().requestDisallowInterceptTouchEvent(false);
                    if (null != tracker) {
                        tracker.recycle();
                        tracker = null;
                    }
                    break;
            }
        }
        return true;
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mHalfItemHeight) {
            if (scrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    public void scrollTo(final int itemPosition) {
        if (itemPosition != currentItemPosition) {
            final int differencesLines = currentItemPosition - itemPosition;
            final int newScrollOffsetY =
                    scrollOffsetY + (differencesLines * mItemHeight); // % adapter.getItemCount();

            ValueAnimator va = ValueAnimator.ofInt(scrollOffsetY, newScrollOffsetY);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    scrollOffsetY = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentItemPosition = itemPosition;
                    onItemSelected();
                }
            });
            va.start();
        }
    }

    private final void onItemSelected() {
        int position = currentItemPosition;
        final V item = this.adapter.getItem(position);
        if (null != onItemSelectedListener) {
            onItemSelectedListener.onItemSelected(this, item, position);
        }
        onItemSelected(position, item);
    }

    protected void onItemSelected(int position, V item) {
        if (listener != null) {
            listener.onSelected(this, position, item);
        }
    }


    protected void onItemCurrentScroll(int position, V item) {
        if (lastScrollPosition != position) {
            if (listener != null) {
                listener.onCurrentScrolled(this, position, item);
                if (lastScrollPosition == adapter.getItemCount() - 1 && position == 0) {
                    onFinishedLoop();
                }
            }
            lastScrollPosition = position;
        }
    }

    protected void onFinishedLoop() {

    }

    protected String getFormattedValue(Object value) {
        return String.valueOf(value);
    }

    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    public void setVisibleItemCount(int count) {
        mVisibleItemCount = count;
        updateVisibleItemCount();
        requestLayout();
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        computeFlingLimitY();
        invalidate();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        onItemSelectedListener = listener;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int position) {
        position = Math.min(position, adapter.getItemCount() - 1);
        position = Math.max(position, 0);
        selectedItemPosition = position;
        currentItemPosition = position;
        scrollOffsetY = 0;
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    public int getDefaultItemPosition() {
        return adapter.getData().indexOf(defaultValue);
    }

    public int getTodayItemPosition() {
        List<V> list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof DateWithLabel) {
                DateWithLabel dwl = (DateWithLabel) list.get(i);
                if (dwl.label.equals(getLocalizedString(R.string.picker_today))) {
                    return i;
                }
            }
        }
        return 0;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;

        updateItemTextAlign();

        computeTextSize();

        notifyDatasetChanged();
    }

    public void notifyDatasetChanged() {
        if (selectedItemPosition > adapter.getItemCount() - 1
                || currentItemPosition > adapter.getItemCount() - 1) {
            selectedItemPosition = currentItemPosition = adapter.getItemCount() - 1;
        } else {
            selectedItemPosition = currentItemPosition;
        }
        scrollOffsetY = 0;
        computeTextSize();
        computeFlingLimitY();
        requestLayout();
        postInvalidate();
    }

    public void setSameWidth(boolean hasSameWidth) {
        this.hasSameWidth = hasSameWidth;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    public boolean hasSameWidth() {
        return hasSameWidth;
    }

    public void setOnWheelChangeListener(OnWheelChangeListener listener) {
        onWheelChangeListener = listener;
    }

    public String getMaximumWidthText() {
        return maxWidthText;
    }

    public void setMaximumWidthText(String text) {
        if (null == text) throw new NullPointerException("Maximum width text can not be null!");
        maxWidthText = text;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public int getMaximumWidthTextPosition() {
        return textMaxWidthPosition;
    }

    public void setMaximumWidthTextPosition(int position) {
        if (!isPosInRang(position)) {
            throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " +
                    adapter.getItemCount() + "), but current is " + position);
        }
        textMaxWidthPosition = position;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    public void setSelectedItemTextColor(int color) {
        mSelectedItemTextColor = color;
        computeCurrentItemRect();
        postInvalidate();
    }

    public int getItemTextColor() {
        return mItemTextColor;
    }

    public void setItemTextColor(int color) {
        mItemTextColor = color;
        postInvalidate();
    }

    public int getItemTextSize() {
        return mItemTextSize;
    }

    public void setItemTextSize(int size) {

        if (mItemTextSize != size) {
            mItemTextSize = size;
            paint.setTextSize(mItemTextSize);
            computeTextSize();
            requestLayout();
            postInvalidate();
        }
    }

    public int getItemSpace() {
        return mItemSpace;
    }

    public void setItemSpace(int space) {
        mItemSpace = space;
        requestLayout();
        postInvalidate();
    }

    public void setCurvedMaxAngle(int maxAngle) {
        this.mMaxAngle = maxAngle;
        requestLayout();
        postInvalidate();
    }

    public void setIndicator(boolean hasIndicator) {
        this.hasIndicator = hasIndicator;
        computeIndicatorRect();
        postInvalidate();
    }

    public boolean hasIndicator() {
        return hasIndicator;
    }

    public int getIndicatorSize() {
        return mIndicatorSize;
    }

    public void setIndicatorSize(int size) {
        mIndicatorSize = size;
        computeIndicatorRect();
        postInvalidate();
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        postInvalidate();
    }

    public void setCurtain(boolean hasCurtain) {
        this.hasCurtain = hasCurtain;
        computeCurrentItemRect();
        postInvalidate();
    }

    public boolean hasCurtain() {
        return hasCurtain;
    }

    public int getCurtainColor() {
        return mCurtainColor;
    }

    public void setCurtainColor(int color) {
        mCurtainColor = color;
        postInvalidate();
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        this.hasAtmospheric = hasAtmospheric;
        postInvalidate();
    }

    public boolean hasAtmospheric() {
        return hasAtmospheric;
    }

    public boolean isCurved() {
        return isCurved;
    }

    public void setCurved(boolean isCurved) {
        this.isCurved = isCurved;
        requestLayout();
        postInvalidate();
    }

    public int getItemAlign() {
        return mItemAlign;
    }

    public void setItemAlign(int align) {
        mItemAlign = align;
        updateItemTextAlign();
        computeDrawnCenter();
        postInvalidate();
    }

    public Typeface getTypeface() {
        if (null != paint) return paint.getTypeface();
        return null;
    }

    public void setTypeface(Typeface tf) {
        if (null != paint) paint.setTypeface(tf);
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    /**
     * TODO: {@link Adapter#data} could contain 'Data' class objects. 'Data' could be composed of
     * a String: displayedValue (the value to be displayed in the wheel) and
     * a Date/Calendar: comparisonDate (a reference date/calendar that will help to find the index).
     * This could clean this method and {@link #getFormattedValue(Object)}.
     * <p>
     * Finds the index in the wheel for a date
     *
     * @param date the targeted date
     * @return the index closed to {@code date}. Returns 0 if not found.
     */
    public int findIndexOfDate(@NonNull Date date) {
        String formatItem = getFormattedValue(date);

        if (this instanceof WheelDayOfMonthPicker) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH) - 1;
        }

        if (this instanceof WheelDayPicker) {
            String today = getFormattedValue(new Date());
            if (today.equals(formatItem)) {
                return getTodayItemPosition();
            }
        }

        if (this instanceof WheelMonthPicker) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH);
        }

        if (this instanceof WheelYearPicker) {
            WheelYearPicker yearPick = (WheelYearPicker) this;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) - yearPick.minYear;
        }

        int formatItemInt = Integer.MIN_VALUE;
        try {
            formatItemInt = Integer.parseInt(formatItem);
        } catch (NumberFormatException e) {
        }

        final int itemCount = adapter.getItemCount();
        int index = 0;
        for (int i = 0; i < itemCount; ++i) {
            final String object = adapter.getItemText(i);

            if (formatItemInt != Integer.MIN_VALUE) {
                // displayed values are Integers
                int objectInt = Integer.parseInt(object);
                if (this instanceof WheelHourPicker && ((WheelHourPicker) this).isAmPm) {
                    // In case of hours and AM/PM mode, apply modulo 12
                    objectInt = objectInt % 12;
                }
                if (objectInt <= formatItemInt) {
                    index = i;
                }
            } else if (formatItem.equals(object)) {
                return i;
            }
        }
        return index;
    }

    public String getLocalizedString(@StringRes int stringRes) {
        return LocaleHelper.getString(getContext(), getCurrentLocale(), stringRes);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (customLocale != null) {
            return customLocale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    public void setDateHelper(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    public DateHelper getDateHelper() {
        return dateHelper;
    }

    public void setShowOnlyFutureDate(boolean showOnlyFutureDate) {
        this.showOnlyFutureDate = showOnlyFutureDate;
    }

    public boolean getShowOnlyFutureDate() {
        return showOnlyFutureDate;
    }

    public interface BaseAdapter<V> {

        int getItemCount();

        V getItem(int position);

        String getItemText(int position);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(WheelPicker picker, Object data, int position);

        void onCurrentItemOfScroll(WheelPicker picker, int position);
    }

    public interface OnWheelChangeListener {
        /**
         * <p>
         * Invoke when WheelPicker scroll stopped
         * WheelPicker will return a distance offset which between current scroll position and
         * initial position, this offset is a positive or a negative, positive means WheelPicker is
         * scrolling from bottom to top, negative means WheelPicker is scrolling from top to bottom
         *
         * @param offset <p>
         *               Distance offset which between current scroll position and initial position
         */
        void onWheelScrolled(int offset);

        /**
         * <p>
         * Invoke when WheelPicker scroll stopped
         * This method will be called when WheelPicker stop and return current selected item data's
         * position in list
         *
         * @param position <p>
         *                 Current selected item data's position in list
         */
        void onWheelSelected(int position);

        /**
         * <p>
         * Invoke when WheelPicker's scroll state changed
         * The state of WheelPicker always between idle, dragging, and scrolling, this method will
         * be called when they switch
         *
         * @param state {@link WheelPicker#SCROLL_STATE_IDLE}
         *              {@link WheelPicker#SCROLL_STATE_DRAGGING}
         *              {@link WheelPicker#SCROLL_STATE_SCROLLING}
         *              <p>
         *              State of WheelPicker, only one of the following
         *              {@link WheelPicker#SCROLL_STATE_IDLE}
         *              Express WheelPicker in state of idle
         *              {@link WheelPicker#SCROLL_STATE_DRAGGING}
         *              Express WheelPicker in state of dragging
         *              {@link WheelPicker#SCROLL_STATE_SCROLLING}
         *              Express WheelPicker in state of scrolling
         */
        void onWheelScrollStateChanged(int state);
    }

    protected interface Listener<PICKER extends WheelPicker, V> {
        void onSelected(PICKER picker, int position, V value);

        void onCurrentScrolled(PICKER picker, int position, V value);
    }

    public static class Adapter<V> implements BaseAdapter {
        private List<V> data;

        public Adapter() {
            this(new ArrayList<V>());
        }

        public Adapter(List<V> data) {
            this.data = new ArrayList<V>();
            this.data.addAll(data);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public V getItem(int position) {
            final int itemCount = getItemCount();
            return itemCount == 0 ? null : data.get((position + itemCount) % itemCount);
        }

        @Override
        public String getItemText(int position) {
            try {
                return String.valueOf(data.get(position));
            } catch (Throwable t) {
                return "";
            }
        }

        public List<V> getData() {
            return data;
        }

        public void setData(List<V> data) {
            this.data.clear();
            this.data.addAll(data);
        }

        public void addData(List<V> data) {
            this.data.addAll(data);
        }

        public int getItemPosition(V value) {
            int position = -1;
            if (data != null) {
                return data.indexOf(value);
            }
            return position;
        }
    }
}
