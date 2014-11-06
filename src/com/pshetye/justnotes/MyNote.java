package com.pshetye.justnotes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MyNote implements Parcelable{
	long _id;
	String pNote;
	String pDate;
	static public SimpleDateFormat NoteDateFormat;
	
	public MyNote() {
		if (NoteDateFormat == null)
			NoteDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		pDate = NoteDateFormat.format(new Date());
	}
	
	public MyNote(long _id, String pNote) {
		if (NoteDateFormat == null)
			NoteDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		this._id = _id;
		this.pNote = pNote;
		this.pDate = NoteDateFormat.format(new Date());
	}
	
	public MyNote(long _id, String pNote, String pDate) {
		if (NoteDateFormat == null)
			NoteDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		this._id = _id;
		this.pNote = pNote;
		this.pDate = pDate;
	}
	
	public MyNote(String pNote) {
		if (NoteDateFormat == null)
			NoteDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		this.pNote = pNote;
		this.pDate = NoteDateFormat.format(new Date());
	}
	
	public long getID() {
		return this._id;
	}
	
	public String getNote() {
		return this.pNote;
	}
	
	public String getDate() {
		return this.pDate;
	}
	
	public void setID(long _id) {
		this._id = _id;
	}
	
	public void setPNote(String pNote) {
		this.pNote = pNote;
	}
	
	public void setDate(String pDate) {
		this.pDate = pDate;
	}
	
	// Parcelling part
    public MyNote(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this._id = Long.getLong(data[0]);
        this.pNote = data[1];
        this.pDate = data[2];
    }
    
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeStringArray(new String[] {Objects.toString(this._id, null),
                this.pNote,
                this.pDate});
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MyNote createFromParcel(Parcel in) {
            return new MyNote(in); 
        }

        public MyNote[] newArray(int size) {
            return new MyNote[size];
        }
    };
}