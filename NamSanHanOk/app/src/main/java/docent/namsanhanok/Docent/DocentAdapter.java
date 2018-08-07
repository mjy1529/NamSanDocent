package docent.namsanhanok.Docent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

    public void setAdapter(ArrayList<DocentDetailData> docentDetailData) {
        this.docentDetailData = docentDetailData;
        notifyDataSetChanged();
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
                .load(docentDetailData.get(position).docent_detail_image_url)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.VH_docent_image);

        holder.VH_docent_text_title.setText(docentDetailData.get(position).docent_dtail_title);


        holder.VH_docent_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentPopUpActivity.class);
                intent.putExtra("docent_detail_title", docentDetailData.get(position).docent_dtail_title);
                intent.putExtra("docent_detail_info", docentDetailData.get(position).docent_detail_info);


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentDetailData != null ? docentDetailData.size() : 0;
    }
}
