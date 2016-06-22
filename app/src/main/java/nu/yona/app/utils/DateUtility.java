/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;

/**
 * Created by bhargavsuthar on 10/05/16.
 */
public class DateUtility {

    public static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("EEE");
    public static SimpleDateFormat DAY_NO_FORMAT = new SimpleDateFormat("d");
    public static SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("yyyy-'W'ww");
    private static final int mNoOfDayPerWeek = 7;


    /**
     * Gets relative date.
     *
     * @param future the future
     * @return the relative date
     */
    public static String getRelativeDate(Calendar future) {

        String relativeDate = "";

        long days = getDateDiff(future.getTime(), Calendar.getInstance().getTime(), TimeUnit.DAYS);

        if (days == 0) {
            relativeDate = YonaApplication.getAppContext().getString(R.string.today);
        } else if (days < 2) {
            relativeDate = YonaApplication.getAppContext().getString(R.string.yesterday);
        } else {
            try {
                Date date = new Date(future.getTimeInMillis());
                Calendar futureCalendar = Calendar.getInstance();
                futureCalendar.setTime(date);
                relativeDate = new SimpleDateFormat("EEEE, d MMM").format(future.getTime());

            } catch (Exception e) {
                Log.e(DateUtility.class.getName(), "Date Format exception: " + e);
            }
        }

        return relativeDate.toUpperCase();
    }

    /**
     * Gets retrive week.
     *
     * @param week the week
     * @return the retrive week
     * @throws ParseException the parse exception
     */
    public static String getRetriveWeek(String week) throws ParseException {
        String retriveWeek = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1);
        if (WEEK_FORMAT.format(new Date()).equals(week)) {
            retriveWeek = YonaApplication.getAppContext().getString(R.string.this_week);
        } else if (WEEK_FORMAT.format(calendar.getTime()).equals(week)) {
            retriveWeek = YonaApplication.getAppContext().getString(R.string.last_week);
        } else {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            Date date = WEEK_FORMAT.parse(week);
            calendar.setTime(date);
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 6);
            Date endDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
            retriveWeek = sdf.format(startDate) + " - " + sdf.format(endDate);
        }
        return retriveWeek.toUpperCase();
    }

    /**
     * Gets date diff.
     *
     * @param date1    the date 1
     * @param date2    the date 2
     * @param timeUnit the time unit
     * @return the date diff
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets long format date.
     *
     * @param date the date
     * @return the long format date
     */
    public static String getLongFormatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstant.YONA_LONG_DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Get Current weeks Days List. example:  Sun 21, Mon 22, Tue 23 , Wed 24 ,Thu 25, Fri 26
     *
     * @return
     */
    public static Map<String, String> getWeekDay(String currentYearWeek) {
        LinkedHashMap<String, String> listOfdates = new LinkedHashMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(WEEK_FORMAT.parse(currentYearWeek));// all done
            int delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK) + 1;
            calendar.add(Calendar.DAY_OF_MONTH, delta);
            for (int i = 0; i < mNoOfDayPerWeek; i++) {
                listOfdates.put(DAY_FORMAT.format(calendar.getTime()), DAY_NO_FORMAT.format(calendar.getTime()));
                Log.i("Adding", DAY_NO_FORMAT.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            Log.e(DateUtility.class.getName(), "Date Format exception: " + e);
        }

        return listOfdates;

    }

}
