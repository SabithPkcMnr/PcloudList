package com.sabithpkcmnr.pcloudlist;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    RecyclerView rvList;
    AdapterList adapterList;
    ServerViewModel serverViewModel;
    String extraUrlData, extraTitleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActivityUtils.myActivity = this;
        rvList = findViewById(R.id.rvList);
        extraUrlData = getIntent().getStringExtra("extraUrlData");
        extraTitleData = getIntent().getStringExtra("extraTitleData");

        if (extraUrlData == null) {
            extraUrlData = "";
        }

        if (extraTitleData == null) {
            extraTitleData = "";
            setTitle("Home");
        } else {
            setTitle("Home" + extraTitleData);
        }

        serverViewModel = new ViewModelProvider(this).get(ServerViewModel.class);
        Observer<ArrayList<ModelFile>> serverObserver = new Observer<ArrayList<ModelFile>>() {
            @Override
            public void onChanged(ArrayList<ModelFile> modelFiles) {
                //modelFile = modelFiles;
                if (modelFiles != null && modelFiles.size() > 0) {
                    adapterList = new AdapterList(ActivityList.this, modelFiles, extraUrlData);
                    rvList.setLayoutManager(new LinearLayoutManager(ActivityList.this));
                    rvList.setHasFixedSize(true);
                    rvList.setNestedScrollingEnabled(false);
                    rvList.setAdapter(adapterList);
                }
            }
        };

        serverViewModel.getListData().observe(this, serverObserver);
        ActivityUtils.requestServerForData(ActivityUtils.PUBLIC_STORAGE_URL + extraUrlData);

    }
}