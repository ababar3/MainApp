package com.project.mainapp.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.models.ScoreModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class handles all the tasks related to the shared preferences.
 */
public class PrefUtils {

    public static final String PREF_APP = "pref_app";


    //Save the score model according to the key
    //the key here passed is the type as we know that the types would be unique in the database
    //so key would be the type and the data would be the score according to that
    public static void save(Context context, String key, ScoreModel data) {
        //3rd party library which converts the class to JSONObject then to String
        //this is used as we cannot save the class directly to the shared preferences
        //so we take help of toJson to convert class to string
        String value = new Gson().toJson(data);

        //saving data into prefs
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void save(Context context, String type, ArrayList<DbModel> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        /**
         * the key that is starting with the string 'prev_'+type means that all the questions
         * related to that specific page would be saved into this
         * */
        String key = "prev_" + type;
        String value = new Gson().toJson(data);


        Log.d("theH", "Saving Data from key: " + key + " Values: " + value);

        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void saveUnanswered(Context context, String type, ArrayList<DbModel> data) {

        if (data == null || data.isEmpty()) {
            Log.d("theH", "Empty unanswered questions are empty or null for type: " + type);
            return;
        }
        /**
         * the key that is starting with the string 'prev_'+type means that all the questions
         * related to that specific type would be saved into this
         * */
        String key = "unans_" + type;
        String value = new Gson().toJson(data);


        Log.d("theH", "Saving Data from key: " + key + " Values: " + value);

        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }


    public static ArrayList<DbModel> fetchUnanswered(Context context, String type) {
        //get all the questions related to a single type
        String key = "unans_" + type;
        String value = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key
                , null);
        if (value == null) {
            return null;
        }
        ArrayList<DbModel> temp = null;
        try {
            Type typeToken = new TypeToken<ArrayList<DbModel>>() {
            }.getType();
            Log.d("theH", "Unanswered question: " + value);
            temp = new Gson().fromJson(value, typeToken);
        } catch (Exception e) {
            return null;
        }
        return temp;
    }


    public static ArrayList<DbModel> fetchList(Context context, String type) {
        //get all the questions related to a single type
        String key = "prev_" + type;
        String value = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key
                , null);
        if (value == null) {
            return null;
        }
        ArrayList<DbModel> temp = null;
        try {
            Type typeToken = new TypeToken<ArrayList<DbModel>>() {
            }.getType();
            temp = new Gson().fromJson(value, typeToken);
        } catch (Exception e) {
            return null;
        }
        return temp;
    }

    public static ScoreModel fetch(Context context, String key) {
        String value = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key
                , null);

        if (value == null) {
            return null;
        }

        try {
            ScoreModel sm = new Gson().fromJson(value, ScoreModel.class);
            return sm;
        } catch (Exception e) {
            return null;
        }
    }

    public static void clearLists(Context context, ArrayList<String> types) {
        //clear the lists once the data has no need to be locally saved
        for (String t : types) {
            String key = "prev_" + t;
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                    .edit()
                    .putString(key, null)
                    .apply();
        }
    }

    public static void clearQuestions(Context context, ArrayList<String> types) {
        //clear the questions once the data has no need to be locally saved
        for (String key : types) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                    .edit()
                    .putString(key, null)
                    .apply();
        }
    }

    public static void clearUnattempted(Context context,ArrayList<String> types){
        for (String key : types) {
            String tempKey = "unans_" + key;
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                    .edit()
                    .putString(tempKey, null)
                    .apply();
        }
    }

    public static void clearQuestions(Context context, String type) {
        //clear the single question once the data has no need to be locally saved
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(type, null)
                .apply();
    }

    public static void clearSingleList(Context context, String type) {
        //clear the single score model once the data has no need to be locally saved
        String key = "prev_" + type;
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putString(key, null)
                .apply();
    }


}
