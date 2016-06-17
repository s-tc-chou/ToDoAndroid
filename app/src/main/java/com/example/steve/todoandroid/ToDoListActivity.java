/***************************************************************************************************
ToDoListActivity.java
 Last updated: Steve Chou 6/17/2016

 References activity_to_do_list.xml

 Main activity. A very basic to-do list with the following functionalities:

 1) List view for item list
 2) Intent to launch edit screen
 3) Stores data via sqllite.

 Future functionalities:

 1) Launch edit screen via fragment
 2) Edit screen to include date, priority and whether the task is finished.
 3) prettier screens
 4) Want to do categories.  Embedded lists.
 5) add ability to add task to calender?
 6) Checkbox for the view list so you don't have to open a screen to complete a task.

 Basic improvements:
 1) Maybe move alerts into separate helper function
 2)

 **************************************************************************************************/

package com.example.steve.todoandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ToDoListActivity extends Activity {

    private ListView toDoList;
    private ArrayAdapter<String> toDoListAdapter;
    private ArrayList<String> items;
    DBHelper toDoListDB;

    //static variables
    private final int REQUEST_CODE = 555;
    private final String PRIORITY_URGENT = "Urgent";
    private final String PRIORITY_NORMAL = "Normal";
    private final String PRIORITY_HIGH = "High";
    private final String PRIORITY_LOW = "Low";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Button addButton = (Button) findViewById(R.id.btnAddButton);
        toDoListDB = new DBHelper(this);
        //toDoListDB.refresh(); //debug: clears db.

        toDoList = (ListView) findViewById(R.id.lstvwToDoList);
        items = toDoListDB.getAllListItems();
        toDoListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        toDoList.setAdapter(toDoListAdapter);

        setupItemListClickListener();
        setupItemListLongClickListener();
    }

    /*
    //save stuff on pause
    @Override
    protected void onPause()
    {
        super.onPause();
        writeItems();
        toDoListAdapter.notifyDataSetChanged();
    }

    //read stuff on resume
    @Override
    protected void onResume()
    {
        super.onResume();
        readItems();
        toDoListAdapter.notifyDataSetChanged();
    }*/

    //Listeners ------------------------------------------------------------------------------
    /*Future functionality:

        1) would like to add something to mark activity as complete, perhaps a checkbox in the listview.
        2) Update the style so its prettier
        3) Add completion due-dates
        4) Use dialogFragment

     */

    //quick click listener will open up the edit screen (editItemActivity) and allow you to change the to-do item name.
    private void setupItemListClickListener()
    {
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ToDoListActivity.this,"pre save pos: " + position + "id: " + id,Toast.LENGTH_SHORT).show();
                launchEditItemActivity(toDoList.getItemAtPosition(position).toString(), position);
            }
        });
    }

    //long click not used atm.  Rremove the item on long click, but add a confirmation screen.
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

        //for debugging purposes.
        /*for(int i=0 ; i<items.size() ; i++)
        {
            Log.d("items= ", items.get(i));
        }*/

        //update all positions that have changed.  Since the list view always uses a sequential position, the DB needs to keep track of what position the record is currently in.
        for(int i=0 ; i<prevPos.size() ; i++)
        {
            //Log.d("DB items= ", "current pos = " + i + " DB pos = " + prevPos.get(i));

            //Note: This should be done using a key-value pair.  The ID is the primary key on the DB, whereas the pos is not so this is a hackish solution.
            // While POS is unique due to the nature of listview, it's better to properly identify the key-value pair using a PK+position.
            if (i !=  Integer.parseInt(prevPos.get(i)))
            {
                toDoListDB.updateListPos(Integer.parseInt(prevPos.get(i)),i);
            }
        }

        /*debug purposes only
        prevPos = toDoListDB.getAllListPos();
        for(int i=0 ; i<prevPos.size() ; i++)
        {
            Log.d("after DB items= ", "current pos = " + i + " DB pos = " + prevPos.get(i));
        }*/
    }


    //ListView Methods --------------------------------------------------------------------


    //adds a new item to the listview
    public void onAdd(View v)
    {
        EditText newTask = (EditText) findViewById(R.id.etxtListItem);
        String itemText = newTask.getText().toString();

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
            //Toast.makeText(ToDoListActivity.this,"ADDING POS " + curPos,Toast.LENGTH_SHORT).show();

            newTask.setText("");
            //Todo: add piece that lets you specify urgency.
            //Todo: add piece that lets you specify an actual date
            toDoListDB.addListItem(curPos, itemText,PRIORITY_NORMAL,"Jan 01 2015",false);
            updateDBPositions();
        }
    }

    //launch edit screen (editItemActivity) via intent.  Pass in the current text of list item you clicked on and the position
    private void launchEditItemActivity(String currentText, int pos)
    {
        Intent editItemActivity = new Intent(this, editItemActivity.class);

        //pass the value of the list item you clicked on.
        editItemActivity.putExtra("List_Item_String",currentText);
        editItemActivity.putExtra("List_Item_Position", pos);

        //start edit screen
        startActivityForResult(editItemActivity,REQUEST_CODE);
    }

    //this gets called when the activity returns from editItemActivity. So lets write to file here too.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE)
        {
            String editedString = data.getExtras().getString("revised_listItem");
            int pos = data.getExtras().getInt("List_Item_Position");
            items.set(pos, editedString);

            //TO DO: update to use priority, and proper date instead of hard coded 1/1/2015.
            toDoListDB.updateListItem(pos,editedString,PRIORITY_NORMAL,"Jan 01 2015",false);
            toDoListAdapter.notifyDataSetChanged();
        }

    }
}
