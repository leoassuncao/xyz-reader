package com.example.xyzreader.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

/**
 * Created by leonardo.filho on 18/04/2018.
 */

public class NewsAdapter extends RecyclerViewAdapter<NewsAdapter.ViewHolder> {
    private Context mContext;

     NewsAdapter(Cursor cursor, Context context) {
        super(cursor);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        Cursor mCursor = cursor;
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        String thumb = mCursor.getString(ArticleLoader.Query.THUMB_URL);
        holder.subtitleView.setText(
                DateUtils.getRelativeTimeSpanString(
                        mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString());
        holder.authorTextView.setText(mCursor.getString(ArticleLoader.Query.AUTHOR));
        holder.thumbnailView.setTransitionName(Integer.toString(ArticleLoader.Query._ID ));
        holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        Glide.clear(holder.thumbnailView);
        Glide.with(holder.thumbnailView.getContext())
                .load(thumb)
                .centerCrop()
                .into(holder.thumbnailView);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));

                }

        });
        return vh;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
         public DynamicHeightNetworkImageView thumbnailView;
         TextView titleView;
         TextView subtitleView;
         TextView authorTextView;

         ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
            authorTextView = (TextView) view.findViewById(R.id.authorTextView);
        }
    }

}
