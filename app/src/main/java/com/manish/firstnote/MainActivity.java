package com.manish.firstnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.firstnote.Data.UserNotes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbarHome;
    @BindView(R.id.text_home_no_data)
    TextView textHomeNoData;
    @BindView(R.id.Button_add_note)
    FloatingActionButton ButtonAddNote;

    //there multiple notes for one user so we create ArrayList to store data
    ArrayList<UserNotes> allNoteslist = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    ProgressDialog progressDialog;
    //we need to display verticaly or in list format
    LinearLayoutManager linearLayoutManager;

    DatabaseReference databasenotes;

    @BindView(R.id.recycler_all_notes)
    RecyclerView recyclerAllNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reading notes..");

        toolbarHome.setTitle("FireNote");
        toolbarHome.setTitleTextColor(Color.WHITE);


        String uid = sharedPreferences.getString("UID", "");
        //for reading the data from Firebase database
        databasenotes = FirebaseDatabase.getInstance().getReference("USERNOTES").child(uid);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerAllNotes.setLayoutManager(linearLayoutManager);

        ReadAllnotes();

    }

    @OnClick(R.id.Button_add_note)
    public void onViewClicked() {
        Intent intentAdd = new Intent(this, AddNotesActivity.class);
        startActivity(intentAdd);//we are not here finish our intent its continuosly running
    }

    public void ReadAllnotes() {
        allNoteslist.clear();
        progressDialog.show();

        databasenotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //single note information
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    allNoteslist.add(userNotes);
                }
                progressDialog.dismiss();
                NotesAdapter notesAdapter=new NotesAdapter(MainActivity.this,allNoteslist);
                recyclerAllNotes.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
