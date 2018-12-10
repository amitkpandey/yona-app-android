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

public class ProfilePhoto extends BaseEntity
{

	@SerializedName("_links")
	@Expose
	private LinksProfilePhoto links;

	@Override
	public ContentValues getDbContentValues()
	{
		return null;
	}

	public LinksProfilePhoto getLinks()
	{
		return links;
	}

	public void setLinks(LinksProfilePhoto links)
	{
		this.links = links;
	}
}
