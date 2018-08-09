package docent.namsanhanok.Event;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import docent.namsanhanok.R;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder>{

    private Context context;
    public ArrayList<EventData> eventList;

    public EventAdapter(Context context, ArrayList<EventData> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_recycler, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);

        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {
        Glide.with(context)
                .load(Environment.getExternalStorageDirectory() + eventList.get(position).getEvent_image_url())
                .into(holder.VH_event_image);
        Log.d("load", Environment.getExternalStorageDirectory() + eventList.get(position).getEvent_image_url());

        holder.VH_event_title.setText(eventList.get(position).getEvent_title());
        holder.VH_event_place.setText(eventList.get(position).getEvent_place());
        holder.VH_start_date.setText(eventList.get(position).getEvent_start_date());
        holder.VH_end_date.setText(eventList.get(position).getEvent_end_date());
        if(eventList.get(position).getEvent_complete().equals("진행중")){
            holder.VH_complete.setText(eventList.get(position).getEvent_complete());
            holder.VH_complete_background.setBackgroundResource(R.drawable.round_shape_ing);
        }
        else{
            holder.VH_complete.setText(eventList.get(position).getEvent_complete());
            holder.VH_complete_background.setBackgroundResource(R.drawable.round_shape_complete);
        }


    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }
}
