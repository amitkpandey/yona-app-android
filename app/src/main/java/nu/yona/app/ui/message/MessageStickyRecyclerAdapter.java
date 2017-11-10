/*
 * Copyright (c) 2016 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nu.yona.app.ui.message;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import nu.yona.app.R;
import nu.yona.app.api.model.YonaMessage;
import nu.yona.app.enums.NotificationMessageEnum;
import nu.yona.app.ui.StickyHeaderHolder;
import nu.yona.app.ui.YonaActivity;
import nu.yona.app.ui.frinends.OnFriendsItemClickListener;

/**
 * Created by bhargavsuthar on 10/05/16.
 */
public class MessageStickyRecyclerAdapter extends RecyclerView.Adapter<MessageItemViewHolder> implements StickyRecyclerHeadersAdapter<StickyHeaderHolder> {

    private List<YonaMessage> listYonaMessage;
    private YonaActivity activity;
    private OnFriendsItemClickListener mOnFriendsItemClickListener;

    /**
     * Instantiates a new Message sticky recycler adapter.
     *
     * @param yonaMessages      the yona messages
     * @param yonaActivity      the yona activity
     * @param itemClickListener the item click listener
     */
    public MessageStickyRecyclerAdapter(List<YonaMessage> yonaMessages, YonaActivity yonaActivity, OnFriendsItemClickListener itemClickListener) {
        this.listYonaMessage = yonaMessages;
        this.activity = yonaActivity;
        this.mOnFriendsItemClickListener = itemClickListener;

    }

    @Override
    public MessageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message_item_layout, parent, false);
        return new MessageItemViewHolder(layoutView, mOnFriendsItemClickListener);
    }

    @Override
    public void onBindViewHolder(MessageItemViewHolder holder, int position) {
        YonaMessage yonaObject = (YonaMessage) getItem(position);

        if (yonaObject != null) {
            if (yonaObject.getNotificationMessageEnum() != null && !TextUtils.isEmpty(yonaObject.getNotificationMessageEnum().getUserMessage())) {
                holder.txtTitleMsg.setText(yonaObject.getNotificationMessageEnum().getUserMessage());
                holder.img_status.setImageResource(yonaObject.getNotificationMessageEnum().getImageId());
            }
            if (yonaObject.getLinks() != null && yonaObject.getLinks().getEdit() != null) {
                holder.swipeLayout.setRightSwipeEnabled(true);
            } else {
                holder.swipeLayout.setRightSwipeEnabled(false);
            }
            if (yonaObject.getNotificationMessageEnum() == NotificationMessageEnum.SYSTEM_MESSAGE) {
                holder.img_avtar.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(yonaObject.getMessage())) {
                    holder.txtFooterMsg.setText(yonaObject.getMessage());
                }
                holder.profileIconTxt.setVisibility(View.VISIBLE);
                holder.profileIconTxt.setText(yonaObject.getNickname().substring(0, 1).toUpperCase());
                holder.profileIconTxt.setBackground(ContextCompat.getDrawable(YonaActivity.getActivity(), R.drawable.bg_small_admin_round));
            } else if (!TextUtils.isEmpty(yonaObject.getNickname())) {
                holder.txtFooterMsg.setText(yonaObject.getNickname());
                if (yonaObject.getNotificationMessageEnum() == NotificationMessageEnum.GOALCONFLICTMESSAGE_ANNOUNCED) {
                    holder.img_avtar.setImageResource(R.drawable.adult_sad);
                    holder.img_avtar.setVisibility(View.VISIBLE);
                    holder.profileIconTxt.setVisibility(View.GONE);
                } else {
                    holder.img_avtar.setVisibility(View.GONE);
                    holder.profileIconTxt.setVisibility(View.VISIBLE);
                    holder.profileIconTxt.setText(yonaObject.getNickname().substring(0, 1).toUpperCase());
                    holder.profileIconTxt.setBackground(ContextCompat.getDrawable(YonaActivity.getActivity(), R.drawable.bg_small_self_round));
                }
            }
            if (yonaObject.getLinks() != null && yonaObject.getLinks().getMarkRead() != null && !TextUtils.isEmpty(yonaObject.getLinks().getMarkRead().getHref())) {
                holder.messageContainer.setBackground(ContextCompat.getDrawable(activity, R.drawable.item_selected_gradient));
            } else {
                holder.messageContainer.setBackground(ContextCompat.getDrawable(activity, R.drawable.item_gradient));
            }
            holder.deleteMsg.setTag(yonaObject);
            holder.messageContainer.setTag(yonaObject);
        }

    }

    /**
     * Gets item.
     *
     * @param position the position
     * @return the item
     */
    public Object getItem(int position) {
        return listYonaMessage.get(position);
    }

    @Override
    public long getHeaderId(int position) {
        Object mObject = getItem(position);
        return ((YonaMessage) mObject).getStickyTitle().charAt(0);
    }

    @Override
    public StickyHeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_header_layout, parent, false);
        return new StickyHeaderHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(StickyHeaderHolder holder, int position) {
        Object yonaObject = getItem(position);
        if (yonaObject != null) {
            holder.getHeaderText().setText(((YonaMessage) yonaObject).getStickyTitle());
        }
    }

    @Override
    public int getItemCount() {
        return listYonaMessage.size();
    }

    /**
     * Update data.
     *
     * @param yonaMessages the yona messages
     */
    public void updateData(final List<YonaMessage> yonaMessages) {
        listYonaMessage.addAll(yonaMessages);
        notifyDataSetChanged();
    }

    /**
     * Notify data set change.
     *
     * @param yonaMessages the yona messages
     */
    public void notifyDataSetChange(final List<YonaMessage> yonaMessages) {
        this.listYonaMessage = yonaMessages;
        notifyDataSetChanged();
    }

    /**
     * Clear.
     */
    public void clear() {
        while (getItemCount() > 0) {
            remove((YonaMessage) getItem(0));
        }
    }

    /**
     * Remove.
     *
     * @param item the item
     */
    public void remove(YonaMessage item) {
        int position = listYonaMessage.indexOf(item);
        if (position > -1) {
            listYonaMessage.remove(position);
            notifyItemRemoved(position);
        }
    }
}
