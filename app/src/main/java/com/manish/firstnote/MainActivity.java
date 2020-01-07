package com.manish.firstnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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


public class MainActivity extends AppCompatActivity implements UpdateInterface{

    @BindView(R.id.toolbar_home)
    Toolbar toolbarHome;
    @BindView(R.id.text_home_no_data)
    TextView textHomeNoData;
    @BindView(R.id.Button_add_note)
    FloatingActionButton ButtonAddNote;

    //there multiple notes for one user so we create ArrayList to store data
    ArrayList<UserNotes> allNoteslist = new ArrayList<>();


    //we need user UID for each user to store data particularly for particular UID so we used SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    //we need to display vertically or in list format
    LinearLayoutManager linearLayoutManager;

    //we need to read the data from database so we create DatabaseReference object
    DatabaseReference databasenotes;

    @BindView(R.id.recycler_all_notes)
    RecyclerView recyclerAllNotes;
    FirebaseAuth auth;

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
        setSupportActionBar(toolbarHome);
        auth=FirebaseAuth.getInstance();


        String uid = auth.getUid(); //getting the current user id
        //for reading the data from Fire base database
        databasenotes = FirebaseDatabase.getInstance().getReference("USERNOTES").child(uid);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerAllNotes.setLayoutManager(linearLayoutManager);


    }

    @OnClick(R.id.Button_add_note)
    public void onViewClicked() {
        Intent intentAdd = new Intent(this, AddNotesActivity.class);
        startActivity(intentAdd);          //we are not here finish our intent its continuously running
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadAllnotes();     //whenever we add a particular note and comeback this method will be call
    }

    // for reading notes
    public void ReadAllnotes() {
        allNoteslist.clear();       // clearing the array list
        progressDialog.show();
        //reading data from fire base
        databasenotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //single note information
                //we can iterate for all the data one by one
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //using this snapshot we get single note separated
                    UserNotes userNotes = snapshot.getValue(UserNotes.class); //we used serializable so those value map accordingly
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

    @Override
    public void updateUsernote(UserNotes userNotes) {
        databasenotes.child(userNotes.getNoteid()).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Note updated Successfully",Toast.LENGTH_SHORT).show();
                    ReadAllnotes();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLayout:
                FirebaseAuth.getInstance().signOut();
                clearPreferences();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                editor.commit();
                break;
        }
        return true;
    }

    @Override
    public void deleteNote(UserNotes userNotes) {
        //you just need to specify child node id which we want to delete
        databasenotes.child(userNotes.getNoteid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Note deleted successfully",Toast.LENGTH_SHORT).show();
                    ReadAllnotes();
                }
            }
        });
    }
    private void clearPreferences(){
        try {
            Runtime runtime=Runtime.getRuntime();
            runtime.exec("com.manish.firstnote");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
