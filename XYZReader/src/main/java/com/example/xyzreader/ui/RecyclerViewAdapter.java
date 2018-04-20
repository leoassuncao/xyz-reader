package com.example.xyzreader.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

import com.example.xyzreader.R;

/**
 * Created by leonardo.filho on 18/04/2018.
 */

public abstract class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private Context mContext;
    private Cursor cursor;
    private boolean mDataIsValid;
    private int mRowIdColumn;
    private DataSetObserver mDataSetObserver;

    public RecyclerViewAdapter(Cursor cursor){
        this.cursor = cursor;
        mDataIsValid = cursor != null;
        mRowIdColumn = mDataIsValid ? this.cursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mDataIsValid) {
            this.cursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataIsValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!mDataIsValid) {
            throw new IllegalStateException(mContext.getResources().getString(R.string.cursor_is_valid));
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException(mContext.getResources().getString(R.string.could_not_move) + position);
        }

        onBindViewHolder(viewHolder, cursor);
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataIsValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataIsValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataIsValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataIsValid = false;
            notifyDataSetChanged();
        }
    }
}