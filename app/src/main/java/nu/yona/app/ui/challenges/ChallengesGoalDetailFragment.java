/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.challenges;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nu.yona.app.R;
import nu.yona.app.api.manager.impl.GoalManagerImpl;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.YonaGoal;
import nu.yona.app.api.utils.ServerErrorCode;
import nu.yona.app.customview.CustomAlertDialog;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.enums.GoalsEnum;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;

/**
 * Created by bhargavsuthar on 20/04/16.
 */
public class ChallengesGoalDetailFragment extends BaseFragment {

    private ImageView mHGoalTypeImg;
    private YonaFontTextView mHTxtGoalTitle, mHTxtGoalSubscribe, mFTxtGoalTitle, mFTxtGoalSubscribe;
    private View budgetGoalView, timezoneGoalView;
    private YonaGoal mYonaGoal;
    private ImageView rightIcon;
    private YonaActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYonaGoal = (YonaGoal) getArguments().getSerializable(AppConstant.GOAL_OBJECT);
        }
        activity = (YonaActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_detail_layout, null);

        mHGoalTypeImg = (ImageView) view.findViewById(R.id.img_bucket);
        mHTxtGoalTitle = (YonaFontTextView) view.findViewById(R.id.goal_challenge_type_title);
        mHTxtGoalSubscribe = (YonaFontTextView) view.findViewById(R.id.goal_challenge_type_subscribeTxt);
        mFTxtGoalTitle = (YonaFontTextView) view.findViewById(R.id.challenges_goal_footer_title);
        mFTxtGoalSubscribe = (YonaFontTextView) view.findViewById(R.id.challenges_goal_footer_subscribeTxt);
        timezoneGoalView = view.findViewById(R.id.timezoneView);
        budgetGoalView = view.findViewById(R.id.goal_item_layout);
        rightIcon = (ImageView) activity.findViewById(R.id.rightIcon);

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteGoal();
            }
        });

        if (mYonaGoal != null) {
            if(mYonaGoal.getLinks().getEdit() != null && !TextUtils.isEmpty(mYonaGoal.getLinks().getEdit().getHref())){
                rightIcon.setVisibility(View.VISIBLE);
            } else {
                rightIcon.setVisibility(View.GONE);
            }
            mHTxtGoalTitle.setText(mYonaGoal.getActivityCategoryName());
            if (mYonaGoal.getType().equalsIgnoreCase(GoalsEnum.BUDGET_GOAL.getActionString()) && mYonaGoal.getMaxDurationMinutes() > 0) {
                mHTxtGoalSubscribe.setText(getString(R.string.budgetgoalheadersubtext, mYonaGoal.getActivityCategoryName()));
                timezoneGoalView.setVisibility(View.GONE);
                budgetGoalView.setVisibility(View.VISIBLE);
                ((YonaFontTextView) view.findViewById(R.id.goal_minutes_num)).setText(String.valueOf(mYonaGoal.getMaxDurationMinutes()));
                mHGoalTypeImg.setImageResource(R.drawable.icn_challenge_timezone);
            } else if (mYonaGoal.getType().equalsIgnoreCase(GoalsEnum.TIME_ZONE_GOAL.getActionString()) && mYonaGoal.getZones() != null) {
                mHTxtGoalSubscribe.setText(getString(R.string.timezonegoalheadersubtext, mYonaGoal.getActivityCategoryName()));
                timezoneGoalView.setVisibility(View.VISIBLE);
                budgetGoalView.setVisibility(View.GONE);
                mHGoalTypeImg.setImageResource(R.drawable.icn_challenge_timebucket);
                RecyclerView timeZoneGoalView = (RecyclerView) view.findViewById(R.id.listView);
                ((YonaFontTextView) view.findViewById(R.id.txt_header_text)).setText(getString(R.string.timezone));

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                timeZoneGoalView.setLayoutManager(layoutManager);

                TimeZoneGoalsAdapter timeZoneGoalsAdapter = new TimeZoneGoalsAdapter(mYonaGoal.getZones(), null);
                timeZoneGoalView.setAdapter(timeZoneGoalsAdapter);
            } else {
                //todo- for nogo
                mHGoalTypeImg.setImageResource(R.drawable.icn_challenge_nogo);
            }
        }

        return view;
    }

    private void doDeleteGoal() {
        activity.showLoadingView(true, null);
        new GoalManagerImpl(getActivity()).deleteGoal(mYonaGoal, new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                activity.showLoadingView(false, null);
                //do back
            }

            @Override
            public void onError(Object errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void showError(Object errorMessage) {
        ErrorMessage message = (ErrorMessage) errorMessage;
        activity.showLoadingView(false, null);
        CustomAlertDialog.show(activity, message.getMessage(), getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }
}
