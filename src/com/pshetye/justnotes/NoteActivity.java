package com.pshetye.justnotes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

public class NoteActivity extends BaseActivity {

    private static final String LOG_TAG = "NoteActivity";
    private FloatingActionButton fab_add_btn = null;
    private static List<MyNote> sMyNotes = new ArrayList<MyNote>();
    MyNoteAdapter mNoteAdapter = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "Inside onCreate");
		
		Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                MyNote note = new MyNote((int) System.currentTimeMillis(),intent.getStringExtra(Intent.EXTRA_SUBJECT),
                        intent.getStringExtra(Intent.EXTRA_TEXT), "");
                db.addNote(note);
            }
        }
        Animation slideBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        
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
		
		sMyNotes = BaseActivity.db.getAllNotes();		
		mNoteAdapter = new MyNoteAdapter(this, sMyNotes);
        ListView gv = (ListView) findViewById(R.id.ListView);
        gv.setAdapter(mNoteAdapter);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mNoteAdapter.clear();
	}

	@Override
	protected int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.activity_note;
	}
}
