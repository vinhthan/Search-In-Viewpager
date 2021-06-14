package com.example.searchviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener, IFragmentListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ArrayList<ISearch> iSearch = new ArrayList<>();
    private MenuItem searchMenuItem;
    private String newText;
    private PageAdapter adapter;
    ArrayList<String> listData = null;

    IDataCallback iDataCallback = null;

    public void setiDataCallback(IDataCallback iDataCallback) {
        this.iDataCallback = iDataCallback;
        iDataCallback.onFragmentCreated(listData);
    }

    @Override
    public void addiSearch(ISearch iSearch) {
        this.iSearch.add(iSearch);
    }

    @Override
    public void removeISearch(ISearch iSearch) {
        this.iSearch.remove(iSearch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        listData = new ArrayList<>();
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), newText);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }


    public void getDataFromFragment_one(ArrayList<String> listData) {
        this.listData = listData;
        Log.e("-->", "" + listData.toString());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.newText = newText;
        adapter.setTextQueryChanged(newText);

        for (ISearch iSearchLocal : this.iSearch)
            iSearchLocal.onTextQuery(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}