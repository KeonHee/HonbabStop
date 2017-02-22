package com.landvibe.android.honbabstop.search.presenter;

import android.app.Activity;

/**
 * Created by user on 2017-02-22.
 */

public interface SearchPresenter {
    interface View{

        void moveToAddChatActivity();

        void showSearchBar();

        void showSuggestions(String[] suggestions);

    }

    interface Presenter{
        void attachView(SearchPresenter.View view, Activity activity);

        void detachView();

        void searchLocation(String query);

        void loadCoord(int position);

    }
}
