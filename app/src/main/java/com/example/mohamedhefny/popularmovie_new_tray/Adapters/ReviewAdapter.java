package com.example.mohamedhefny.popularmovie_new_tray.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mohamedhefny.popularmovie_new_tray.Model.ReviewModel;
import com.example.mohamedhefny.popularmovie_new_tray.R;

import java.util.List;

/**
 * Created by Mohamed Hefny on 20/04/2016.
 */
public class ReviewAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final ReviewModel mLock = new ReviewModel();

    private List<ReviewModel> reviewList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewList) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.reviewList = reviewList;
    }

    public void add(ReviewModel object) {
        synchronized (mLock) {
            reviewList.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            reviewList.clear();
        }
        notifyDataSetChanged();
    }

    public Context getContext() {return context;}

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public ReviewModel getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.review_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final ReviewModel reviewModel = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.reviewAuthor.setText(reviewModel.getAuthor());
        viewHolder.reviewContent.setText(Html.fromHtml(reviewModel.getContent()));

        return view;
    }

    public static class ViewHolder {
        public final TextView reviewAuthor;
        public final TextView reviewContent;

        public ViewHolder(View view) {
            reviewAuthor = (TextView) view.findViewById(R.id.author);
            reviewContent = (TextView) view.findViewById(R.id.content);
        }
    }
}
