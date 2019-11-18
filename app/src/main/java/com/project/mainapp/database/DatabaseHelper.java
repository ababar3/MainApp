package com.project.mainapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.mainapp.Activities.TypeAlreadyExistsException;
import com.project.mainapp.models.DbModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * The name of the database
     */
    private static final String DATABASE_NAME = "Questions.db";

    /**
     * The database version needed
     * If the database structure has changed then this version needs to be updated(incremented)
     * For example if the previous version was 1 then the next version after updation would be 2
     */
    private static int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        /**
         * As this class extends from SQLiteOpenHelper so by super(...) we have to pass the
         * database name and database version that are very important for getting info about
         * database, the context is also required and is passed. The factory method is not used
         * so null is passed
         * */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * From line 29 - 43 are the name of the columns of the database
     * The column names would be like as shown below
     * |----------------------------------------------------------------------
     * | _id | question | option_1 | option_2 | .....
     * |-----------------------------------------------------------------------
     * so the table columns are like as shown below
     * we use "public static final String" so that instead of writing string again and again we
     * can use the variable
     */
    public static final String TABLE_NAME = "QUESTION";
    public static final String KEY_ID = "_id";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_OPTION_1 = "option_1";
    public static final String KEY_OPTION_2 = "option_2";
    public static final String KEY_OPTION_3 = "option_3";
    public static final String KEY_OPTION_4 = "option_4";
    public static final String KEY_ANSWER = "answer";
    public static final String Total_Score = "et";
    public static final String Choice1 = "et2";
    public static final String Choice2 = "et3";
    public static final String Choice3 = "et4";
    public static final String Choice4 = "et5";
    public static final String Type = "type";
    public static final String DummyEntry = "dummy_entry";

    /**
     * information about
     * @see DummyEntry
     * this variable is of string type but has only '1' or '0' saved at a time
     * if the saved value is '1' then that means the row does not have any question
     * and is only used to save the type into the database
     * if the saved value is '0' then that means the row has question saved into it
     * */


    /**
     * Simple sql query which creates the table for the first time
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUESTION + " not null,"
            + KEY_OPTION_1 + " not null,"
            + KEY_OPTION_2 + " ,"
            + KEY_OPTION_3 + " ,"
            + KEY_OPTION_4 + " ,"
            + Total_Score + " ,"
            + Choice1 + " ,"
            + Choice2 + " ,"
            + Choice3 + " ,"
            + Choice4 + " ,"
            + Type + " not null,"
            + KEY_ANSWER + " not null,"
            + DummyEntry + " INTEGER NOT NULL"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * this function is called and the string
         * @see CREATE_TABLE is passed
         * this is a simple sql query and runs only if the table is not created before
         * if the table is created before then the sql query is ignored
         * */
        db.execSQL(CREATE_TABLE);
    }


    /**
     * CURRENTLY NOT IN USE
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * CURRENTLY NOT IN USE
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    /**
     * Returns the total number of rows in the database
     * if the table is empty or has no rows then 0 would be returned
     */
    public long getCount() {
        SQLiteDatabase db = getReadableDatabase();
        /**
         * count would now have the total number of rows in the database
         * */
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    /**
     * this function was used in debugging to get all the data from database and is not used now
     */
    public ArrayList<DbModel> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<DbModel> toReturn = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                toReturn.add(getModel(cursor));
            } while (cursor.moveToNext());
        }
        return toReturn;
    }

    /**
     * This function pass all the types saved in the database
     * Some name of types can be : SOCIAL MEDIA, DEVICE , etc....
     * so this would return the Arraylist<String> which would contains all the type names
     */
    public ArrayList<String> getAllTypes() {
        //getting readable database as we currently have to only read data from database
        SQLiteDatabase db = getReadableDatabase();
        //raw query is used to pass simple sql query
        //the second parameter is not used
        //for information about dummy entry see information above on LINE 67 of the code
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DummyEntry + " = 1 ",
                null);
        ArrayList<String> types = new ArrayList<>();
        //moveToFirst() returns true if there is data retrieved from database according to rawQuery
        //moveToFirst() returns false if there was no data retrieved from database according to
        // rawQuery
        if (c.moveToFirst()) {
            do {
                /**
                 * we use getColumnIndexOrThrow(String) to get the index number of column in the
                 * table
                 * in the database. If there is no column name passed into it then it throws
                 * exception
                 * about it. c.getString(Int) returns the data in the column index
                 * for example
                 * |----------------------------------------------------------------------
                 * | _id | question | option_1 | option_2 | .....
                 * |-----------------------------------------------------------------------
                 *
                 * if i use c.getString(0) then the value inside the column _id is fetched
                 * if i use c.getString(1) then the value inside the column question is fetched
                 * if i use c.getString(0) then the value inside the column option_1 is fetched
                 * ...... and same for the other ones
                 * */
                types.add(c.getString(c.getColumnIndexOrThrow(Type)));
            } while (c.moveToNext());
        }
        c.close();

        return types;
    }

    /**
     * questionId is passed and this function deletes that specific question from the database
     */
    public boolean deleteQuestion(String questionId) {
        SQLiteDatabase db = getWritableDatabase();

        /**
         * This function returns the number of rows that are deleted when the data is passed to it
         * if we write SQL query for it then it would become like
         * DELETE FROM TABLE_NAME WEHERE KEY_ID = questionId
         * */
        int totalRowsDeleted = db.delete(TABLE_NAME,
                KEY_ID + " =? ",
                new String[]{questionId}
        );
        //return true if rows deleted are greater than zero cause that means some rows were deleted
        //else return false that there was no row deleted
        return totalRowsDeleted > 0;
    }


    /**
     * function to delete the type from the database
     * the type can be passed in the following formats
     * 'social media'
     * '  Social media'
     * etc...
     */
    public String deleteType(String type) {
        ArrayList<String> types = getAllTypes();


        String typeToDelete = null;
        for (String t : types) {
            /**
             * String.trim() remove the space before first character and after last character
             * for example if we use trim on ' social media ' then this would become 'social media'
             * the spaces between the names are not changed in trim
             *
             * compareToIgnoreCase is used when we just want to compare letters
             * for example
             * the values 'social' and 'Social' are equal if compareToIgnoreCase() is used
             * but they are false if equals() function is used
             * */
            if (type.trim().compareToIgnoreCase(t) == 0) {
                //there was a type found and is saved in the variable 'typeToDelete'
                typeToDelete = t;
                break;
            }
        }


        //if the value is null then the type value passed to this function is not in database
        if (typeToDelete == null) {
            return null;
        }

        SQLiteDatabase db = getWritableDatabase();
        int totalRowsDeleted = db.delete(TABLE_NAME,
                Type + " =? ",
                new String[]{typeToDelete}
        );

        Log.d("theH", "Type delete " + typeToDelete + " affected rows: " + totalRowsDeleted);
        return typeToDelete;
    }

    public void addType(String newType) throws RuntimeException {
        ArrayList<String> allTypes = getAllTypes();


        for (String s : allTypes) {
            if (s.compareToIgnoreCase(newType.trim()) == 0) {
                //if the same type is found before then we throw exception as we know that
                //there cannot be two types of the same name
                throw new TypeAlreadyExistsException("Type (" + newType + ") already exists in " +
                        "the " +
                        "database");
            }
        }


        DbModel model = new DbModel();
        model.type = newType;
        insert(model);
    }

    public ArrayList<DbModel> getAllFromType(String type) {
        ArrayList<DbModel> models = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * FROM " + TABLE_NAME + " WHERE " + Type + " = '" + type + "' AND "
                + DummyEntry + " != 1";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                DbModel m = getModel(c);
                if (!m.isDummyEntry)
                    models.add(m);
            } while (c.moveToNext());
        }
        c.close();
        return models;
    }


    /**
     * Passing cursor to this function would make sure that data is fetched from the cursor
     * and is saved into the db model. for info about db model
     * @see DbModel
     * */
    public DbModel getModel(Cursor c) {
        DbModel model = new DbModel();
        model.isDummyEntry = false;

        model.id = String.valueOf(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
        model.question = c.getString(c.getColumnIndexOrThrow(KEY_QUESTION));
        model.option1 = c.getString(c.getColumnIndexOrThrow(KEY_OPTION_1));
        model.option2 = c.getString(c.getColumnIndexOrThrow(KEY_OPTION_2));
        model.option3 = c.getString(c.getColumnIndexOrThrow(KEY_OPTION_3));
        //model.option4 = c.getString(c.getColumnIndexOrThrow(KEY_OPTION_4));
        model.totalScore = c.getString(c.getColumnIndexOrThrow(Total_Score));
        model.choice1 = c.getString(c.getColumnIndexOrThrow(Choice1));
        model.choice2 = c.getString(c.getColumnIndexOrThrow(Choice2));
        model.choice3 = c.getString(c.getColumnIndexOrThrow(Choice3));
        //model.choice4 = c.getString(c.getColumnIndexOrThrow(Choice4));
        model.type = c.getString(c.getColumnIndexOrThrow(Type));
        model.answer = c.getString(c.getColumnIndexOrThrow(KEY_ANSWER));

        return model;
    }

    public void insert(DbModel dataToInsert) {

        /**
         * Save all the column data respectively according to their column names in the database
         * */
        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION, dataToInsert.question);
        cv.put(KEY_OPTION_1, dataToInsert.option1);
        cv.put(KEY_OPTION_2, dataToInsert.option2);
        cv.put(KEY_OPTION_3, dataToInsert.option3);
       // cv.put(KEY_OPTION_4, dataToInsert.option4);
        cv.put(Total_Score, dataToInsert.totalScore);
        cv.put(Choice1, dataToInsert.choice1);
        cv.put(Choice2, dataToInsert.choice2);
        cv.put(Choice3, dataToInsert.choice3);
       // cv.put(Choice4, dataToInsert.choice4);
        cv.put(Type, dataToInsert.type);
        cv.put(KEY_ANSWER, dataToInsert.answer);
        cv.put(DummyEntry, dataToInsert.isDummyEntry ? 1 : 0);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);
    }

}
