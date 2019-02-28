package com.vaibhav.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CustomAdapter list_adapter;
    MyDatabase myDB=new MyDatabase(this);
    public static int editor=0;
    public static int editorindex=0;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager list_manager;
    FloatingActionButton floatingActionButton;
    Context context;
    int undo=0,backcounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB.open();
        listTools.mynote=myDB.getData();
        listTools.coordinatorLayout=findViewById(R.id.coordinatorlayout);
        context=this;
        SharedPreferences sPrefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        listTools.ID = sPrefs.getInt("id", 0);
        UIinit();
        recyclerViewInit();
        swipeAction();
        if (recyclerView!=null){
            noteclick();
        }


    }

    public void noteclick(){
        list_adapter.setNoteClickListener(new NoteClickListener() {
            @Override
            public void OnNoteClick(NoteContents n) {
                editor=1;
                editorindex=listTools.mynote.indexOf(n);
                EditorActivity.editornote=n;
                Intent intent=new Intent(context,EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void recyclerViewInit(){
        recyclerView=findViewById(R.id.list);
        list_manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(list_manager);
        list_adapter=new CustomAdapter(this);
        recyclerView.setAdapter(list_adapter);
        list_adapter.notifyDataSetChanged();
    }
    public void UIinit(){
        floatingActionButton=findViewById(R.id.floatbutton);
        floatingActionButton.setOnClickListener(openEditorActivity);
    }

    public void swipeAction(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        final NoteContents temp = listTools.mynote.get(viewHolder.getAdapterPosition());
                        final int pos = viewHolder.getAdapterPosition();
                        listTools.mynote.remove(pos);
                        list_adapter.notifyItemRemoved(pos);
                        Snackbar sbar = Snackbar.make(listTools.coordinatorLayout, "Fixture removed", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        undo = 1;
                                        listTools.mynote.add(pos, temp);
                                        list_adapter.notifyItemInserted(pos);
                                        undo = 1;
                                    }
                                })
                                .addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        if (undo != 1) {
                                            myDB.removeRow(temp);
                                        }
                                        undo = 0;
                                        super.onDismissed(transientBottomBar, event);
                                    }
                                });
                        sbar.show();
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public final View.OnClickListener openEditorActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditorActivity.class);
            startActivity(intent);
        }
    };

    public interface NoteClickListener{
        void OnNoteClick(NoteContents n);
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
        editor.apply();
        myDB.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (backcounter==0) {
            Toast.makeText(context, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
            backcounter++;
        }
        else if (backcounter>0){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();
            }
    }
}
