package com.example.steve.todoandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;


public class editScreenFragment extends DialogFragment {

    private bundleDB currentDB;

    public static editScreenFragment newInstance()
    {
        return new editScreenFragment();
    }

    public editScreenFragment() {
        // Required empty public constructor
    }

    /*---------------
    Helper Classes
    -----------------*/
    //Initializes all the data fields involved,
    //1: sets the edit text field to the current item and moves cursor to the end.
    //2: sets DB to activity's DB.
    //sets priority and date.
    private void initializeData(View v) {
        //fragment activity for acquiring string
        ToDoListActivity myActivity = (ToDoListActivity) getActivity();

        //fragment view items
        EditText editTask = (EditText) v.findViewById(R.id.etxEditItem);

        //db items
        currentDB =myActivity.getCurrentDB();

        String originalText =  currentDB.getListItemString();
        //Log.d("init date picker", "init date picker: " + currentDB.getYear() + " " + currentDB.getMonth() + " " + currentDB.getDay());

        //sets the text box and moves cursor to the end
        editTask.setText(originalText);
        editTask.setSelection(originalText.length());
        editTask.requestFocus();
    }

    //initializes the spinner and sets it according to the item being passed in.
    public void initializePrioritySpinner(View v)
    {
        Spinner prioritySpinner = (Spinner) v.findViewById(R.id.priority);
        //TODO: the background is dark and so's the text.Fix this later.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, R.layout.support_simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        String priority = currentDB.getPriority();


        if (!priority.equals(null)) {
            int spinnerPosition = adapter.getPosition(priority);
            prioritySpinner.setSelection(spinnerPosition);
        }
    }

    //initialize date picker dialog
    public void initDatePickerDialog(View v)
    {
        DatePicker dueDate = (DatePicker)v.findViewById(R.id.datePicker);
        dueDate.updateDate(currentDB.getYear(),currentDB.getMonth(),currentDB.getDay());
    }

    //interfaces with the save button to pass data back into main activity and calls finish listener
    public void initBtnSaveOnClickListener(View v)
    {
        //ToDoListActivity myActivity = new ToDoListActivity();
        Button btnSave = (Button) v.findViewById(R.id.btnSave);
        final View calledView = v;

        //for some reason, the below view is always null so I end up having to pass through the view
        // that comes in with the click.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialize various elemnts on the fragment: can we put this into a function of somesort somehow?
                ToDoListActivity myActivity = (ToDoListActivity) getActivity();
                EditText editTask = (EditText) calledView.findViewById(R.id.etxEditItem);
                Spinner spnPriority = (Spinner) calledView.findViewById(R.id.priority);
                DatePicker dueDate = (DatePicker) calledView.findViewById(R.id.datePicker);

                String priority = spnPriority.getSelectedItem().toString();
                String editText = editTask.getText().toString();
                //date picker normalizes month while month starts at 0,, so need to add 1 when saving.
                int month = dueDate.getMonth() +1 ;
                int day = dueDate.getDayOfMonth();
                int year = dueDate.getYear();

                if (editText.matches("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                    //set all the various DB attributes.Can toss this in a separate function if we need.
                    currentDB.setListItemString(editText);
                    currentDB.setPriority(priority);
                    currentDB.setDueDate(day,month,year);

                    //pass this stuff back to main activity and end fragment.
                    myActivity.setMainActivityDB(currentDB);
                    onEditFinishedListener listener = (onEditFinishedListener) getActivity();
                    listener.onEditFinish(editText);
                    dismiss();  //is this right?
                }
            }
        });
    }


    //interface for killing fragment.
    public interface onEditFinishedListener
    {
        void onEditFinish(String editText);
    }


    /*  Generated lifecycle Classes
    *
    * */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_screen, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeData(view);
        initBtnSaveOnClickListener(view);
        initializePrioritySpinner(view);
        initDatePickerDialog(view);
    }

    @Override
    public void onResume()
    {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // Call super onResume after sizing
        super.onResume();
    }

}