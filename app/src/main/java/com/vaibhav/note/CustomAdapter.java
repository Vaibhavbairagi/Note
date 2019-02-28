package com.vaibhav.note;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.listHolder> {
    Context myContext;
    private MainActivity.NoteClickListener noteClickListener;
    public CustomAdapter(Context context) {
        myContext=context;
    }

    public void setNoteClickListener(MainActivity.NoteClickListener noteClickListener){
        this.noteClickListener=noteClickListener;
    }

    @NonNull
    @Override
    public listHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listlayout, parent, false);
        return new listHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final listHolder holder, final int position) {
        if (listTools.mynote.get(position).notetitle.equals(""))
            holder.mtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        else
            holder.mtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        holder.mtitle.setText(listTools.mynote.get(position).notetitle);
        holder.mcontent.setText(listTools.mynote.get(position).notecontent);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteClickListener.OnNoteClick(listTools.mynote.get(position));
            }
        };
        holder.mcard.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return listTools.mynote.size();
    }

    public static class listHolder extends RecyclerView.ViewHolder {
        private TextView mtitle;
        private TextView mcontent;
        private CardView mcard;
        private listHolder(View v){
            super(v);
            this.mtitle=v.findViewById(R.id.title);
            this.mcontent=v.findViewById(R.id.content);
            this.mcard=v.findViewById(R.id.card);
        }

    }
}
