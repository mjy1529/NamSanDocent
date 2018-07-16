package docent.namsanhanok;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DocentViewHolder extends RecyclerView.ViewHolder {
    public ImageView VH_docent_image;
    public TextView VH_docent_text_title;

    public DocentViewHolder(View itemView) {
        super(itemView);

        VH_docent_image = (ImageView)itemView.findViewById(R.id.docent_recycler_image);
        VH_docent_text_title = (TextView)itemView.findViewById(R.id.docent_recycler_title);
    }
}

