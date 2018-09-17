/*
 * Copyright (c) 2018 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nu.yona.app.R;
import nu.yona.app.YonaApplication;
import nu.yona.app.analytics.AnalyticsConstant;
import nu.yona.app.analytics.YonaAnalytics;
import nu.yona.app.api.manager.APIManager;
import nu.yona.app.api.model.ErrorMessage;
import nu.yona.app.api.model.Href;
import nu.yona.app.api.model.User;
import nu.yona.app.api.model.YonaBuddy;
import nu.yona.app.api.model.YonaHeaderTheme;
import nu.yona.app.api.model.YonaMessage;
import nu.yona.app.api.model.YonaMessages;
import nu.yona.app.enums.IntentEnum;
import nu.yona.app.enums.NotificationEnum;
import nu.yona.app.enums.NotificationMessageEnum;
import nu.yona.app.enums.StatusEnum;
import nu.yona.app.listener.DataLoadListener;
import nu.yona.app.listener.DataLoadListenerImpl;
import nu.yona.app.ui.BaseFragment;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.ui.frinends.OnFriendsItemClickListener;
import nu.yona.app.utils.AppConstant;
import nu.yona.app.utils.AppUtils;

/**
 * Created by kinnarvasa on 21/03/16.
 */
public class NotificationFragment extends BaseFragment
{

	private RecyclerView mMessageRecyclerView;
	private MessageStickyRecyclerAdapter mMessageStickyRecyclerAdapter;
	private YonaMessages mYonaMessages;
	private int currentPage = 0;
	private boolean mIsLoading = false;
	private LinearLayoutManager mLayoutManager;

	/**
	 * Click listener for item click and delete click of recycler view's item
	 */
	private final OnFriendsItemClickListener onFriendsItemClickListener = new OnFriendsItemClickListener()
	{
		@Override
		public void onFriendsItemClick(View view)
		{
			if (view.getTag() instanceof YonaMessage)
			{
				YonaMessage yonaMessage = (YonaMessage) view.getTag();
				Intent mMessageIntent = null;
				if (yonaMessage.getNotificationMessageEnum() == NotificationMessageEnum.SYSTEM_MESSAGE)
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_ADMIN_MESSAGE_DETAIL.getActionString());
					mMessageIntent.putExtra(AppConstant.ADMIN_MESSAGE, yonaMessage);
					mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(false, null, null, 0, 0, null, R.color.grape, R.drawable.triangle_shadow_grape));
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, AnalyticsConstant.ADMIN_MESSAGE_SCREEN);
				}
				else if (yonaMessage.getLinks() != null && yonaMessage.getLinks().getEdit() != null && yonaMessage.getNotificationMessageEnum().getStatusEnum() == StatusEnum.ACCEPTED)
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_FRIEND_PROFILE.getActionString());
					YonaHeaderTheme yonaHeaderTheme = new YonaHeaderTheme(false, null, null, 0, 0, null, R.color.mid_blue_two, R.drawable.triangle_shadow_blue);
					mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, yonaHeaderTheme);
					mMessageIntent.putExtra(AppConstant.YONAMESSAGE_OBJ, yonaMessage);
					mMessageIntent.putExtra(AppConstant.SECOND_COLOR_CODE, R.color.grape);
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, getString(R.string.friends));
				}
				else if (yonaMessage.getNotificationMessageEnum().getStatusEnum() == StatusEnum.REQUESTED)
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_FRIEND_REQUEST.getActionString());
					mMessageIntent.putExtra(AppConstant.YONAMESSAGE_OBJ, yonaMessage);
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, getString(R.string.status_friend_request));
				}
				else if (yonaMessage.getNotificationMessageEnum().getNotificationEnum() == NotificationEnum.ACTIVITYCOMMENTMESSAGE)
				{
					if (yonaMessage.getLinks() != null && yonaMessage.getLinks().getYonaDayDetails() != null)
					{
						mMessageIntent = new Intent(IntentEnum.ACTION_SINGLE_ACTIVITY_DETAIL_VIEW.getActionString());
						mMessageIntent.putExtra(AppConstant.YONA_DAY_DEATIL_URL, yonaMessage.getLinks().getYonaDayDetails().getHref());
						if (yonaMessage.getLinks().getReplyComment() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getReplyComment().getHref()))
						{
							mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, null, null, 0, 0, null, R.color.mid_blue, R.drawable.triangle_shadow_blue));
						}
						else
						{
							mMessageIntent.putExtra(AppConstant.YONA_BUDDY_OBJ, findBuddy(yonaMessage.getLinks().getYonaBuddy()));
							mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(false, null, null, 0, 0, null, R.color.grape, R.drawable.triangle_shadow_grape));
						}
						YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, AnalyticsConstant.DAY_ACTIVITY_DETAIL_SCREEN);
					}
					else if (yonaMessage.getLinks() != null && yonaMessage.getLinks().getWeekDetails() != null)
					{
						mMessageIntent = new Intent(IntentEnum.ACTION_SINGLE_WEEK_DETAIL_VIEW.getActionString());
						mMessageIntent.putExtra(AppConstant.YONA_WEEK_DETAIL_URL, yonaMessage.getLinks().getWeekDetails().getHref());
						if (yonaMessage.getLinks().getReplyComment() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getReplyComment().getHref()))
						{
							mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(false, null, null, 0, 0, null, R.color.grape, R.drawable.triangle_shadow_grape));
						}
						else
						{
							mMessageIntent.putExtra(AppConstant.YONA_BUDDY_OBJ, findBuddy(yonaMessage.getLinks().getYonaBuddy()));
							mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, null, null, 0, 0, null, R.color.mid_blue, R.drawable.triangle_shadow_blue));
						}
						YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, AnalyticsConstant.WEEK_ACTIVITY_DETAIL_SCREEN);
					}
				}
				else if ((yonaMessage.getNotificationMessageEnum().getNotificationEnum() == NotificationEnum.GOALCONFLICTMESSAGE)
						&& yonaMessage.getLinks().getYonaDayDetails() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getYonaDayDetails().getHref()))
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_SINGLE_ACTIVITY_DETAIL_VIEW.getActionString());
					mMessageIntent.putExtra(AppConstant.YONA_DAY_DEATIL_URL, yonaMessage.getLinks().getYonaDayDetails().getHref());
					mMessageIntent.putExtra(AppConstant.URL, yonaMessage.getUrl());

					try
					{
						SimpleDateFormat sdf = new SimpleDateFormat(AppConstant.YONA_LONG_DATE_FORMAT, Locale.getDefault());
						Date date = sdf.parse(yonaMessage.getActivityStartTime());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);

						mMessageIntent.putExtra(AppConstant.EVENT_TIME, calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
					}
					catch (Exception e)
					{
						AppUtils.reportException(NotificationFragment.class.getSimpleName(), e, Thread.currentThread());
					}

					if (yonaMessage.getLinks() != null && yonaMessage.getLinks().getSelf() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getSelf().getHref()))
					{
						mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, null, null, 0, 0, null, R.color.grape, R.drawable.triangle_shadow_grape));
					}
					else
					{
						mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, null, null, 0, 0, null, R.color.mid_blue, R.drawable.triangle_shadow_blue));
					}
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, NotificationEnum.GOALCONFLICTMESSAGE.getNotificationType());
				}
				else if (yonaMessage.getNotificationMessageEnum().getNotificationEnum() == NotificationEnum.GOALCHANGEMESSAGE && yonaMessage.getLinks().getYonaBuddy() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getYonaBuddy().getHref()))
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_DASHBOARD.getActionString());
					YonaBuddy yonaBuddy = findBuddy(yonaMessage.getLinks().getYonaBuddy());
					mMessageIntent.putExtra(AppConstant.YONA_BUDDY_OBJ, yonaBuddy);
					if (yonaBuddy.getLinks() != null)
					{
						mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, yonaBuddy.getLinks().getYonaDailyActivityReports(), yonaBuddy.getLinks().getYonaWeeklyActivityReports(), 0, 0, yonaBuddy.getEmbedded().getYonaUser().getFirstName() + " " + yonaBuddy.getEmbedded().getYonaUser().getLastName(), R.color.mid_blue_two, R.drawable.triangle_shadow_blue));
					}
					else
					{
						mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, new YonaHeaderTheme(true, null, null, 0, 0, yonaBuddy.getEmbedded().getYonaUser().getFirstName() + " " + yonaBuddy.getEmbedded().getYonaUser().getLastName(), R.color.mid_blue_two, R.drawable.triangle_shadow_blue));
					}
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, NotificationEnum.GOALCHANGEMESSAGE.getNotificationType());
				}
				else if (yonaMessage.getNotificationMessageEnum().getNotificationEnum() == NotificationEnum.BUDDYCONNECTREQUESTMESSAGE)
				{
					if (yonaMessage.getNotificationMessageEnum().getStatusEnum() == StatusEnum.REQUESTED)
					{
						//self.performSegueWithIdentifier(R.segue.notificationsViewController.showAcceptFriend, sender: self)
						return;
					}

					if (yonaMessage.getNotificationMessageEnum().getStatusEnum() == StatusEnum.ACCEPTED)
					{
						return;
					}
				}
				else if (yonaMessage.getNotificationMessageEnum().getNotificationEnum() == NotificationEnum.BUDDYINFOCHANGEMESSAGE)
				{
					mMessageIntent = new Intent(IntentEnum.ACTION_FRIEND_PROFILE.getActionString());
					YonaHeaderTheme yonaHeaderTheme = new YonaHeaderTheme(false, null, null, 0, 0, null, R.color.mid_blue_two, R.drawable.triangle_shadow_blue);
					mMessageIntent.putExtra(AppConstant.YONA_THEME_OBJ, yonaHeaderTheme);
					mMessageIntent.putExtra(AppConstant.YONAMESSAGE_OBJ, yonaMessage);
					mMessageIntent.putExtra(AppConstant.SECOND_COLOR_CODE, R.color.grape);
					YonaAnalytics.createTapEventWithCategory(AnalyticsConstant.NOTIFICATION, getString(R.string.friends));
				}
				updateStatusAsRead(yonaMessage);
				if (mMessageIntent != null)
				{
					mMessageIntent.putExtra(AppConstant.YONA_MESSAGE, yonaMessage);
				}
				YonaActivity.getActivity().replaceFragment(mMessageIntent);
			}
		}

		@Override
		public void onFriendsItemDeleteClick(View view)
		{
			if (view.getTag() instanceof YonaMessage)
			{
				YonaMessage yonaMessage = (YonaMessage) view.getTag();
				updateStatusAsRead(yonaMessage);
				if (yonaMessage != null && yonaMessage.getLinks() != null && yonaMessage.getLinks().getEdit() != null && !TextUtils.isEmpty(yonaMessage.getLinks().getEdit().getHref()))
				{
					YonaActivity.getActivity().showLoadingView(true, null);
					APIManager.getInstance().getNotificationManager().deleteMessage(yonaMessage.getLinks().getEdit().getHref(), 0, 0, new DataLoadListener()
					{
						@Override
						public void onDataLoad(Object result)
						{
							YonaActivity.getActivity().showLoadingView(false, null);
							refreshAdapter();
						}

						@Override
						public void onError(Object errorMessage)
						{
							YonaActivity.getActivity().showLoadingView(false, null);
							YonaActivity.getActivity().showError((ErrorMessage) errorMessage);
						}
					});
				}
			}
		}

		@Override
		public void onItemClick(View view)
		{
			if (view.getTag() instanceof YonaMessage)
			{
				YonaMessage yonaMessage = (YonaMessage) view.getTag();
				updateStatusAsRead(yonaMessage);
			}
		}
	};

	/**
	 * Recyclerview's scroll listener when its getting end to load more data till the pages not reached
	 */
	private final RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener()
	{
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState)
		{
			super.onScrollStateChanged(recyclerView, newState);
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy)
		{
			super.onScrolled(recyclerView, dx, dy);
			if (dy > 0)
			{
				int visibleItemCount = mLayoutManager.getChildCount();
				int totalItemCount = mLayoutManager.getItemCount();
				int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

				if (!mIsLoading && currentPage < mYonaMessages.getPage().getTotalPages())
				{
					if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)
					{
						loadMoreItems();
					}
				}
			}
		}
	};

	/**
	 * load more items
	 */
	private void loadMoreItems()
	{
		currentPage += 1;
		getUserMessages(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.notification_layout, null);

		setupToolbar(view);

		mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
		mLayoutManager = new LinearLayoutManager(YonaActivity.getActivity());
		mMessageRecyclerView.setLayoutManager(mLayoutManager);
		mMessageStickyRecyclerAdapter = new MessageStickyRecyclerAdapter(new ArrayList<YonaMessage>(), YonaActivity.getActivity(), onFriendsItemClickListener);
		//mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(YonaActivity.getActivity()));
		mMessageRecyclerView.setAdapter(mMessageStickyRecyclerAdapter);
		mMessageRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
		setRecyclerHeaderAdapterUpdate(new StickyRecyclerHeadersDecoration(mMessageStickyRecyclerAdapter));
		getUser();
		setHook(new YonaAnalytics.BackHook(AnalyticsConstant.BACK_FROM_NOTIFICATION));
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setTitleAndIcon();
		refreshAdapter();
	}

	/**
	 * update toolbar's Item
	 */
	private void setTitleAndIcon()
	{
		((YonaActivity) getActivity()).updateTabIcon(false);
		profileCircleImageView.setVisibility(View.GONE);
		toolbarTitle.setText(getString(R.string.message));
		rightIcon.setVisibility(View.GONE);
	}

	/**
	 * Refresh recyclerview's adapter
	 */
	private void refreshAdapter()
	{
		mMessageStickyRecyclerAdapter.clear();
		currentPage = 0;
		getUserMessages(false);
		//YonaApplication.getEventChangeManager().getDataState().setEmbeddedWithBuddyActivity(null);
	}

	private void getUser()
	{
		APIManager.getInstance().getAuthenticateManager().getUserFromServer();
	}

	private Href getURLToFetchMessages(boolean loadMore)
	{
		Href urlForMessageFetch = null;
		if (mYonaMessages == null)
		{
			User user = YonaApplication.getEventChangeManager().getDataState().getUser();
			urlForMessageFetch = user.getLinks().getYonaMessages();
		}
		else if (mYonaMessages.getLinks().getNext() != null && loadMore)
		{
			urlForMessageFetch = mYonaMessages.getLinks().getNext();
		}
		else if (mYonaMessages.getLinks().getFirst() != null)
		{
			urlForMessageFetch = mYonaMessages.getLinks().getFirst();
		}
		else
		{
			urlForMessageFetch = mYonaMessages.getLinks().getSelf();
		}
		return urlForMessageFetch;
	}


	/**
	 * to get the list of user's messages
	 */
	private void getUserMessages(boolean loadMore)
	{
		try
		{
			mIsLoading = true;
			String urlForMessageFetch = getURLToFetchMessages(loadMore).getHref();
			DataLoadListenerImpl dataLoadListenerImpl = new DataLoadListenerImpl(((result) -> handleYonaMessagesFetchSuccess((YonaMessages) result)), ((result) -> handleYonaMessagesFetchFailure(result)), null);
			APIManager.getInstance().getNotificationManager().getMessages(urlForMessageFetch, false, dataLoadListenerImpl);
		}
		catch (IllegalArgumentException e)
		{
			AppUtils.reportException(NotificationFragment.class.getSimpleName(), e, Thread.currentThread(), null);
		}
	}

	private Object handleYonaMessagesFetchSuccess(YonaMessages result)
	{
		YonaActivity.getActivity().showLoadingView(false, null);
		if (isAdded() && result != null && result instanceof YonaMessages)
		{
			YonaMessages mMessages = (YonaMessages) result;
			if (mMessages.getEmbedded() != null && mMessages.getEmbedded().getYonaMessages() != null)
			{
				mYonaMessages = mMessages;
				if (mIsLoading)
				{
					mMessageStickyRecyclerAdapter.updateData(mYonaMessages.getEmbedded().getYonaMessages());
				}
				else
				{
					mMessageStickyRecyclerAdapter.notifyDataSetChange(mYonaMessages.getEmbedded().getYonaMessages());
				}
			}
		}
		mIsLoading = false;
		return null;
	}


	private Object handleYonaMessagesFetchFailure(Object errorMessage)
	{
		YonaActivity.getActivity().showLoadingView(false, null);
		YonaActivity.getActivity().showError((ErrorMessage) errorMessage);
		mIsLoading = false;
		return null;
	}

	/**
	 * update RecyclerView item header for grouping section
	 *
	 * @param headerDecor
	 */
	private void setRecyclerHeaderAdapterUpdate(StickyRecyclerHeadersDecoration headerDecor)
	{
		mMessageRecyclerView.addItemDecoration(headerDecor);

		// Add decoration for dividers between list items
		//mMessageRecyclerView.addItemDecoration(new DividerDecoration(getActivity()));

		// Add touch listeners
		StickyRecyclerHeadersTouchListener touchListener =
				new StickyRecyclerHeadersTouchListener(mMessageRecyclerView, headerDecor);
		touchListener.setOnHeaderClickListener(
				new StickyRecyclerHeadersTouchListener.OnHeaderClickListener()
				{
					@Override
					public void onHeaderClick(View header, int position, long headerId)
					{
					}
				});
	}

	private YonaBuddy findBuddy(Href href)
	{
		return APIManager.getInstance().getActivityManager().findYonaBuddy(href);
	}

	private void updateStatusAsRead(YonaMessage message)
	{
		APIManager.getInstance().getNotificationManager().setReadMessage(mYonaMessages.getEmbedded().getYonaMessages(), message, new DataLoadListener()
		{
			@Override
			public void onDataLoad(Object result)
			{
				mMessageStickyRecyclerAdapter.notifyDataSetChange((List<YonaMessage>) result);
			}

			@Override
			public void onError(Object errorMessage)
			{

			}
		});
	}

	@Override
	public String getAnalyticsCategory()
	{
		return AnalyticsConstant.NOTIFICATION;
	}
}
