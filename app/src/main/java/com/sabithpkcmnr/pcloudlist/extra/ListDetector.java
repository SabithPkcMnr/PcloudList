package com.sabithpkcmnr.pcloudlist.extra;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ListDetector extends ViewModel {

    MutableLiveData<ArrayList<ModelList>> listData;

    public MutableLiveData<ArrayList<ModelList>> getListData() {
        if (listData == null){
            listData = new MutableLiveData<>();
        }
        return listData;
    }
}
