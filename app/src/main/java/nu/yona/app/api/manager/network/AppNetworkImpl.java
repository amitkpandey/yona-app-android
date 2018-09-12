/*
 * Copyright (c) 2018 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.api.manager.network;

import java.util.Locale;

import nu.yona.app.api.model.AppMetaInfo;
import nu.yona.app.listener.DataLoadListenerImpl;

public class AppNetworkImpl extends BaseImpl
{
	/**
	 * post open app event from the device.
	 *
	 * @param url      the device password
	 * @param listener the listener
	 */
	public void postYonaOpenAppEvent(String url, String yonaPassword, DataLoadListenerImpl listener)
	{
		AppMetaInfo appMetaInfo = AppMetaInfo.getInstance();
		getRestApi().postOpenAppEvent(url, yonaPassword, Locale.getDefault().toString().replace('_', '-'), appMetaInfo).enqueue(createCallBack(listener));
	}
}
