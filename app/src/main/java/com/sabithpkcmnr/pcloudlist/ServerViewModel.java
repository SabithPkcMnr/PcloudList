package com.sabithpkcmnr.pcloudlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ServerViewModel extends ViewModel {

    MutableLiveData<ArrayList<ModelFile>> listData;

    public MutableLiveData<ArrayList<ModelFile>> getListData() {
        if (listData == null){
            listData = new MutableLiveData<>();
        }
        return listData;
    }
}
