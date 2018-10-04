package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import docent.namsanhanok.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private Context context;
    public ArrayList<CategoryData> categoryDataList;

    public CategoryAdapter(Context context, ArrayList<CategoryData> categoryDataList) {
        this.context = context;
        this.categoryDataList = categoryDataList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_recycler, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        //view.getLayoutParams().height = parent.getHeight() / getItemCount();

        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        final int blackFilter = context.getResources().getColor(R.color.black_color_filter);
        final PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);

        Log.d("check1", "categoryList img_url : " + Environment.getExternalStorageDirectory() + categoryDataList.get(position).category_image_url);

        Glide.with(context).load(Environment.getExternalStorageDirectory() + categoryDataList.get(position).category_image_url)
                .apply(new RequestOptions()
                .centerCrop()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.VH_category_layout.setBackground(resource);
                    resource.setColorFilter(blakcColorFilter);
                }

            }

        });

        holder.VH_category_title.setText(categoryDataList.get(position).category_title);

        holder.VH_category_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryListActivity.class);
                intent.putExtra("category", categoryDataList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.VH_category_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryListActivity.class);
                intent.putExtra("category", categoryDataList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryDataList != null ? categoryDataList.size() : 0;
    }

}
