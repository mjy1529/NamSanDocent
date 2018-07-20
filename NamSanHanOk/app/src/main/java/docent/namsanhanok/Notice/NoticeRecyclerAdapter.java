package docent.namsanhanok.Notice;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import docent.namsanhanok.R;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    ArrayList<NoticeRecyclerItem> noticeList;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public NoticeRecyclerAdapter(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        noticeList = new ArrayList<>();
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView) {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.loadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return noticeList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new NoticeItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_recycler, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item_progress, parent, false));
        }
    }

    public void addAll(ArrayList<NoticeRecyclerItem> noticeList) {
        this.noticeList.clear();
        this.noticeList.addAll(noticeList);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<NoticeRecyclerItem> noticeList) {
        this.noticeList.addAll(noticeList);
        notifyItemChanged(0, this.noticeList.size());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoticeItemViewHolder) {
            final NoticeRecyclerItem item = (NoticeRecyclerItem) noticeList.get(position);
            ((NoticeItemViewHolder) holder).notice_title.setText(item.getTitle());
            ((NoticeItemViewHolder) holder).notice_date.setText(item.getDate());
            ((NoticeItemViewHolder) holder).notice_readCnt.setText(String.valueOf(item.getRead_cnt()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoticeRecyclerItem sendObject = new NoticeRecyclerItem();
                    sendObject.setTitle(item.getTitle());
                    sendObject.setDate(item.getDate());
                    sendObject.setRead_cnt(item.getRead_cnt());
                    sendObject.setContent(item.getContent());

                    Intent intent = new Intent(view.getContext(), NoticeReadActivity.class);
                    intent.putExtra("object", sendObject);
                    view.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }

    @Override
    public int getItemCount() {
        return noticeList == null ? 0 : noticeList.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new android.os.Handler().post(new Runnable() {
                @Override
                public void run() {
                    noticeList.add(null);
                    notifyItemInserted(noticeList.size() - 1);
                }
            });
        } else {
            noticeList.remove(noticeList.size() - 1);
            notifyItemRemoved(noticeList.size());
        }
    }

    class NoticeItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout notice_recyclerLayout;
        private TextView notice_title;
        private TextView notice_date;
        private TextView notice_readCnt;

        public NoticeItemViewHolder(View view) {
            super(view);
            notice_recyclerLayout = (RelativeLayout) view.findViewById(R.id.notice_recyclerLayout);
            notice_title = (TextView) view.findViewById(R.id.notice_title);
            notice_date = (TextView) view.findViewById(R.id.notice_date);
            notice_readCnt = (TextView) view.findViewById(R.id.notice_readCnt);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }
}
