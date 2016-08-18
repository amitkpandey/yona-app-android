/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.model.YonaBuddy;
import nu.yona.app.api.model.YonaHeaderTheme;
import nu.yona.app.enums.IntentEnum;
import nu.yona.app.state.EventChangeListener;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.ViewPagerAdapter;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.TextDrawable;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public class DashboardFragment extends BaseFragment implements EventChangeListener {

    private TabLayout tabLayout;
    private YonaHeaderTheme mYonaHeaderTheme;
    private YonaBuddy yonaBuddy;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private String pageTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYonaHeaderTheme = (YonaHeaderTheme) getArguments().getSerializable(AppConstant.YONA_THEME_OBJ);
            pageTitle = mYonaHeaderTheme.getHeader_title();
            yonaBuddy = (YonaBuddy) getArguments().getSerializable(AppConstant.YONA_BUDDY_OBJ);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_fragment, null);
        YonaApplication.getEventChangeManager().registerListener(this);
        resetData();
        setupToolbar(view);
        if (mYonaHeaderTheme != null) {
            mToolBar.setBackgroundResource(mYonaHeaderTheme.getToolbar());
        }
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_NOTIFICATION_COUNT, null);
        setTitleAndIcon();
    }

    private void resetData() {
        if (mYonaHeaderTheme.isBuddyFlow()) {
            YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_CLEAR_ACTIVITY_LIST, null);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        resetData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        YonaApplication.getEventChangeManager().unRegisterListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        setTabs();
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        PerDayFragment perDayFragment = new PerDayFragment();
        perDayFragment.setArguments(getArguments());
        PerWeekFragment perWeekFragment = new PerWeekFragment();
        perWeekFragment.setArguments(getArguments());
        adapter.addFragment(perDayFragment, getString(R.string.perday));
        adapter.addFragment(perWeekFragment, getString(R.string.perweek));
        viewPager.setAdapter(adapter);
    }

    private void setTabs() {
        ViewGroup.LayoutParams mParams = tabLayout.getLayoutParams();
        mParams.height = getResources().getDimensionPixelSize(R.dimen.topTabBarHeight);
        tabLayout.setPadding(0, getResources().getDimensionPixelSize(R.dimen.ten), 0, 0);
        if (mYonaHeaderTheme != null) {
            tabLayout.setBackgroundResource(mYonaHeaderTheme.getHeadercolor());
            if (mYonaHeaderTheme.isBuddyFlow()) {
                tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.friends_deselected_tab), ContextCompat.getColor(getActivity(), R.color.friends_selected_tab));
            } else {
                tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.dashboard_deselected_tab), ContextCompat.getColor(getActivity(), R.color.dashboard_selected_tab));
            }
        }
        tabLayout.setLayoutParams(mParams);
    }

    private void setTitleAndIcon() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (YonaApplication.getEventChangeManager().getDataState().getUser() != null && !TextUtils.isEmpty(YonaApplication.getEventChangeManager().getDataState().getUser().getFirstName())) {
                    if (mYonaHeaderTheme != null) {
                        if (mYonaHeaderTheme.isBuddyFlow()) {
                            leftIcon.setVisibility(View.GONE);
                            rightIcon.setVisibility(View.GONE);
                            rightIconProfile.setVisibility(View.VISIBLE);
                            txtNotificationCounter.setVisibility(View.GONE);
                            rightIconProfile.setImageDrawable(TextDrawable.builder()
                                    .beginConfig().withBorder(AppConstant.PROFILE_ICON_BORDER_SIZE).endConfig()
                                    .buildRound(YonaActivity.getActivity(), yonaBuddy.getEmbedded().getYonaUser().getFirstName().substring(0, 1).toUpperCase(),
                                            ContextCompat.getColor(YonaActivity.getActivity(), R.color.mid_blue), YonaActivity.getActivity().getResources().getInteger(R.integer.profile_icon_circle_font_size)));
                            profileClickEvent(rightIconProfile);
                        } else {
                            leftIcon.setVisibility(View.VISIBLE);
                            leftIcon.setImageDrawable(TextDrawable.builder()
                                    .beginConfig().withBorder(AppConstant.PROFILE_ICON_BORDER_SIZE).endConfig()
                                    .buildRound(YonaActivity.getActivity(), YonaApplication.getEventChangeManager().getDataState().getUser().getFirstName().substring(0, 1).toUpperCase(),
                                            ContextCompat.getColor(YonaActivity.getActivity(), R.color.mid_blue), YonaActivity.getActivity().getResources().getInteger(R.integer.profile_icon_circle_font_size)));
                            rightIcon.setVisibility(View.VISIBLE);
                            rightIconProfile.setVisibility(View.GONE);
                            if (YonaApplication.getEventChangeManager().getDataState().getNotificaitonCount() > 0) {
                                txtNotificationCounter.setText("" + YonaApplication.getEventChangeManager().getDataState().getNotificaitonCount());
                                txtNotificationCounter.setVisibility(View.VISIBLE);
                            } else {
                                txtNotificationCounter.setVisibility(View.GONE);
                            }
                            rightIcon.setImageDrawable(ContextCompat.getDrawable(YonaActivity.getActivity(), R.drawable.icn_reminder));

                            rightIconClickEvent(rightIcon);
                            profileClickEvent(leftIcon);
                        }
                    }
                }
                toolbarTitle.setText(pageTitle);
                tabLayout.setVisibility(View.VISIBLE);
            }
        }, AppConstant.TIMER_DELAY_HUNDRED);

    }

    /**
     * Pass the view of profile icon for Me and buddies Profile
     *
     * @param profileView
     */
    private void profileClickEvent(View profileView) {
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntentEnum.ACTION_PROFILE.getActionString());
                intent.putExtra(AppConstant.YONA_THEME_OBJ, mYonaHeaderTheme);
                if (yonaBuddy != null) {
                    intent.putExtra(AppConstant.YONA_BUDDY_OBJ, yonaBuddy);
                } else {
                    intent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(false, null, null, 0, R.drawable.icn_reminder, getString(R.string.dashboard), R.color.grape, R.drawable.triangle_shadow_grape));
                    intent.putExtra(AppConstant.USER, YonaApplication.getEventChangeManager().getDataState().getUser());
                }
                YonaActivity.getActivity().replaceFragment(intent);
            }
        });
    }

    /**
     * To Show the Message Notification list and redirect to that view by click on notification icon
     *
     * @param rightIconView
     */
    private void rightIconClickEvent(View rightIconView) {
        txtNotificationCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendIntent = new Intent(IntentEnum.ACTION_MESSAGE.getActionString());
                YonaActivity.getActivity().replaceFragment(friendIntent);
            }
        });
        rightIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendIntent = new Intent(IntentEnum.ACTION_MESSAGE.getActionString());
                YonaActivity.getActivity().replaceFragment(friendIntent);
            }
        });
    }

    @Override
    public void onStateChange(int eventType, Object object) {
        switch (eventType) {
            case EventChangeManager.EVENT_UPDATE_NOTIFICATION_COUNT:
                setTitleAndIcon();
                break;
            default:
                break;
        }
    }
}
