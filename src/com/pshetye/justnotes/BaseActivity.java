
package com.pshetye.justnotes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String LOG_TAG = "BaseActivity";

    public static final int VIEW_CODE = 1;

    public static final int INPUT_CODE = 2;
    
    private boolean useTBasAB = true;

    protected abstract int getLayoutResource();
    
    private Toolbar toolbar= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        setContentView(getLayoutResource());

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null && useTBasAB) {
        	toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    public void setToolbarFlag(boolean useTBasAB) {
    	this.useTBasAB = useTBasAB;
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }
    
    public Toolbar getToolbar() {
    	return toolbar;
    }
}
