
package com.pshetye.justnotes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class NoteActivity extends BaseActivity {

    private static final String LOG_TAG = "NoteActivity";

    private FloatingActionButton fab_add_btn = null;

    private static List<MyNote> sMyNotes = new ArrayList<MyNote>();

    public static boolean doUpdate = true;

    MyNoteAdapter mNoteAdapter = null;

    int index = 0;

    private RecyclerView mRecyclerView;

    @SuppressWarnings("rawtypes")
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");

        mRecyclerView = (RecyclerView) findViewById(R.id.ListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Animation slideInBottom = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.abc_slide_in_bottom);
        slideInBottom.setDuration(750);

        // Add Button - Holder Fragment
        fab_add_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_new))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.BOTTOM | Gravity.END).withMargins(0, 0, 15, 15).create();

        fab_add_btn.setAnimation(slideInBottom);
        fab_add_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputActivity.launchInput(NoteActivity.this, v, LOG_TAG);
            }
        });
        
        mRecyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // TODO Auto-generated method stub
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // special handler to avoid displaying half elements
                    //recyclerView.scrollTo(rec, y);
                    animateFAB(getApplicationContext(), "IN");
                } else {
                    animateFAB(getApplicationContext(), "OUT");
                }
                recyclerView.animate();
            }
            
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // TODO Auto-generated method stub
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.animate();
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (doUpdate) {
            Log.d(LOG_TAG, "doUpdate is TRUE");
            sMyNotes = DatabaseHelper.getInstance(NoteActivity.this).getAllNotes();
            mAdapter = new RecycleAdapter(sMyNotes, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(LOG_TAG, "doUpdate is FALSE");
        }
        doUpdate = true;
        animateFAB(getApplicationContext(), "IN");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        animateFAB(getApplicationContext(), "OUT");
    }

    @Override
    protected int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.activity_note;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == BaseActivity.VIEW_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "Gonna Update the Notes List");
                doUpdate = true;
            } else {
                Log.d(LOG_TAG, "NOT Gonna Update the Notes List");
                doUpdate = false;
            }
        }
    }

    private void animateFAB(Context context, String direction) {
        if (direction.equals("IN")) {
            Animation slideInBottom = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.abc_slide_in_bottom);
            slideInBottom.setDuration(750);
            fab_add_btn.setAnimation(slideInBottom);
            fab_add_btn.animate();
            fab_add_btn.setVisibility(View.VISIBLE);
        } else {
            Animation slideOutBottom = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.abc_slide_out_bottom);
            slideOutBottom.setDuration(750);
            fab_add_btn.setAnimation(slideOutBottom);
            fab_add_btn.animate();
            fab_add_btn.setVisibility(View.INVISIBLE);
        }
    }
}
