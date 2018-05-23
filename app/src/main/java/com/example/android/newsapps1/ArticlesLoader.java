package com.example.android.newsapps1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ArticlesLoader extends AsyncTaskLoader<List<Articles>> {

    //Query url
    private String GuardianUrl;
    //the Tag for the Log messages used for exceptions
    private static final String LOG_TAG = ArticlesLoader.class.getName();

    //public constructor of ArticlesLoader class
    public ArticlesLoader(Context context, String url){
        super(context);
        GuardianUrl = url;
    }

    //start loading the data
    protected void onStartLoading(){
        forceLoad();
    }

    //perform actions in the background thread
    @Override
    public List<Articles> loadInBackground() {
        //if the GuardianUrl is null, return it (it's contents)
        if(GuardianUrl == null){
            return null;
        }

        List<Articles> articles = QueryUtils.fetchArticlesData(GuardianUrl);
        return articles;
    }
}
