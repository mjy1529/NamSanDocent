package docent.namsanhanok.Docent;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import docent.namsanhanok.Docent.DocentDetailData;

import docent.namsanhanok.R;

public class DocentAdapter extends RecyclerView.Adapter<DocentViewHolder> {
    private Context context;
    public ArrayList<DocentDetailData> docentDetailData;

    public DocentAdapter(Context context, ArrayList<DocentDetailData> docentDetailData) {
        this.context = context;
        this.docentDetailData = docentDetailData;
    }


    @Override
    public DocentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_docent_recycler, parent, false);
        DocentViewHolder docentViewHolder = new DocentViewHolder(view);

        return docentViewHolder;
    }

    @Override
    public void onBindViewHolder(DocentViewHolder holder, final int position) {

        //recycler viewholder
        Glide.with(context)
                .load(Environment.getExternalStorageDirectory() + docentDetailData.get(position).docent_detail_image_url)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.VH_docent_image);

        Log.d("check1", "docentAdapter imageurl: " + Environment.getExternalStorageDirectory() + docentDetailData.get(position).docent_detail_image_url);

        holder.VH_docent_text_title.setText(docentDetailData.get(position).docent_detail_title);

        holder.VH_docent_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentPopUpActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("docent_id", docentDetailData.get(position).docent_id);

                Log.d("check1" , "docent : " + docentDetailData.get(position).docent_id);
                Log.d("check1" , "docent_detail_id : " + docentDetailData.get(position).docent_detail_id);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentDetailData != null ? docentDetailData.size() : 0;
    }
}
