package com.example.steve.todoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editItemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        setCursorEditText();
    }

    //sets the edit text field to the current item and moves cursor to the end
    private void setCursorEditText()
    {
        String originalText;
        EditText editTask = (EditText) findViewById(R.id.etxEditItem);

        try
        {
            originalText = getIntent().getStringExtra("List_Item_String");
        }
        catch(NullPointerException e)
        {
            originalText = "";
        }

        //sets the text box and moves cursor to the end
        editTask.setText(originalText);
        editTask.setSelection(originalText.length());
    }

    public void onSave(View v)
    {
        Intent data = new Intent();
        EditText editTask = (EditText) findViewById(R.id.etxEditItem);
        String editedText;

        try
        {
            editedText = editTask.getText().toString();
        }
        catch(NullPointerException e)
        {
            editedText = "";
        }

        //return edited text & position
        data.putExtra("revised_listItem", editedText);
        data.putExtra("List_Item_Position", getIntent().getIntExtra("List_Item_Position",0));

        //finished
        setResult(RESULT_OK, data);
        this.finish();
    }

    public void onSubmit(View V)
    {
        this.finish();
    }
}
