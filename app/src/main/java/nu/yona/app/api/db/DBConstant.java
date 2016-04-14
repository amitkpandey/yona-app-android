/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.db;

/**
 * Created by kinnarvasa on 28/03/16.
 */
public interface DBConstant {
    String DATABASE_NAME = "yonaDB";
    int DATABASE_VERSION = 1;

    /**
     * DB TABLES
     **/
    String TBL_USER_DATA = "userData";
    String TBL_ACTIVITY_CATEGORIES = "activityCategories";
    /** DB TABLES **/

    /**
     * DB Fields
     **/
    String ID = "Id";
    String SOURCE_OBJECT = "sourceObject";
    /** DB Fields **/
}
