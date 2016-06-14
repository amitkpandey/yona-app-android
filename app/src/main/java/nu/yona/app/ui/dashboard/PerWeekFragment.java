/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.List;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.model.EmbeddedYonaActivity;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.WeekActivity;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.recyclerViewDecor.DividerDecoration;
import nu.yona.app.state.EventChangeManager;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public class PerWeekFragment extends BaseFragment {


    private RecyclerView listView;
    private PerWeekStickyAdapter perWeekStickyAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsLoading = false;

    /**
     * Recyclerview's scroll listener when its getting end to load more data till the pages not reached
     */
    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            try {
                if (dy > 0) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                    EmbeddedYonaActivity embeddedYonaActivity = YonaApplication.getEventChangeManager().getDataState().getEmbeddedWeekActivity();
                    if (!mIsLoading &&
                            embeddedYonaActivity != null && embeddedYonaActivity.getPage() != null
                            && embeddedYonaActivity.getPage().getNumber() < embeddedYonaActivity.getPage().getTotalPages()
                            && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        loadMoreItems();
                    }
                }
            } catch (Exception e) {
                Log.e(PerWeekFragment.class.getSimpleName(), e.getMessage());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_perweek_fragment, null);

        listView = (RecyclerView) view.findViewById(R.id.listView);
        mLayoutManager = new LinearLayoutManager(YonaActivity.getActivity());

        perWeekStickyAdapter = new PerWeekStickyAdapter(new ArrayList<WeekActivity>(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO on click of list item, listener will return here.
            }
        });

        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(perWeekStickyAdapter);
        listView.addOnScrollListener(mRecyclerViewOnScrollListener);
        setRecyclerHeaderAdapterUpdate(new StickyRecyclerHeadersDecoration(perWeekStickyAdapter));
        refreshAdapter();
        return view;

    }

    /**
     * update RecyclerView item header for grouping section
     *
     * @param headerDecor
     */
    private void setRecyclerHeaderAdapterUpdate(StickyRecyclerHeadersDecoration headerDecor) {
        listView.addItemDecoration(headerDecor);

        // Add decoration for dividers between list items
        listView.addItemDecoration(new DividerDecoration(getActivity()));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(listView, headerDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                    }
                });
    }

    /**
     * Refresh recyclerview's adapter
     */
    private void refreshAdapter() {
        perWeekStickyAdapter.clear();
        getWeekActivity(false);
    }

    /**
     * load more items
     */
    private void loadMoreItems() {
        mIsLoading = true;
        getWeekActivity(true);
    }

    /**
     * to get the list of user's messages
     */
    private void getWeekActivity(boolean loadMore) {
        if(YonaActivity.getActivity().isToDisplayLogin()) {
            YonaApplication.getEventChangeManager().notifyChange(EventChangeManager.EVENT_CLEAR_ACTIVITY_LIST, null);
        }
        final EmbeddedYonaActivity embeddedYonaActivity = YonaApplication.getEventChangeManager().getDataState().getEmbeddedWeekActivity();
        if ((embeddedYonaActivity == null || embeddedYonaActivity.getPage() == null)
                || (embeddedYonaActivity != null && embeddedYonaActivity.getPage() != null && embeddedYonaActivity.getPage().getNumber() < embeddedYonaActivity.getPage().getTotalPages())) {
            YonaActivity.getActivity().showLoadingView(true, null);
            APIManager.getInstance().getActivityManager().getWeeksActivity(loadMore, new DataLoadListener() {
                @Override
                public void onDataLoad(Object result) {
                    YonaActivity.getActivity().showLoadingView(false, null);
                    if (isAdded() && result instanceof EmbeddedYonaActivity) {
                        EmbeddedYonaActivity yonaActivity = (EmbeddedYonaActivity) result;
                        if (yonaActivity.getEmbedded() != null && yonaActivity.getDayActivityList() != null) {
                            showData(yonaActivity.getWeekActivityList());
                        } else {
                            YonaActivity.getActivity().showError(new ErrorMessage(getString(R.string.no_data_found)));
                        }
                    } else if (result instanceof List<?>) {
                        showData((List<WeekActivity>) result);
                    }
                    mIsLoading = false;
                }

                @Override
                public void onError(Object errorMessage) {
                    YonaActivity.getActivity().showLoadingView(false, null);
                    YonaActivity.getActivity().showError((ErrorMessage) errorMessage);
                }
            });
        } else {
            YonaActivity.getActivity().showError(new ErrorMessage(getString(R.string.no_data_found)));
        }
    }

    private void showData(List<WeekActivity> weekActivityList) {
        if (mIsLoading) {
            perWeekStickyAdapter.updateData(weekActivityList);
        } else {
            perWeekStickyAdapter.notifyDataSetChange(weekActivityList);
        }
    }
}
