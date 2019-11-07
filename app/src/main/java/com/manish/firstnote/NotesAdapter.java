package com.manish.firstnote;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manish.firstnote.Data.UserNotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

//notes adapter for a recycler view
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    String noteId="",noteDate="";
    Context context;
    ArrayList<UserNotes> datalist = new ArrayList<>();

    UpdateInterface updateInterface;


    //this hold the notes

    public NotesAdapter(Context con, ArrayList<UserNotes> list) {
        context = con;
        datalist = list;
        updateInterface=(UpdateInterface)context;
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
        //getting data from the
        UserNotes userNotes = datalist.get(position);
        String title = userNotes.getNoteTitle();
        String desc = userNotes.getNotedesc();
        String date = userNotes.getNotedate();

        holder.textRowTitle.setText(title);

        holder.textRowDesc.setText(desc);
        holder.textRowDate.setText(date);

        holder.imageRowEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1=datalist.get(position);

                showdailog(userNotes1);
            }
        });

        //for deletion part
        holder.imageRowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1=datalist.get(position);
                ConfirmDeleteDailog(userNotes1);

            }
        });

    }

    public void showdailog(UserNotes noteobj){

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_note);
        dialog.show();

        EditText eTitle=(EditText) dialog.findViewById(R.id.edittext_update_title);
        EditText eDesc=(EditText) dialog.findViewById(R.id.edittext_update_desc);

        eTitle.setText(noteobj.getNoteTitle());// previosly store title and description should be there for editing
        eDesc.setText(noteobj.getNotedesc());
        noteId=noteobj.getNoteid();

        Button buttonupdate=(Button) dialog.findViewById(R.id.button_Update_note);

        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM yyyy");
                Calendar calendar=Calendar.getInstance();
                noteDate=simpleDateFormat.format(calendar.getTime());

                String title=eTitle.getText().toString();
                String description=eDesc.getText().toString();

                UserNotes userNotes=new UserNotes(title,description,noteDate,noteId);
                updateInterface.updateUsernote(userNotes);


            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

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
            ButterKnife.bind(this, itemView);
        }
    }

    public void ConfirmDeleteDailog(UserNotes notesobject){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_delete);
        dialog.show();

        TextView tvdelete=(TextView)dialog.findViewById(R.id.textView_Delete);
        TextView tvCancel=(TextView)dialog.findViewById(R.id.textView_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateInterface.deleteNote(notesobject);

            }
        });

    }

}
