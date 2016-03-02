package com.github.k24.simplistic_rss_sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.shirwa.simplistic_rss.RssFeed;
import com.shirwa.simplistic_rss.RssItem;
import com.shirwa.simplistic_rss.RssReader;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final EasyRecyclerAdapter adapter = new EasyRecyclerAdapter(this);
        adapter.bind(String.class, TextViewHolder.class);
        adapter.bind(RssItem.class, RssItemViewHolder.class);
        recyclerView.setAdapter(adapter);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Task.callInBackground(new Callable<RssFeed>() {
            @Override
            public RssFeed call() throws Exception {
                RssReader rssReader = new RssReader("https://developer.github.com/changes.atom");
                return rssReader.getFeed();
            }
        }).continueWith(new Continuation<RssFeed, Void>() {
            @Override
            public Void then(Task<RssFeed> task) throws Exception {
                progressDialog.dismiss();

                if (task.isFaulted()) {
                    Log.w("sample", task.getError());
                    if (!isFinishing()) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }

                RssFeed result = task.getResult();

                adapter.add(result.getTitle());
                adapter.appendAll(result.getRssItems());

                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
