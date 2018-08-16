package docent.namsanhanok.Category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import docent.namsanhanok.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout VH_category_layout;
    public TextView VH_category_title;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        VH_category_layout = (LinearLayout) itemView.findViewById(R.id.category_recycler_layout);
        VH_category_title = (TextView)itemView.findViewById(R.id.category_title);
    }
}
