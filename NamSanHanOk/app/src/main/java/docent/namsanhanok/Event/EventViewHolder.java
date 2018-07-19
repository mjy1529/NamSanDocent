package docent.namsanhanok.Event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import docent.namsanhanok.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public ImageView VH_event_image;
    public TextView VH_event_title;
    public TextView VH_event_place;
    public TextView VH_start_date;
    public TextView VH_end_date;
    public TextView VH_complete;
    public LinearLayout VH_complete_background;

    public EventViewHolder(View itemView) {
        super(itemView);

        VH_event_image = (ImageView)itemView.findViewById(R.id.event_recycler_image);
        VH_event_title = (TextView)itemView.findViewById(R.id.event_recycler_title);
        VH_event_place = (TextView)itemView.findViewById(R.id.event_recycler_place);
        VH_start_date = (TextView)itemView.findViewById(R.id.event_recycler_start_date);
        VH_end_date = (TextView)itemView.findViewById(R.id.event_recycler_end_date);
        VH_complete = (TextView)itemView.findViewById(R.id.event_complete);
        VH_complete_background = (LinearLayout)itemView.findViewById(R.id.event_complete_background);

    }
}
