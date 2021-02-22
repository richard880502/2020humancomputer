package com.example.human_computer_interaction_design_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_professor_course extends AppCompatActivity {

    private String userUID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerView;
    private Adapter_professor_course adapter;
    private ArrayList<String> course_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_professor_course);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userUID = user.getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int courseNum = (int) dataSnapshot.child("Course").getChildrenCount();
                for(int i = 0; i < courseNum; i++){
                    if(dataSnapshot.child("Course").child("course" + i).child("professor_id").getValue().toString().equals(userUID)) {
                        String str = dataSnapshot.child("Course").child("course" + i).child("course_id").getValue().toString()
                                + " " + dataSnapshot.child("Course").child("course" + i).child("course_name").getValue().toString();
                        course_name.add(str);
                    }
                }

                adapter = new Adapter_professor_course(Activity_professor_course.this, database, userUID, course_name);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 連結元件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 設置RecyclerView為列表型態
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 將資料交給adapter
        adapter = new Adapter_professor_course(null,null, userUID, course_name);
        // 設置adapter給recycler_view
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_history) {
            Intent intent = new Intent(Activity_professor_course.this, Activity_professor_course_history.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}