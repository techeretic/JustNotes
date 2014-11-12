
package com.pshetye.justnotes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String LOG_TAG = "BaseActivity";

    public static final int VIEW_CODE = 1;

    public static final int INPUT_CODE = 2;

    protected abstract int getLayoutResource();
    
    private Toolbar toolbar= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        setContentView(getLayoutResource());

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        /*Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.abc_slide_out_top);
        slideOut.setDuration(750);
        toolbar.setAnimation(slideOut);
        toolbar.animate();*/
        toolbar.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
