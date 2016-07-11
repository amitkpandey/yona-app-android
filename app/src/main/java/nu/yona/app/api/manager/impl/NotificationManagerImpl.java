/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.manager.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.manager.NotificationManager;
import nu.yona.app.api.manager.network.NotificationNetworkImpl;
import nu.yona.app.api.model.Embedded;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.MessageBody;
import nu.yona.app.api.model.Properties;
import nu.yona.app.api.model.YonaMessage;
import nu.yona.app.api.model.YonaMessages;
import nu.yona.app.enums.NotificationMessageEnum;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.AppUtils;
import nu.yona.app.utils.DateUtility;

/**
 * Created by kinnarvasa on 09/05/16.
 */
public class NotificationManagerImpl implements NotificationManager {

    private NotificationNetworkImpl notificationNetwork;
    private Context mContext;

    /**
     * Instantiates a new Notification manager.
     *
     * @param context the context
     */
    public NotificationManagerImpl(Context context) {
        notificationNetwork = new NotificationNetworkImpl();
        this.mContext = context;
    }

    /**
     * Gets message.
     *
     * @param listener the listener
     */
    public void getMessage(DataLoadListener listener) {
        getMessage(0, 0, listener); //set default page 0, start page = 0
    }

    /**
     * Gets message.
     *
     * @param itemsPerPage the items per page
     * @param pageNo       the page no
     * @param listener     the listener
     */
    public void getMessage(final int itemsPerPage, final int pageNo, final DataLoadListener listener) {
        getMessage(itemsPerPage, pageNo, listener, false);
    }

    private void getMessage(final int itemsPerPage, final int pageNo, final DataLoadListener listener, final boolean isProcessUpdate) {
        try {
            if (YonaApplication.getEventChangeManager().getDataState().getUser().getLinks() != null && YonaApplication.getEventChangeManager().getDataState().getUser().getLinks().getYonaMessages() != null
                    && !TextUtils.isEmpty(YonaApplication.getEventChangeManager().getDataState().getUser().getLinks().getYonaMessages().getHref())) {
                notificationNetwork.getMessage(YonaApplication.getEventChangeManager().getDataState().getUser().getLinks().getYonaMessages().getHref(), YonaApplication.getEventChangeManager().getSharedPreference().getYonaPassword(), itemsPerPage, pageNo, new DataLoadListener() {
                    @Override
                    public void onDataLoad(Object result) {
                        if (listener != null) {
                            if (result instanceof YonaMessages) {
                                YonaMessages yonaMessages = (YonaMessages) result;
                                if (yonaMessages != null && yonaMessages.getEmbedded() != null) {
                                    Embedded embedded = yonaMessages.getEmbedded();
                                    List<YonaMessage> listMessages = embedded.getYonaMessages();
                                    boolean isAnyProcessed = false;
                                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstant.YONA_LONG_DATE_FORMAT, Locale.getDefault());
                                    for (YonaMessage message : listMessages) {
                                        //update enum
                                        if (message.getStatus() != null) {
                                            message.setNotificationMessageEnum(NotificationMessageEnum.getNotificationMessageEnum(message.getType(), message.getStatus()));
                                        } else if (message.getDropBuddyReason() != null) {
                                            message.setNotificationMessageEnum(NotificationMessageEnum.getNotificationMessageEnum(message.getType(), message.getDropBuddyReason()));
                                        } else {
                                            message.setNotificationMessageEnum(NotificationMessageEnum.getNotificationMessageEnum(message.getType(), message.getChange()));
                                        }
                                        String uploadDate = "";

                                        String createdTime = message.getCreationTime();
                                        try {
                                            Date date = sdf.parse(createdTime);

                                            Calendar futureCalendar = Calendar.getInstance();
                                            futureCalendar.setTime(date);

                                            uploadDate = DateUtility.getRelativeDate(futureCalendar);
                                            message.setStickyTitle(uploadDate);
                                        } catch (Exception e) {
                                            AppUtils.throwException(ActivityManagerImpl.class.getSimpleName(), e, Thread.currentThread(), null);
                                        }
                                        yonaMessages.setEmbedded(embedded);
                                        if (!isProcessUpdate && message != null && message.getLinks() != null && message.getLinks().getYonaPreocess() != null
                                                && !TextUtils.isEmpty(message.getLinks().getYonaPreocess().getHref())) {
                                            isAnyProcessed = true;
                                            MessageBody body = new MessageBody();
                                            body.setProperties(new Properties());
                                            postMessageForProcess(message.getLinks().getYonaPreocess().getHref(), body);
                                        }
                                    }
                                    if (isAnyProcessed) {
                                        getMessage(itemsPerPage, pageNo, listener, true);
                                    }
                                    APIManager.getInstance().getAuthenticateManager().getUserFromServer();
                                }
                                listener.onDataLoad(yonaMessages);
                            }
                        }
                    }

                    @Override
                    public void onError(Object errorMessage) {
                        throwError(listener, errorMessage);
                    }
                });
            } else {
                listener.onError(new ErrorMessage(mContext.getString(R.string.urlnotfound)));
            }
        } catch (Exception e) {
            AppUtils.throwException(NotificationManagerImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    /**
     * Delete message.
     *
     * @param message      the message
     * @param itemsPerPage the items per page
     * @param pageNo       the page no
     * @param listener     the listener
     */
    public void deleteMessage(YonaMessage message, final int itemsPerPage, final int pageNo, final DataLoadListener listener) {
        try {
            if (message != null && message.getLinks() != null
                    && message.getLinks().getSelf() != null && !TextUtils.isEmpty(message.getLinks().getSelf().getHref())) {
                notificationNetwork.deleteMessage(message.getLinks().getSelf().getHref(), YonaApplication.getEventChangeManager().getSharedPreference().getYonaPassword(), new DataLoadListener() {
                    @Override
                    public void onDataLoad(Object result) {
                        getMessage(itemsPerPage, pageNo, listener);
                    }

                    @Override
                    public void onError(Object errorMessage) {
                        throwError(listener, errorMessage);
                    }
                });
            }
        } catch (Exception e) {
            AppUtils.throwException(NotificationManagerImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    private void postMessageForProcess(String url, MessageBody body) {
        notificationNetwork.postMessage(url, YonaApplication.getEventChangeManager().getSharedPreference().getYonaPassword(), body, null);
    }

    /**
     * Post message.
     *
     * @param url          the url
     * @param body         the body
     * @param itemsPerPage the items per page
     * @param pageNo       the page no
     * @param listener     the listener
     */
    public void postMessage(String url, MessageBody body, final int itemsPerPage, final int pageNo, final DataLoadListener listener) {
        try {
            if (!TextUtils.isEmpty(url)) {
                notificationNetwork.postMessage(url, YonaApplication.getEventChangeManager().getSharedPreference().getYonaPassword(), body, new DataLoadListener() {
                    @Override
                    public void onDataLoad(Object result) {
                        getMessage(itemsPerPage, pageNo, listener);
                    }

                    @Override
                    public void onError(Object errorMessage) {
                        throwError(listener, errorMessage);
                    }
                });
            }
        } catch (Exception e) {
            AppUtils.throwException(NotificationManagerImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    public void deleteMessage(@NonNull String url, final int itemsPerPage, final int pageNo, final DataLoadListener listener) {
        try {
            notificationNetwork.deleteMessage(url, YonaApplication.getEventChangeManager().getSharedPreference().getYonaPassword(), new DataLoadListener() {
                @Override
                public void onDataLoad(Object result) {
                    getMessage(itemsPerPage, pageNo, listener);
                }

                @Override
                public void onError(Object errorMessage) {
                    throwError(listener, errorMessage);
                }
            });
        } catch (Exception e) {
            AppUtils.throwException(NotificationManagerImpl.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    private void throwError(DataLoadListener listener, Object errorMessage) {
        if (listener != null) {
            if (errorMessage instanceof ErrorMessage) {
                listener.onError(errorMessage);
            } else {
                listener.onError(new ErrorMessage(errorMessage.toString()));
            }
        }
    }
}
