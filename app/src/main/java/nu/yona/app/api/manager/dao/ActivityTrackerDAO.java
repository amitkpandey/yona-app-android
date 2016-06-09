/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.manager.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nu.yona.app.api.db.DBConstant;
import nu.yona.app.api.model.Activity;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 08/06/16.
 */
public class ActivityTrackerDAO extends BaseDAO {

    /**
     * Instantiates a new Base dao.
     *
     * @param mOpenHelper the m open helper
     */
    public ActivityTrackerDAO(SQLiteOpenHelper mOpenHelper) {
        super(mOpenHelper);
    }

    /**
     * Save activities.
     *
     * @param activityList the activity list
     */
    public void saveActivities(List<Activity> activityList) {
        bulkInsert(DBConstant.TBL_ACTIVITY_TRACKER, activityList);
    }

    /**
     * Save activities.
     *
     * @param activity the activity
     */
    public void saveActivities(Activity activity) {
        insert(DBConstant.TBL_ACTIVITY_TRACKER, activity.getDbContentValues());
    }

    /**
     * Gets activities.
     *
     * @return the activities
     */
    public List<Activity> getActivities() {
        Cursor c = query(DBConstant.TBL_ACTIVITY_TRACKER);
        List<Activity> activityList = new ArrayList<>();
        try {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                do {
                    activityList.add(new Activity(c));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            AppUtils.throwException(GoalDAO.class.getSimpleName(), e, Thread.currentThread(), null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return activityList;
    }

    /**
     * Clear activities.
     */
    public void clearActivities() {
        delete(DBConstant.TBL_ACTIVITY_TRACKER, null, null);
    }
}
