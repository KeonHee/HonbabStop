package com.landvibe.android.honbabstop.search;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.search.adapter.SearchAdapter;
import com.landvibe.android.honbabstop.search.presenter.SearchPresenter;
import com.landvibe.android.honbabstop.search.presenter.SearchPresenterImpl;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchPresenter.View,
        MaterialSearchView.OnQueryTextListener, AdapterView.OnItemClickListener{

    private final static String TAG="SearchActivity";

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_search_image)
    ImageView mSearchImageView;

    @BindView(R.id.tv_search_text)
    TextView mSearchTextView;

    private SearchPresenter.Presenter mSearchPresenter;

    private SearchAdapter mSearchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        setActionBar();

        mSearchPresenter=new SearchPresenterImpl();
        mSearchPresenter.attachView(this, this);

        mSearchView.post(()->{
            mSearchView.showSearch();
            mSearchView.showKeyboard(getWindow().getDecorView().getRootView());
        });
        mSearchView.setVoiceSearch(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnItemClickListener(this);

        mSearchImageView.setOnClickListener(v->mSearchView.showSearch());

    }

    private void setActionBar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.detachView();
        mSearchPresenter=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveToAddChatActivity();
                return true;
            case R.id.action_search:
                showSearchBar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void moveToAddChatActivity() {
        finish();
    }

    @Override
    public void showSearchBar(){
        mSearchView.showSearch();
    }

    @Override
    public void showSuggestions(String[] suggestions) {
        for(String s : suggestions){
            Log.d(TAG, "suggestions : " + s);
        }
        mSearchView.post(()->{
            mSearchAdapter = new SearchAdapter(this, suggestions);
            mSearchView.setAdapter(mSearchAdapter);
        });
    }

    /**
     * MaterialSearchView.OnQueryTextListener
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "query : "+query);
        mSearchPresenter.searchLocation(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    /**
     * AdapterView.OnItemClickListener
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSearchView.dismissSuggestions();

        String selectedName = (String) mSearchAdapter.getItem(position);
        Log.d(TAG,"position : "+position);
        Log.d(TAG,"selectedName : "+selectedName);
        mSearchPresenter.loadCoord(position);

    }

}
