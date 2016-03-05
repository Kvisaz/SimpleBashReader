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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ru.kvisaz.bashreader.adapter.*;
import ru.kvisaz.bashreader.model.*;
import ru.kvisaz.bashreader.loader.LoaderBash;
import ru.kvisaz.bashreader.parser.Parser;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // did 3 - навигация по страницам
    //          + нужна функция
    //          +  извлекаем pagecode из страницы в Parser
    //          + дизайн кнопок страниц в quotes_main.xml
    //          + включаем-выключаем кнопки в зависимости от наличия страниц
    //          + передаем параметр страницы и обновляем её

    // did Polish
    //          + выделяем текущую страницу
    //          + значки стрелок на prev, next

    //          + изначально пейджер невидим - включаем в setPagerButtons
//              + сообщение о загрузке в startLoader

    // did BUG - btCurrent сбрасывает страницу, а не обновляет текущую
    // did BUG - страницы Abbyss после второй слетают
    // did Polish - при загрузке страницы, отмотать ListView вверх

    // todo Polish
    //          + лампа индикатор онлайна в заголовке Drawer
    //          + если офлайн - желтая, если онлайн - зелёная
    //          - офлайн режим - отключить онлайн-пункты, сообщение
    //          - офлайн режим - оставлять активными только офлайн-часть (буд)
    //                          - 1 пункт "проверить связь"

    // todo 9 - комиксы

    // todo 5  - создание БД
    // todo 6  - сохранение полученных страниц в БД
    // todo 7  - получаем страницы из БД
    // todo 8 - навигация по страницам (если нет в БД - обращаемся в онлайн с уведомлением)



    private boolean isStaticDrawer;
    private boolean needReload;
    private boolean isOnline;

    private ListView drawerListView;
    private View drawer;

    private DrawerLayout drawerLayout;

    private int topicUsed;
    private int topicCurrent;

    private BashPageType pageType;
    private String pageCode;

    Toolbar toolbar;
    TextView drawerToolbar;

    LinearLayout comicsLayout;
    LinearLayout quotesLayout;
    LinearLayout pagerLayout;
    Button btPrev;
    String btPrevCode;
    Button btCurrent;
    String btCurrentCode;
    Button btNext;
    String btNextCode;

    ListView listViewBashQuotes;
    SimpleAdapter adapter;
    ArrayList<Map<String,Object>> currentQuotes;

    final int BASH_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStaticDrawer = getResources().getBoolean(R.bool.w820dp);

        // fix for orientation change
        // http://stackoverflow.com/questions/10170481/loader-can-not-be-restarted-after-orientation-changed
        // just to initialize it
        getLoaderManager();

        setupBar();
        setupDrawer();
        setupPager();
        setupOutputView();

        if (savedInstanceState == null)
        {
            switchTopic(topicUsed);
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
        switchTopic(topicUsed);
    }



    // Setup ..................................................................
    private void setupOutputView() {
        listViewBashQuotes = (ListView) findViewById(R.id.listOfBashQuotes);
        comicsLayout = (LinearLayout) findViewById(R.id.comicsLayout);
        quotesLayout = (LinearLayout)findViewById(R.id.quotesLayout);

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(BashMenu.getTitle(topicUsed));
        // обращение через Toolbar toolbar глючит
        // возможное объяснение - см. https://code.google.com/p/android/issues/detail?id=77763

        if(!isStaticDrawer){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


    }

    // Menu ..................................................................
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
            needReload = true;
            startBashLoader(topicUsed,"");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Pager ..................................................................
    private void setupPager(){
        pagerLayout = (LinearLayout) findViewById(R.id.pagerLayout);
        btPrev = (Button)findViewById(R.id.btPrev);
        btCurrent = (Button)findViewById(R.id.btCurrent);
        btNext = (Button)findViewById(R.id.btNext);

        btPrevCode = "";
        btCurrentCode = "";
        btNextCode = "";

       /* btPrev.setEnabled(false);
        btCurrent.setEnabled(true);
        btNext.setEnabled(false);*/


        View.OnClickListener pagerListener = new PagerListener();
        btPrev.setOnClickListener(pagerListener);
        btCurrent.setOnClickListener(pagerListener);
        btNext.setOnClickListener(pagerListener);
    }

    private void setPagerButtons(String prevCode,String currentCode,String nextCode){
        pagerLayout.setVisibility(View.VISIBLE);
        if(prevCode.length()<1){
            btPrevCode = "";
            btPrev.setText("");
            btPrev.setVisibility(View.INVISIBLE);
        }
        else{
            btPrevCode = prevCode;
            btPrev.setText(getPagerButtonTitle(prevCode));
            btPrev.setVisibility(View.VISIBLE);
        }

        if(currentCode.length()<1) {
            btCurrentCode = "";
            btCurrent.setText(getString(R.string.pager_current_default));
        }
        else{
            btCurrentCode=currentCode;
            btCurrent.setText(getPagerButtonTitle(currentCode));
        }

        if(nextCode.length()<1){
            btNextCode = "";
            btNext.setText("");
            btNext.setVisibility(View.INVISIBLE);
        }
        else{
            btNextCode = nextCode;
            btNext.setText(getPagerButtonTitle(nextCode));
            btNext.setVisibility(View.VISIBLE);
        }
    }

    private String getPagerButtonTitle(String pageCode) {
        String title = pageCode;
        if(pageType==BashPageType.AbyssBest){
            try {
                Date date = new SimpleDateFormat("yyyyMMdd").parse(pageCode);
                title = new SimpleDateFormat("dd.MM.yyyy").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return title;
            }
        }

        return title;
    }

    private class PagerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            needReload = true;
            int id = v.getId();
            switch (id){
                case R.id.btPrev:
                    showMessage(getString(R.string.pager_prev_message)+" "+getPagerButtonTitle(btPrevCode));
                    switchPage(btPrevCode);
                    break;
                case R.id.btCurrent:
                    showMessage(getString(R.string.action_refresh_message)+" "+getPagerButtonTitle(btCurrentCode));
                    switchPage(btCurrentCode);
                    break;
                case R.id.btNext:
                    showMessage(getString(R.string.pager_next_message)+" "+getPagerButtonTitle(btNextCode));
                    switchPage(btNextCode);
                    break;
            }
        }
    }

    // Show content .....................................................................
    private void showBashPage(BashPage bashPage){
        if(bashPage==null){
            showMessage(getResources().getString(R.string.ui_server_error_message));
            return;
        }
        topicUsed = topicCurrent;

        setPagerButtons(bashPage.prevPage, bashPage.currentPage, bashPage.nextPage);
        scrollQuotesToTop();

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
    private void startBashLoader(int topicNumber, String pagecode)
    {
        if(pagecode==null) pagecode="";

        this.pageCode = pagecode;
        this.pageType = BashMenu.getType(topicNumber);

        if(!isNetworkAvailable()) {
            showMessage(getResources().getString(R.string.ui_internet_error_message));
            setOnlineMode(false);
            return;
        }


        setOnlineMode(true);

        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(BashMenu.bundleTopicTag, topicNumber);
        loaderArgs.putString(BashMenu.bundlePageCodeTag, pagecode);

        if(needReload){
            getLoaderManager().restartLoader(BASH_LOADER_ID, loaderArgs, this);
            needReload = false;
        }
        else{
            getLoaderManager().initLoader(BASH_LOADER_ID, loaderArgs, this);
        }
    }

    private void scrollQuotesToTop() {
        // scroll list to begin
        listViewBashQuotes.post(new Runnable() {
            @Override
            public void run() {
                listViewBashQuotes.setSelection(0);
            }
        });
    }

    private void setOnlineMode(boolean online) {
        isOnline = online;
        String toolbarMessage = getString(R.string.app_name);
        if(isOnline){
            setDrawerToolbarIcon(R.drawable.ic_action_circle_green);
            toolbarMessage = toolbarMessage + " " + getString(R.string.drawer_online_message);
          }
        else{
            setDrawerToolbarIcon(R.drawable.ic_action_circle_yell);
            toolbarMessage = toolbarMessage + " " + getString(R.string.drawer_offline_message);
           }
        drawerToolbar.setText(toolbarMessage);
    }

    private void setDrawerToolbarIcon(int resid) {
        drawerToolbar.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(resid),null,null,null);
    }


    @Override
    public Loader<String> onCreateLoader(int loaderId, Bundle args) {
        int topicNumber = args.getInt(BashMenu.bundleTopicTag);
        BashPageType type = BashMenu.getType(topicNumber);

        String pageCode = args.getString(BashMenu.bundlePageCodeTag);
        return new LoaderBash(this,type,pageCode);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(loader==null){
            log("Null Loader in Main Activity");
            return;
        }

        BashPage page = Parser.convert(data, pageType, pageCode);
        showBashPage(page);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        switch(loader.getId()){
            case(BASH_LOADER_ID):
                log("Sample Loader Reset");
                break;
        }
    }


    //  Drawer ..........................................................................
    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);

        drawerToolbar = (TextView) findViewById(R.id.drawerBar);
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

            needReload = true;

            switchTopic(position);

            if(!isStaticDrawer) {
                drawerLayout.closeDrawer(drawer);
            }
        }

    }

    // Switch to Topic..................................................................
    private void switchTopic(int topicNumber) {
        topicCurrent = topicNumber;

        getSupportActionBar().setTitle(BashMenu.getTitle(topicNumber));
        // обращение через toolbar глючит, хоть тресни
        // toolbar.setTitle(BashMenu.getTitle(topicCurrent));

        if(BashMenu.getType(topicCurrent) == BashPageType.Comics)
        {
            showMessage("Show Comics - menu position " + topicCurrent);
            showComicsView(true);
        }
        else {
            showComicsView(false);
            showMessage(getString(R.string.action_refresh_message));
            startBashLoader(topicCurrent, null);
        }
    }

    private void showComicsView(boolean isShowComic) {
        if(isShowComic){
            comicsLayout.setVisibility(View.VISIBLE);
            quotesLayout.setVisibility(View.GONE);

        }
        else{
            comicsLayout.setVisibility(View.GONE);
            quotesLayout.setVisibility(View.VISIBLE);
        }
    }

    // Switch to Page..................................................................
    private void switchPage(String pageCode) {
        startBashLoader(topicUsed, pageCode);
    }


    // ..........................................................................
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
