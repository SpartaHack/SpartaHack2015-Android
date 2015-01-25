package com.mhacks.android.ui.nav;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.spartahack.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl johnson on 1/24/2015.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<Sponsor> mData;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView specialty;

        public SimpleViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.concierge_name);
            specialty = (TextView) view.findViewById(R.id.concierge_specialty);
        }
    }

    public SimpleAdapter(Context context, ArrayList<Sponsor> data) {
        mContext = context;
        if (data != null) {
            mData = new ArrayList<Sponsor>(data);
        }
        else {
            mData = new ArrayList<Sponsor>();
        }
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.concierge_grid_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getName());
        holder.specialty.setText(mData.get(position).getSpecialty());
//        holder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}