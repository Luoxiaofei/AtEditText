package com.example.atedittext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SelectPeopleAdapter extends MyBaseAdapter<Member> {

    private final String TAG = Member.class.getSimpleName();

    public SelectPeopleAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        Member info = getItem(position);
        if (convertView == null || false == (convertView instanceof SelectPeopleItemView)) {
            SelectPeopleItemView FView = new SelectPeopleItemView(mContext);
            holder = new ViewHolder();
            FView.setTag(holder);
            holder.view = FView;
            convertView = FView;

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.view.setData(info);

        return convertView;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Member getItem(int position) {
        if (mList != null && position < mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    public static class ViewHolder {
        SelectPeopleItemView view;
    }

    public void alertData(ArrayList<Member> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
