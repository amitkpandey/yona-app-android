/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.api.model;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Author @MobiquityInc.
 */
public abstract class BaseEntity implements Cloneable, Serializable {

    private static final long serialVersionUID = 4243329623573859700L;

    BaseEntity() {

    }

    public abstract ContentValues getDbContentValues();
}
