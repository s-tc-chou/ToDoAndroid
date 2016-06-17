/***************************************************************************************************
 DBHelper.java
 Last updated: Steve Chou 6/17/2016

 Helper class for sqlite support for persistent data.Reads and writes to ToDo.db

 **************************************************************************************************/
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

    //Column references
    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TODO_LIST_TABLE_NAME = "TODO_LIST_ITEMS";
    public static final String TODO_LIST_ITEM = "todo_list_item";
    public static final String TODO_LIST_COLUMN_ID = "ID";
    public static final String TODO_LIST_CUR_POS = "cur_pos";
    public static final String TODO_LIST_PRIORITY = "priority";
    public static final String TODO_LIST_DUE_DATE = "due_date";
    public static final String TODO_LIST_STATUS = "status";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
            "create table toDoListItems " +
            "(id integer primary key, cur_pos integer, todo_list_item text not null, priority text, due_date date, status bool)"
        );
    }
    //debug use only.
    public void refresh()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS toDoListItems");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS toDoListItems");
        onCreate(db);
    }

    //Add and delete methods ------------------------------------------------

    //insert to do list item into DB
    public boolean addListItem(Integer pos, String listItem, String priority, String dueDate, boolean completed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cur_pos", pos);
        contentValues.put("todo_list_item", listItem);
        contentValues.put("priority", priority);
        contentValues.put("due_date", dueDate);
        contentValues.put("status", completed);
        db.insert("toDoListItems", null, contentValues);

        return true;
    }


    //delete 1 item at specified position
    public boolean deleteListItem (Integer pos)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("toDoListItems", "cur_pos = ? ", new String[] { Integer.toString(pos) });

        return true;
    }

    //Update methods ----------------------------------------------------

    //updates list item according to position.
    public boolean updateListItem(Integer pos, String listItem, String priority, String dueDate, boolean completed)
    {
        //Log.d("UPDATELISTITEM", "id: " + (id +1) + "list item to update: " + listItem);
        /*SQLiteDatabase rdb = this.getReadableDatabase();
        Cursor res = rdb.rawQuery( "select * from toDoListItems where todo_list_item="+listItem+"", null );
        res.moveToFirst();
        String item = res.getString(res.getColumnIndex(TODO_LIST_ITEM));
        String DBid = res.getString(res.getColumnIndex(TODO_LIST_COLUMN_ID));
        Log.d("UPDATELISTITEM", "id: " + (DBid) + "list item to update: " + item);
*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo_list_item", listItem);
        contentValues.put("priority", priority);
        contentValues.put("due_date", dueDate);
        contentValues.put("status", completed);
        db.update("toDoListItems", contentValues, "cur_pos = ? ", new String[] { Integer.toString(pos) } );

        return true;
    }

    public boolean updateListPos(Integer oldPos, Integer newPos)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cur_pos", newPos);
        db.update("toDoListItems", contentValues, "cur_pos = ? ", new String[] { Integer.toString(oldPos) } );

        return true;
    }


    //Fetch queries----------------------------------------------


    //fetches all the data from the db
    public ArrayList<String> getAllListItems()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from toDoListItems", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            //add something to array list items
            //TODO: will need to return other attributes (dates, priority) when we add them.
            array_list.add(res.getString(res.getColumnIndex(TODO_LIST_ITEM)));
            res.moveToNext();
        }

        res.close();
        return array_list;
    }

    //fetches all the data from the db
    public ArrayList<String> getAllListPos()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from toDoListItems", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            //add something to array list items
            //TODO: will need to return other attributes (dates, priority) when we add them.
            array_list.add(res.getString(res.getColumnIndex(TODO_LIST_CUR_POS)));
            res.moveToNext();
        }

        res.close();
        return array_list;
    }


}

