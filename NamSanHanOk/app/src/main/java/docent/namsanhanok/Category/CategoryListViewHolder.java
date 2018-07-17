package docent.namsanhanok.Category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import docent.namsanhanok.R;

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

        public ImageView VH_category_list_image;
        public TextView VH_category_list_text_title;

        public CategoryListViewHolder(View itemView) {
            super(itemView);

            VH_category_list_image = (ImageView)itemView.findViewById(R.id.category_list_recycler_image);
            VH_category_list_text_title = (TextView)itemView.findViewById(R.id.category_list_recycler_title);
        }
}

