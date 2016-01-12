package com.example.atedittext;

import android.graphics.Typeface;
import android.os.Parcel;
import android.text.style.StyleSpan;

/**
 * Created by luoxf on 16-1-7.
 */
public class MystyleSpan extends StyleSpan {
    private String mId;
    public MystyleSpan(int style, String id) {
        super(Typeface.NORMAL);
        mId = id;
    }
    public MystyleSpan(int style) {
        super(style);
    }

    public MystyleSpan(Parcel src) {
        super(src);
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
