package com.redant.freecanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by hwjoy on 04/02/2018.
 */

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "CanvasView";

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Thread mThread;
    private boolean mIsRunning;

    private float mLastX;
    private float mLastY;
    private Path mPath;

    public CanvasView(Context context) {
        super(context);

        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
        mThread.interrupt();
        mSurfaceHolder.removeCallback(this);
    }

    @Override
    public void run() {
        while (mIsRunning) {
            drawing();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mPath.moveTo(mLastX, mLastY);
                break;


            case MotionEvent.ACTION_MOVE:
//                mPath.quadTo(mLastX, mLastY, event.getX(), event.getY());
                mPath.lineTo(event.getX(), event.getY());

                mLastX = event.getX();
                mLastY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
//        setFocusable(true);
//        setFocusableInTouchMode(true);

        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    private void drawing() {
        try {
            if (mSurfaceHolder != null) {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawPath(mPath, mPaint);
            } else {
                Log.w(TAG, "SurfaceHolder is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mSurfaceHolder != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void clear() {
        mPath.reset();
    }
}
