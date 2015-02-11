package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.spartahack.android.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Omkar Moghe on 10/25/2014.
 * edited by Carl Johnson sometime in Spring 2015
 */
public class ConciergeFragment extends Fragment{

    RecyclerView mRecyclerView;
    // Adapter for the listView
    ConciergeAdapter mListAdapter;
    private View mConciergeFragView;
    private ArrayList<Sponsor> sponsors;
    private List<ConciergeSectionedRecyclerViewAdapter.Section> sections;
    private Button mConciergeContact;
    private EditText mConciergeContactInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mConciergeFragView = inflater.inflate(R.layout.fragment_concierge, container, false);
        mRecyclerView = (RecyclerView)  mConciergeFragView.findViewById(R.id.list_cards);
        //Put code for instantiating views, etc here. (before the return statement.)
        mConciergeContact = (Button) mConciergeFragView.findViewById(R.id.concierge_contact);
        mConciergeContactInput = (EditText) mConciergeFragView.findViewById(R.id.conciergeContactInput);
        return mConciergeFragView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sponsors = new ArrayList<Sponsor>();
        sections = new ArrayList<ConciergeSectionedRecyclerViewAdapter.Section>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        //mListAdapter = new MainNavAdapter(getActivity());



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Concierge");
        query.include("Sponsor");
        query.include("Sponsor.object_id");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Sponsors", "Retrieved " + objectList.size() + " Sponsors");
                    String lastTitle = null;
//                    int numSponsors = 0;

                    for (int i=0; i<objectList.size();i++) {
                        Sponsor sponsor = (Sponsor) objectList.get(i);
                        ParseObject user = sponsor.getParseUser("Sponsor");
                        if(!sponsor.getTitle().equals(lastTitle)) {
                            sections.add(new ConciergeSectionedRecyclerViewAdapter.Section(i,sponsor.getTitle()));
//                            numSponsors++;
                        }
                        sponsor.setName(user.getString("Name"));
                        sponsor.setSpecialty(user.getString("Specialty"));
                        sponsor.setTwitter(user.getString("Twitter"));
                        sponsor.setEmail(user.getString("email"));
                        lastTitle = sponsor.getTitle();
                        sponsors.add(sponsor);
                    }
                }
                else {
                    Log.d("Sponsors", "Error: " + e.getMessage());
                }
                mListAdapter = new ConciergeAdapter(getActivity(),sponsors);
                mRecyclerView.setAdapter(mListAdapter);

                //Add your adapter to the sectionAdapter
                ConciergeSectionedRecyclerViewAdapter.Section[] dummy = new ConciergeSectionedRecyclerViewAdapter.Section[sections.size()];
                ConciergeSectionedRecyclerViewAdapter mSectionedAdapter = new
                        ConciergeSectionedRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,mListAdapter);
                mSectionedAdapter.setSections(sections.toArray(dummy));

                //Apply this adapter to the RecyclerView
                mRecyclerView.setAdapter(mSectionedAdapter);

            }
        });

    mConciergeContact.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new postToHTTP().execute(mConciergeContactInput.getText().toString());
        }
    });

    }

    /**
     * post to Slack API for Concierge Service asynchronously
     * we will use #concierge in slack to deal with problems attendess are having
     */
    private class postToHTTP extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... attendeeContactInput) {
            postData(attendeeContactInput[0]);
            return null;
        }
        public void postData(String attendeeContactInput)  {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://hooks.slack.com/services/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");

            try {
                // Add your data
                JSONObject json = new JSONObject();

                try {
                    json.put("text",attendeeContactInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("payload", json.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                int xx=0;
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }



    class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.ViewHolder> {
        Context mContext;
        private List<String> mData;
        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;
        }

//        public ConciergeAdapter(Context context, String[] data) {
//            mContext = context;
//            if (data != null)
//                mData = new ArrayList<String>(Arrays.asList(data));
//            else mData = new ArrayList<String>();
//        }

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