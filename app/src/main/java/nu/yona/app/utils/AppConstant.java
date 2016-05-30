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
    /**
     * The constant CLEAR_FRAGMENT_STACK.
     */
    String CLEAR_FRAGMENT_STACK = "clearFragmentStack";

    /**
     * The constant SERVER_URL.
     */
    String SERVER_URL = "serverUrl";
    /**
     * The constant MAX_COUNTER.
     */
    int MAX_COUNTER = 4;

    /**
     * The constant TIMER_DELAY_HUNDRED.
     */
    int TIMER_DELAY_HUNDRED = 100;

    /**
     * The constant TIMER_DELAY_THREE_HUNDRED.
     */
    int TIMER_DELAY_THREE_HUNDRED = 300;
    /**
     * The constant TIMER_DELAY.
     */
    int TIMER_DELAY = 500;
    /**
     * The constant MOBILE_NUMBER_LENGTH.
     */
    int MOBILE_NUMBER_LENGTH = 12;
    /**
     * The constant OTP_LENGTH.
     */
    int OTP_LENGTH = 4;
    /**
     * The constant ADD_DEVICE_PASSWORD_CHAR_LIMIT.
     */
    int ADD_DEVICE_PASSWORD_CHAR_LIMIT = 6;
    /**
     * The constant YONA_PASSWORD_CHAR_LIMIT.
     */
    int YONA_PASSWORD_CHAR_LIMIT = 20;
    /**
     * The constant ONE_SECOND.
     */
    int ONE_SECOND = 1000;
    /**
     * The constant TIME_INTERVAL_ONE.
     */
    int TIME_INTERVAL_ONE = 1;
    /**
     * The constant TIME_INTERVAL_FIFTEEN.
     */
    int TIME_INTERVAL_FIFTEEN = 15;
    /**
     * The constant MIN_DEFAULT_TIME.
     */
    int MIN_DEFAULT_TIME = 5;

    /**
     * The constant PROFILE_IMAGE_BORDER_SIZE.
     */
    int PROFILE_IMAGE_BORDER_SIZE = 10;

    /**
     * The constant PROFILE_ICON_BORDER_SIZE.
     */
    int PROFILE_ICON_BORDER_SIZE = 5;

    /**
     * The constant READ_CONTACTS_PERMISSIONS_REQUEST.
     */
    int READ_CONTACTS_PERMISSIONS_REQUEST = 10001;
    /**
     * The constant PASSCODE.
     */
    String PASSCODE = "passcode";
    /**
     * The constant PASSCODE_VERIFY.
     */
    String PASSCODE_VERIFY = "passcode_verify";
    /**
     * The constant OTP.
     */
    String OTP = "otp";
    /**
     * The constant NEW_DEVICE_REQUESTED.
     */
    String NEW_DEVICE_REQUESTED = "newDeviceRequested";

    /**
     * The constant LOGGED_IN.
     */
    String LOGGED_IN = "logged_in";
    /**
     * The constant SCREEN_TYPE.
     */
    String SCREEN_TYPE = "screen_type";

    /**
     * The constant PROGRESS_DRAWABLE.
     */
    String PROGRESS_DRAWABLE = "progress_drawable";

    /**
     * The constant PASSCODE_TEXT_BACKGROUND.
     */
    String PASSCODE_TEXT_BACKGROUND = "passcodeBackground";
    /**
     * The constant FROM_LOGIN.
     */
    String FROM_LOGIN = "fromLogin";

    /**
     * The constant FROM_SETTINGS.
     */
    String FROM_SETTINGS = "fromSettings";

    /**
     * The constant PIN_RESET_VERIFICATION.
     */
    String PIN_RESET_VERIFICATION = "pinResetVerfication";

    /**
     * The first time app open
     */
    String FIRST_TIME_APP_OPEN = "firstTimeAppOpen";
    /**
     * The constant PIN_RESET_FIRST_STEP.
     */
    String PIN_RESET_FIRST_STEP = "pinResetFirstStep";

    /**
     * The constant PIN_RESET_SECOND_STEP.
     */
    String PIN_RESET_SECOND_STEP = "pinResetSecondStep";
    /**
     * The constant GOAL_OBJECT.
     */
    String GOAL_OBJECT = "yonaGoalObject";
    /**
     * The constant NEW_GOAL_TYPE.
     */
    String NEW_GOAL_TYPE = "newGoalType";

    /**
     * The constant COLOR_CODE.
     */
    String COLOR_CODE = "colorCode";

    /**
     * The constant SECOND_COLOR_CODE.
     */
    String SECOND_COLOR_CODE = "secondColorCode";
    /**
     * The constant TITLE_BACKGROUND_RESOURCE.
     */
    String TITLE_BACKGROUND_RESOURCE = "titleBackgroundResource";

    /**
     * The constant TAB_DESELECTED_COLOR.
     */
    String TAB_DESELECTED_COLOR = "tabSelectedColor";
    /**
     * The constant USER.
     */
    String USER = "user";

    /**
     * The constant TIME.
     */
    String TIME = "time";
    /**
     * The constant POSITION.
     */
    String POSITION = "position";

    /**
     * The constant YONAMESSAGE_OBJ
     */
    String YONAMESSAGE_OBJ = "yonaMessageObj";

    /**
     * The constant environmentList.
     */
    CharSequence[] environmentList = new CharSequence[]{"Development", "Acceptance"};
    /**
     * The constant environemntPath.
     */
    CharSequence[] environemntPath = new CharSequence[]{"http://85.222.227.142", "http://85.222.227.84"};

    /**
     * The Screen Title
     */
    String SCREEN_TITLE = "screenTitle";
}
