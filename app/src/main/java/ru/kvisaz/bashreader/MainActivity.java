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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.adapter.*;
import ru.kvisaz.bashreader.model.*;
import ru.kvisaz.bashreader.loader.LoaderBash;
import ru.kvisaz.bashreader.parser.Parser;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

     // todo 2 - обновление титла только после загрузки (если произошла загрузка)

     // todo 3 - навигация по страницам

    // todo 3.5 - адаптация к поворотам экрана


    // todo 4  - можно ли подключить Spanned к адаптеру?
    // todo 5  - создание БД
    // todo 6  - сохранение полученных страниц в БД
    // todo 7  - получаем страницы из БД
    // todo 8 - навигация по страницам (если нет в БД - обращаемся в онлайн с уведомлением)

    // todo 9 - комиксы

    boolean isStaticDrawer;

    private ListView drawerListView;
    private View drawer;

    private DrawerLayout drawerLayout;

    private int topicCurrent;

    Toolbar toolbar;

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

        setupBar();

        setupDrawer();


        // todo 20 - отделить настройку адаптера от вывода страниц
        setupListViewQuotes();

        // тест

        // todo BUG - не выводится этот заголовок
        toolbar.setTitle("Тестовая страница");
        showBashPage(new BashPageTest2());

        loadTopic(0);

    }




    private void setupListViewQuotes() {
        listViewBashQuotes = (ListView) findViewById(R.id.listOfBashQuotes);
        setupListViewAdapter(listViewBashQuotes);
    }

    private void setupListViewAdapter(ListView listView) {
        int itemViewId = R.layout.quote;
        currentQuotes = AdapterDataFactory.getData(new BashPageTest1());
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
            restartBashLoader(topicCurrent);
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

    private void loadTopic(int topicNumber){
        toolbar.setTitle(BashMenu.getTitle(topicNumber));
        restartBashLoader(topicNumber);
    }

    //  Loader ..........................................................................

    private void startBashLoader() {
        restartBashLoader(0);
    }

    private void restartBashLoader(int topicNumber) {

        topicCurrent = topicNumber;

        if(isNetworkAvailable()) {
            setLoaderArgs(topicNumber);
            getLoaderManager().restartLoader(BASH_LOADER_ID, loaderArgs, this);
        }
        else{
            showMessage(getResources().getString(R.string.ui_internet_error_message));
        }
    }

    private Bundle setLoaderArgs(int topicNumber) {
        loaderArgs = new Bundle();
        loaderArgs.putInt(BashMenu.bundleStringName, topicNumber);
        return null;
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
            Log.d(Constants.LOGTAG,"Null Loader in Main Activity");
            return;
        }

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


    // ..........................................................................
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
           loadTopic(position);

           if(!isStaticDrawer)
               drawerLayout.closeDrawer(drawer);
        }
    }




    // ..........................................................................
    private void showMessage(String message){
        Snackbar.make(listViewBashQuotes, message, Snackbar.LENGTH_LONG).show();
    }

}
