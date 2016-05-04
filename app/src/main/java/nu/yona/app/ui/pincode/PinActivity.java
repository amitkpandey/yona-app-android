/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.pincode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.impl.AuthenticateManagerImpl;
import nu.yona.app.api.manager.impl.PasscodeManagerImpl;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.PinResetDelay;
import nu.yona.app.customview.CustomAlertDialog;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.state.EventChangeListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseActivity;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.ui.signup.OTPActivity;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.AppUtils;
import nu.yona.app.utils.PreferenceConstant;

/**
 * Created by bhargavsuthar on 3/30/16.
 */
public class PinActivity extends BaseActivity implements EventChangeListener {

    private PasscodeManagerImpl passcodeManagerImpl;
    private TextView txtTitle;
    private PasscodeFragment passcodeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_container_layout);

        passcodeManagerImpl = new PasscodeManagerImpl();

        YonaApplication.getEventChangeManager().registerListener(this);

        txtTitle = (TextView) findViewById(R.id.toolbar_title);

        passcodeFragment = new PasscodeFragment();
        Bundle loginBundle = new Bundle();
        loginBundle.putString(AppConstant.SCREEN_TYPE, AppConstant.LOGGED_IN);
        passcodeFragment.setArguments(loginBundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.replace(R.id.blank_container, passcodeFragment);
        fragmentTransaction.commit();
    }

    private void updateTitle(String title) {
        txtTitle.setText(title);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        YonaApplication.getEventChangeManager().unRegisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTitle(getString(R.string.login));
        if (YonaApplication.getUserPreferences().getBoolean(PreferenceConstant.USER_BLOCKED, false) && passcodeFragment != null) {
            updateBlockMsg();
        }
    }

    private void updateBlockMsg() {
        YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_PASSCODE_ERROR, getString(R.string.msgblockuser));
        passcodeFragment.disableEditable();
    }

    @Override
    public void onStateChange(int eventType, Object object) {
        switch (eventType) {
            case EventChangeManager.EVENT_PASSCODE_STEP_TWO:
                String passcode = (String) object;
                if (passcodeManagerImpl.validatePasscode(passcode)) {
                    showChallengesScreen();
                } else if (passcodeManagerImpl.isWrongCounterReached()) {
                    YonaApplication.getUserPreferences().edit().putBoolean(PreferenceConstant.USER_BLOCKED, true).commit();
                    updateBlockMsg();
                } else {
                    YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_PASSCODE_ERROR, getString(R.string.passcodetryagain));
                }
                break;
            case EventChangeManager.EVENT_PASSCODE_RESET:
                doPinReset();
                break;
            default:
                break;
        }

    }

    private void showChallengesScreen() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }


    private void doPinReset() {
        showLoadingView(true, null);
        new AuthenticateManagerImpl(this).requestPinReset(new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                showLoadingView(false, null);
                if (result instanceof PinResetDelay) {
                    PinResetDelay delay = (PinResetDelay) result;
                    CustomAlertDialog.show(PinActivity.this, getString(R.string.resetpinrequest, AppUtils.getTimeForOTP(delay.getDelay())), getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            loadOTPScreen();
                        }
                    });
                }
            }

            @Override
            public void onError(Object errorMessage) {
                ErrorMessage message = (ErrorMessage) errorMessage;
                showLoadingView(false, null);
                CustomAlertDialog.show(PinActivity.this, message.getMessage(), getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
    }

    private void loadOTPScreen() {
        startActivity(new Intent(PinActivity.this, OTPActivity.class));
        finish();
    }
}
