
package com.pshetye.justnotes;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

public class NoteActivity extends BaseActivity {

    private static final String LOG_TAG = "NoteActivity";

    protected Object mActionMode;

    private FloatingActionButton fab_add_btn = null;

    private static List<MyNote> sMyNotes = new ArrayList<MyNote>();

    public static boolean doUpdate = false;

    MyNoteAdapter mNoteAdapter = null;
/*
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			// assumes that you have "contexual.xml" menu resources
			inflater.inflate(R.menu.note, menu);
			return true;
		}

		// called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// called when the user selects a contextual menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				Toast.makeText(NoteActivity.this, "Selected DELETE",
						Toast.LENGTH_SHORT).show();
				mode.finish(); // Action picked, so close the CAB
				return true;
			case R.id.action_edit:
				Toast.makeText(NoteActivity.this, "Selected EDIT",
						Toast.LENGTH_SHORT).show();
				mode.finish(); // Action picked, so close the CAB
				return true;
			case R.id.action_share:
				Toast.makeText(NoteActivity.this, "Selected SHARE",
						Toast.LENGTH_SHORT).show();
				mode.finish(); // Action picked, so close the CAB
				return true;
			default:
				return false;
			}
		}

		// called when the user exits the action mode
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};
*/
    int index = 0;

    private RecyclerView mRecyclerView;

    @SuppressWarnings("rawtypes")
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");

        int orientation = getScreenOrientation();
        mRecyclerView = (RecyclerView) findViewById(R.id.ListView);
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
        		orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
        	mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        } else {
        	mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        }
        /*
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
        		orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
        	mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
        	mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        */
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sMyNotes = DatabaseHelper.getInstance(NoteActivity.this).getAllNotes();
        mAdapter = new RecycleAdapter(sMyNotes, this);
        mRecyclerView.setAdapter(mAdapter);

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

		mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(NoteActivity.this,
				new RecyclerItemClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
                Log.d(LOG_TAG, "Inside onClick");
                MyNote note = sMyNotes.get(position);
                ViewNoteActivity.launchViewNote(NoteActivity.this, view.findViewById(R.id.temp_view), note);
			}
		}));
		
		setActionBarIcon(R.drawable.ic_assignment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notesearch, menu);
        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.search);
        if (searchItem != null) {
        	Log.d(LOG_TAG, "searchItem is NOT null");
        } else {
        	Log.d(LOG_TAG, "searchItem is null");
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
        	searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        	LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        	LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        	LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        	AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        	autoComplete.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
				        animateFAB(getApplicationContext(), "OUT");
					} else {
				        animateFAB(getApplicationContext(), "IN");
					}
				}
			});
        } else {
        	Log.d(LOG_TAG, "searchView is null");
        }
		
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	return true;
        }

        return super.onOptionsItemSelected(item);
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
            int orientation = getScreenOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
            		orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
            	mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            } else {
            	mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
            }
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
    private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
            (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;              
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;              
            }
        }

        return orientation;
    }
    
}
