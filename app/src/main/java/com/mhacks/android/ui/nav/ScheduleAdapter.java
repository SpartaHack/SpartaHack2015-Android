package com.mhacks.android.ui.nav;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhacks.android.data.model.Event;
import com.spartahack.android.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl johnson on 1/24/2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private final Context mContext;
    private List<Event> mData;

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventTitle;
        public final TextView eventDescription;
        public final TextView eventTime;

        public ScheduleViewHolder(View view) {
            super(view);
            eventTitle = (TextView) view.findViewById(R.id.event_title);
            eventDescription = (TextView) view.findViewById(R.id.event_description);
            eventTime = (TextView) view.findViewById(R.id.event_time);
        }
    }

    public ScheduleAdapter(Context context, ArrayList<Event> data) {
        mContext = context;
        if (data != null) {
            mData = new ArrayList<Event>(data);
        }
        else {
            mData = new ArrayList<Event>();
        }
    }

    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.schedule_grid_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, final int position) {
        holder.eventTitle.setText(mData.get(position).getTitle());
        holder.eventDescription.setText(mData.get(position).getDetails());
        try {
            holder.eventTime.setText(mData.get(position).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}