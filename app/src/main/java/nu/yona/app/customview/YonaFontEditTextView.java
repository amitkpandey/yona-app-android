/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by bhargavsuthar on 3/31/16.
 */
public class YonaFontEditTextView extends EditText {

    public YonaFontEditTextView(Context context) {
        super(context);

        YonaFontUtils.applyCustomFont(this, context, null);
    }

    public YonaFontEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        YonaFontUtils.applyCustomFont(this, context, attrs);
    }

    public YonaFontEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        YonaFontUtils.applyCustomFont(this, context, attrs);
    }
}
