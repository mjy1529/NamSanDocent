package docent.namsanhanok.Category;

import android.content.Context;
import android.content.Intent;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {
    private Context context;
    public ArrayList<DocentData> docentData;

    public CategoryListAdapter(Context context, ArrayList<DocentData> docentData) {
        this.context = context;
        this.docentData = docentData;
    }

    public void setAdapter(ArrayList<DocentData> docentData) {
        this.docentData = docentData;
        notifyDataSetChanged();
    }


    @Override
    public CategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_list_recycler, parent, false);
        CategoryListViewHolder categoryListViewHolder = new CategoryListViewHolder(view);

        return categoryListViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryListViewHolder holder, final int position) {

        Glide.with(context)
                    .load(Environment.getExternalStorageDirectory() + docentData.get(position).docent_image_url)
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.VH_category_list_image);

//        holder.VH_category_image.setImageResource(categoryActivityItem.get(position).getImage());
        holder.VH_category_list_text_title.setText(docentData.get(position).docent_title);



        holder.VH_category_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int docent_postion = position;
                Intent intent = new Intent(context, DocentActivity.class);
                intent.putExtra("cate_id", docentData.get(position).category_id);
                intent.putExtra("position", docent_postion);
                intent.putExtra("docent_id", docentData.get(position).docent_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.VH_category_list_text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("cate_id", docentData.get(position).category_id);
                intent.putExtra("docent_id", docentData.get(position).docent_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentData != null ? docentData.size() : 0;
    }



}
