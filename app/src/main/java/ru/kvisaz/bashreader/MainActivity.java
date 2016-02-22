package ru.kvisaz.bashreader;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import ru.kvisaz.bashreader.adapter.AdapterDataFactory;
import ru.kvisaz.bashreader.adapter.AdapterMapping;
import ru.kvisaz.bashreader.model.BashPageTest;
import ru.kvisaz.bashreader.model.BashPageType;
import ru.kvisaz.bashreader.model.Constants;
import ru.kvisaz.bashreader.loader.LoaderBash;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // todo 2  - load in ListView real page
    //           - parse page into BashPage object

    TextView sampleText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListView();

        setupBar();
        setupFAB();

//        getLoaderManager().initLoader(R.id.sampleText,Bundle.EMPTY,this);

    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.listViewMy);
        setupListViewAdapter(listView);
    }

    private void setupListViewAdapter(ListView listView) {
        int itemViewId = R.layout.quote;
        SimpleAdapter adapter = new SimpleAdapter(this,
                AdapterDataFactory.getData(new BashPageTest()),
                itemViewId,
                AdapterMapping.from,
                AdapterMapping.to);
        listView.setAdapter(adapter);
    }



    private void setupTextView() {
        sampleText = (TextView)findViewById(R.id.sampleText);
        sampleText.setMovementMethod(new ScrollingMovementMethod());
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

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        switch(id){
            case(R.id.sampleText):
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
            case(R.id.sampleText):
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

    // Show new Content on Screen
    private void refreshContentOnScreen(String data) {
        sampleText.setText(data);
    }
}
