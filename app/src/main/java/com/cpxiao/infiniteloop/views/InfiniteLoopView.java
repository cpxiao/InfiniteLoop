package com.cpxiao.infiniteloop.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
import com.cpxiao.infiniteloop.R;
import com.cpxiao.infiniteloop.imps.OnGameListener;

import java.util.Random;

/**
 * InfiniteLoopView
 *
 * @author cpxiao on 2016/8/29.
 */
public class InfiniteLoopView extends BaseSurfaceViewFPS implements View.OnClickListener {

    private boolean isGameOver = false;

    private int mScore = 0;

    private Paint mBigCirclePaint;
    private Paint mSmallCirclePaint;
    private Paint mSelfCirclePaint;

    /**
     * 障碍
     */
    private int mBigCircleNum = 5;
    private BigCircle[] mBigCircleArray;

    /**
     * 自己
     */
    private int mIndex = 99;
    private BigCircle mSelfCircle;

    private Random mRandom = new Random();

    private OnGameListener mOnGameListener;

    public InfiniteLoopView(Context context, int score) {
        super(context);
        initScore(score);
        init();
    }

    private void initScore(int score) {
        mScore = score;
        mBigCircleNum = Math.min(mBigCircleNum + mScore / 5, 10);
    }

    public InfiniteLoopView(Context context) {
        super(context);
        init();
    }

    public InfiniteLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfiniteLoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBigCirclePaint = new Paint();
        mBigCirclePaint.setAntiAlias(true);
        mBigCirclePaint.setStyle(Paint.Style.STROKE);
        mBigCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.game_view_big_circle));


        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.game_view_small_circle));
        mSmallCirclePaint.setAntiAlias(true);

        mSelfCirclePaint = new Paint();
        mSelfCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.game_view_self_circle));
        mSelfCirclePaint.setAntiAlias(true);

        setOnClickListener(this);
        setBgColor(ContextCompat.getColor(getContext(), R.color.game_view_bg));
    }

    @Override
    protected void timingLogic() {
        if (isGameOver) {
            return;
        }
        if (mBigCircleNum <= 0 || mBigCircleArray == null) {
            return;
        }
        for (BigCircle bigCircle : mBigCircleArray) {
            timingLogicBigCircle(bigCircle);
        }

        timingLogicBigCircle(mSelfCircle);

        //判断是否碰撞
        if (mSelfCircle != null && mSelfCircle.mSmallCircleArray != null) {
            if (mIndex >= 0 && mIndex < mBigCircleNum) {
                for (BigCircle.SmallCircle smallCircle : mBigCircleArray[mIndex].mSmallCircleArray) {
                    if (isTouched(mSelfCircle.mSmallCircleArray[0], smallCircle)) {
                        // game over
                        isGameOver = true;
                        if (mOnGameListener != null) {
                            mOnGameListener.onGameOver();
                        }
                    }
                }
            }
        }
    }


    private boolean isTouched(BigCircle.SmallCircle self, BigCircle.SmallCircle small) {
        //坑：一定要360求余
        double delta = (self.angle - small.angle) % 360;
        return Math.abs(delta) <= 0.03 * 360;
    }

    private void timingLogicBigCircle(BigCircle bigCircle) {
        if (bigCircle == null) {
            return;
        }
        int deltaAngle = bigCircle.loopSpeed / mFPS;
        if (bigCircle.mSmallCircleArray != null) {
            for (BigCircle.SmallCircle smallCircle : bigCircle.mSmallCircleArray) {
                smallCircle.angle += deltaAngle;

                //换算成弧度（坑：纠结了半天位置计算出错，原因是没有乘以PI）
                double radian = Math.PI * smallCircle.angle / 180;
                smallCircle.cX = (int) (bigCircle.centerX + bigCircle.r * Math.cos(radian));
                smallCircle.cY = (int) (bigCircle.centerY + bigCircle.r * Math.sin(radian));
            }
        }
    }

    @Override
    protected void initWidget() {
        if (mBigCircleNum <= 0) {
            return;
        }
        //设置大圆的画笔粗细
        int width = Math.max(mViewLength / 200, 5);
        mBigCirclePaint.setStrokeWidth(width);

        //初始化障碍和自己
        mBigCircleArray = new BigCircle[mBigCircleNum];
        int centerX = mViewWidth / 2;
        int centerY = mViewHeight / 2;
        for (int i = 0; i < mBigCircleNum; i++) {
            int r = (int) (0.8f * (i + 1) * mViewLength / 2 / mBigCircleNum);
            int loopSpeed = 30 + mRandom.nextInt(90) + mRandom.nextInt(1 + mScore * 10);//取值范围为[30,120)+mScore*?
            loopSpeed = mRandom.nextBoolean() ? loopSpeed : -loopSpeed;
            int startAngle = mRandom.nextInt(360);
            int smallCircleNum = 2 + mRandom.nextInt(3 + mScore / 5);//最少俩
            int smallCircleR = (int) Math.min(0.03 * 3.14 * r, r / mBigCircleNum / 2) - 1;
            if (smallCircleR <= mViewLength / 100) {
                smallCircleR = mViewLength / 100;
            }
            mBigCircleArray[i] = new BigCircle(centerX, centerY, r, loopSpeed, startAngle, smallCircleNum, smallCircleR);


            //初始化自己
            if (i == mBigCircleNum - 1) {
                mSelfCircle = new BigCircle(centerX, centerY, mViewLength / 2, 0, 0, 1, (int) (1.1 * smallCircleR));
                mSelfCircle.mSmallCircleArray[0].angle = 90;
                timingLogicBigCircle(mSelfCircle);
            }
        }


    }

    @Override
    public void drawCache() {
        if (mBigCircleArray == null) {
            return;
        }
        //绘制中心实心圆
        if (mBigCircleArray[0] != null) {
            mCanvasCache.drawCircle(mBigCircleArray[0].centerX, mBigCircleArray[0].centerY, mBigCircleArray[0].r / 3, mSmallCirclePaint);
        }

        //绘制障碍
        for (BigCircle bigCircle : mBigCircleArray) {
            mCanvasCache.drawCircle(bigCircle.centerX, bigCircle.centerY, bigCircle.r, mBigCirclePaint);
            drawSmallCircleArray(bigCircle.mSmallCircleArray, mSmallCirclePaint);
        }

        //绘制自己
        if (mSelfCircle != null) {
            drawSmallCircleArray(mSelfCircle.mSmallCircleArray, mSelfCirclePaint);
        }

    }

    private void drawSmallCircleArray(BigCircle.SmallCircle[] smallCircleArray, Paint paint) {
        if (smallCircleArray == null) {
            return;
        }
        for (BigCircle.SmallCircle smallCircle : smallCircleArray) {
            mCanvasCache.drawCircle(smallCircle.cX, smallCircle.cY, smallCircle.r, paint);
        }
    }

    @Override
    public void onClick(View v) {
        if (mIndex <= 0) {
            // success
            if (mOnGameListener != null) {
                mOnGameListener.onSuccess();
            }
            return;
        }
        if (mIndex == 99) {
            mIndex = mBigCircleNum - 1;
        } else {
            mIndex--;
        }

        if (mSelfCircle == null || mSelfCircle.mSmallCircleArray == null) {
            return;
        }
        if (mBigCircleArray == null || mBigCircleArray.length < mIndex + 1 || mBigCircleArray[mIndex].mSmallCircleArray == null) {
            return;
        }
        mSelfCircle.r = mBigCircleArray[mIndex].r;
        mSelfCircle.loopSpeed = (int) (0.6f * mBigCircleArray[mIndex].loopSpeed);
        mSelfCircle.mSmallCircleArray[0].r = mBigCircleArray[mIndex].mSmallCircleArray[0].r;

    }

    public void setOnGameListener(OnGameListener l) {
        mOnGameListener = l;
    }


    private class BigCircle {
        int centerX;
        int centerY;
        int r;
        int loopSpeed;//旋转速度,正数、负数;单位:角度/每秒
        int startAngle;//初始角度;单位:角度


        int smallCircleNum;//小圆数量
        int smallCircleR;//小圆半径
        SmallCircle[] mSmallCircleArray;

        public BigCircle(int centerX, int centerY, int r, int loopSpeed, int startAngle, int smallCircleNum, int smallCircleR) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.r = r;
            this.loopSpeed = loopSpeed;
            this.startAngle = startAngle;
            this.smallCircleNum = smallCircleNum;
            this.smallCircleR = smallCircleR;
            initSmallCircle(smallCircleNum, smallCircleR);
        }

        private void initSmallCircle(int smallCircleNum, int smallCircleR) {
            if (smallCircleNum < 0) {
                return;
            }
            mSmallCircleArray = new SmallCircle[smallCircleNum];
            for (int i = 0; i < smallCircleNum; i++) {
                int angle = startAngle + 360 / smallCircleNum * i;

                mSmallCircleArray[i] = new SmallCircle(angle, smallCircleR);
            }
        }

        private class SmallCircle {
            int cX;
            int cY;
            int angle;
            int r;

            public SmallCircle(int angle, int r) {
                this.angle = angle;
                this.r = r;
            }
        }
    }


}
