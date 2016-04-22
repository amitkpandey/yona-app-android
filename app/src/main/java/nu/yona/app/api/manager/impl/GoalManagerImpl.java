/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.api.manager.impl;

import android.content.Context;

import nu.yona.app.YonaApplication;
import nu.yona.app.api.db.DatabaseHelper;
import nu.yona.app.api.manager.GoalManager;
import nu.yona.app.api.manager.dao.GoalDAO;
import nu.yona.app.api.manager.network.GoalNetworkImpl;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.Goals;
import nu.yona.app.api.model.PostBudgetYonaGoal;
import nu.yona.app.api.model.PostTimeZoneYonaGoal;
import nu.yona.app.api.model.YonaGoal;
import nu.yona.app.listener.DataLoadListener;

/**
 * Created by bhargavsuthar on 14/04/16.
 */
public class GoalManagerImpl implements GoalManager {


    private GoalNetworkImpl goalNetwork;
    private GoalDAO goalDAO;

    public GoalManagerImpl(Context context) {
        goalNetwork = new GoalNetworkImpl();
        goalDAO = new GoalDAO(DatabaseHelper.getInstance(context), context);
    }

    /**
     * Get the User's Goal from server and update into database
     *
     * @param listener
     */
    @Override
    public void getUserGoal(final DataLoadListener listener) {
        goalNetwork.getUserGoals(YonaApplication.getUser().getEmbedded().getYonaGoals().getLinks().getSelf().getHref(), new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                goalDAO.saveGoalData((Goals) result, listener);
                if (listener != null) {
                    listener.onDataLoad(result);
                }
            }

            @Override
            public void onError(Object errorMessage) {
                handleError(errorMessage, listener);
            }
        });
    }

    @Override
    public void postBudgetGoals(PostBudgetYonaGoal goal, final DataLoadListener listener) {
        goalNetwork.putUserBudgetGoals(YonaApplication.getUser().getEmbedded().getYonaGoals().getLinks().getSelf().getHref(), YonaApplication.getYonaPassword(), goal, new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                getUserGoal(listener);
            }

            @Override
            public void onError(Object errorMessage) {
                handleError(errorMessage, listener);
            }
        });
    }

    @Override
    public void postTimeZoneGoals(PostTimeZoneYonaGoal goal, final DataLoadListener listener) {
        goalNetwork.putUserTimeZoneGoals(YonaApplication.getUser().getEmbedded().getYonaGoals().getLinks().getSelf().getHref(), YonaApplication.getYonaPassword(), goal, new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                goalDAO.saveGoalData((Goals) result, listener);
                if (listener != null) {
                    listener.onDataLoad(result);
                }
            }

            @Override
            public void onError(Object errorMessage) {
                handleError(errorMessage, listener);
            }
        });
    }

    @Override
    public void deleteGoal(YonaGoal yonaGoal, final DataLoadListener listener) {
        goalNetwork.deleteGoal(yonaGoal.getLinks().getSelf().getHref(), YonaApplication.getYonaPassword(), new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                getUserGoal(listener);
            }

            @Override
            public void onError(Object errorMessage) {
                handleError(errorMessage, listener);
            }
        });
    }

    /**
     * Get Goals from Database
     *
     * @return
     */
    public Goals getUserGoalFromDb() {
        return goalDAO.getUserGoal();
    }

    private void handleError(Object errorMessage, DataLoadListener listener) {
        if (listener == null) {
            return;
        }
        if (errorMessage instanceof ErrorMessage) {
            listener.onError(errorMessage);
        } else {
            listener.onError(new ErrorMessage(errorMessage.toString()));
        }
    }
}
