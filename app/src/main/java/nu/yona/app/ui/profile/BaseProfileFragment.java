/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.amulyakhare.textdrawable.TextDrawable;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;

/**
 * Created by kinnarvasa on 10/05/16.
 */
public class BaseProfileFragment extends BaseFragment {
    private final int ALPHA = 50;
    /**
     * The Activity.
     */
    protected YonaActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (YonaActivity) getActivity();
    }

    /**
     * Gets image.
     *
     * @param withAlpha the with alpha
     * @return the image
     */
    protected Drawable getImage(boolean withAlpha) {
        if (false) {// TODO: 10/05/16 When server provides user profile image, we need to check and enable if part on base of that.
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.profile);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), src);
            drawable.setCornerRadius(Math.max(src.getWidth(), src.getHeight()));
            drawable.setAlpha(ALPHA);
            return drawable;
        } else {
            TextDrawable image = TextDrawable.builder().beginConfig().withBorder(AppConstant.PROFILE_IMAGE_BORDER_SIZE)
                    .textColor(Color.WHITE).endConfig()
                    .buildRound(YonaApplication.getUser().getFirstName().substring(0, 1).toUpperCase(),
                            ContextCompat.getColor(activity, R.color.mid_blue));
            if (withAlpha) {
                image.setAlpha(ALPHA);
            }
            return image;
        }
    }
}
