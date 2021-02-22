package com.example.human_computer_interaction_design_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_student_activity_list extends AppCompatActivity {

    private String userUID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerView;
    private Adapter_student_activity_list adapter;
    private ArrayList<String> course_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_student_activity_list);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseUser user = auth.getCurrentUser();
    userUID = user.getUid();

    database = FirebaseDatabase.getInstance();
    myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int activityNum = (int) dataSnapshot.child("Activity").getChildrenCount();
            int courseNum = (int) dataSnapshot.child("Course").getChildrenCount();
            for(int i = 0; i < activityNum; i++){
                String activity_course_id = dataSnapshot.child("Activity").child("activity" + i).child("course_id").getValue().toString();
                for(int j = 0; j < courseNum; j++) {
                    String course_id = dataSnapshot.child("Course").child("course" + j).child("course_id").getValue().toString();
                    if (course_id.equals(activity_course_id)) {
                        String str = course_id + " " + dataSnapshot.child("Course").child("course" + j).child("course_name").getValue().toString();
                        course_name.add(str);
                    }
                }
            }

            adapter = new Adapter_student_activity_list(Activity_student_activity_list.this, database, userUID, course_name);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

    // 將資料交給adapter
    adapter = new Adapter_student_activity_list(null,null, userUID, course_name);
    // 設置adapter給recycler_view
        recyclerView.setAdapter(adapter);
    }
}