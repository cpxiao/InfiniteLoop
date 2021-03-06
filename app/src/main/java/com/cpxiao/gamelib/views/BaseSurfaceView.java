package com.cpxiao.gamelib.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cpxiao.gamelib.Config;


/**
 * BaseSurfaceView
 *
 * @author cpxiao on 2016/5/31
 */
public abstract class BaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    protected static final boolean DEBUG = Config.DEBUG;
    protected final String TAG = getClass().getSimpleName();

    /**
     * 背景色
     */
    private static final int BACKGROUND_COLOR = Color.WHITE;
    private int mBackgroundColor = BACKGROUND_COLOR;

    /**
     * SurfaceHolder用于控制SurfaceView的大小、格式等，用于监听SurfaceView的状态。
     */
    protected SurfaceHolder mSurfaceHolder;

    /**
     * 声明画笔
     */
    protected Paint mPaint;

    /**
     * 二级缓存：bitmap
     */
    protected Bitmap mBitmapCache;

    /**
     * 二级缓存：canvas
     */
    protected Canvas mCanvasCache;

    /**
     * 声明SurfaceView的宽高,获取视图的宽高一定要在视图创建之后才可获取，即surfaceCreated之后获取，否则一直为0
     */
    protected int mViewWidth, mViewHeight, mViewLength;

    public BaseSurfaceView(Context context) {
        super(context);
        initSurfaceView();
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSurfaceView();
    }

    public BaseSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSurfaceView();
    }

    private void initSurfaceView() {
        if (DEBUG) {
            Log.d(TAG, "initSurfaceView: ");
        }
        /** 实例SurfaceHolder*/
        mSurfaceHolder = getHolder();

        /**为SurfaceView添加状态监听*/
        mSurfaceHolder.addCallback(this);

        /**实例一个画笔*/
        mPaint = new Paint();

        /**设置焦点*/
        setFocusable(true);
    }

    /**
     * 重写SurfaceHolder.Callback接口的三个方法surfaceCreated()、surfaceChanged()、surfaceDestroyed()
     */

    /**
     * 当SurfaceView被创建完成后响应的方法
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "surfaceCreated().........");
        }
    }


    /**
     * 当SurfaceView状态发生改变时响应的方法
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (DEBUG) {
            Log.d(TAG, "surfaceChanged().........");
        }
        mViewWidth = width;
        mViewHeight = height;
        mViewLength = Math.min(width, height);

        /**
         * 初级化二级缓存
         */
        mBitmapCache = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
        mCanvasCache = new Canvas(mBitmapCache);

        initWidget();
        myDraw();
    }

    /**
     * 初始化控件
     */
    protected abstract void initWidget();

    /**
     * 当SurfaceView状态Destroyed时响应的方法
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "surfaceChanged().........");
        }
    }


    /**
     * 自定义绘图方法
     */
    public abstract void drawCache();

    protected void myDraw() {
        Canvas canvas = null;
        try {
            if (mSurfaceHolder != null && mBitmapCache != null && mPaint != null) {
                /**使用SurfaceHolder.lockCanvas()获取SurfaceView的Canvas对象，并对画布加锁.*/
                canvas = mSurfaceHolder.lockCanvas();
                /** 在绘制之前需要将画布清空，否则画布上会显示之前绘制的内容,以下三种方法效果一致*/
                //                mCanvasCache.drawRect(0, 0, getWidth(), getHeight(), new Paint());
                //                mCanvasCache.drawRGB(255, 255, 255);
                mCanvasCache.drawColor(mBackgroundColor);
                drawCache();

                if (canvas != null) {
                    canvas.drawColor(mBackgroundColor);
                    canvas.drawBitmap(mBitmapCache, 0, 0, mPaint);
                }
                //                if (mSurfaceHolder != null) {
                //                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                //                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        } finally {
            if (mSurfaceHolder != null && canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    protected void setBgColor(int color) {
        mBackgroundColor = color;
    }

}