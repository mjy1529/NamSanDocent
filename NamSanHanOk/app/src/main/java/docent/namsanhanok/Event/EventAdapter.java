package docent.namsanhanok.Event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
    public ArrayList<EventActivityItem> eventActivityItem;

    public EventAdapter(Context context, ArrayList<EventActivityItem> eventActivityItem) {
        this.context = context;
        this.eventActivityItem = eventActivityItem;
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_recycler, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);

        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {
//        if (homeActivityItem.get(position).getImage() == null) {
//            Glide.with(context)
//                    .load(R.drawable.bae)
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(holder.VH_docent_image);
//        }
//        else {
//            Glide.with(context)
//                    .load(homeActivityItem.get(position).getImage())
//                    .apply(new RequestOptions()
//                            .centerCrop())
//                    .into(holder.VH_docent_image);
//        }
        Glide.with(context)
                .load(eventActivityItem.get(position).getImage())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.VH_event_image);

//        holder.VH_event_image.setImageResource(eventActivityItem.get(position).getImage());
        holder.VH_event_title.setText(eventActivityItem.get(position).getTitle());
        holder.VH_event_place.setText(eventActivityItem.get(position).getPlace());
        holder.VH_start_date.setText(eventActivityItem.get(position).getStartDate());
        holder.VH_end_date.setText(eventActivityItem.get(position).getEndDate());
        if(eventActivityItem.get(position).getComplete().equals("진행중")){
            holder.VH_complete.setText(eventActivityItem.get(position).getComplete());
            holder.VH_complete_background.setBackgroundResource(R.drawable.round_shape_ing);
        }
        else{
            holder.VH_complete.setText(eventActivityItem.get(position).getComplete());
            holder.VH_complete_background.setBackgroundResource(R.drawable.round_shape_complete);
        }


    }

    @Override
    public int getItemCount() {
        return eventActivityItem != null ? eventActivityItem.size() : 0;
    }
}
