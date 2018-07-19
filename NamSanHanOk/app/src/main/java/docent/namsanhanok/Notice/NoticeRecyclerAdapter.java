package docent.namsanhanok.Notice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import docent.namsanhanok.R;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.ItemViewHolder> {
    ArrayList<NoticeRecyclerItem> noticeList;

    public NoticeRecyclerAdapter(ArrayList<NoticeRecyclerItem> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public NoticeRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_recycler, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoticeRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.notice_title.setText(noticeList.get(position).getTitle());
        holder.notice_date.setText(noticeList.get(position).getDate());
        holder.notice_readCnt.setText(String.valueOf(noticeList.get(position).getRead_cnt()));

        holder.notice_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.notice_recyclerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout notice_recyclerLayout;
        private TextView notice_title;
        private TextView notice_date;
        private TextView notice_readCnt;

        public ItemViewHolder(View view) {
            super(view);
            notice_recyclerLayout = (RelativeLayout) view.findViewById(R.id.notice_recyclerLayout);
            notice_title = (TextView) view.findViewById(R.id.notice_title);
            notice_date = (TextView) view.findViewById(R.id.notice_date);
            notice_readCnt = (TextView) view.findViewById(R.id.notice_readCnt);
        }
    }
}
