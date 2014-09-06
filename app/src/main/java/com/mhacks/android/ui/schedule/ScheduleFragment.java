package com.mhacks.android.ui.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.R;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.Util;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class ScheduleFragment extends Fragment implements
  ParseAdapter.ListCallbacks<Event>,
  AdapterView.OnItemClickListener,
  AdapterView.OnItemLongClickListener {
  public static final String TAG = "ScheduleFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<Event> mAdapter;

  public ScheduleFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<Event> factory = new ParseQueryAdapter.QueryFactory<Event>() {
      @Override
      public ParseQuery<Event> create() {
        return Event.query().orderByAscending(Event.TIME);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_event, this, factory)
      .setSectioning(Event.SAME_DAY)
      .load();

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_events, container, false);
    mLayout = (SwipeRefreshLayout) view.findViewById(R.id.events_swipe_container);

    mListView = (ListView) view.findViewById(R.id.events_list);
    mListView.setAdapter(mAdapter);

    if (User.canAdmin()) {
      mListView.setOnItemClickListener(this);
      mListView.setOnItemLongClickListener(this);
    }

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.canAdmin()) {
      inflater.inflate(R.menu.fragment_schedule, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.events_new:
        EventEditDialogFragment.newInstance(null).show(getFragmentManager(), EventEditDialogFragment.TAG);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void populateView(ViewHolder holder, Event event, boolean hasSectionHeader, boolean hasSectionFooter) {
    View header = holder.get(R.id.event_card_header);
    View footer = holder.get(R.id.event_card_footer);
    TextView title = holder.get(R.id.event_title);
    TextView details = holder.get(R.id.event_details);
    TextView host = holder.get(R.id.event_host);
    TextView date = holder.get(R.id.event_date);
    TextView time = holder.get(R.id.event_time);

    header.setVisibility(hasSectionHeader ? View.VISIBLE : View.GONE);
    footer.setVisibility(hasSectionFooter ? View.VISIBLE : View.GONE);

    Date timeTime = event.getTime();
    timeTime.setTime(timeTime.getTime() + 14400000); // Offset, quick hack

    title.setText(event.getTitle());
    details.setText(event.getDetails());
    host.setText(event.getHost().getTitle());
    date.setText(new SimpleDateFormat("EEEE").format(timeTime));
    time.setText(Util.Time.roundTimeAndFormat(timeTime, 2));
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    EventEditDialogFragment.newInstance(mAdapter.getItem(i)).show(getFragmentManager(), EventEditDialogFragment.TAG);
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
    new AlertDialog.Builder(getActivity())
      .setTitle(R.string.confirm_delete)
      .setMessage(R.string.confirm_delete_message)
      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          mAdapter.getItem(position).deleteEventually();
          dialogInterface.dismiss();
        }
      })
      .setNegativeButton(android.R.string.cancel, null)
      .show();
    return true;
  }
}

