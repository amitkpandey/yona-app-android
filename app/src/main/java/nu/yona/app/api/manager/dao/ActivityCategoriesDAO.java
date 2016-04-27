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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import nu.yona.app.api.db.DBConstant;
import nu.yona.app.api.model.ActivityCategories;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 14/04/16.
 */
public class ActivityCategoriesDAO extends BaseDAO {

    private final String ID = "1";

    public ActivityCategoriesDAO(SQLiteOpenHelper mOpenHelper, Context context) {
        super(mOpenHelper, context);
    }

    /**
     * This method will clear all data from db and store new data in table.
     *
     * @param listener
     */
    public void saveActivityCategories(final ActivityCategories activityCategories, final DataLoadListener listener) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.ID, ID);
            values.put(DBConstant.SOURCE_OBJECT, serializer.serialize(activityCategories));
            if (getActivityCategories() == null) {
                insert(DBConstant.TBL_ACTIVITY_CATEGORIES, values);
            } else {
                update(DBConstant.TBL_ACTIVITY_CATEGORIES, values, DBConstant.ID + " = ?", new String[]{ID});
            }
            listener.onDataLoad(activityCategories);
        } catch (Exception e) {
            AppUtils.throwException(ActivityCategories.class.getSimpleName(), e, Thread.currentThread(), listener);
        }
    }

    public ActivityCategories getActivityCategories() {
        Cursor c = query(DBConstant.TBL_ACTIVITY_CATEGORIES, null, null, null, null, null);
        try {
            if (c != null && c.getCount() > 0) {

                if (c.moveToFirst()) {
                    return serializer.deserialize(c.getBlob(c.getColumnIndex(DBConstant.SOURCE_OBJECT)), ActivityCategories.class);
                }
            }
        } catch (Exception e) {
            AppUtils.throwException(ActivityCategories.class.getSimpleName(), e, Thread.currentThread(), null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }
}
