//package com.xshengcn.diycode.view.newsdetail;
//
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.xshengcn.diycode.R;
//import com.xshengcn.diycode.entity.news.News;
//
//import me.drakeet.multitype.ItemViewProvider;
//
//public class ReplyHeaderViewProvider extends ItemViewProvider<News, ReplyHeaderViewProvider.ViewHolder> {
//    @NonNull
//    @Override
//    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.view_item_news_reply_header, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull News news) {
//
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
