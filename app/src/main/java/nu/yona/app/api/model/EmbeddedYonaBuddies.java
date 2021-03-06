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
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Embedded yona buddies.
 */
public class EmbeddedYonaBuddies extends BaseEntity
{

	@SerializedName("yona:buddies")
	@Expose
	private List<YonaBuddy> yonaBuddies = new ArrayList<>();

	/**
	 * Gets yona buddies.
	 *
	 * @return The yonaBuddies
	 */
	public List<YonaBuddy> getYonaBuddies()
	{
		return yonaBuddies;
	}

	/**
	 * Sets yona buddies.
	 *
	 * @param yonaBuddies The yona:buddies
	 */
	public void setYonaBuddies(List<YonaBuddy> yonaBuddies)
	{
		this.yonaBuddies = yonaBuddies;
	}

	@Override
	public ContentValues getDbContentValues()
	{
		return null;
	}
}
