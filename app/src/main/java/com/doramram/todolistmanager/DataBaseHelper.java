package com.doramram.todolistmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_E_TASK = "e_task";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";

    // TABLE_E_TASK table create statement
    private static final String CREATE_TABLE_E_TASK= "CREATE TABLE " + TABLE_E_TASK + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_TITLE + " TEXT," +
            KEY_DATE + " LONG" + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_E_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_E_TASK);

        // create new tables
        onCreate(db);
    }

    public String getTableAsString(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    // insert task element
    public long createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, task.get_id());
        values.put(KEY_TITLE, task.get_title());
        values.put(KEY_DATE, task.get_date().getTime());

        long product_id = db.insert(TABLE_E_TASK, null, values);

        return product_id;
    }

    // fetch task element
    public Task getTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_E_TASK + " WHERE " + KEY_ID + " = " + task_id;

        Log.e(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task();
        task.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        task.set_title(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        task.set_date(new Date(cursor.getLong(cursor.getColumnIndex(KEY_DATE))));

        return task;
    }

    // get all tasks
    public List<Task> getAllTasks(){
        List<Task> tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_E_TASK;

        Log.e(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.set_title(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                task.set_date(new Date(cursor.getLong(cursor.getColumnIndex(KEY_DATE))));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        return tasks;
    }



    // deleting a task
    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_E_TASK + " WHERE " + KEY_ID + " = '" + task_id +"'";
        db.execSQL(query);
    }

    // get max task id
    public int generateNewId(){

        int maxId;

        if(isTableEmpty(TABLE_E_TASK)){

            maxId =  1;

        } else {

            SQLiteDatabase db = this.getWritableDatabase();
            String MAXQuery = "SELECT MAX(id) as id FROM " + TABLE_E_TASK;
            Cursor cursor = db.rawQuery(MAXQuery, null);
            cursor.moveToFirst();
            maxId = cursor.getInt(0);

        }

        return maxId + 1;

    }

    public boolean isTableEmpty(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + tableName;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount == 0;
    }
}
