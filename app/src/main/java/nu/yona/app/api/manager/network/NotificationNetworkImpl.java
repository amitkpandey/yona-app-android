/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.manager.network;

import nu.yona.app.api.model.MessageBody;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 09/05/16.
 */
public class NotificationNetworkImpl extends BaseImpl {

    /**
     * Gets message.
     *
     * @param url          the url
     * @param yonaPassword the yona password
     * @param itemsPerPage the items per page
     * @param pageNo       the page no
     * @param listener     the listener
     */
    public void getMessage(String url, String yonaPassword, int itemsPerPage, int pageNo, DataLoadListener listener) {
        try {
            getRestApi().getMessages(url, yonaPassword, itemsPerPage, pageNo);
        } catch (Exception e) {
            AppUtils.throwException(NotificationNetworkImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    /**
     * Delete message.
     *
     * @param url      the url
     * @param password the password
     * @param listener the listener
     */
    public void deleteMessage(String url, String password, DataLoadListener listener) {
        try {
            getRestApi().deleteMessage(url, password).enqueue(getCall(listener));
        } catch (Exception e) {
            AppUtils.throwException(NotificationNetworkImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    /**
     * Post message.
     *
     * @param url      the url
     * @param password the password
     * @param body     the body
     * @param listener the listener
     */
    public void postMessage(String url, String password, MessageBody body, DataLoadListener listener) {
        try {
            getRestApi().postMessage(url, password, body).enqueue(getCall(listener));
        } catch (Exception e) {
            AppUtils.throwException(NotificationNetworkImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }
}
