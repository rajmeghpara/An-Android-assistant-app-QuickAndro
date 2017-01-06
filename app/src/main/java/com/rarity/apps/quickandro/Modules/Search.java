package com.rarity.apps.quickandro.Modules;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Search {

    private Context context;

    public Search(Context context){
        this.context = context;
    }

    public String googleSearch(String query){
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SearchManager.QUERY, query);
        context.startActivity(intent);
        return "Searching Google for " + query;
    }

    public String wikiSearch(String query){
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.com/wiki/" + query.replaceAll(" ", "_")) );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return "Searching Wikipedia for " + query;
    }

    public String dictionarySearch(String query){
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.dictionary.com/browse/" + query) );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return "Searching dictionary for " + query;
    }

    public String youtubeSearch(String query){
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/results?search_query=" + query.replaceAll(" ", "+")) );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return "Searching youtube for " + query;
    }
}
