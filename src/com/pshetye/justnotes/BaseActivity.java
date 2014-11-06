
package com.pshetye.justnotes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String LOG_TAG = "BaseActivity";

    protected abstract int getLayoutResource();

    public static DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        setContentView(getLayoutResource());

        db = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
