/***************************************************************************************************
ToDoListActivity.java
 Last updated: Steve Chou 6/17/2016

 References activity_to_do_list.xml

 Main activity. A very basic to-do list with the following functionalities:

 1) List view for item list
 2) Intent to launch edit screen
 3) Stores data via sqllite.
 4) launches edit screen via a fragment.
 5) includes Priority and dates.

 Future functionalities:

 1) --
 2) Edit screen to include whether the task is finished.
 3) prettier screens
 4) Want to do categories.  Embedded lists.
 5) add ability to add task to calender?
 6) Checkbox for the view list so you don't have to open a screen to complete a task.

 Basic improvements:
 1) Maybe move alerts into separate helper function

 **************************************************************************************************/

package com.example.steve.todoandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ToDoListActivity extends AppCompatActivity implements editScreenFragment.onEditFinishedListener{

    private ListView toDoList;
    private ArrayAdapter<String> toDoListAdapter;
    private ArrayList<String> items;
    private DBHelper toDoListDB;

    //static variables
    private final int REQUEST_CODE = 555;
    private final String PRIORITY_VH = "Very High";
    private final String PRIORITY_HIGH = "High";
    private final String PRIORITY_NORMAL = "Medium";
    private final String PRIORITY_LOW = "Low";
    private final String PRIORITY_LOWEST = "Meh";

    //bundled fragment variables used for DB.
    private bundleDB dbVariables;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        toDoListDB = new DBHelper(this);
        //toDoListDB.refresh(); //debug: clears db.

        toDoList = (ListView) findViewById(R.id.lstvwToDoList);
        items = toDoListDB.getAllListItems();
        toDoListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        toDoList.setAdapter(toDoListAdapter);
        dbVariables = new bundleDB();

        setupItemListClickListener();
        setupItemListLongClickListener();
    }

    //Listeners ------------------------------------------------------------------------------
    /*Future functionality:
        1) would like to add something to mark activity as complete, perhaps a checkbox in the listview.
        2) Update the style so its prettier
     */

    //quick click listener will open up the edit screen in a dialog fragment (editScreenFragment) and allow you to change the to-do item name.
    private void setupItemListClickListener()
    {
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ToDoListActivity.this,"pre save pos: " + position + "item: " + toDoList.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();

                //get data from pos.  Need to make it so getter returns everything properly
                dbVariables = toDoListDB.getListItemAtPos(position);
                showEditDialog();
            }
        });
    }

    //long click removes the item on long click with a confirmation screen.
    private void setupItemListLongClickListener()
    {
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                final int position = pos;
                AlertDialog.Builder alert = new AlertDialog.Builder(ToDoListActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("This will remove the list item");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.remove(position);
                                toDoListDB.deleteListItem(position);
                                updateDBPositions();
                                toDoListAdapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    //this method fixes the positions being off after you delete a record.
    // Since we're trying to ID the DB records using listview positions, every time you delete something in the middle of the list we need to shift everything.
    private void updateDBPositions()
    {
        ArrayList<String> prevPos;
        prevPos = toDoListDB.getAllListPos();

        //update all positions that have changed.  Since the list view always uses a sequential position, the DB needs to keep track of what position the record is currently in.
        for(int i=0 ; i<prevPos.size() ; i++)
        {
            //Note: This should be done using a key-value pair.  The ID is the primary key on the DB, whereas the pos is not so this is a hackish solution.
            // While POS is unique due to the nature of listview, it's better to properly identify the key-value pair using a PK+position.
            if (i !=  Integer.parseInt(prevPos.get(i)))
            {
                toDoListDB.updateListPos(Integer.parseInt(prevPos.get(i)),i);
            }
        }
    }

    //Fragment functions ------------------------------------------
    @Override
    public void onEditFinish(String editText)
    {
        String editedString = editText;
        String priority = dbVariables.getPriority();
        //Toast.makeText(ToDoListActivity.this,"priority before update: " + priority,Toast.LENGTH_SHORT).show();
        String date = dbVariables.getMonth() + " " + dbVariables.getDay() + " " + dbVariables.getYear();

        items.set(dbVariables.getCurPos(), editedString);
        toDoListDB.updateListItem(dbVariables.getCurPos(),editedString,priority,date,false);
        toDoListAdapter.notifyDataSetChanged();
    }


    //Helper to launch edit dialogfragment.
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        editScreenFragment editFragment = new editScreenFragment();
        editFragment.setStyle(editFragment.STYLE_NORMAL, android.R.style.Theme_Holo);
        editFragment.show(fm, "fragment_edit_name");
    }

    public bundleDB getCurrentDB()
    {
        return dbVariables;
    }

    public void setMainActivityDB(bundleDB myDB)
    {
        dbVariables = myDB;

    }

    //ListView Methods --------------------------------------------------------------------
    //adds a new item to the listview
    public void onAdd(View v)
    {
        EditText newTask = (EditText) findViewById(R.id.etxtListItem);
        String itemText = newTask.getText().toString();
        SimpleDateFormat df = new SimpleDateFormat("MM dd yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        if (itemText.matches(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(ToDoListActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("List item cannot be blank");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else
        {
            toDoListAdapter.add(itemText);
            int curPos = toDoListAdapter.getPosition(itemText);
            toDoListAdapter.notifyDataSetChanged();
            //Toast.makeText(ToDoListActivity.this,"cur time " + date,Toast.LENGTH_SHORT).show();

            newTask.setText("");
            toDoListDB.addListItem(curPos, itemText,PRIORITY_NORMAL,date,false);
            updateDBPositions();
        }
    }


}
