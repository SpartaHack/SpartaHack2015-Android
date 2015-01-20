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
import android.widget.GridView;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.ImageAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.spartahack.android.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Omkar Moghe on 10/25/2014.
 * edited by Carl Johnson sometime in Spring 2015
 */
public class SponsorsFragment extends Fragment{

    RecyclerView mRecyclerView;
    // Adapter for the listView
    MainNavAdapter mListAdapter;
    private View mSponsorsFragView;
    private GridView sponsorView;
    private ImageAdapter adapter;
    private Context context;
    private ArrayList<Sponsor> sponsors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mSponsorsFragView = inflater.inflate(R.layout.fragment_concierge, container, false);
        mRecyclerView = (RecyclerView)  mSponsorsFragView.findViewById(R.id.list_cards);
        //Put code for instantiating views, etc here. (before the return statement.)

        return mSponsorsFragView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sponsors = new ArrayList<Sponsor>();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        mListAdapter = new MainNavAdapter(getActivity());
        mRecyclerView.setAdapter(mListAdapter);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Concierge");
        query.include("Sponsor");
        query.include("Sponsor.object_id");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Sponsors", "Retrieved " + objectList.size() + " Sponsors");
                    for (ParseObject p : objectList) {
                        Sponsor sponsor = (Sponsor) p;
                        ParseObject user = p.getParseUser("Sponsor");
                        sponsor.put("Name",user.getString("Name"));
                        sponsor.put("Specialty",user.getString("Specialty"));
                        sponsors.add(sponsor);
                    }
                }
                else {
                    Log.d("Sponsors", "Error: " + e.getMessage());
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
            public TextView nameView;
            public TextView specialtyView;
            public TextView descriptionView;
            public TextView valueView;


            // Default constructor, itemView holds all the views that need to be saved
            public ViewHolder(View itemView) {
                super(itemView);

                // Save the TextViews
                this.nameView = (TextView) itemView.findViewById(R.id.concierge_name);
                this.specialtyView = (TextView) itemView.findViewById(R.id.concierge_specialty);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // Create the view for this row
            View row = LayoutInflater.from(mContext)
                    .inflate(R.layout.concierge_grid_item, viewGroup, false);

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
            Sponsor sponsor = sponsors.get(i);

            // Set this item's views based off of the announcement data
            viewHolder.nameView.setText(sponsor.getName());
            viewHolder.specialtyView.setText(sponsor.getSpecialty());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return sponsors.size();
        }
    }


}