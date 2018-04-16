package com.berbageek.beritaku.feature.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berbageek.beritaku.R;
import com.berbageek.beritaku.feature.home.model.ArticleItem;
import com.berbageek.beritaku.feature.home.model.ListItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListViewHolder> {
    private List<ListItem> listItems;

    private SimpleDateFormat showFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    public ListItemAdapter(List<ListItem> listItems) {
        if (listItems != null) {
            this.listItems = listItems;
        } else {
            this.listItems = new ArrayList<>();
        }
    }

    public void clearListItems() {
        listItems.clear();
        notifyDataSetChanged();
    }

    public void replaceListItems(List<ListItem> newItems) {
        listItems.clear();
        listItems.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bindView(((ArticleItem) listItems.get(position)));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    abstract class ListViewHolder<T> extends RecyclerView.ViewHolder {
        ListViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindView(T t);
    }

    public class ArticleViewHolder extends ListViewHolder<ArticleItem> {

        private ImageView articleImageView;
        private TextView titleView;
        private TextView descriptionView;
        private TextView sourceView;
        private TextView publishedTimeView;


        ArticleViewHolder(View itemView) {
            super(itemView);
            articleImageView = itemView.findViewById(R.id.article_imageview);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
            sourceView = itemView.findViewById(R.id.source);
            publishedTimeView = itemView.findViewById(R.id.published_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        openCustomTab(pos);
                    }
                }
            });
        }

        void openCustomTab(int position) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            final Context context = itemView.getContext();
            builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(((ArticleItem) listItems.get(position)).getUrl()));
        }

        @Override
        void bindView(ArticleItem article) {
            if (!TextUtils.isEmpty(article.getImagePath())) {
                Picasso.get()
                        .load(article.getImagePath())
                        .placeholder(R.drawable.ic_image_grey_300_24dp)
                        .error(R.drawable.ic_broken_image_grey_300_24dp)
                        .into(articleImageView);
            } else {
                Picasso.get()
                        .load(R.drawable.ic_image_grey_300_24dp)
                        .into(articleImageView);
            }
            titleView.setText(article.getTitle());
            if (!TextUtils.isEmpty(article.getDescription())) {
                descriptionView.setVisibility(View.VISIBLE);
                descriptionView.setText(article.getDescription());
            } else {
                descriptionView.setVisibility(View.GONE);
            }
            sourceView.setText(article.getSourceName());
            if (!TextUtils.isEmpty(article.getPublishedAt())) {
                String publishedAt;
                try {
                    Date articleTime = dataFormat.parse(article.getPublishedAt());
                    publishedAt = showFormat.format(articleTime);
                } catch (Exception e) {
                    publishedAt = "-";
                }
                publishedTimeView.setText(publishedAt);
            } else {
                publishedTimeView.setText("-");
            }
        }
    }
}
