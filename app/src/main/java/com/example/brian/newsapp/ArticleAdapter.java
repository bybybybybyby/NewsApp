package com.example.brian.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        final Article currentArticle = getItem(position);

        // Find the TextView with the title
        TextView title = (TextView) convertView.findViewById(R.id.title_text_view);
        title.setText(currentArticle.getTitle());

        // Find the TextView with the section
        TextView section = (TextView) convertView.findViewById(R.id.section_text_view);
        section.setText(currentArticle.getSection());

        // Find the TextView with the author
        TextView author = (TextView) convertView.findViewById(R.id.author_text_view);
        author.setText(currentArticle.getAuthor());

        return convertView;

    }





}
