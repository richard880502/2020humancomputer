package com.example.human_computer_interaction_design_final;

import android.os.Bundle;

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

public class Activity_professor_course_history extends AppCompatActivity {

    private String userUID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerView;
    private Adapter_professor_course_history adapter;
    private ArrayList<String> course_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_professor_course_history);

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
                String str_id;
                String str_time;
                String str_name;
                for(int i = 0; i < activityNum; i++){
                    if(dataSnapshot.child("Activity").child("activity" + i).child("professor_id").getValue().toString().equals(userUID)) {
                        str_id = dataSnapshot.child("Activity").child("activity" + i).child("course_id").getValue().toString();
                        str_time = dataSnapshot.child("Activity").child("activity" + i).child("activity_time").getValue().toString();
                        for(int j = 0; j < courseNum; j++){
                            if(dataSnapshot.child("Course").child("course" + j).child("course_id").getValue().toString().equals(str_id)){
                                str_name = str_id + " " + dataSnapshot.child("Course").child("course" + j).child("course_name").getValue().toString()
                                        + "       " + str_time;
                                course_name.add(str_name);
                            }
                        }
                    }
                }

                adapter = new Adapter_professor_course_history(Activity_professor_course_history.this, database, userUID, course_name);
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
        adapter = new Adapter_professor_course_history(null,null, userUID, course_name);
        // 設置adapter給recycler_view
        recyclerView.setAdapter(adapter);
    }

}
