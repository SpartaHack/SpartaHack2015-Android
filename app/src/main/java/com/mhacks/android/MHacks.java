package com.mhacks.android;

import android.app.Application;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.EventType;
import com.mhacks.android.data.model.Sponsor;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.spartahack.android.R;

/**
 * Created by Omkar Moghe on 11/15/2014.
 */
public class MHacks extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Announcement.class);
        ParseObject.registerSubclass(Sponsor.class);
        ParseObject.registerSubclass(Award.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(EventType.class);



        // enabling local data store causes weird 'ParseObject not found for update' error

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        PushService.startServiceIfRequired(getApplicationContext());

        Bugsnag.register(this, getString(R.string.bugsnag_key));

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }

}
