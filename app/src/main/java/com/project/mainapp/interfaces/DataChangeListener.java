package com.project.mainapp.interfaces;

import com.project.mainapp.models.DbModel;

import java.util.ArrayList;

public interface DataChangeListener {
    void onDataChange(ArrayList<DbModel> latestData);
}
