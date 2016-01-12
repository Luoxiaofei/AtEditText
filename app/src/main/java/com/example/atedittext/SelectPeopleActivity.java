package com.example.atedittext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectPeopleActivity extends AppCompatActivity {

    private ListView at_select_list;
    public static final String RESULT_PEOPLE_NAME = "select_name";
    public static final String RESULT_PEOPLE_ID = "select_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);
        at_select_list = (ListView) findViewById(R.id.at_select_list);
        SelectPeopleAdapter peopleAdapter = new SelectPeopleAdapter(this);
        peopleAdapter.setDatas(initData());
        at_select_list.setAdapter(peopleAdapter);
        at_select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof SelectPeopleItemView) {
                    SelectPeopleItemView tmpItem = (SelectPeopleItemView) view;
                    Member member = tmpItem.getMember();
                    if (member != null) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_PEOPLE_NAME, member.getNickName());
                        intent.putExtra(RESULT_PEOPLE_ID, member.getId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        Logger.e("所选用户信息不存在");
                    }
                }
            }
        });
    }

    private ArrayList<Member> initData() {
        ArrayList<Member> members = new ArrayList<Member>();
        for (int i=0; i<10; i++) {
            Member mb = new Member();
            String tmpStr = "selectMember" + i;
            mb.setId(tmpStr);
            mb.setNickName(tmpStr);
            members.add(mb);
        }

        return members;
    }

}
