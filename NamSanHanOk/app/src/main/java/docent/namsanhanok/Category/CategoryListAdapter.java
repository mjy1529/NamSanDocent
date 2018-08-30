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
import docent.namsanhanok.Manager.DocentMemList;
import docent.namsanhanok.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {
    private Context context;
    public ArrayList<DocentData> docentDataList;

    public CategoryListAdapter(Context context, ArrayList<DocentData> docentDataList) {
        this.context = context;
        this.docentDataList = docentDataList;
    }

    public void setAdapter(ArrayList<DocentData> docentDataList) {
        this.docentDataList = docentDataList;
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

        Log.d("check1", "categoryList img_url : " + Environment.getExternalStorageDirectory() + docentDataList.get(position).docent_image_url);

        Glide.with(context)
                    .load(Environment.getExternalStorageDirectory() + docentDataList.get(position).docent_image_url)
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.VH_category_list_image);

        holder.VH_category_list_text_title.setText(docentDataList.get(position).docent_title);

        holder.VH_category_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentActivity.class);
                intent.putExtra("docentObject", docentDataList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.VH_category_list_text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentActivity.class);
                intent.putExtra("docentObject", docentDataList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentDataList != null ? docentDataList.size() : 0;
    }
}
