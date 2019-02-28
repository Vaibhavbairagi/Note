package com.vaibhav.note;

import android.support.design.widget.CoordinatorLayout;

import java.util.ArrayList;

public class listTools {
    public static ArrayList<NoteContents> mynote;
    public static int ID;
    public static CoordinatorLayout coordinatorLayout;
    public static void init_Note(){
        mynote=new ArrayList<>();
    }
}
