/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.manager;

import nu.yona.app.api.model.RegisterUser;
import nu.yona.app.api.model.User;
import nu.yona.app.listener.DataLoadListener;

/**
 * Created by kinnarvasa on 25/03/16.
 */
public interface AuthenticateManager {

    /**
     * Validate text boolean.
     *
     * @param string the string
     * @return the boolean
     */
    boolean validateText(String string);

    /**
     * Validate mobile number boolean.
     *
     * @param mobileNumber the mobile number
     * @return the boolean
     */
    boolean validateMobileNumber(String mobileNumber);

    /**
     * Register user.
     *
     * @param user     the user
     * @param listener the listener
     */
    void registerUser(RegisterUser user, DataLoadListener listener);

    /**
     * Verify otp.
     *
     * @param user     the user
     * @param otp      the otp
     * @param listener the listener
     */
    void verifyOTP(RegisterUser user, String otp, DataLoadListener listener);

    /**
     * Verify otp.
     *
     * @param otp      the otp
     * @param listener the listener
     */
    void verifyOTP(String otp, DataLoadListener listener);

    /**
     * Resend otp.
     *
     * @param listener the listener
     */
    void resendOTP(DataLoadListener listener);

    /**
     * Request pin reset.
     *
     * @param listener the listener
     */
    void requestPinReset(DataLoadListener listener);

    /**
     * Gets user.
     *
     * @return the user
     */
    User getUser();

    /**
     * Gets user.
     *
     * @param url      the url
     * @param listener the listener
     */
    void getUser(final String url, final DataLoadListener listener);

    /**
     * Request user override.
     *
     * @param mobileNumber the mobile number
     * @param listener     the listener
     */
    void requestUserOverride(String mobileNumber, final DataLoadListener listener);

    /**
     * Delete user.
     *
     * @param listener the listener
     */
    void deleteUser(final DataLoadListener listener);
}
