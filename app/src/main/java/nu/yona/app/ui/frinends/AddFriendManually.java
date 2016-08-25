/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.ui.frinends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.RegisterUser;
import nu.yona.app.customview.YonaFontButton;
import nu.yona.app.customview.YonaFontEditTextView;
import nu.yona.app.customview.YonaFontNumberTextView;
import nu.yona.app.customview.YonaPhoneWatcher;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.state.EventChangeListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;

/**
 * Created by kinnarvasa on 27/04/16.
 */
public class AddFriendManually extends BaseFragment implements EventChangeListener {
    private YonaFontEditTextView firstName, lastName, email;
    private YonaFontNumberTextView mobileNumber;
    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout, mobileNumberLayout;
    private boolean isAdding;
    
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isAdding = count == 1 ? true : false;
            hideErrorMessage();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0 && (s.length() == 1 || s.charAt(s.length() - 1) == ' ')  && isAdding) {
                firstName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                lastName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            }
        }
    };
    private YonaFontButton addFriendButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_friend_manually_fragment, null);

        getView(view);
        addButtonListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        YonaApplication.getEventChangeManager().registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        YonaApplication.getEventChangeManager().unRegisterListener(this);
    }

    private void getView(View view) {
        View activityRootView = view.findViewById(R.id.addfriendLayout);
        udpateBottomTabVisibility(activityRootView);
        firstName = (YonaFontEditTextView) view.findViewById(R.id.first_name);
        lastName = (YonaFontEditTextView) view.findViewById(R.id.last_name);
        email = (YonaFontEditTextView) view.findViewById(R.id.email);
        mobileNumber = (YonaFontNumberTextView) view.findViewById(R.id.mobile_number);

        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        firstNameLayout = (TextInputLayout) view.findViewById(R.id.first_name_layout);
        lastNameLayout = (TextInputLayout) view.findViewById(R.id.last_name_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.email_layout);
        mobileNumberLayout = (TextInputLayout) view.findViewById(R.id.mobile_number_layout);

        addFriendButton = (YonaFontButton) view.findViewById(R.id.addFriendButton);

        mobileNumber.setNotEditableLength(getString(R.string.country_code_with_zero).length());
        mobileNumber.addTextChangedListener(new YonaPhoneWatcher(mobileNumber, getString(R.string.country_code_with_zero), getActivity(), mobileNumberLayout));

        firstNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YonaActivity.getActivity().showKeyboard(firstName);
            }
        });

        lastNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YonaActivity.getActivity().showKeyboard(lastName);
            }
        });

        mobileNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YonaActivity.getActivity().showKeyboard(mobileNumber);
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YonaActivity.getActivity().showKeyboard(email);
            }
        });

        mobileNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    goToNext();
                }
                return false;
            }
        });

        mobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && TextUtils.isEmpty(mobileNumber.getText())) {
                    mobileNumber.setText(R.string.country_code_with_zero);
                }
            }
        });

        firstName.requestFocus();
    }

    private void goToNext() {
        if (validateFields()) {
            addFriend();
        }
    }

    private void addButtonListener() {
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });
    }

    private boolean validateFields() {
        if (!APIManager.getInstance().getBuddyManager().validateText(firstName.getText().toString())) {
            updateErrorView(firstNameLayout, getString(R.string.enterfirstnamevalidation), firstName);
            return false;
        } else if (!APIManager.getInstance().getBuddyManager().validateText(lastName.getText().toString())) {
            updateErrorView(lastNameLayout, getString(R.string.enterlastnamevalidation), lastName);
            return false;
        } else if (!APIManager.getInstance().getBuddyManager().validateEmail(email.getText().toString())) {
            updateErrorView(emailLayout, getString(R.string.enteremailvalidation), email);
            return false;
        } else if (!APIManager.getInstance().getBuddyManager().validateMobileNumber(mobileNumber.getText().toString())) {
            updateErrorView(mobileNumberLayout, getString(R.string.enternumbervalidation), mobileNumber);
            return false;
        }
        return true;
    }

    private void updateErrorView(final TextInputLayout mInputLayout, final String mErrorMsg, final YonaFontEditTextView mEditText) {
        mInputLayout.setErrorEnabled(true);
        mInputLayout.setError(mErrorMsg);
        mInputLayout.setFocusable(true);
        YonaActivity.getActivity().showKeyboard(mEditText);
        mEditText.requestFocus();
    }

    private void updateErrorView(final TextInputLayout mInputLayout, final String mErrorMsg, final YonaFontNumberTextView mEditText) {
        mInputLayout.setErrorEnabled(true);
        mInputLayout.setError(mErrorMsg);
        mInputLayout.setFocusable(true);
        YonaActivity.getActivity().showKeyboard(mEditText);
        mEditText.requestFocus();
    }

    private void addFriend() {
        ((YonaActivity) getActivity()).showLoadingView(true, null);
        APIManager.getInstance().getBuddyManager().addBuddy(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), mobileNumber.getText().toString(), new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                YonaActivity.getActivity().showLoadingView(false, null);
                YonaActivity.getActivity().onBackPressed();
                YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_UPDATE_FRIEND_OVERVIEW, null);
            }

            @Override
            public void onError(Object errorMessage) {
                ErrorMessage message = (ErrorMessage) errorMessage;
                YonaActivity.getActivity().showLoadingView(false, null);
                Snackbar.make(YonaActivity.getActivity().findViewById(android.R.id.content), message.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void hideErrorMessage() {
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
        mobileNumberLayout.setError(null);
    }

    @Override
    public void onStateChange(int eventType, Object object) {
        switch (eventType) {
            case EventChangeManager.EVENT_CONTAT_CHOOSED:
                if (object instanceof RegisterUser) {
                    updateUser((RegisterUser) object);
                }
                break;
            default:
                break;
        }
    }

    private void updateUser(RegisterUser user) {
        int NUMBER_LENGTH = 9;
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmailAddress());
        if (!TextUtils.isEmpty(user.getMobileNumber())) {
            String number = user.getMobileNumber().replace(getString(R.string.space), getString(R.string.blank));
            if (number.length() > NUMBER_LENGTH) {
                number = number.substring(number.length() - NUMBER_LENGTH);
                number = number.substring(0, 3) + getString(R.string.space) + number.substring(3, 6) + getString(R.string.space) + number.substring(6, 9);
            }
            mobileNumber.setText(getString(R.string.country_code_with_zero) + number);
        } else if (user.getMultipleNumbers() != null && user.getMultipleNumbers().size() > 1) {
            showNumberChooser(user.getMultipleNumbers(), user);
        } else {
            mobileNumber.setText(getString(R.string.country_code_with_zero));
        }
    }

    private void showNumberChooser(final List<String> numbers, final RegisterUser user) {
        final CharSequence[] charSequences = numbers.toArray(new CharSequence[numbers.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(YonaActivity.getActivity());
        builder.setTitle(getString(R.string.choose_number, user.getFirstName()));
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                user.setMobileNumber(charSequences[item].toString());
                updateUser(user);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
