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

/**
 * Created by kinnarvasa on 28/04/16.
 */
public class Buddy extends BaseEntity
{
	@SerializedName("_embedded")
	@Expose
	private Embedded Embedded;
	@SerializedName("_links")
	@Expose
	private Links Links;

	/**
	 * Gets embedded.
	 *
	 * @return The Embedded
	 */
	public Embedded getEmbedded()
	{
		return Embedded;
	}

	/**
	 * Sets embedded.
	 *
	 * @param Embedded The _embedded
	 */
	public void setEmbedded(Embedded Embedded)
	{
		this.Embedded = Embedded;
	}

	/**
	 * Gets links.
	 *
	 * @return The Links
	 */
	public Links getLinks()
	{
		return Links;
	}

	/**
	 * Sets links.
	 *
	 * @param Links The _links
	 */
	public void setLinks(Links Links)
	{
		this.Links = Links;
	}

	@Override
	public ContentValues getDbContentValues()
	{
		return null;
	}
}
