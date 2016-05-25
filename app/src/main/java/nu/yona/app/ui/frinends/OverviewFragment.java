/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.frinends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.List;

import nu.yona.app.R;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.YonaBuddies;
import nu.yona.app.api.model.YonaBuddy;
import nu.yona.app.enums.IntentEnum;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.recyclerViewDecor.DividerDecoration;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public class OverviewFragment extends BaseFragment {

    private List<YonaBuddy> mListBuddy;
    private OverViewAdapter mOverViewAdapter;
    private RecyclerView mFriendsRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_overview_fragment, null);

        mListBuddy = new ArrayList<>();
        mFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(YonaActivity.getActivity());
        mFriendsRecyclerView.setLayoutManager(mLayoutManager);
        mOverViewAdapter = new OverViewAdapter(mListBuddy, null);
        mFriendsRecyclerView.setAdapter(mOverViewAdapter);
        setRecyclerHeaderAdapterUpdate(new StickyRecyclerHeadersDecoration(mOverViewAdapter));

        YonaActivity.getActivity().getRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });

        getBuddies();
        return view;

    }

    /**
     * Get Buddies
     */
    private void getBuddies() {
        YonaActivity.getActivity().showLoadingView(true, null);
        APIManager.getInstance().getBuddyManager().getBuddies(new DataLoadListener() {
            @Override
            public void onDataLoad(Object result) {
                YonaActivity.getActivity().showLoadingView(false, null);
                if (result instanceof YonaBuddies) {
                    YonaBuddies buddies = (YonaBuddies) result;
                    mListBuddy.clear();
                    mListBuddy = buddies.getEmbedded().getYonaBuddies();
                    mOverViewAdapter.notifyDataSetChange(mListBuddy);
                }
            }

            @Override
            public void onError(Object errorMessage) {
                YonaActivity.getActivity().showLoadingView(false, null);
                YonaActivity.getActivity().showError((ErrorMessage) errorMessage);
            }
        });
    }

    /**
     * update RecyclerView item header for grouping section
     *
     * @param headerDecor
     */
    private void setRecyclerHeaderAdapterUpdate(StickyRecyclerHeadersDecoration headerDecor) {
        mFriendsRecyclerView.addItemDecoration(headerDecor);

        // Add decoration for dividers between list items
        mFriendsRecyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(mFriendsRecyclerView, headerDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                    }
                });
    }

    private void addFriend() {
        Intent friendIntent = new Intent(IntentEnum.ACTION_ADD_FRIEND.getActionString());
        YonaActivity.getActivity().replaceFragment(friendIntent);
    }
}
