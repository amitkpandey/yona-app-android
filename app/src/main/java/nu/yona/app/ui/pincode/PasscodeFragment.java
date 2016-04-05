/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.pincode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.impl.PasscodeManagerImpl;
import nu.yona.app.customview.YonaFontEditTextView;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.state.EventChangeListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.utils.AppConstant;

/**
 * Created by bhargavsuthar on 4/3/16.
 */
public class PasscodeFragment extends BaseFragment implements EventChangeListener, View.OnClickListener {

    private YonaFontTextView passcode_title, passcode_description, passcode_error, passcode_reset;
    private YonaFontEditTextView passcode1, passcode2, passcode3, passcode4;
    private ProgressBar profile_progress;
    private PasscodeManagerImpl passcodeManagerImpl;
    private ImageView accont_image;
    private String screen_type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            screen_type = getArguments().getString(AppConstant.SCREEN_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pincode_layout, container, false);

        YonaApplication.getEventChangeManager().registerListener(this);

        passcode_title = (YonaFontTextView) view.findViewById(R.id.passcode_title);
        passcode_description = (YonaFontTextView) view.findViewById(R.id.passcode_description);
        passcode_error = (YonaFontTextView) view.findViewById(R.id.passcode_error);
        passcode_reset = (YonaFontTextView) view.findViewById(R.id.passcode_reset);
        accont_image = (ImageView) view.findViewById(R.id.img_account_check);
        passcode_reset = (YonaFontTextView) view.findViewById(R.id.passcode_reset);

        profile_progress = (ProgressBar) view.findViewById(R.id.profile_progress);

        passcode1 = (YonaFontEditTextView) view.findViewById(R.id.passcode1);
        passcode2 = (YonaFontEditTextView) view.findViewById(R.id.passcode2);
        passcode3 = (YonaFontEditTextView) view.findViewById(R.id.passcode3);
        passcode4 = (YonaFontEditTextView) view.findViewById(R.id.passcode4);


        passcodeManagerImpl = new PasscodeManagerImpl();

        YonaPasswordTransformationManager yonaPasswordTransformationManager = new YonaPasswordTransformationManager();
        passcode1.setTransformationMethod(yonaPasswordTransformationManager);
        passcode2.setTransformationMethod(yonaPasswordTransformationManager);
        passcode3.setTransformationMethod(yonaPasswordTransformationManager);
        passcode4.setTransformationMethod(yonaPasswordTransformationManager);


        passcode1.setOnKeyListener(keyListener);
        passcode2.setOnKeyListener(keyListener);
        passcode3.setOnKeyListener(keyListener);
        passcode4.setOnKeyListener(keyListener);

        FieldTextWatcher watcher = new FieldTextWatcher();

        passcode1.addTextChangedListener(watcher);
        passcode2.addTextChangedListener(watcher);
        passcode3.addTextChangedListener(watcher);
        passcode4.addTextChangedListener(watcher);

        passcode_reset.setOnClickListener(this);


        updateScreenUI();
        resetDigit();

        return view;

    }

    private void updateScreenUI() {
        if (!TextUtils.isEmpty(screen_type)) {
            if (screen_type.equalsIgnoreCase(AppConstant.PASSCODE)) {
                populatePasscodeView();
                visibleView();
            } else if (screen_type.equalsIgnoreCase(AppConstant.PASSCODE_VERIFY)) {
                populateVerifyPasscodeView();
                visibleView();
            } else if (screen_type.equalsIgnoreCase(AppConstant.LOGGED_IN)) {
                visibleLoginView();
                populateLoginView();
            } else if (screen_type.equalsIgnoreCase(AppConstant.SMS)) {
                //Todo for SMS screen
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        YonaApplication.getEventChangeManager().unRegisterListener(this);
    }

    private void visibleView() {
        passcode_title.setVisibility(View.VISIBLE);
        passcode_description.setVisibility(View.VISIBLE);
        profile_progress.setVisibility(View.VISIBLE);
    }

    private void visibleLoginView() {
        passcode_title.setVisibility(View.VISIBLE);
        passcode_description.setVisibility(View.GONE);
        profile_progress.setVisibility(View.GONE);
        passcode_reset.setVisibility(View.VISIBLE);
    }

    private void populateLoginView() {
        accont_image.setImageResource(R.drawable.icn_y);
        passcode_title.setText(getString(R.string.passcode_title));
        ((LoggedInActivity) getActivity()).updateTitle(getString(R.string.login));
    }


    /**
     * update screen's text as per account pincode's verification
     */
    private void populateVerifyPasscodeView() {
        accont_image.setImageResource(R.drawable.icn_secure);
        passcode_title.setText(getString(R.string.passcode_step2_title));
        passcode_description.setText(getString(R.string.passcode_step2_desc));
        ((PasscodeActivity) getActivity()).updateTitle(getString(R.string.pincode));
    }

    /**
     * update screen's text as per Account pincode creation
     */
    private void populatePasscodeView() {
        accont_image.setImageResource(R.drawable.icn_account_created);
        passcode_title.setText(getString(R.string.passcode_step1_title));
        passcode_description.setText(getString(R.string.passcode_step1_desc));
        ((PasscodeActivity) getActivity()).updateTitle(getString(R.string.pincode));
    }


    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (v.getId() == R.id.passcode4) {
                    if (passcode4.getText().length() == 0) {
                        passcode3.requestFocus();
                        passcode3.setFocusableInTouchMode(true);
                        passcode4.setFocusableInTouchMode(false);
                        passcode3.setText("");
                    }
                } else if (v.getId() == R.id.passcode3) {
                    if (passcode3.getText().length() == 0) {
                        passcode2.requestFocus();
                        passcode2.setFocusableInTouchMode(true);
                        passcode3.setFocusableInTouchMode(false);
                        passcode2.setText("");
                    }
                } else if (v.getId() == R.id.passcode2) {
                    if (passcode2.getText().length() == 0) {
                        passcode1.requestFocus();
                        passcode1.setFocusableInTouchMode(true);
                        passcode2.setFocusableInTouchMode(false);
                        passcode1.setText("");
                    }
                }
            }
            return false;
        }
    };

    /**
     * Reset all fields
     */
    protected void resetDigit() {
        passcode1.getText().clear();
        passcode1.setFocusableInTouchMode(true);
        passcode2.getText().clear();
        passcode2.setFocusableInTouchMode(false);
        passcode3.getText().clear();
        passcode3.setFocusableInTouchMode(false);
        passcode4.getText().clear();
        passcode4.setFocusableInTouchMode(false);
        passcode1.requestFocus();

    }

    @Override
    public void onStateChange(int eventType, Object object) {
        switch (eventType) {
            case EventChangeManager.EVENT_PASSCODE_ERROR:
                passcode_error.setText((String) object);
                resetDigit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.passcode_reset:
                //Todo redirect user to reset Pincode process again
                break;
            default:
                break;
        }
    }

    private final class FieldTextWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                if (passcode1.hasFocus()) {
                    passcode2.setFocusableInTouchMode(true);
                    passcode2.requestFocus();
                    passcode1.setFocusableInTouchMode(false);
                   // passcode3.setFocusableInTouchMode(false);
                   // passcode4.setFocusableInTouchMode(false);
                } else if (passcode2.hasFocus()) {
                    //passcode1.setFocusableInTouchMode(false);
                    passcode3.setFocusableInTouchMode(true);
                    passcode3.requestFocus();
                    passcode2.setFocusableInTouchMode(false);
                    //passcode4.setFocusableInTouchMode(false);
                } else if (passcode3.hasFocus()) {
                    passcode4.setFocusableInTouchMode(true);
                    passcode4.requestFocus();
                    //passcode1.setFocusableInTouchMode(false);
                    //passcode2.setFocusableInTouchMode(false);
                    passcode3.setFocusableInTouchMode(false);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String code =
                    passcode1.getText().toString() + passcode2.getText().toString() + passcode3.getText().toString() + passcode4.getText().toString();
            if (passcode4.hasFocus() && passcodeManagerImpl.checkPasscodeLength(code)) {
                YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_PASSCODE_STEP_TWO, Integer.parseInt(code));
            }
        }

    }
}
