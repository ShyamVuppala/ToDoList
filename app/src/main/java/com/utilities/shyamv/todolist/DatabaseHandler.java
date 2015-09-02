package com.utilities.shyamv.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ToDoListDataBase";

    // Table Name
    private static final String TABLE_TO_DO_LIST = "ToDoList";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static final String db_date_format = "yyyy-MM-dd";
    private static final String db_time_format = "HH:mm";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_TO_DO_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT"
                + ")");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TO_DO_LIST);

        // Create tables again
        onCreate(db);
    }



    long addToDoTask(ToDoContent.ToDoTaskItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_DATE, DateTimeUtils.getDateTimeInFormat(item.getDate(), DateTimeUtils.getDateFormat(), db_date_format));
        values.put(KEY_TIME, DateTimeUtils.getDateTimeInFormat(item.getTime(), DateTimeUtils.getTimeFormat(), db_time_format));

        // Inserting Row
        long id = db.insert(TABLE_TO_DO_LIST, null, values);
        db.close(); // Closing database connection

        return id;
    }

    ToDoContent.ToDoTaskItem getToDoTask(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TO_DO_LIST, new String[] { KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_TIME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoContent.ToDoTaskItem item = new ToDoContent.ToDoTaskItem(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                DateTimeUtils.getDateTimeInFormat(cursor.getString(3), db_date_format, DateTimeUtils.getDateFormat()),
                DateTimeUtils.getDateTimeInFormat(cursor.getString(4), db_time_format, DateTimeUtils.getTimeFormat()));
        return item;
    }

    public void loadAllToDoItems() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TO_DO_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ToDoContent.ITEMS.clear();
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                ToDoContent.ToDoTaskItem item = new ToDoContent.ToDoTaskItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                        DateTimeUtils.getDateTimeInFormat(cursor.getString(3), db_date_format, DateTimeUtils.getDateFormat()),
                        DateTimeUtils.getDateTimeInFormat(cursor.getString(4), db_time_format, DateTimeUtils.getTimeFormat()));
                ToDoContent.ITEMS.add(item);
            } while (cursor.moveToNext());
        }
    }

    public int updateToDoItem(ToDoContent.ToDoTaskItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_DATE, DateTimeUtils.getDateTimeInFormat(item.getDate(), DateTimeUtils.getDateFormat(), db_date_format));
        values.put(KEY_TIME, DateTimeUtils.getDateTimeInFormat(item.getTime(), DateTimeUtils.getTimeFormat(), db_time_format));

        // updating row
        return db.update(TABLE_TO_DO_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }

    public void deleteToDoItem(ToDoContent.ToDoTaskItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TO_DO_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }


    public int getToDoItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TO_DO_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
