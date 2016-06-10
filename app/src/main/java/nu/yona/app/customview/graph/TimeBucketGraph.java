/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.customview.graph;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by bhargavsuthar on 07/06/16.
 */
public class TimeBucketGraph extends View {

    private Context mContext;
    private int mTotalActivityBeyondGoal;
    private int mTotalMinTarget;
    private int mTotalActivityDurationMin;
    private float mFillStartRange;
    private float mFillEndRange;
    private float mStartPoint;
    private float mEndPoint;
    private Canvas mCanvas;
    private float mDifference;
    //equal parts
    private int mVolume;


    private Paint linePaint;
    private float scaleFactor;


    /**
     * Instantiates a new Time bucket graph.
     *
     * @param context the context
     */
    public TimeBucketGraph(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    /**
     * Instantiates a new Time bucket graph.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public TimeBucketGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initialize();
    }

    /**
     * Instantiates a new Time bucket graph.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public TimeBucketGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initialize();
    }

    /**
     * Instantiates a new Time bucket graph.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeBucketGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initialize();
    }


    /**
     * Initialize.
     */
    public void initialize() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        scaleFactor = metrics.density;


        linePaint = new Paint();
        linePaint.setStrokeWidth(1);
        linePaint.setColor(GraphUtils.COLOR_WHITE_THREE);
    }

    /**
     * Graph arguments.
     *
     * @param totalActivityBeyondGoal      the total activity beyond goal
     * @param totalMinTarget               the total min target
     * @param totalActivityDurationMinutes the total activity duration minutes
     */
    public void graphArguments(int totalActivityBeyondGoal, int totalMinTarget, int totalActivityDurationMinutes) {
        mTotalActivityBeyondGoal = totalActivityBeyondGoal;
        mTotalActivityDurationMin = totalActivityDurationMinutes;
        mTotalMinTarget = totalMinTarget;

        //using mDifference to check wheather its beyond time or not
        mDifference = mTotalMinTarget - mTotalActivityDurationMin;

        //if beyond time then its start value should be difference value else its zero(0)
        if (mDifference < 0) {
            mFillStartRange = mDifference;
        } else {
            mFillStartRange = 0;
        }

        mStartPoint = mFillStartRange;
        //end point should be total minutes of goal
        mEndPoint = mTotalMinTarget;

        if (mFillStartRange < 0) {
            mFillEndRange = 0;
        } else {
            mFillEndRange = mDifference;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        int fullWidth = getWidth();
        float height = scaleFactor * 25;

        //goint to divide into equal part of width
        if (mTotalMinTarget > 0) {
            mVolume = fullWidth / mTotalMinTarget;
        }


        float left = 0, top = 0;

        float right = fullWidth;
        float bottom = top + height;

        //Drawing main Rectangle of Grey
        RectF myRectum = new RectF(left, top, right, bottom);
        mCanvas.drawRect(myRectum, linePaint);


        //Draw bottom text of start and end point
        Paint paint = new Paint();
        paint.setColor(GraphUtils.COLOR_TEXT);
        paint.setTextSize(scaleFactor * GraphUtils.TEXT_SIZE);
        canvas.drawText(String.valueOf((int) mStartPoint), left, bottom + height, paint);

        //if startpoint is less than zero then we need to display negative, zero and positive point
        if (mStartPoint < 0) {
            canvas.drawText(String.valueOf(0), mVolume * mTotalActivityBeyondGoal, bottom + height, paint);
        }
        canvas.drawText(String.valueOf((int) mEndPoint), right - 50, bottom + height, paint);

        //Filling usage of time
        Paint mDrawRange = new Paint();
        float fillStartPoint;
        float fillendPoint;
        if (mDifference < 0) {
            mDrawRange.setColor(GraphUtils.COLOR_PINK);
            fillStartPoint = left;
            fillendPoint = mVolume * mTotalActivityBeyondGoal;
        } else {
            mDrawRange.setColor(GraphUtils.COLOR_GREEN);
            fillStartPoint = left;
            fillendPoint = mDifference * mVolume;
        }

        RectF rectFill = new RectF(fillStartPoint, top, fillendPoint, bottom);
        canvas.drawRect(rectFill, mDrawRange);

    }
}
