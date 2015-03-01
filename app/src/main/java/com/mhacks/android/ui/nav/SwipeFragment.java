package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.spartahack.android.R;

/**
 * Created by Carl Johnson on 2/15/2015.
 */
public class SwipeFragment extends Fragment {


    private SwipeRefreshLayout mSwipeLayout;

    protected void initSwipeLayout(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        } );
        mSwipeLayout.setColorSchemeResources(R.color.darkBrown);
        mSwipeLayout.setProgressBackgroundColor(R.color.lightBrown);
    }

    protected void reloadFragment() {
        initParseData();
    }

    void initParseData() {

    }

    protected class AsyncTaskRunner extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            reloadFragment();
            return 1;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Integer result) {
            // execution of result of Long time consuming operation
            mSwipeLayout.setRefreshing(false);
        }
    }

}
