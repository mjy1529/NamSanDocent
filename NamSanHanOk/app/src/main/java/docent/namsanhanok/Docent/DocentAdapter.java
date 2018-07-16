package docent.namsanhanok.Docent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import docent.namsanhanok.R;

public class DocentAdapter extends RecyclerView.Adapter<DocentViewHolder> {
    private Context context;
    public ArrayList<DocentActivityItem> docentActivityItem;

    public DocentAdapter(Context context, ArrayList<DocentActivityItem> docentActivityItem) {
        this.context = context;
        this.docentActivityItem = docentActivityItem;
    }


    @Override
    public DocentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_docent_recycler, parent, false);
        DocentViewHolder docentViewHolder = new DocentViewHolder(view);

        return docentViewHolder;
    }

    @Override
    public void onBindViewHolder(DocentViewHolder holder, final int position) {
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

        holder.VH_docent_image.setImageResource(docentActivityItem.get(position).getImage());
        holder.VH_docent_text_title.setText(docentActivityItem.get(position).getTitle());


        holder.VH_docent_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocentPopUpActivity.class);
                intent.putExtra("title", docentActivityItem.get(position).getTitle());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentActivityItem != null ? docentActivityItem.size() : 0;
    }
}
