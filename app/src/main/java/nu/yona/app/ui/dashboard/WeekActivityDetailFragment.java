/*
 *  Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 *
 */

package nu.yona.app.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.model.EmbeddedYonaActivity;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.WeekActivity;
import nu.yona.app.api.model.YonaHeaderTheme;
import nu.yona.app.customview.YonaFontTextView;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 13/06/16.
 */
public class WeekActivityDetailFragment extends BaseFragment {

    private CustomPageAdapter customPageAdapter;
    private ViewPager viewPager;
    private WeekActivity activity;
    private View view;
    private ImageView previousItem, nextItem;
    private YonaFontTextView dateTitle;
    private List<WeekActivity> weekActivityList;
    private YonaHeaderTheme mYonaHeaderTheme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYonaHeaderTheme = (YonaHeaderTheme) getArguments().getSerializable(AppConstant.YONA_THEME_OBJ);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_pager_fragment, null);

        setupToolbar(view);
        if (mYonaHeaderTheme != null) {
            mToolBar.setBackgroundResource(mYonaHeaderTheme.getToolbar());
        }

        previousItem = (ImageView) view.findViewById(R.id.previous);
        nextItem = (ImageView) view.findViewById(R.id.next);
        dateTitle = (YonaFontTextView) view.findViewById(R.id.date);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        customPageAdapter = new CustomPageAdapter(getActivity());
        viewPager.setAdapter(customPageAdapter);
        if (getArguments() != null) {
            if (getArguments().get(AppConstant.OBJECT) != null) {
                activity = (WeekActivity) getArguments().get(AppConstant.OBJECT);
            }
        }
        previousItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });
        nextItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != weekActivityList.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fetchComments(position);
                updateFlow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        weekActivityList = new ArrayList<>();
        EmbeddedYonaActivity embeddedYonaActivity = YonaApplication.getEventChangeManager().getDataState().getEmbeddedWeekActivity();
        if (embeddedYonaActivity != null && embeddedYonaActivity.getWeekActivityList() != null && embeddedYonaActivity.getWeekActivityList().size() > 0) {

            for (int i = embeddedYonaActivity.getWeekActivityList().size() - 1; i >= 0; i--) {
                try {
                    if (embeddedYonaActivity.getWeekActivityList().get(i).getYonaGoal().getLinks().getSelf().getHref().equals(activity.getLinks().getYonaGoal().getHref())) {
                        weekActivityList.add(embeddedYonaActivity.getWeekActivityList().get(i));
                    }
                } catch (Exception e) {
                    AppUtils.throwException(WeekActivityDetailFragment.class.getSimpleName(), e, Thread.currentThread(), null);
                }
            }
            customPageAdapter.notifyDataSetChanged(weekActivityList);
            fetchComments(weekActivityList.indexOf(activity));
            viewPager.setCurrentItem(weekActivityList.indexOf(activity));
            updateFlow(weekActivityList.indexOf(activity));
        } else {
            YonaActivity.getActivity().onBackPressed();
        }

        setTitleAndIcon();
    }

    private void setTitleAndIcon() {
        if (activity != null && activity.getYonaGoal() != null && !TextUtils.isEmpty(activity.getYonaGoal().getActivityCategoryName())) {
            toolbarTitle.setText(activity.getYonaGoal().getActivityCategoryName().toUpperCase());
        }
        leftIcon.setVisibility(View.GONE);
        rightIcon.setVisibility(View.GONE);
    }

    private void updateFlow(int position) {
        dateTitle.setText(weekActivityList.get(position).getStickyTitle());
        if (position == 0) {
            previousItem.setVisibility(View.INVISIBLE);
        } else {
            previousItem.setVisibility(View.VISIBLE);
        }
        if (position == weekActivityList.size() - 1) {
            nextItem.setVisibility(View.INVISIBLE);
        } else {
            nextItem.setVisibility(View.VISIBLE);
        }
    }

    private void fetchComments(int position) {
        APIManager.getInstance().getActivityManager().getCommentsForWeek(weekActivityList, position, new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                if (result instanceof List<?>) {
                    weekActivityList = (List<WeekActivity>) result;
                    customPageAdapter.notifyDataSetChanged(weekActivityList);
                }
            }

            @Override
            public void onError(Object errorMessage) {
                if (errorMessage instanceof ErrorMessage) {
                    YonaActivity.getActivity().showError((ErrorMessage) errorMessage);
                } else {
                    YonaActivity.getActivity().showError(new ErrorMessage(getString(R.string.no_data_found)));
                }
            }
        });
    }
}
