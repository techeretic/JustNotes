package com.pshetye.justnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ViewNoteActivity extends BaseActivity {

    private static final String LOG_TAG = "ViewNoteActivity";

    private FloatingActionButton fab_share_btn = null;

    private FloatingActionButton fab_edit_btn = null;

    private FloatingActionButton fab_delete_btn = null;

    private String noteContent = null;

    private String noteTitle = null;
    
    private MyNote mNote = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        
        mNote = (MyNote) getIntent().getParcelableExtra("Note");
        noteContent = mNote.getNote();
        noteTitle = mNote.getTitle();
        
        Log.d(LOG_TAG, "noteTitle == " + noteTitle);

        TextView textView1 = (TextView) findViewById(R.id.noteview);
        TextView textView0 = (TextView) findViewById(R.id.notetitleview);
        if (noteTitle.isEmpty()) {
            textView0.setVisibility(View.GONE);
            textView1.setPaddingRelative(0, 30, 0, 0);
        } else {
            textView0.setText(noteTitle);
            textView0.setMovementMethod(new ScrollingMovementMethod());
            textView0.setPaddingRelative(0, 30, 0, 0);
        }
        
        textView1.setText(noteContent);
        textView1.setMovementMethod(new ScrollingMovementMethod());
        
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
        slideIn.setDuration(750);
        
        // Share Button - Note Fragment
        fab_share_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_share))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END)
                .withMargins(15, 15, 0, 0).create();
        fab_share_btn.setAnimation(slideIn);
        fab_share_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                shareNote();
            }
        });

        // Delete Button - Note Fragment
        fab_delete_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_discard))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END)
                .withMargins(0, 15, 65, 0).create();

        fab_delete_btn.setAnimation(slideIn);
        fab_delete_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                deleteNote();
            }
        });

        // Delete Button - Note Fragment
        fab_edit_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_edit))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END)
                .withMargins(0, 15, 130, 0).create();

        fab_edit_btn.setAnimation(slideIn);
        fab_edit_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputActivity.launchInput(ViewNoteActivity.this, v, mNote);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.activity_view_note;
    }
    
    public static void launchInput(BaseActivity activity, View transitionView, MyNote note) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, LOG_TAG);
        Intent intent = new Intent(activity, ViewNoteActivity.class);
        intent.putExtra("Note", note);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private void shareNote() {
        Log.d(LOG_TAG, "Inside ShareNote");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "From - Type Anything");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mNote.getNote());
        startActivity(Intent.createChooser(sharingIntent, "Choose option"));
    }

    private void deleteNote() {
        Log.d(LOG_TAG, "Inside DeleteNote");
        BaseActivity.db.deleteNote(mNote);
        finish();
    }
}