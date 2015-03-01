package com.mhacks.android.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.spartahack.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl johnson on 1/24/2015.
 */
public class ConciergeAdapter extends RecyclerView.Adapter<ConciergeAdapter.ConciergeViewHolder> {

    private final Context mContext;
    private List<Sponsor> mData;

    public static class ConciergeViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView specialty;
        public final ImageButton twitterButton;
        public final ImageButton mailButton;


        public ConciergeViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.concierge_name);
            specialty = (TextView) view.findViewById(R.id.concierge_specialty);

            twitterButton = (ImageButton) view.findViewById(R.id.concierge_twitter_btn);
            mailButton = (ImageButton) view.findViewById(R.id.concierge_mail_btn);
        }
    }

    public ConciergeAdapter(Context context, ArrayList<Sponsor> data) {
        mContext = context;
        if (data != null) {
            mData = new ArrayList<Sponsor>(data);
        }
        else {
            mData = new ArrayList<Sponsor>();
        }
    }

    public ConciergeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.concierge_grid_item, parent, false);
        return new ConciergeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConciergeViewHolder holder, final int position) {
        String sponsorName = mData.get(position).getName();
        String sponsorSpecialty = mData.get(position).getSpecialty();
        final String twitterHandle = mData.get(position).getTwitter();
        final String emailAddress = mData.get(position).getEmail();
        if(sponsorName != null) {
            holder.name.setText(sponsorName);
        }
        if(sponsorSpecialty != null) {
            holder.specialty.setText(mData.get(position).getSpecialty());
        }
        if(twitterHandle == null) {
            ViewGroup layout = (ViewGroup) holder.twitterButton.getParent();
            if(null!=layout) //for safety only  as you are doing onClick
                layout.removeView(holder.twitterButton);
        }
        if(emailAddress == null) {
            ViewGroup layout = (ViewGroup) holder.mailButton.getParent();
            if(null!=layout) //for safety only  as you are doing onClick
                layout.removeView(holder.mailButton);
        }
        if(twitterHandle != null) {
            holder.twitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterHandle)));
                    } catch (Exception e) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + emailAddress)));
                    }
                }
            });
        }
        if(emailAddress != null) {
            holder.mailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mData.get(position).getEmail()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SpartaHack Help");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(intent, ""));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}