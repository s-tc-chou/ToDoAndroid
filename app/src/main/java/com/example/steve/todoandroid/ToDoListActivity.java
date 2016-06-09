package com.example.steve.todoandroid;

import android.app.Activity;
import android.content.Intent;
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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ToDoListActivity extends Activity {

    private ListView toDoList;
    private ArrayAdapter<String> toDoListAdapter;
    private ArrayList<String> items;
    private final int REQUEST_CODE = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Button addButton = (Button) findViewById(R.id.btnAddButton);
        items = new ArrayList<String>();

        readItems();

        toDoList = (ListView) findViewById(R.id.lstvwToDoList);
        toDoListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        toDoList.setAdapter(toDoListAdapter);

        setupItemListClickListener();
    }

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
                //Toast.makeText(ToDoListActivity.this,"Hello World",Toast.LENGTH_SHORT).show();

                launchEditItemActivity(toDoList.getItemAtPosition(position).toString(), position);
            }
        });
    }

    //long click not used atm.  Can remove the item on long click, but I think that's better done in a confirmation screen (maybe editItemActivity) so we will not entertain the idea.
    private void setupItemListLongClickListener()
    {
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,View item, int pos, long id) {

                return true;
            }
        });
    }

    //List Methods --------------------------------------------------------------------

    //adds a new item to the listview
    public void onAdd(View v)
    {
        EditText newTask = (EditText) findViewById(R.id.etxtListItem);
        String itemText = newTask.getText().toString();
        toDoListAdapter.add(itemText);
        newTask.setText("");
        writeItems();
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

            //set item for listviewAdapter and update the view.
            items.set(pos, editedString);
            toDoListAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    //FILE I/O SECTION ----------------------------------------------------------------------
    //basic file saving functionality: read and write your to-do-list to a txt file "todo.txt".

    /*Future functionality:

        1) Change to SQLite instead of text file.

     */

    private void readItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
