package com.example.atedittext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectPeopleItemView extends SNSItemView {
    private final String TAG = SelectPeopleItemView.class.getSimpleName();

    private ImageView member_header;
    private TextView member_name;
    private Member mMemberInfo;

    public SelectPeopleItemView(Context context) {
        super(context);
        init();
    }

    public void setData(Member info) {
        mMemberInfo = info;
        setUI();
    }

    public Member getMember() {
        return mMemberInfo;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        removeAllViews();
        LayoutInflater factory = LayoutInflater.from(mContext);
        View convertView = factory.inflate(R.layout.at_people_item, null);
        addView(convertView);
        member_header = (ImageView) convertView.findViewById(R.id.member_header);
        member_name = (TextView) convertView.findViewById(R.id.member_name);
    }

    private void setUI() {
        if (mMemberInfo == null) {
            Logger.d(TAG, "GroupMember can not be null!");
        } else {
            member_name.setText(mMemberInfo.getNickName() == null ? "" : mMemberInfo.getNickName());
        }
    }


}
