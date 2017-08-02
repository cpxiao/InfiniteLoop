package com.cpxiao.infiniteloop.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.gamelib.activity.BaseActivity;
import com.cpxiao.infiniteloop.mode.Extra;



/**
 * HomeActivity
 *
 * @author cpxiao on 2016/8/29.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    protected Button mNewGameButton;
    protected Button mBestScoreButton;
    protected Button mQuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidget();
        initFbAds50("1579509002351231_1579509065684558");
        initAdMobAds50("ca-app-pub-4157365005379790/1737248065");
    }

    protected void initWidget() {
        mNewGameButton = (Button) findViewById(R.id.btn_new_game);
        mNewGameButton.setOnClickListener(this);

        mBestScoreButton = (Button) findViewById(R.id.btn_best_score);
        mBestScoreButton.setOnClickListener(this);

        mQuitButton = (Button) findViewById(R.id.btn_quit);
        mQuitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_new_game) {
            GameActivity.comeToMe(this);
        } else if (id == R.id.btn_best_score) {
            int bestScore = PreferencesUtils.getInt(HomeActivity.this, Extra.Key.BEST_SCORE, 0);

            AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle(R.string.best_score)
                    .setMessage(String.valueOf(bestScore))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else if (id == R.id.btn_quit) {
            showQuitConfirmDialog();
        }
    }
    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        showQuitConfirmDialog();
    }

    private void showQuitConfirmDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                //                .setTitle(R.string.quit_msg)
                .setMessage(R.string.quit_msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        //            dialog.setCancelable(true);
        //            dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
