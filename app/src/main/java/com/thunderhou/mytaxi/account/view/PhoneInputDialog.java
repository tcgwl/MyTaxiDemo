package com.thunderhou.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thunderhou.mytaxi.R;
import com.thunderhou.mytaxi.common.util.FormatUtil;

public class PhoneInputDialog extends Dialog {
    private View mRoot;
    private EditText mPhoneEditText;
    private Button mNextButton;

    public PhoneInputDialog(Context context) {
        this(context, R.style.Dialog);
    }

    public PhoneInputDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflater.inflate(R.layout.dialog_phone_input, null);
        setContentView(mRoot);
        initListener();
    }

    private void initListener() {
        mNextButton = (Button) findViewById(R.id.btn_next);
        mNextButton.setEnabled(false);
        mPhoneEditText = (EditText) findViewById(R.id.phone);

        // 监听手机号输入是否合法
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneInputDialog.this.dismiss();

                String phoneNum = mPhoneEditText.getText().toString();
                SmsCodeDialog dialog = new SmsCodeDialog(getContext(), phoneNum);
                dialog.show();
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneInputDialog.this.dismiss();
            }
        });
    }

    private void check(){
        String phone = mPhoneEditText.getText().toString();
        mNextButton.setEnabled(FormatUtil.checkMobile(phone));
    }
}
