package docent.namsanhanok.Docent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Docent.DocentDetailData;

import docent.namsanhanok.R;

public class DocentAdapter extends RecyclerView.Adapter<DocentViewHolder> {
    private Context context;
    public ArrayList<DocentDetailData> docentDetailDataList;

    public DocentAdapter(Context context, ArrayList<DocentDetailData> docentDetailDataList) {
        this.context = context;
        this.docentDetailDataList = docentDetailDataList;
    }

    public void setAdapter(ArrayList<DocentDetailData> docentDetailData) {
                this.docentDetailDataList = docentDetailData;
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
                .load(Environment.getExternalStorageDirectory() + docentDetailDataList.get(position).docent_detail_image_url)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.VH_docent_image);

        Log.d("check1", "docentAdapter imageurl: " + Environment.getExternalStorageDirectory() + docentDetailDataList.get(position).docent_detail_image_url);

        holder.VH_docent_text_title.setText(docentDetailDataList.get(position).docent_detail_title);

        holder.VH_docent_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Application.getInstance().checkInternet()) {
                    Intent intent = new Intent(context, DocentPopUpActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("docent_id", docentDetailDataList.get(position).docent_id);

                    Log.d("check1", "docent : " + docentDetailDataList.get(position).docent_id);
                    Log.d("check1", "docent_detail_id : " + docentDetailDataList.get(position).docent_detail_id);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, R.string.wifi_disconnect, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return docentDetailDataList != null ? docentDetailDataList.size() : 0;
    }

    public boolean checkInternet() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}

