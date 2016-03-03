package ru.kvisaz.bashreader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.adapter.*;
import ru.kvisaz.bashreader.model.*;
import ru.kvisaz.bashreader.loader.LoaderBash;
import ru.kvisaz.bashreader.parser.Parser;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // todo 3 - навигация по страницам

    // todo 5  - создание БД
    // todo 6  - сохранение полученных страниц в БД
    // todo 7  - получаем страницы из БД
    // todo 8 - навигация по страницам (если нет в БД - обращаемся в онлайн с уведомлением)

    // todo 9 - комиксы

    // todo легкий фикс1 - при обратном повороте лоадер обращается к серверу (при первом нет), надо чтобы не делал

    boolean isStaticDrawer;

    private ListView drawerListView;
    private View drawer;

    private DrawerLayout drawerLayout;

    private int topicUsed;
    private int topicCurrent;

    Toolbar toolbar;

    LinearLayout comicsLayout;

    ListView listViewBashQuotes;
    SimpleAdapter adapter;
    ArrayList<Map<String,Object>> currentQuotes;

    final int BASH_LOADER_ID = 1;

    Bundle loaderArgs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStaticDrawer = getResources().getBoolean(R.bool.w820dp);

        // fix for orientation change
        // http://stackoverflow.com/questions/10170481/loader-can-not-be-restarted-after-orientation-changed
        // just to initialize it
        this.getLoaderManager();

        setupBar();
        setupDrawer();
        setupOutputView();

        if (savedInstanceState == null)
        {
            showQuotesOrComics(topicUsed);
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(Constants.SAVEDSTATE_TOPIC, topicUsed);
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        topicUsed = savedInstanceState.getInt(Constants.SAVEDSTATE_TOPIC);

        // title and page
        showQuotesOrComics(topicUsed);

    }

    private void setupOutputView() {
        listViewBashQuotes = (ListView) findViewById(R.id.listOfBashQuotes);
        comicsLayout = (LinearLayout) findViewById(R.id.comicsLayout);
        comicsLayout.setSaveEnabled(true);

        setupListViewAdapter(listViewBashQuotes);
    }

    private void setupListViewAdapter(ListView listView) {
        int itemViewId = R.layout.quote;
        currentQuotes = AdapterDataFactory.getData(new BashPage()); // no quotes yet
        adapter = new SimpleAdapter(this,
                currentQuotes,
                itemViewId,
                AdapterMapping.from,
                AdapterMapping.to);
        listView.setAdapter(adapter);
    }




    private void setupBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(BashMenu.getTitle(topicUsed));

        // не работает само по себе - см. https://code.google.com/p/android/issues/detail?id=77763
        //  toolbar.setTitle(BashMenu.getTitle(topicUsed));

        if(!isStaticDrawer){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_refresh) {
            showMessage(getString(R.string.action_refresh_message));
            restartBashLoader(topicUsed);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // show content .....................................................................
    private void showBashPage(BashPage bashPage){

        if(bashPage==null){
            showMessage(getResources().getString(R.string.ui_server_error_message));
            return;
        }

        topicUsed = topicCurrent;
        refreshQuotes(bashPage);
    }

    private void refreshQuotes(BashPage bashPage) {
        //  смотрите, тут интересный для новичков баг, связанный с тем, как работает адаптер и ссылочные переменные
        // currentQuotes = AdapterDataFactory.getData(bashPage); // не работает!
        // присваивание создавало новый объект
        // и currentQuotes ссылалось на новый массив, который не был связан с адаптером
        //  а связанный массив оставался в памяти, т.к. ссылка на него осталась в адаптере
        // то есть у нас 1. не обновлялся список 2. возникала течь

        // очистить тот же массив и добавить в него все данные
        // - вот рабочий вариант для обновления ListView полностью
        currentQuotes.clear();
        currentQuotes.addAll(AdapterDataFactory.getData(bashPage));
        adapter.notifyDataSetChanged();
    }

    //  Loader ..........................................................................
    private void restartBashLoader(int topicNumber) {
       if(!isNetworkAvailable()) {
            showMessage(getResources().getString(R.string.ui_internet_error_message));
            return;
        }

        setLoaderArgs(topicNumber);
        getLoaderManager().restartLoader(BASH_LOADER_ID, loaderArgs, this);

    }

    private void reinitTopic(int topicNumber) {
        setLoaderArgs(topicNumber);
        getLoaderManager().initLoader(BASH_LOADER_ID, loaderArgs, this);
    }

    private Bundle setLoaderArgs(int topicNumber) {
        loaderArgs = new Bundle();
        loaderArgs.putInt(BashMenu.bundleStringName, topicNumber);
        return loaderArgs;
    }


    @Override
    public Loader<String> onCreateLoader(int loaderId, Bundle args) {
        int topicNumber = args.getInt(BashMenu.bundleStringName);
        BashPageType type = BashMenu.getType(topicNumber);
        int pageId = 0;
        return new LoaderBash(this,pageId,type);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(loader==null){
            Log.d(Constants.LOGTAG, "Null Loader in Main Activity");
            return;
        }

        // todo обработка комикса
        showBashPage(Parser.convert(data));
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        switch(loader.getId()){
            case(BASH_LOADER_ID):
                Log.d(Constants.LOGTAG, "Sample Loader Reset");
                break;
        }
    }


    //  Drawer ..........................................................................

    private void setupDrawer() {



        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);

        drawer = findViewById(R.id.drawerLayout);

        drawerListView = (ListView)findViewById(R.id.drawerListView);
        drawerListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                BashMenu.getNamesArray()));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        if(isStaticDrawer){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawer);
            drawerLayout.setScrimColor(Color.TRANSPARENT);
        }
    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerListView.setItemChecked(position, true);
            showQuotesOrComics(position);

            if(!isStaticDrawer) {
                drawerLayout.closeDrawer(drawer);
            }
        }

    }

    // ..........................................................................
    private void showQuotesOrComics(int topicNumber) {
        topicCurrent = topicNumber;

        log("showQuotesOrComics...........");
        log("topicCurrent = "+topicCurrent);

        getSupportActionBar().setTitle(BashMenu.getTitle(topicCurrent));

        if(BashMenu.getType(topicNumber) == BashPageType.Comics)
        {
            showMessage("Show Comics - menu position " + topicNumber);
            showComicsView(true);
        }
        else {
            showComicsView(false);
            loadTopic(topicNumber);
        }
    }

    private void loadTopic(int topicNumber){
        log("loadTopic...........");
        topicCurrent = topicNumber;
        restartBashLoader(topicNumber);
    }

    private void showComicsView(boolean isShowComic) {
        if(isShowComic){
            comicsLayout.setVisibility(View.VISIBLE);
        }
        else{
            comicsLayout.setVisibility(View.GONE);
        }
    }

    private void showMessage(String message){
        Snackbar.make(listViewBashQuotes, message, Snackbar.LENGTH_LONG).show();
    }

    private void log(String message) {
        Log.d(Constants.LOGTAG,message);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
