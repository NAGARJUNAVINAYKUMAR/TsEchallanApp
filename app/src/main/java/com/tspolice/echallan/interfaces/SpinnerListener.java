package com.tspolice.echallan.interfaces;

import com.tspolice.echallan.models.KeyPairBoolData;

import java.util.List;

/**
 * Created by Srinivas on 11/14/2018.
 */



    public interface SpinnerListener {
        void onItemsSelected(List<KeyPairBoolData> items);
    }

