/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.ActivityCategoryManager;
import nu.yona.app.api.manager.ChallengesManager;
import nu.yona.app.api.manager.GoalManager;
import nu.yona.app.api.manager.impl.ChallengesManagerImpl;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.YonaActivityCategories;
import nu.yona.app.api.model.YonaGoal;
import nu.yona.app.customview.CustomAlertDialog;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.enums.ChallengesEnum;
import nu.yona.app.enums.GoalsEnum;
import nu.yona.app.enums.IntentEnum;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;

/**
 * Created by bhargavsuthar on 13/04/16.
 */
public class BaseGoalCreateFragment extends BaseFragment {

    /**
     * The Challenges manager.
     */
    ChallengesManager challengesManager;
    /**
     * The M goal list view.
     */
    ListView mGoalListView;
    private ListView mGoalCreationListView;
    /**
     * The Btn goal add.
     */
    ImageButton btnGoalAdd;
    /**
     * The M desc tab.
     */
    YonaFontTextView mDescTab;
    /**
     * The Budget categories goal list.
     */
    protected List<YonaGoal> budgetCategoriesGoalList;
    /**
     * The Time zone categories goal list.
     */
    protected List<YonaGoal> timeZoneCategoriesGoalList;
    /**
     * The No go categories goal list.
     */
    protected List<YonaGoal> noGoCategoriesGoalList;
    /**
     * The M yona activity categories list.
     */
    protected List<YonaActivityCategories> mYonaActivityCategoriesList;
    /**
     * The M goal categories map.
     */
    protected HashMap<String, String> mGoalCategoriesMap;
    private GoalManager goalManager;
    private ActivityCategoryManager activityCategoryManager;
    private YonaActivity activity;
    private int CURRENT_TAB;
    /**
     * The Item click listener.
     */
    final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            handleClickEvent(parent.getAdapter().getItem(position));
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_creation_layout, null);

        activity = (YonaActivity) getActivity();
        challengesManager = new ChallengesManagerImpl(activity);
        mGoalListView = (ListView) view.findViewById(R.id.goal_listview);
        mGoalCreationListView = (ListView) view.findViewById(R.id.new_goal_listview);
        GoalCategoryListAdapter categoryGoalListAdapter = new GoalCategoryListAdapter(activity, challengesManager.getListOfCategories());
        mGoalCreationListView.setAdapter(categoryGoalListAdapter);
        btnGoalAdd = (ImageButton) view.findViewById(R.id.img_add_goal);
        mDescTab = (YonaFontTextView) view.findViewById(R.id.txt_header_text);
        mGoalCreationListView.setOnItemClickListener(itemClickListener);
        return view;
    }

    /**
     * Show current goal list view.
     *
     * @param tab the tab
     */
    synchronized void showCurrentGoalListView(int tab) {
        btnGoalAdd.setVisibility(View.VISIBLE);
        mGoalListView.setVisibility(View.VISIBLE);
        mGoalCreationListView.setVisibility(View.GONE);
        CURRENT_TAB = tab;
    }

    /**
     * Show new list of goal view.
     *
     * @param tab the tab
     */
    synchronized void showNewListOfGoalView(int tab) {
        btnGoalAdd.setVisibility(View.GONE);
        mGoalListView.setVisibility(View.GONE);
        mGoalCreationListView.setVisibility(View.VISIBLE);
        CURRENT_TAB = tab;
    }

    /**
     * It will check the visibility of child View
     *
     * @return the boolean
     */
    public boolean checkIsChildViewVisible() {
        return mGoalCreationListView.getVisibility() == View.VISIBLE;
    }

    /**
     * onBackpressed of button it will call from main activity
     */
    public void onBackPressedView() {
        showCurrentGoalListView(CURRENT_TAB);
    }

    private void handleClickEvent(Object object) {
        Intent goalIntent = new Intent(IntentEnum.ACTION_CHALLENGES_GOAL.getActionString());
        if (object != null) {

            if (object instanceof YonaGoal) {
                goalIntent.putExtra(AppConstant.GOAL_OBJECT, (YonaGoal) object);
            } else if (object instanceof YonaActivityCategories) {
                YonaGoal yonaGoal = challengesManager.getYonaGoalByCategoryType((YonaActivityCategories) object);
                if (yonaGoal == null) {
                    goalIntent.putExtra(AppConstant.GOAL_OBJECT, (YonaActivityCategories) object);
                } else {
                    CustomAlertDialog.show(getActivity(), "You already Added this category.", "Ok");
                    return;
                    //goalIntent.putExtra(AppConstant.GOAL_OBJECT, yonaGoal);
                }
            }
        }
        switch (ChallengesEnum.getEnum(CURRENT_TAB)) {
            case CREDIT_TAB:
                goalIntent.putExtra(AppConstant.NEW_GOAL_TYPE, GoalsEnum.BUDGET_GOAL.getActionString());
                activity.replaceFragment(goalIntent);
                break;
            case ZONE_TAB:
                goalIntent.putExtra(AppConstant.NEW_GOAL_TYPE, GoalsEnum.TIME_ZONE_GOAL.getActionString());
                activity.replaceFragment(goalIntent);
                break;
            case NO_GO_TAB:
                if (object instanceof YonaActivityCategories) {
                    addNoGoChallange(object);
                } else if (object instanceof YonaGoal) {
                    activity.replaceFragment(goalIntent);
                }
                break;
            default:
                break;
        }
    }

    private void addNoGoChallange(final Object object) {

        YonaActivityCategories categories = (YonaActivityCategories) object;
        activity.showLoadingView(true, null);
        challengesManager.postBudgetGoals(0, categories, new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                activity.showLoadingView(false, null);
                showCurrentGoalListView(CURRENT_TAB);
                YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_UPDATE_GOALS, result);
            }

            @Override
            public void onError(Object errorMessage) {
                activity.showLoadingView(false, null);
                String message;
                if (errorMessage instanceof ErrorMessage) {
                    message = ((ErrorMessage) errorMessage).getMessage();
                } else {
                    message = errorMessage.toString();
                }
                CustomAlertDialog.show(activity, message, getString(R.string.ok));
            }
        });
    }
}
