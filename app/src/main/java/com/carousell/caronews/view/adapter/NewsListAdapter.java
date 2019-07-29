package com.carousell.caronews.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carousell.caronews.R;
import com.carousell.caronews.model.pojo.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.carousell.caronews.util.RelativeTimeHelper.getRelativeTime;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private final Context mContext;
    private List<News> dataList = new ArrayList<>();

    public NewsListAdapter(Context context, final List<News> newsList) {
        mContext = context;
        dataList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        News news = dataList.get(position);

        holder.title.setText(news.getTitle());
        holder.description.setText(news.getDescription());

        holder.createdOn.setText(getRelativeTime(mContext,news.getTimeCreated()));

        if (news.getBannerUrl() != null) {
            Glide.with(mContext)
                    .load(news.getBannerUrl())
                    .apply(new RequestOptions().centerCrop())
                    //.error(R.mipmap.ic_launcher)
                    .into(holder.image);
        }

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_listitem) ImageView image;
        @BindView(R.id.textview_title) TextView title;
        @BindView(R.id.textview_description) TextView description;
        @BindView(R.id.textview_createdBy) TextView createdOn;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<News> getDataList() {
        return dataList;
    }

    public void updateItems(List<News> data) {
        this.dataList = data;
        notifyDataSetChanged();
    }
}