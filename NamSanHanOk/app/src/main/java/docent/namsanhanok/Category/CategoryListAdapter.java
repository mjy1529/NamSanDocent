package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {
    private Context context;
    public ArrayList<CategoryListActivityItem> categoryListActivityItem;

    public CategoryListAdapter(Context context, ArrayList<CategoryListActivityItem> categoryListActivityItem) {
        this.context = context;
        this.categoryListActivityItem = categoryListActivityItem;
    }


    @Override
    public CategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_list_recycler, parent, false);
        CategoryListViewHolder categoryListViewHolder = new CategoryListViewHolder(view);

        return categoryListViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryListViewHolder holder, final int position) {
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
                    .load(categoryListActivityItem.get(position).getImage())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.VH_category_list_image);

        final int blackFilter = context.getResources().getColor(R.color.black_color_filter);
        PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);



//        holder.VH_category_image.setImageResource(categoryActivityItem.get(position).getImage());
        holder.VH_category_list_text_title.setText(categoryListActivityItem.get(position).getTitle());
        holder.VH_category_list_image.setColorFilter(blakcColorFilter);



        holder.VH_category_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentActivity.class);
                intent.putExtra("title", categoryListActivityItem.get(position).getTitle());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryListActivityItem != null ? categoryListActivityItem.size() : 0;
    }


}
