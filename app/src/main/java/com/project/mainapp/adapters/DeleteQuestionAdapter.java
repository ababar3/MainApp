package com.project.mainapp.adapters;

import com.project.mainapp.interfaces.LongClickCallback;
import com.project.mainapp.models.DbModel;

import java.util.ArrayList;


public class DeleteQuestionAdapter extends QuestionsListAdapter {

    //as the adapter for QuestionsListAdapter is same as delete adapter so we inherit the
    //functionalities used in it

    public DeleteQuestionAdapter(ArrayList<DbModel> questions, LongClickCallback listener) {
        super(questions,listener);
    }


}


