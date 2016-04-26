/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import nu.yona.app.R;
import nu.yona.app.api.manager.impl.AuthenticateManagerImpl;
import nu.yona.app.api.manager.impl.DeviceManagerImpl;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.customview.CustomAlertDialog;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.LaunchActivity;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public class SettingsFragment extends BaseFragment {
    private View view;
    private ListView listView;
    private DeviceManagerImpl deviceManager;
    private YonaActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, null);

        listView = (ListView) view.findViewById(R.id.list_view);
        deviceManager = new DeviceManagerImpl(getActivity());

        activity = (YonaActivity) getActivity();

        listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.settings_list_item, new String[]{getString(R.string.change_pin), getString(R.string.privacy), getString(R.string.add_device), getString(R.string.delete_user)}));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((YonaFontTextView) view).getText().toString().equals(getString(R.string.change_pin))) {
                    //Load change pin screen
                } else if (((YonaFontTextView) view).getText().toString().equals(getString(R.string.privacy))) {
                    //Load privacy screen
                } else if (((YonaFontTextView) view).getText().toString().equals(getString(R.string.add_device))) {
                    activity.showLoadingView(true, null);
                    addDevice(AppUtils.getRandomString(AppConstant.ADD_DEVICE_PASSWORD_CHAR_LIMIT));
                } else if (((YonaFontTextView) view).getText().toString().equals(getString(R.string.delete_user))) {
                    unsubscribeUser();
                }
            }
        });

        return view;
    }

    private void unsubscribeUser() {
        CustomAlertDialog.show(activity, getString(R.string.delete_user), getString(R.string.delete_user_message),
                getString(R.string.ok), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doUnsubscribe();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void doUnsubscribe() {
        activity.showLoadingView(true, null);
        new AuthenticateManagerImpl(activity).deleteUser(new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                activity.showLoadingView(false, null);
                startActivity(new Intent(activity, LaunchActivity.class));
            }

            @Override
            public void onError(Object errorMessage) {
                CustomAlertDialog.show(activity, ((ErrorMessage) errorMessage).getMessage(), getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void addDevice(final String pin) {
        try {
            deviceManager.addDevice(pin, new DataLoadListener() {
                @Override
                public void onDataLoad(Object result) {
                    showAlert(pin + getString(R.string.yona_add_device_message), true);
                }

                @Override
                public void onError(Object errorMessage) {
                    showAlert(((ErrorMessage) errorMessage).getMessage(), false);
                }
            });
        } catch (Exception e) {
            showAlert(e.toString(), false);
        }
    }

    private void showAlert(String message, final boolean doDelete) {
        activity.showLoadingView(false, null);
        CustomAlertDialog.show(activity,
                message,
                getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (doDelete) {
                            doDeleteDeviceRequest();
                        }
                        dialogInterface.dismiss();
                    }
                });
    }

    private void doDeleteDeviceRequest() {
        try {
            deviceManager.deleteDevice(new DataLoadListener() {
                @Override
                public void onDataLoad(Object result) {
                    //do nothing if server response success
                }

                @Override
                public void onError(Object errorMessage) {
                    showAlert(((ErrorMessage) errorMessage).getMessage(), false);
                }
            });
        } catch (Exception e) {
            showAlert(e.toString(), false);
        }
    }
}
