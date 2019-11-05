package com.manish.firstnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manish.firstnote.Data.UserNotes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    Context context;
    ArrayList<UserNotes> datalist = new ArrayList<>();



    public class NotesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_row_title)
        TextView textRowTitle;
        @BindView(R.id.text_row_desc)
        TextView textRowDesc;
        @BindView(R.id.text_row_date)
        TextView textRowDate;
        @BindView(R.id.image_row_edit)
        ImageView imageRowEdit;
        @BindView(R.id.image_row_delete)
        ImageView imageRowDelete;


        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public NotesAdapter(Context con, ArrayList<UserNotes> list) {
        context = con;
        datalist = list;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_recycler_all_notes, parent, false);
        NotesHolder notesHolder = new NotesHolder(view);
        return notesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        UserNotes userNotes=datalist.get(position);
        String title=userNotes.getNoteTitle();
        String desc=userNotes.getNotedesc();
        String date=userNotes.getNotedate();

        holder.textRowTitle.setText(title);
        holder.textRowDesc.setText(desc);
        holder.textRowDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }



}
