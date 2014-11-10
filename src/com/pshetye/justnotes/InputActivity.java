
package com.pshetye.justnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class InputActivity extends BaseActivity {

    private static final String LOG_TAG = "InputActivity";

    private FloatingActionButton fab_save_btn = null;
    
    private MyNote mNote = null;

    private String noteContent = null;

    private String noteTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
        
        mNote = (MyNote) getIntent().getParcelableExtra("Note");
        if (mNote != null) {
            noteContent = mNote.getNote();
            noteTitle = mNote.getTitle();
        }

        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
        slideIn.setDuration(750);

        // Add Button - Holder Fragment
        fab_save_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_save))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END).withMargins(15, 15, 0, 0).create();

        fab_save_btn.setAnimation(slideIn);
        fab_save_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EditText textView0 = (EditText) findViewById(R.id.editText0);
                EditText textView1 = (EditText) findViewById(R.id.editText1);
                if (mNote == null) {
                    if (!textView0.getText().toString().isEmpty()
                            || !textView1.getText().toString().isEmpty()) {
                        MyNote note = new MyNote((int) System.currentTimeMillis(), textView0.getText()
                                .toString(), textView1.getText().toString(), "");
                        BaseActivity.db.addNote(note);
                        finish();
                        setResult(RESULT_OK);
                    }
                } else {
                    Log.d(LOG_TAG,"mNote != null");
                    if (textView0.getText().toString().isEmpty()
                    		&& textView1.getText().toString().isEmpty()) {
                        Log.d(LOG_TAG,"EVERYTHING BLANK!!!");
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
	                    mNote.setPNote(textView1.getText().toString());                    
	                    mNote.setPTitle(textView0.getText().toString());
	                    BaseActivity.db.updateNote(mNote);
	                    Intent i = new Intent();
	                    i.putExtra("Note", mNote);
	                    setResult(RESULT_OK, i);
	                    finish();
                    }
                }
            }
        });
        
        if (mNote != null) {
            EditText textView0 = (EditText) findViewById(R.id.editText0);
            EditText textView1 = (EditText) findViewById(R.id.editText1);
            
            textView0.setText(noteTitle);
            textView1.setText(noteContent);
        }
    }

    @Override
    protected int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.activity_input;
    }

    public static void launchInput(BaseActivity activity, View transitionView, String url) {
        /*ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, LOG_TAG);*/
		ActivityOptionsCompat options = ActivityOptionsCompat
				.makeScaleUpAnimation(transitionView, (int)transitionView.getTranslationX(),
						(int)transitionView.getTranslationY(), transitionView.getWidth(),
						transitionView.getHeight());
        Intent intent = new Intent(activity, InputActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void launchInput(BaseActivity activity, View transitionView, MyNote note) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, transitionView, LOG_TAG);
        Intent intent = new Intent(activity, InputActivity.class);
        intent.putExtra("Note", note);
        ActivityCompat.startActivityForResult(activity, intent, BaseActivity.INPUT_CODE, options.toBundle());
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	setResult(RESULT_CANCELED);
    	super.onBackPressed();
    }
}
