package com.example.steve.todoandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TODO_LIST_TABLE_NAME = "TODO_LIST_ITEMS";
    public static final String TODO_LIST_ITEM = "todo_list_item";
    public static final String TODO_LIST_COLUMN_ID = "id";
    public static final String TODO_LIST_PRIORITY = "priority";
    public static final String TODO_LIST_DUE_DATE = "due_date";
    public static final String TODO_LIST_STATUS = "status";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
            "create table toDoListItems " +
            "(id integer primary key, todo_list_item text not null, priority text, due_date date, status bool)"
        );
    }
    //debug use only.
    /*public void refresh()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS toDoListItems");
        onCreate(db);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS toDoListItems");
        onCreate(db);
    }

    //insert to do list item into DB
    public boolean addListItem(String listItem, String priority, String dueDate, boolean completed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo_list_item", listItem);
        contentValues.put("priority", priority);
        contentValues.put("due_date", dueDate);
        contentValues.put("status", completed);
        db.insert("toDoListItems", null, contentValues);

        return true;
    }

    public boolean updateListItem(Integer id, String listItem, String priority, String dueDate, boolean completed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo_list_item", listItem);
        contentValues.put("priority", priority);
        contentValues.put("due_date", dueDate);
        contentValues.put("status", completed);
        db.update("toDoListItems", contentValues, "id = ? ", new String[] { Integer.toString(id+1) } ); //the primary key list is off by 1, so offset this by adding 1

        return true;
    }

    //delete
    public Integer deleteListItem (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("toDoListItems",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    //fetch queries----------------------------
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "select * from toDoListItems where id="+id+"", null );
    }

    public ArrayList<String> getAllListItems()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from toDoListItems", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            //add something to array list items
            array_list.add(res.getString(res.getColumnIndex(TODO_LIST_ITEM)));
            res.moveToNext();
        }

        res.close();
        return array_list;
    }


}

