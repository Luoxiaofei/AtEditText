package com.example.atedittext;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView clickable_image;
    private EditText input_edit;
    private TextView at_name_value;
    public MystyleSpan[] mSpans;
    public static boolean isKeyDelePressed = false;
    private static final int SELECT_AT_PEOPLE_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickable_image = (ImageView) findViewById(R.id.clickable_image);
        clickable_image.setOnLongClickListener(headerImageLongClickListener);
        input_edit = (EditText) findViewById(R.id.input_edit);
        input_edit.addTextChangedListener(new editTextChangedListener());
        input_edit.setOnKeyListener(editTextKeyListener);
        at_name_value = (TextView) findViewById(R.id.at_name_value);
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpans != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("select people ids:");
                    for (MystyleSpan span : mSpans) {
                        Logger.d("select span id : " + span.getmId());
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append(span.getmId());
                    }

                    ((TextView)findViewById(R.id.select_value)).setText(sb.toString());
                }
            }
        });
    }

    private View.OnLongClickListener headerImageLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            longPressAvatar(at_name_value.getText().toString(), "123");
            return false;
        }
    };

    private void selectPeopleResult(String realName, final String id) {
        longClickProgress(realName, id, false);
    }

    private void longPressAvatar(String realName, final String id) {
        longClickProgress(realName, id, true);
    }

    private void longClickProgress(String realName, final String id, boolean isLongPressAvatar) {
        if (TextUtils.isEmpty(realName)) {
            Logger.e("click Avatar user name is null");
        } else {
            int index = input_edit.getSelectionStart();
            Editable editable = input_edit.getText();

            String appendStr = "@" + realName + " ";
            if (isLongPressAvatar) {
                editable.insert(index, appendStr);
            } else {
                if (index > 0) {
                    editable.replace(index - 1, index, appendStr);
                    //替换了一个@，光标计算位置应该前移一位 将@xxx作为整体
                    index = index - 1;
                }
            }

            String editStr = input_edit.getText().toString();
            input_edit.setText(getClickableSpan(index, appendStr, editStr, id));
            input_edit.requestFocus();
            input_edit.setSelection(editStr.length());
        }
    }

    private SpannableStringBuilder getClickableSpan(int index, String appendStr, String oldText, String xbid) {
        if (oldText.length() > 0 && oldText.endsWith("@")) {
            oldText = oldText.substring(0, oldText.length() - 1);// 如果最后一个字符是@说明是输入@后选择@人，所以去掉最后面那个导致选择@人的@符号然后再执行下面
        }

        SpannableStringBuilder spannableStringBuilder = spanOrgAt(oldText);
        spannableStringBuilder.setSpan(new MystyleSpan(Typeface.NORMAL, xbid), index, index + appendStr.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //還原以前以前長按得結果
    private SpannableStringBuilder spanOrgAt(String oldtext) {
        SpannableStringBuilder appendSb = new SpannableStringBuilder();
        appendSb.append(oldtext);
        if (mSpans != null) {
            for (MystyleSpan mspan : mSpans) {
                if (mspan instanceof MystyleSpan) {
                    Spanned s = input_edit.getEditableText();
                    int start = s.getSpanStart(mspan);
                    int end = s.getSpanEnd(mspan);
                    appendSb.setSpan(new MystyleSpan(Typeface.BOLD, mspan.getmId()), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return appendSb;
    }

    private View.OnKeyListener editTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (input_edit.getText().length() > 0) { // 删除操作
                    isKeyDelePressed = true;
                }

                //获取光标位置
                int index = input_edit.getSelectionStart();
                if (mSpans != null) {
                    for (MystyleSpan mspan : mSpans) {
                        if (mspan instanceof MystyleSpan) {
                            final Editable editable = input_edit.getEditableText();
                            int start = editable.getSpanStart(mspan);
                            int end = editable.getSpanEnd(mspan);
                            if (index == end) {
                                editable.delete(start + 1, end);
                                break;
                            }

                        }
                    }
                }
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(resultCode == RESULT_OK && requestCode == SELECT_AT_PEOPLE_REQUEST_CODE){
            String selectName = data.getStringExtra(SelectPeopleActivity.RESULT_PEOPLE_NAME);
            String selectId = data.getStringExtra(SelectPeopleActivity.RESULT_PEOPLE_ID);
            if (selectId == null) {
                Logger.e("back user xbid is null");
            }else {
                selectPeopleResult(selectName, selectId);
            }

        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    class editTextChangedListener implements TextWatcher {

        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                mSpans = input_edit.getText().getSpans(0, input_edit.length(), MystyleSpan.class);

                String text = s.toString();
                if (!isKeyDelePressed)// 删除到@符号时不出现选择人列表
                {
                    int index = input_edit.getSelectionStart();
                    String subStr = text.substring(0, index);
                    if (subStr.endsWith("@")) {
                        boolean needToSelect = true;
                        if (text.equals("@")) {
                            Logger.i("is nothing before @");
                            needToSelect = true;
                        } else {
                            int lastIndex = text.lastIndexOf("@");
                            String lastChar = text.substring(lastIndex - 1, lastIndex);
                            if (TextUtils.isEmpty(lastChar)) {
                                Logger.i("is nothing before @");
                            } else {
                                Pattern p = Pattern.compile("[0-9]*");
                                Matcher m = p.matcher(lastChar);
                                if (m.matches()) { //判断是否是数字，是数字不需要弹出选择人，有可能是输入邮箱
                                    needToSelect = false;
                                } else {
                                    p = Pattern.compile("[a-zA-Z]"); //判断是否是字母，有可能是输入邮箱
                                    m = p.matcher(lastChar);
                                    if (m.matches()) {
                                        needToSelect = false;
                                    }
                                }
                            }
                        }
                        if (needToSelect) {
                            //goto select people
                            startActivityForResult(new Intent(MainActivity.this, SelectPeopleActivity.class), SELECT_AT_PEOPLE_REQUEST_CODE);
                        }
                    }
                }
            }
            isKeyDelePressed = false;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mSpans != null) {
                for (MystyleSpan mspan : mSpans) {
                    if (mspan instanceof MystyleSpan) {
                        final Editable editable = input_edit.getEditableText();
                        int editablestart = editable.getSpanStart(mspan);
                        int editableend = editable.getSpanEnd(mspan);
                        if (start > editablestart && start < editableend) {
                            input_edit.getText().removeSpan(mspan);
                            break;
                        }

                    }
                }
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
