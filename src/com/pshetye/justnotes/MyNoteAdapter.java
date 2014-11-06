
package com.pshetye.justnotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyNoteAdapter extends ArrayAdapter<MyNote> {

    private List<MyNote> mNotes = new ArrayList<MyNote>();

    private LayoutInflater mInflater;

    Context mContext = null;

    private static final String LOG_TAG = "MyNoteAdapter";

    public MyNoteAdapter(Context context, List<MyNote> objects) {
        super(context, 0);
        // TODO Auto-generated constructor stub
        this.mNotes = objects;
        this.mContext = context;
        Collections.reverse(mNotes);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNotes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (convertView == null) {
        	Log.d(LOG_TAG, "Inflating NOW");
            view = mInflater.inflate(R.layout.note_list_item, null);
        }
        
        Log.d(LOG_TAG, position + "Title = " + mNotes.get(position).getTitle());
        Log.d(LOG_TAG, position + "Note  = " + mNotes.get(position).getNote());
        Log.d(LOG_TAG, position + "Date  = " + mNotes.get(position).getDate());

        TextView tv0 = (TextView) view.findViewById(R.id.textView0);
        TextView tv1 = (TextView) view.findViewById(R.id.textView1);
        TextView tv2 = (TextView) view.findViewById(R.id.textView2);
        view.setTag(position);
        if (mNotes.get(position).getTitle().isEmpty()) {
            tv0.setVisibility(View.GONE);
            tv1.setTextSize(20);
        } else {
            tv0.setText(mNotes.get(position).getTitle());
        }
        tv1.setText(mNotes.get(position).getNote());
        tv2.setText(mNotes.get(position).getDate());

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(LOG_TAG, "Inside onClick");
                MyNote note = mNotes.get((int) v.getTag());
                ViewNoteActivity.launchInput((BaseActivity) mContext, v, note);
            }
        });

        return view;
    }
}
