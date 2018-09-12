/*
 * Copyright (c) 2018 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.api.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;

/**
 * Created by kinnarvasa on 09/05/16.
 */
public class Properties extends BaseEntity
{

	@Expose
	private String message = null;

	/**
	 * Instantiates a new Properties.
	 */
	public Properties()
	{

	}

	@Override
	public ContentValues getDbContentValues()
	{
		return null;
	}

	public String getMessage()
	{
		return this.message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
