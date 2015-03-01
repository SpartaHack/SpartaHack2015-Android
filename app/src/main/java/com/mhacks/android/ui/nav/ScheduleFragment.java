package com.mhacks.android.ui.nav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhacks.android.data.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.spartahack.android.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Carl Johnson sometime in Spring 2015
 */
public class ScheduleFragment extends SwipeFragment{

    RecyclerView mRecyclerView;
    // Adapter for the listView
    ScheduleAdapter mListAdapter;
    private View mScheduleFragView;
    private ArrayList<Event> events;
    private List<ScheduleSectionedRecyclerViewAdapter.Section> sections;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView)  mScheduleFragView.findViewById(R.id.list_cards);
        initSwipeLayout(mScheduleFragView);
        return mScheduleFragView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        events = new ArrayList<Event>();
        sections = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        //mListAdapter = new MainNavAdapter(getActivity());
        initParseData();


    }
    @Override
    protected void initParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.orderByAscending("Time");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Events", "Retrieved " + objectList.size() + " Events");
                    String lastDay = null;
                    int numEvents = 0;

                    for (int i = 0; i < objectList.size(); i++) {
                        Event event = (Event) objectList.get(i);

                        String eventDay = event.getDay();

                        if (!eventDay.equals(lastDay)) {
                            sections.add(new ScheduleSectionedRecyclerViewAdapter.Section(i + numEvents, eventDay));
                            //numEvents++;
                        }
                        event.setTitle(event.getString("Title"));
                        event.setDetails(event.getString("Details"));
                        Date timeOfEvent = event.getDate("Time");
                        event.setStartTime(event.getDate("Time"));

                        lastDay = eventDay;
                        events.add(event);
                    }
                } else {
                    Log.d("Events", "Error: " + e.getMessage());
                }
                mListAdapter = new ScheduleAdapter(getActivity(), events);
                mRecyclerView.setAdapter(mListAdapter);

                //Add your adapter to the sectionAdapter
                ScheduleSectionedRecyclerViewAdapter.Section[] dummy = new ScheduleSectionedRecyclerViewAdapter.Section[sections.size()];
                ScheduleSectionedRecyclerViewAdapter mSectionedAdapter = new
                        ScheduleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, mListAdapter);
                mSectionedAdapter.setSections(sections.toArray(dummy));

                //Apply this adapter to the RecyclerView
                mRecyclerView.setAdapter(mSectionedAdapter);

            }
        });
    }
    @Override
    protected void reloadFragment() {
        events = new ArrayList<Event>();
        sections = new ArrayList<>();
        super.reloadFragment();
    }

}

