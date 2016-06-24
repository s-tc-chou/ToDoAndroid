package com.example.steve.todoandroid;

public class bundleDB {

    //, cur_pos integer, todo_list_item text not null, priority text, due_date date, status bool
    private int curPos;
    private String listItemText;
    private String priority;
    private int day;
    private int month;
    private int year;
    private boolean status;

    /*  possible priority choices:

        private final String PRIORITY_VH = "Very High";
        private final String PRIORITY_HIGH = "High";
        private final String PRIORITY_NORMAL = "Medium";
        private final String PRIORITY_LOW = "Low";
        private final String PRIORITY_LOWEST = "Meh";
     */

    //blank constructor
    public bundleDB()
    {
        curPos = 0;
        listItemText = "";
        priority = "Medium";
        day = 1;
        month = 1;
        year = 1900;
        status = false;
    }

    //constructor with stuff passed in
    public bundleDB(int inCurPos, String inPriority,int inDay, int inMonth, int inYear,boolean inStatus,String item)
    {
        curPos = inCurPos;
        priority = inPriority;
        day = inDay; month = inMonth; year = inYear;
        status = inStatus;
        listItemText = item;
    }

    //Setters
    public void setCurPos(int inCurPos) {curPos = inCurPos;}
    public void setPriority(String inPriority) {priority = inPriority;}
    public void setDueDate(int inDay, int inMonth, int inYear) {day = inDay; month = inMonth; year = inYear;}
    public void setStatus(boolean inStatus) {status = inStatus;}
    public void setListItemString(String item) {listItemText = item;}

    //Getters ---------
    public int getCurPos() {return curPos;}
    public String getPriority(){return priority;}
    public int getDay(){return day;}
    public int getMonth(){return month;}
    public int getYear(){return year;}
    public boolean getStatus(){return status;}
    public String getListItemString() {return listItemText; }

}
