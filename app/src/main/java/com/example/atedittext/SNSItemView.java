package com.example.atedittext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class SNSItemView extends LinearLayout {
	protected Context mContext;

	public SNSItemView(Context context) {
		super(context);	
		mContext = context;
	}
	public SNSItemView(Context ctx, AttributeSet attrs) 
	{
		super(ctx, attrs);
		mContext = ctx;
	}
}
