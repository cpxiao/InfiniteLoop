package com.cpxiao.infiniteloop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.infiniteloop.R;
import com.cpxiao.infiniteloop.imps.OnGameListener;
import com.cpxiao.infiniteloop.mode.Extra;
import com.cpxiao.infiniteloop.utils.DialogUtils;
import com.cpxiao.infiniteloop.views.InfiniteLoopView;
import com.cpxiao.lib.activity.BaseActivity;

/**
 * GameActivity
 *
 * @author cpxiao on 2016/8/29.
 */
public class GameActivity extends BaseActivity {

    private int mScore = 0;
    private int mLifeNum = 3;

    /**
     * 分数
     */
    protected int mBestScore = 0;
    /**
     * 当前分数view
     */
    protected TextView mScoreView;
    /**
     * 最高分view
     */
    protected TextView mBestScoreView;
    /**
     * 生命条
     */
    protected LinearLayout mLifeBar;
    protected ImageView mLifeBarLife0;
    protected ImageView mLifeBarLife1;
    protected ImageView mLifeBarLife2;
    /**
     * Game View Layout
     */
    protected LinearLayout mGameViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initWidget();
        initFbAds50("1579509002351231_1579509275684537");
    }

    protected void initWidget() {
        mScoreView = (TextView) findViewById(R.id.score);
        mBestScoreView = (TextView) findViewById(R.id.best_score);
        mLifeBar = (LinearLayout) findViewById(R.id.layout_life_bar);
        mLifeBarLife0 = (ImageView) findViewById(R.id.life_bar_0);
        mLifeBarLife1 = (ImageView) findViewById(R.id.life_bar_1);
        mLifeBarLife2 = (ImageView) findViewById(R.id.life_bar_2);
        mGameViewLayout = (LinearLayout) findViewById(R.id.game_view_layout);

        setScores();

        resetView(mScore);
    }

    private void resetView(int score) {
        mGameViewLayout.removeAllViews();
        InfiniteLoopView view = new InfiniteLoopView(getApplicationContext(), score);
        view.setOnGameListener(mOnGameListener);
        mGameViewLayout.addView(view);
    }

    private OnGameListener mOnGameListener = new OnGameListener() {

        @Override
        public void onGameOver() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLifeNum--;
                    setLifeBar(mLifeNum);
                    //如果还有生命值，就重置，否则游戏结束
                    if (mLifeNum > 0) {
                        resetView(mScore);
                        return;
                    }

                    DialogUtils.createGameOverDialog(GameActivity.this,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GameActivity.comeToMe(GameActivity.this);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                }
            });

        }

        @Override
        public void onSuccess() {
            mScore++;
            setScores();
            setLifeBar(3);
            resetView(mScore);
        }
    };

    private void setScores() {
        setScore(mScore);
        mBestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE, 0);
        if (mScore > mBestScore) {
            mBestScore = mScore;
            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE, mBestScore);
        }
        setBestScore(mBestScore);
    }


    protected void setScore(int score) {
        if (mScoreView != null) {
            mScoreView.setText(String.valueOf(score));
        }
    }

    protected void setBestScore(int bestScore) {
        if (mBestScoreView != null) {
            String bestScoreText = getResources().getText(R.string.best_score) + ": " + String.valueOf(bestScore);
            mBestScoreView.setText(bestScoreText);
        }
    }

    protected void setLifeBar(int life) {
        if (life <= 0) {
            mLifeBarLife0.setImageResource(R.drawable.life_dead);
            mLifeBarLife1.setImageResource(R.drawable.life_dead);
            mLifeBarLife2.setImageResource(R.drawable.life_dead);
        } else if (life == 1) {
            mLifeBarLife0.setImageResource(R.drawable.life_alive);
            mLifeBarLife1.setImageResource(R.drawable.life_dead);
            mLifeBarLife2.setImageResource(R.drawable.life_dead);
        } else if (life == 2) {
            mLifeBarLife0.setImageResource(R.drawable.life_alive);
            mLifeBarLife1.setImageResource(R.drawable.life_alive);
            mLifeBarLife2.setImageResource(R.drawable.life_dead);
        } else {
            mLifeBarLife0.setImageResource(R.drawable.life_alive);
            mLifeBarLife1.setImageResource(R.drawable.life_alive);
            mLifeBarLife2.setImageResource(R.drawable.life_alive);
        }

    }

    public static void comeToMe(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


}
