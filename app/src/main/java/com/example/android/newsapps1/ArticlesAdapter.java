package com.example.android.newsapps1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ArticlesAdapter extends ArrayAdapter<Articles> {

    private static final String SEPARATE_DATE_TIME = "T";
    private static final String TIME_ENDING_SYMBOL = "Z";
    private static final String ADD_COMMA = ",";
    private static final String ADD_BY_BEFORE_AUTHOR = "By ";

    public ArticlesAdapter(Context context, List<Articles> article) {
        super(context, 0, article);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if convertView is empty, and if it is, use it
        View ArticleView = convertView;
        if (ArticleView == null) {
            ArticleView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //find the current Article at a given position
        Articles currentArticle = getItem(position);

        //find the TextView with id:section and store it to ArticleSection variable
        TextView ArticleSection = ArticleView.findViewById(R.id.section);
        //display the given section of the article in this TextView
        ArticleSection.setText(currentArticle.getSection());

        //find the TextView with id:title and store it to ArticleTitle variable
        TextView ArticleTitle = ArticleView.findViewById(R.id.title);
        //display the given title of the article at this TextView
        ArticleTitle.setText(currentArticle.getTitle());

        //get the publisher's name from the Article object
        String authorName = currentArticle.getAuthorName();
        //variable for the author's name
        //we need this so we can add the word "By" before the author's name and a comma after that
        String authorNameComplete = ADD_BY_BEFORE_AUTHOR + authorName + ADD_COMMA;
        //find the TextView with id:publisher_name and store it to PublisherNameView variable
        TextView PublisherNameView = ArticleView.findViewById(R.id.publisher_name);
        //display the given Publisher Name of the article to this TextView
        PublisherNameView.setText(authorNameComplete);

        //get the whole original Date from the Article object
        String originalDate = currentArticle.getDate();
        //variable to use for Date when we split the Date and Time from the original Date
        String publicationDate="";
        //variable to use for Time when we split Date and Time from the original Date
        String publicationTime="";

        //while the original Date contains the "T" symbol(SEPARATE_DATE_TIME) do the following
        if(originalDate.contains(SEPARATE_DATE_TIME)) {
            //split the string from the Article object into parts where the SEPARATE_DATE_TIME constant is present
            String[] parts = originalDate.split(SEPARATE_DATE_TIME);
            //set the value of the publicationDate variable to be the part that is before the split constant
            publicationDate = parts[0] + ADD_COMMA;
            //set the value of the publicationTime variable to be the part that is after the split constant
            publicationTime = parts[1];
            //while the publicationTime variable contains the "Z" symbol(TIME_ENDING_SYMBOL) do the following
            while (publicationTime.contains(TIME_ENDING_SYMBOL)) {
                //split the publicationTime sring into parts where the TIME_ENDING_SYMBOL constant is present
                String[] wholeTime = publicationTime.split(TIME_ENDING_SYMBOL);
                //set the value publicationTime variable to be the part before the split
                publicationTime = wholeTime[0];
            }
        }
        //find the TextView with id:date and store it to ArticleDate variable
        TextView ArticleDate = ArticleView.findViewById(R.id.date);
        //display the given date to of the article at this TextView
        ArticleDate.setText(publicationDate);

        //find the TextView with id:time and store it to ArticleTime variable
        TextView ArticleTime = ArticleView.findViewById(R.id.time);
        //display the given time of the article in this TextView
        ArticleTime.setText(publicationTime);

        return ArticleView;
    }
}
