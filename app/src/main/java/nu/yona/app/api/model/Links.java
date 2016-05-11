/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.api.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Links.
 */
public class Links extends BaseEntity {

    @SerializedName("self")
    @Expose
    private Href self;
    @SerializedName("edit")
    @Expose
    private Href edit;
    @SerializedName("yona:activityCategory")
    @Expose
    private Href yonaActivityCategory;
    @SerializedName("yona:messages")
    @Expose
    private Href yonaMessages;
    @SerializedName("yona:dailyActivityReports")
    @Expose
    private Href yonaDailyActivityReports;
    @SerializedName("yona:weeklyActivityReports")
    @Expose
    private Href yonaWeeklyActivityReports;
    @SerializedName("yona:newDeviceRequest")
    @Expose
    private Href yonaNewDeviceRequest;
    @SerializedName("yona:appActivity")
    @Expose
    private Href yonaAppActivity;
    @SerializedName("curies")
    @Expose
    private List<Cury> curies = new ArrayList<>();
    @SerializedName("yona:confirmMobileNumber")
    @Expose
    private Href yonaConfirmMobileNumber;
    @SerializedName("yona:resendMobileNumberConfirmationCode")
    @Expose
    private Href resendMobileNumberConfirmationCode;
    @SerializedName("yona:requestPinReset")
    @Expose
    private Href requestPinReset;
    @SerializedName("yona:clearPinReset")
    @Expose
    private Href clearPinReset;
    @SerializedName("yona:verifyPinReset")
    @Expose
    private Href verifyPinReset;
    @SerializedName("yona:user")
    @Expose
    private Href yonaUser;
    @SerializedName("yona:reject")
    @Expose
    private Href yonaReject;
    @SerializedName("yona:accept")
    @Expose
    private Href yonaAccept;

    /**
     * Gets self.
     *
     * @return The self
     */
    public Href getSelf() {
        return self;
    }

    /**
     * Sets self.
     *
     * @param self The self
     */
    public void setSelf(Href self) {
        this.self = self;
    }

    /**
     * Gets edit.
     *
     * @return The edit
     */
    public Href getEdit() {
        return edit;
    }

    /**
     * Sets edit.
     *
     * @param edit The edit
     */
    public void setEdit(Href edit) {
        this.edit = edit;
    }

    /**
     * Gets yona messages.
     *
     * @return The yonaMessages
     */
    public Href getYonaMessages() {
        return yonaMessages;
    }

    /**
     * Sets yona messages.
     *
     * @param yonaMessages The yona:messages
     */
    public void setYonaMessages(Href yonaMessages) {
        this.yonaMessages = yonaMessages;
    }

    /**
     * Gets yona daily activity reports.
     *
     * @return The yonaDailyActivityReports
     */
    public Href getYonaDailyActivityReports() {
        return yonaDailyActivityReports;
    }

    /**
     * Sets yona daily activity reports.
     *
     * @param yonaDailyActivityReports The yona:dailyActivityReports
     */
    public void setYonaDailyActivityReports(Href yonaDailyActivityReports) {
        this.yonaDailyActivityReports = yonaDailyActivityReports;
    }

    /**
     * Gets yona weekly activity reports.
     *
     * @return The yonaWeeklyActivityReports
     */
    public Href getYonaWeeklyActivityReports() {
        return yonaWeeklyActivityReports;
    }

    /**
     * Sets yona weekly activity reports.
     *
     * @param yonaWeeklyActivityReports The yona:weeklyActivityReports
     */
    public void setYonaWeeklyActivityReports(Href yonaWeeklyActivityReports) {
        this.yonaWeeklyActivityReports = yonaWeeklyActivityReports;
    }

    /**
     * Gets yona new device request.
     *
     * @return The yonaNewDeviceRequest
     */
    public Href getYonaNewDeviceRequest() {
        return yonaNewDeviceRequest;
    }

    /**
     * Sets yona new device request.
     *
     * @param yonaNewDeviceRequest The yona:newDeviceRequest
     */
    public void setYonaNewDeviceRequest(Href yonaNewDeviceRequest) {
        this.yonaNewDeviceRequest = yonaNewDeviceRequest;
    }

    /**
     * Gets yona app activity.
     *
     * @return The yonaAppActivity
     */
    public Href getYonaAppActivity() {
        return yonaAppActivity;
    }

    /**
     * Sets yona app activity.
     *
     * @param yonaAppActivity The yona:appActivity
     */
    public void setYonaAppActivity(Href yonaAppActivity) {
        this.yonaAppActivity = yonaAppActivity;
    }

    /**
     * Gets curies.
     *
     * @return The curies
     */
    public List<Cury> getCuries() {
        return curies;
    }

    /**
     * Sets curies.
     *
     * @param curies The curies
     */
    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

    /**
     * Gets yona confirm mobile number.
     *
     * @return the yona confirm mobile number
     */
    public Href getYonaConfirmMobileNumber() {
        return yonaConfirmMobileNumber;
    }

    /**
     * Sets yona confirm mobile number.
     *
     * @param yonaConfirmMobileNumber the yona confirm mobile number
     */
    public void setYonaConfirmMobileNumber(Href yonaConfirmMobileNumber) {
        this.yonaConfirmMobileNumber = yonaConfirmMobileNumber;
    }

    /**
     * Gets resend mobile number confirmation code.
     *
     * @return the resend mobile number confirmation code
     */
    public Href getResendMobileNumberConfirmationCode() {
        return resendMobileNumberConfirmationCode;
    }

    /**
     * Sets resend mobile number confirmation code.
     *
     * @param resendMobileNumberConfirmationCode the resend mobile number confirmation code
     */
    public void setResendMobileNumberConfirmationCode(Href resendMobileNumberConfirmationCode) {
        this.resendMobileNumberConfirmationCode = resendMobileNumberConfirmationCode;
    }

    /**
     * Gets request pin reset.
     *
     * @return the request pin reset
     */
    public Href getRequestPinReset() {
        return requestPinReset;
    }

    /**
     * Sets request pin reset.
     *
     * @param requestPinReset the request pin reset
     */
    public void setRequestPinReset(Href requestPinReset) {
        this.requestPinReset = requestPinReset;
    }

    /**
     * Gets verify pin reset.
     *
     * @return the verify pin reset
     */
    public Href getVerifyPinReset() {
        return verifyPinReset;
    }

    /**
     * Sets verify pin reset.
     *
     * @param verifyPinReset the verify pin reset
     */
    public void setVerifyPinReset(Href verifyPinReset) {
        this.verifyPinReset = verifyPinReset;
    }

    /**
     * Gets clear pin reset.
     *
     * @return the clear pin reset
     */
    public Href getClearPinReset() {
        return clearPinReset;
    }

    /**
     * Sets clear pin reset.
     *
     * @param clearPinReset the clear pin reset
     */
    public void setClearPinReset(Href clearPinReset) {
        this.clearPinReset = clearPinReset;
    }

    /**
     * Gets yona user.
     *
     * @return The yonaUser
     */
    public Href getYonaUser() {
        return yonaUser;
    }

    /**
     * Sets yona user.
     *
     * @param yonaUser The yona:user
     */
    public void setYonaUser(Href yonaUser) {
        this.yonaUser = yonaUser;
    }

    /**
     * Gets yona activity category.
     *
     * @return the yona activity category
     */
    public Href getYonaActivityCategory() {
        return yonaActivityCategory;
    }

    /**
     * Sets yona activity category.
     *
     * @param yonaActivityCategory the yona activity category
     */
    public void setYonaActivityCategory(Href yonaActivityCategory) {
        this.yonaActivityCategory = yonaActivityCategory;
    }

    @Override
    public ContentValues getDbContentValues() {
        return null;
    }

    /**
     * Gets yona reject.
     *
     * @return The yonaReject
     */
    public Href getYonaReject() {
        return yonaReject;
    }

    /**
     * Sets yona reject.
     *
     * @param yonaReject The yona:reject
     */
    public void setYonaReject(Href yonaReject) {
        this.yonaReject = yonaReject;
    }

    /**
     * Gets yona accept.
     *
     * @return The yonaAccept
     */
    public Href getYonaAccept() {
        return yonaAccept;
    }

    /**
     * Sets yona accept.
     *
     * @param yonaAccept The yona:accept
     */
    public void setYonaAccept(Href yonaAccept) {
        this.yonaAccept = yonaAccept;
    }

}
