/*
 * Copyright (c) 2016 Stichting Yona Foundation
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
 * Created by bhargavsuthar on 14/04/16.
 */
public class YonaActivityCategories extends BaseEntity {

    @SerializedName("name")
    @Expose
    private
    String name;

    @SerializedName("mandatoryNoGo")
    @Expose
    private
    boolean mandatoryNoGo;

    @SerializedName("applications")
    @Expose
    private List<String> applications = new ArrayList<>();

    @SerializedName("_links")
    @Expose
    private Links _links;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Is mandatory no go boolean.
     *
     * @return the boolean
     */
    public boolean isMandatoryNoGo() {
        return mandatoryNoGo;
    }

    /**
     * Sets mandatory no go.
     *
     * @param mandatoryNoGo the mandatory no go
     */
    public void setMandatoryNoGo(boolean mandatoryNoGo) {
        this.mandatoryNoGo = mandatoryNoGo;
    }

    /**
     * Gets applications.
     *
     * @return the applications
     */
    public List<String> getApplications() {
        return applications;
    }

    /**
     * Sets applications.
     *
     * @param applications the applications
     */
    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    /**
     * Gets links.
     *
     * @return the links
     */
    public Links get_links() {
        return _links;
    }

    /**
     * Sets links.
     *
     * @param _links the links
     */
    public void set_links(Links _links) {
        this._links = _links;
    }

    @Override
    public ContentValues getDbContentValues() {
        return null;
    }


}
