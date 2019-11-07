package com.manish.firstnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manish.firstnote.Data.UserNotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNotesActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_add_note)
    Toolbar toolbarAddNote;
    @BindView(R.id.edittext_title)
    EditText edittextTitle;
    @BindView(R.id.edittext_desc)
    EditText edittextDesc;
    @BindView(R.id.button_save_note)
    Button buttonSaveNote;
    //for creating a new table in Firebase database
    DatabaseReference databasenotes;

    //for pasing unique userid for each user

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_note);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving notes..");
        String userid=sharedPreferences.getString("UID","");
        //creation of table with name of user notes in firebase database
        //we need to store all the notes in used id only so we add child here for the database reference path
        //whenever we need to store or read the data for a particular user so we get all the information or note from dabasenotes variable
        databasenotes= FirebaseDatabase.getInstance().getReference("USERNOTES").child(userid);

        //this thing is used for toolbar
        toolbarAddNote.setTitle("Add Note");
        toolbarAddNote.setTitleTextColor(Color.WHITE);
        toolbarAddNote.setNavigationIcon(R.drawable.ic_arrow_back);//we are adding back button in toolbar
        //whenever user press this back button we just need to finish this activity
        toolbarAddNote.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.button_save_note)
    public void onViewClicked() {

        String title=edittextTitle.getText().toString();
        String desc=edittextDesc.getText().toString();

        //this is used for detailed of publishing the note
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar=Calendar.getInstance();
        String todaysDate=simpleDateFormat.format(calendar.getTime());

        if(!title.equalsIgnoreCase("")){
            if(!desc.equalsIgnoreCase("")){
                progressDialog.show();
                //this will generate random unique key for everytime
                //everytime we are going to save our data and that key also we need to store later we want to edit or delete our notes we use the same key
                String key=databasenotes.push().getKey();
                UserNotes userNotes=new UserNotes(title,desc,todaysDate,key);
                //we are storing the data in USERNOTES under the child node of key
                databasenotes.child(key).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(AddNotesActivity.this,"Note Save Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddNotesActivity.this,"Error Saving notes",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }else{
                Toast.makeText(this,"Please enter description",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Please enter Title",Toast.LENGTH_SHORT).show();
        }

    }
}
