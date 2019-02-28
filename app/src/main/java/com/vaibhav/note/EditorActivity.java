package com.vaibhav.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {
    public static NoteContents editornote,new_note;
    MyDatabase myDB=new MyDatabase(this);
    FloatingActionButton done;
    Context context;
    EditText titletext,contenttext;
    String titlestring;
    String notestring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        listTools.ID=0;
        myDB.open();
        context=this;
        SharedPreferences sPrefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        listTools.ID = sPrefs.getInt("id", 0);
        initializeUI();
        if (MainActivity.editor==1){
            titletext.setText(editornote.notetitle);
            contenttext.setText(editornote.notecontent);
        }
        done.setOnClickListener(doneclick);
    }

    public View.OnClickListener doneclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            titlestring=titletext.getText().toString();
            notestring=contenttext.getText().toString();
            if (!titlestring.equals("") || !notestring.equals("")){
                if (MainActivity.editor==1){
                    new_note=new NoteContents(MainActivity.editorindex,titlestring,notestring);
                    listTools.mynote.add(MainActivity.editorindex,new_note);
                    myDB.removeRow(myDB.getData().get(MainActivity.editorindex));
                    myDB.createEntry(new_note);
                    MainActivity.editor=0;
                }
                else{
                    new_note=new NoteContents(listTools.ID,titlestring,notestring);
                    listTools.mynote.add(new_note);
                    myDB.createEntry(new_note);
                    listTools.ID++;
                }
            }
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
        }
    };

    public void initializeUI(){
        done=findViewById(R.id.donebutton);
        titletext=findViewById(R.id.edittitle);
        contenttext=findViewById(R.id.notecontent);

    }

    @Override
    protected void onPause() {
        SharedPreferences sPrefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putInt("id", listTools.ID);
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences sPrefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putInt("id", listTools.ID);
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sPrefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putInt("id", listTools.ID);
        myDB.close();
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.editor==1){
            Toast.makeText(this,"Click Done if you've edited",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }
}
