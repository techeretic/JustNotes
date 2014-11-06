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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "Inside onCreate");
		
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
        
		// Add Button - Holder Fragment
        fab_save_btn = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_save))
                .withButtonColor(getResources().getColor(R.color.accent_blue))
                .withGravity(Gravity.TOP | Gravity.END)
                .withMargins(15, 15, 0, 0).create();
        
        fab_save_btn.setAnimation(fadeIn);
        fab_save_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	EditText textView = (EditText) findViewById(R.id.editText1);
                if (!textView.getText().toString().isEmpty()) {
                    MyNote note = new MyNote((int) System.currentTimeMillis(), textView.getText()
                            .toString());
                    BaseActivity.db.addNote(note);
                    finish();
                }
            }
        });
	}

	@Override
	protected int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.activity_input;
	}
	
	public static void launchInput(BaseActivity activity, View transitionView, String url) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, LOG_TAG);
        Intent intent = new Intent(activity, InputActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
