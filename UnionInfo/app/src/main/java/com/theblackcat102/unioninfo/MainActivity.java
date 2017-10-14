package com.theblackcat102.unioninfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theblackcat102.unioninfo.Adapter.StudentAdapter;
import com.theblackcat102.unioninfo.Models.Student;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main";
    private ArrayList<Student> items = new ArrayList<>();
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private StudentAdapter studentAdapter;
    private FirebaseAuth mAuth;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private TextView searchBar;
    private MainActivity mContext;
    private boolean isNumeric;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchBar = (TextView) findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        isNumeric = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mContext = this;
        if(mAuth.getCurrentUser() != null){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            TextView userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.user_name);
            userName.setText(""+mAuth.getCurrentUser().getDisplayName());
            TextView userContact = (TextView)navigationView.getHeaderView(0).findViewById(R.id.user_contact);
            userContact.setText(mAuth.getCurrentUser().getEmail());
            new SendHttpRequestTask().execute(mAuth.getCurrentUser().getPhotoUrl().toString());

        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(mContext,AddStudent.class);
                startActivity(intent);
//                readAll();
//                writeNewStudent("Zhi Rui","94328","eee","male");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        studentAdapter = new StudentAdapter(this);
        mRecyclerView.setAdapter(studentAdapter);

        mDatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot data_students = (DataSnapshot) snapshot.child("students");
                items.clear();
                for(DataSnapshot student: data_students.getChildren()){
                    Student studentObj = student.getValue(Student.class);
                    items.add(studentObj);
                }
                studentAdapter.updateStudentView(items);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }

        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Student> new_items = new ArrayList<Student>();
                if(isNumeric(""+charSequence)){
                    for(Student student :items){
                        if(student.getStudentID().contains(""+charSequence)){
                            new_items.add(student);
                        }
                    }
                }else{
                    for(Student student :items){
                        if(student.getStudentName().contains(""+charSequence)){
                            new_items.add(student);
                        }
                    }
                }
                studentAdapter.updateStudentView(new_items);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            changeKeyboard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout_action) {
            mAuth.signOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_student) {
            Intent intent = new Intent(mContext,AddStudent.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_change_keyboard) {
            changeKeyboard();
        }else if (id == R.id.nav_manage) {
            if(fab.getVisibility() == View.VISIBLE){
                fab.setVisibility(View.INVISIBLE);
            }else{
              fab.setVisibility(View.VISIBLE);
            }

        }
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeKeyboard(){
        isNumeric = !isNumeric;
        if(isNumeric){
            searchBar.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else{
            searchBar.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }


    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String userID = params[0];
                URL url = new URL( userID );
                Log.d(MainActivity.TAG, userID );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (IOException e){
                Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
            Log.d(MainActivity.TAG,"setting image url");
            imageView.setImageBitmap(result);
        }
    }
}
