package ru.kvisaz.bashreader;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
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

import ru.kvisaz.bashreader.adapter.AdapterDataFactory;
import ru.kvisaz.bashreader.adapter.AdapterMapping;
import ru.kvisaz.bashreader.model.BashMenu;
import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashPageTest1;
import ru.kvisaz.bashreader.model.BashPageTest2;
import ru.kvisaz.bashreader.model.BashPageType;
import ru.kvisaz.bashreader.model.Constants;
import ru.kvisaz.bashreader.loader.LoaderBash;
import ru.kvisaz.bashreader.parser.Parser;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // todo 1 - загрузка разных рубрик через Drawer

    // todo 2 - полиш Drawer
//           - запуск по нажатию иконки приложения в ActionBar
//           - статический для планшетов
//           - с тулбаром

    // todo 3 - навигация по страницам

    // todo 3.5 - адаптация к поворотам экрана


    // todo 4  - можно ли подключить Spanned к адаптеру?
    // todo 5  - создание БД
    // todo 6  - сохранение полученных страниц в БД
    // todo 7  - получаем страницы из БД
    // todo 8 - навигация по страницам (если нет в БД - обращаемся в онлайн с уведомлением)

    // todo 9 - комиксы

    boolean tabletWidth;

    private ListView listViewDrawer;
    private DrawerLayout drawerLayout;

    ListView listViewQuotes;
    SimpleAdapter adapter;
    ArrayList<Map<String,Object>> currentQuotes;

    final int BASH_LOADER_ID = 1;

    Bundle loaderArgs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabletWidth = getResources().getBoolean(R.bool.w820dp);
        setupBar();

        setupDrawer();

        // todo 20 - отделить настройку адаптера от вывода страниц
        setupListViewQuotes();

        // тест
        showBashPage(new BashPageTest2());

        startBashLoader();

    }




    private void setupListViewQuotes() {
        listViewQuotes = (ListView) findViewById(R.id.listViewMy);
        setupListViewAdapter(listViewQuotes);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //  Loader ..........................................................................
    private void startBashLoader() {
        setLoaderArgs(0);
        getLoaderManager().restartLoader(BASH_LOADER_ID, loaderArgs, this);
    }

    private void restartBashLoader(int topicNumber) {
        setLoaderArgs(topicNumber);
        getLoaderManager().restartLoader(BASH_LOADER_ID, loaderArgs, this);
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


    //  Refresh screen ..........................................................................

    private void showBashPage(BashPage bashPage){
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



    //  Drawer ..........................................................................

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);

        listViewDrawer = (ListView)findViewById(R.id.drawerListView);

        listViewDrawer.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                BashMenu.getNamesArray()));
        listViewDrawer.setOnItemClickListener(new DrawerItemClickListener());

    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
       @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

            //  todo 00 - смена заголовка у тулбара

            drawerLayout.closeDrawer(listViewDrawer);

        }
    }

    //  todo 01 - вызов команды, показ любой другой рубрики
    private void selectItem(int topicNumber) {
        listViewDrawer.setItemChecked(topicNumber, true);

        restartBashLoader(topicNumber);
    }
}
