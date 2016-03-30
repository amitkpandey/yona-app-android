/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Robolectric;

import nu.yona.app.R;
import nu.yona.app.YonaTestCase;
import nu.yona.app.ui.signup.SignupActivity;

import static org.robolectric.Shadows.shadowOf;

/**
 * Created by kinnarvasa on 30/03/16.
 */
public class LaunchActivityTest extends YonaTestCase {

    private Activity activity;
    private Button jonMeBtn, loginBtn;

    private void doInitialize() {
        if (activity == null) {
            activity = Robolectric.setupActivity(LaunchActivity.class);
            jonMeBtn = (Button) activity.findViewById(R.id.join);
            loginBtn = (Button) activity.findViewById(R.id.login);
        }
    }

    @Test
    public void testSignupClick() {
        doInitialize();
        jonMeBtn.performClick();
        Assert.assertEquals(shadowOf(activity).getNextStartedActivity(), new Intent(activity, SignupActivity.class));
    }

    @Test
    public void testLoginClick() {
        doInitialize();
        loginBtn.performClick();
        Assert.assertEquals(shadowOf(activity).getNextStartedActivity(), new Intent(activity, SignupActivity.class));
    }
}
