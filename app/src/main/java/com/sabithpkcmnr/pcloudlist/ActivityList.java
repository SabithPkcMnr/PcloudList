package com.sabithpkcmnr.pcloudlist;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabithpkcmnr.pcloudlist.extra.ActivityUtils;
import com.sabithpkcmnr.pcloudlist.extra.ListDetector;
import com.sabithpkcmnr.pcloudlist.extra.ModelList;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    TextView txFolders;
    RecyclerView rvList;
    String extraTitleData;
    AdapterList adapterList;
    ListDetector myListDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityUtils.myActivity = this;
        rvList = findViewById(R.id.rvList);
        txFolders = findViewById(R.id.txFolders);
        extraTitleData = getIntent().getStringExtra("extraTitleData");

        if (extraTitleData == null) {
            extraTitleData = "";
            txFolders.setText("Home");
        } else {
            setTitle(extraTitleData.substring(extraTitleData.lastIndexOf("/"))
                    .replace("/",""));
            txFolders.setText("Home" + extraTitleData.replaceAll("/", " > "));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(extraTitleData != null && extraTitleData.length() > 0);

        myListDetector = new ViewModelProvider(this).get(ListDetector.class);
        Observer<ArrayList<ModelList>> serverObserver = new Observer<ArrayList<ModelList>>() {
            @Override
            public void onChanged(ArrayList<ModelList> modelList) {
                if (modelList != null && modelList.size() > 0) {
                    adapterList = new AdapterList(ActivityList.this, modelList, extraTitleData);
                    rvList.setLayoutManager(new LinearLayoutManager(ActivityList.this));
                    rvList.setHasFixedSize(true);
                    rvList.setNestedScrollingEnabled(false);
                    rvList.setAdapter(adapterList);
                }
            }
        };

        myListDetector.getListData().observe(this, serverObserver);
        ActivityUtils.requestServerForData(ActivityUtils.PUBLIC_STORAGE_URL + extraTitleData);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}