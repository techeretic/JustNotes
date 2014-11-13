
package com.pshetye.justnotes;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
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

    private TextView noteTitleView;

    private TextView noteTextView;

    private boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        setWindowExitTransition();

        mNote = (MyNote) getIntent().getParcelableExtra("Note");
        noteContent = mNote.getNote();
        noteTitle = mNote.getTitle();

        Log.d(LOG_TAG, "noteTitle == " + noteTitle);

        noteTextView = (TextView) findViewById(R.id.noteview);
        noteTitleView = (TextView) findViewById(R.id.notetitleview);
        if (noteTitle.isEmpty()) {
            noteTitleView.setVisibility(View.GONE);
        } else {
            noteTitleView.setText(noteTitle);
            noteTitleView.setMovementMethod(new ScrollingMovementMethod());
        }

        noteTextView.setText(noteContent);
        noteTextView.setMovementMethod(new ScrollingMovementMethod());
        // Share Button - Note Fragment
        fab_share_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_share))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END).withMargins(15, 15, 0, 0).create();
        fab_share_btn.setVisibility(View.GONE);
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
                .withGravity(Gravity.TOP | Gravity.END).withMargins(0, 15, 65, 0).create();
        fab_delete_btn.setVisibility(View.GONE);
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
                .withGravity(Gravity.TOP | Gravity.END).withMargins(0, 15, 130, 0).create();
        fab_edit_btn.setVisibility(View.GONE);
        fab_edit_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputActivity.launchInput(ViewNoteActivity.this, v, mNote);
            }
        });

    }

    @Override
    protected int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.activity_view_note;
    }

    public static void launchViewNote(BaseActivity activity, View transitionView, MyNote note) {
		List<Pair<View, String>> pairs = new ArrayList<>();
		pairs.add(Pair.create(activity.findViewById(android.R.id.navigationBarBackground),
		        Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
		pairs.add(Pair.create(activity.findViewById(android.R.id.statusBarBackground),
		        Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
		Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, 
		        pairs.toArray(new Pair[pairs.size()])).toBundle();
        ActivityOptionsCompat options2 = ActivityOptionsCompat.makeSceneTransitionAnimation( activity,
                         transitionView, "noteview");
        /*ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(transitionView,
                (int) transitionView.getTranslationX(), (int) transitionView.getTranslationY(),
                transitionView.getWidth(), transitionView.getHeight());*/
        Intent intent = new Intent(activity, ViewNoteActivity.class);
        intent.putExtra("Note", note);
        ActivityCompat.startActivityForResult(activity, intent, BaseActivity.VIEW_CODE,
                options);
    }

    private void shareNote() {
        Log.d(LOG_TAG, "Inside ShareNote");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mNote.getTitle());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mNote.getNote() + "\n\nShared from JustNotes");
        startActivity(Intent.createChooser(sharingIntent, "Choose option"));
    }

    private void deleteNote() {
        Log.d(LOG_TAG, "Inside DeleteNote");
        DatabaseHelper.getInstance(ViewNoteActivity.this).deleteNote(mNote);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (updated) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.abc_slide_out_top);
        slideOut.setDuration(750);
        fab_delete_btn.setAnimation(slideOut);
        fab_edit_btn.setAnimation(slideOut);
        fab_share_btn.setAnimation(slideOut);

        fab_delete_btn.animate();
        fab_edit_btn.animate();
        fab_share_btn.animate();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == BaseActivity.INPUT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mNote = data.getParcelableExtra("Note");
                noteTitle = mNote.getTitle();
                if (noteTitle.isEmpty()) {
                    noteTitleView.setVisibility(View.GONE);
                } else {
                    noteTitleView.setVisibility(View.VISIBLE);
                    noteTitleView.setText(noteTitle);
                    noteTitleView.setMovementMethod(new ScrollingMovementMethod());
                }
                noteTextView.setText(mNote.getNote());
                updated = true;
            } else {
                setResult(RESULT_CANCELED);
            }
        }
    }
    
    @TargetApi(Build.VERSION_CODES.L)
    private void setWindowExitTransition() {
        /*Transition exp = new Fade();
        exp.setDuration(750);
        getWindow().setExitTransition(exp);*/
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setWindowExitTransition();
        animateFAB(getApplicationContext(),"IN");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        animateFAB(getApplicationContext(),"OUT");
    }

    private void animateFAB(Context context, String direction) {
        if (direction.equals("IN")) {
            Animation slideInTop = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.abc_slide_in_top);
            slideInTop.setDuration(750);
            fab_delete_btn.setAnimation(slideInTop);
            fab_delete_btn.animate();
            fab_delete_btn.setVisibility(View.VISIBLE);

            fab_edit_btn.setAnimation(slideInTop);
            fab_edit_btn.animate();
            fab_edit_btn.setVisibility(View.VISIBLE);

            fab_share_btn.setAnimation(slideInTop);
            fab_share_btn.animate();
            fab_share_btn.setVisibility(View.VISIBLE);
        } else {
            Animation slideOutTop = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.abc_slide_out_top);
            slideOutTop.setDuration(750);
            fab_delete_btn.setAnimation(slideOutTop);
            fab_delete_btn.animate();
            fab_delete_btn.setVisibility(View.GONE);

            fab_edit_btn.setAnimation(slideOutTop);
            fab_edit_btn.animate();
            fab_edit_btn.setVisibility(View.GONE);

            fab_share_btn.setAnimation(slideOutTop);
            fab_share_btn.animate();
            fab_share_btn.setVisibility(View.GONE);
        }
    }
}
