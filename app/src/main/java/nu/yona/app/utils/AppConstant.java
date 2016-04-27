/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.utils;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public interface AppConstant {
    String CLEAR_FRAGMENT_STACK = "clearFragmentStack";
    int MAX_COUNTER = 4;
    int TIMER_DELAY = 500;
    int MOBILE_NUMBER_LENGTH = 12;
    int OTP_LENGTH = 4;
    int ADD_DEVICE_PASSWORD_CHAR_LIMIT = 6;
    int YONA_PASSWORD_CHAR_LIMIT = 20;
    int ONE_SECOND = 1000;
    int TIME_INTERVAL_ONE = 1;
    int TIME_INTERVAL_FIFTEEN = 15;
    int MIN_DEFAULT_TIME = 5;

    String PASSCODE = "passcode";
    String PASSCODE_VERIFY = "passcode_verify";
    String OTP = "otp";
    String NEW_DEVICE_REQUESTED = "newDeviceRequested";

    String LOGGED_IN = "logged_in";
    String SCREEN_TYPE = "screen_type";
    String FROM_LOGIN = "fromLogin";
    String GOAL_OBJECT = "yonaGoalObject";
    String NEW_GOAL_TYPE = "newGoalType";
    String USER = "user";

    String TIME = "time";
    String POSITION = "position";
}
