/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.ui.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.customview.YonaFontEditTextView;
import nu.yona.app.customview.YonaFontNumberTextView;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.customview.YonaPhoneWatcher;
import nu.yona.app.state.EventChangeListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;

/**
 * Created by kinnarvasa on 25/03/16.
 */
public class StepTwo extends BaseFragment implements EventChangeListener {

    private YonaFontNumberTextView mobileNumber;
    private YonaFontEditTextView nickName;
    private TextInputLayout mobileNumberLayout, nickNameLayout;
    private SignupActivity activity;
    private AppBarLayout appbar;
    private boolean isAdding;

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isAdding = count == 1 ? true : false;
            nickName.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0 && (s.length() == 1 || s.charAt(s.length() - 1) == ' ') && isAdding) {
                nickName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_steptwo_fragment, null);

        activity = (SignupActivity) getActivity();

        ((YonaFontTextView) view.findViewById(R.id.toolbar_title)).setText(R.string.join);

        mobileNumber = (YonaFontNumberTextView) view.findViewById(R.id.mobile_number);
        nickName = (YonaFontEditTextView) view.findViewById(R.id.nick_name);
        nickName.addTextChangedListener(watcher);

        mobileNumberLayout = (TextInputLayout) view.findViewById(R.id.mobile_number_layout);
        nickNameLayout = (TextInputLayout) view.findViewById(R.id.nick_name_layout);

        appbar = (AppBarLayout) view.findViewById(R.id.appbar);

        mobileNumber.setText(R.string.country_code_with_zero);
        mobileNumber.requestFocus();
        activity.showKeyboard(mobileNumber);
        mobileNumber.setNotEditableLength(getString(R.string.country_code_with_zero).length());
        mobileNumber.addTextChangedListener(new YonaPhoneWatcher(mobileNumber, getString(R.string.country_code_with_zero), getActivity(), mobileNumberLayout));

        mobileNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showKeyboard(mobileNumber);
            }
        });

        nickNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showKeyboard(nickName);
            }
        });

        nickName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    goToNext();
                }
                return false;
            }
        });
        YonaApplication.getEventChangeManager().registerListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appbar.setExpanded(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YonaApplication.getEventChangeManager().unRegisterListener(this);
    }

    @Override
    public void onStateChange(int eventType, Object object) {
        if (eventType == EventChangeManager.EVENT_SIGNUP_STEP_TWO_NEXT) {
            goToNext();
        }
    }

    private void goToNext() {
        String number = getString(R.string.country_code) + mobileNumber.getText().toString().substring(getString(R.string.country_code_with_zero).length());
        String phonenumber = number.replace(" ", "");
        if (validateMobileNumber(phonenumber) && validateNickName()) {
            YonaApplication.getEventChangeManager().getDataState().getRegisterUser().setMobileNumber(phonenumber);
            YonaApplication.getEventChangeManager().getDataState().getRegisterUser().setNickName(nickName.getText().toString());
            YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_SIGNUP_STEP_TWO_ALLOW_NEXT, null);
        }
    }

    private boolean validateMobileNumber(String number) {
        if (!APIManager.getInstance().getAuthenticateManager().validateMobileNumber(number)) {
            mobileNumberLayout.setErrorEnabled(true);
            mobileNumberLayout.setError(getString(R.string.enternumbervalidation));
            activity.showKeyboard(mobileNumber);
            mobileNumber.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateNickName() {
        if (!APIManager.getInstance().getAuthenticateManager().validateText(nickName.getText().toString())) {
            nickNameLayout.setErrorEnabled(true);
            nickNameLayout.setError(getString(R.string.enternicknamevalidation));
            activity.showKeyboard(nickName);
            nickName.requestFocus();
            return false;
        }
        return true;
    }
}
