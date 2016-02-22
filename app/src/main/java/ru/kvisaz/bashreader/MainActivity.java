package ru.kvisaz.bashreader;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.adapter.AdapterDataFactory;
import ru.kvisaz.bashreader.adapter.AdapterMapping;
import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashPageTest1;
import ru.kvisaz.bashreader.model.BashPageTest2;
import ru.kvisaz.bashreader.model.BashPageType;
import ru.kvisaz.bashreader.model.Constants;
import ru.kvisaz.bashreader.loader.LoaderBash;
import ru.kvisaz.bashreader.parser.Parser;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // todo 3  - вывод HTML в TextView с форматированием


    TextView sampleText;
    ListView listView;
    SimpleAdapter adapter;
    ArrayList<Map<String,Object>> currentQuotes;
    final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBar();
        setupFAB();

        // todo - отделить настройку адаптера от вывода страниц
        setupListView();

        // тест
        showBashPage(new BashPageTest2());

        startBashStringLoader();

    }


    private void setupListView() {
        listView = (ListView) findViewById(R.id.listViewMy);
        setupListViewAdapter(listView);
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

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    //  Loader .....................................
    private Loader<String> startBashStringLoader() {
        return getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        switch(id){
            case(LOADER_ID):
                int pageId = 0;
                BashPageType type = BashPageType.Index;
                return new LoaderBash(this,pageId,type);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(loader==null){
            Log.d(Constants.LOGTAG,"Null Loader in Main Activity");
            return;        }

        int id = loader.getId();
        switch(id){
            case(LOADER_ID):
                refreshContentOnScreen(data);
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {
        int id = loader.getId();
        switch(id){
            case(R.id.sampleText):
                Log.d(Constants.LOGTAG, "Sample Loader Reset");
                break;
        }
    }

    //  Refresh screen .....................................
    private void refreshContentOnScreen(String data) {
        // sampleText.setText(data);
        showBashPage(Parser.convert(data));
    }

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

}
