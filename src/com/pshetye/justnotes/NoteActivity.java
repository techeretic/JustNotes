package com.pshetye.justnotes;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static boolean doUpdate = true;
    MyNoteAdapter mNoteAdapter = null;
    int index = 0;
    private RecyclerView mRecyclerView;
    @SuppressWarnings("rawtypes")
	private RecyclerView.Adapter mAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "Inside onCreate");
		
		Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                if (title == null)
                    title = "";
                MyNote note = new MyNote((int) System.currentTimeMillis(), title,
                        intent.getStringExtra(Intent.EXTRA_TEXT), "");
                db.addNote(note);
            }
        }
        
        mRecyclerView = (RecyclerView) findViewById(R.id.ListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        Animation slideBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        slideBottom.setDuration(750);
        
        // Add Button - Holder Fragment
        fab_add_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_new))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.BOTTOM | Gravity.END)
                .withMargins(0, 0, 15, 15).create();
        
        fab_add_btn.setAnimation(slideBottom);
        fab_add_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	InputActivity.launchInput(NoteActivity.this, v, LOG_TAG);
            }
        });
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (doUpdate) {
			sMyNotes = BaseActivity.db.getAllNotes();
			mAdapter = new RecycleAdapter(sMyNotes, this);
	        mRecyclerView.setAdapter(mAdapter);
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
	        	doUpdate = true;
	        } else {
	        	doUpdate = false;
	        }
	    }
	}
}
