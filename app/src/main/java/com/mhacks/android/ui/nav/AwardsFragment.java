package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhacks.android.data.model.Award;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.spartahack.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AwardsFragment extends Fragment{

    private View mAwardsFragView;
    RecyclerView mRecyclerView;
    private List<Award> awardList;
    // Adapter for the listView
    MainNavAdapter mListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mAwardsFragView = inflater.inflate(R.layout.fragment_awards, container, false);
        mRecyclerView = (RecyclerView)  mAwardsFragView.findViewById(R.id.list_cards);
        //Put code for instantiating views, etc here. (before the return statement.)

        return mAwardsFragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        awardList = new ArrayList<Award>();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        mListAdapter = new MainNavAdapter(getActivity());
        mRecyclerView.setAdapter(mListAdapter);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Award");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Awards", "Retrieved " + objectList.size() + " awards");
                    for (ParseObject p : objectList) {
                        Award a = (Award) p;
                        awardList.add(a);
                    }
                }
                else {
                    Log.d("Awards", "Error: " + e.getMessage());
                }
                mListAdapter.notifyDataSetChanged();
            }
        });

    }

    class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.ViewHolder> {
        Context mContext;

        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;
        }

        // Simple class that holds all the views that need to be reused
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView titleView;
            public TextView sponsorView;
            public TextView descriptionView;

            // Default constructor, itemView holds all the views that need to be saved
            public ViewHolder(View itemView) {
                super(itemView);

                // Save the TextViews
                this.titleView = (TextView) itemView.findViewById(R.id.info_title);
                this.sponsorView = (TextView) itemView.findViewById(R.id.award_sponsor);
                this.descriptionView = (TextView) itemView.findViewById(R.id.info_description);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // Create the view for this row
            View row = LayoutInflater.from(mContext)
                    .inflate(R.layout.award_grid_item, viewGroup, false);

            // Create a new viewHolder which caches all the views that needs to be saved
            ViewHolder viewHolder = new ViewHolder(row);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // Get the current announcement item
            Award award = awardList.get(i);

            // Set this item's views based off of the announcement data
            viewHolder.titleView.setText(award.getTitle());
//            viewHolder.sponsorView.setText(award.getSponsor());
            viewHolder.descriptionView.setText(award.getDescription());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return awardList.size();
        }
    }
}
