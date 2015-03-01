package com.mhacks.android.ui.nav;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class ConciergeFragment extends SwipeFragment{

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
        initSwipeLayout(mConciergeFragView);
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
        initParseData();

    mConciergeContact.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try  {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            String attendeeRequest = mConciergeContactInput.getText().toString();
            if(attendeeRequest.length()!=0) {
                mConciergeContact.setEnabled(false);
                new postToHTTP().execute(attendeeRequest);
            }
        }
    });

    }

    @Override
    protected void initParseData() {
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
                        String sponsorTitle = sponsor.getTitle();
                        if(sponsorTitle == null) {
                            sponsorTitle = "SpartaHack";
                        }
                        if(!sponsorTitle.equals(lastTitle)) {
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
    }
    public void reloadFragment() {
        //initialize your lists
        sponsors = new ArrayList<Sponsor>();
        sections = new ArrayList<ConciergeSectionedRecyclerViewAdapter.Section>();
        super.reloadFragment();
    }
    /**
     * post to Slack API for Concierge Service via webhook asynchronously
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(mConciergeFragView.getContext(), "Thanks! Your request was sent to the SpartaHack Team.",Toast.LENGTH_SHORT);
            mConciergeContactInput.setText("");
            mConciergeContact.setEnabled(true);
        }
    }
}