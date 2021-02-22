package com.example.human_computer_interaction_design_final;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facecompare.VerificationMenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter_student_activity_list extends RecyclerView.Adapter<Adapter_student_activity_list.ViewHolder>  {

    private String userUID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    // Time
    private SimpleDateFormat formatter;
    private Date curDate;
    private String time;

    private Context course_activity;
    private List<String> course_name;

    Adapter_student_activity_list(Context context, FirebaseDatabase db, String id, List<String> name) {
        course_activity = context;
        database = db;
        userUID = id;
        course_name = name;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView course_name_tv;

        ViewHolder(View itemView) {
            super(itemView);
            course_name_tv = (TextView) itemView.findViewById(R.id.course_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(database != null){
                        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        curDate = new Date(System.currentTimeMillis());
                        time = formatter.format(curDate);

                        myRef = database.getReference();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                new AlertDialog.Builder(course_activity)
                                        .setTitle("進行點名")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int pos = getAdapterPosition();
                                                int studentNum = (int) dataSnapshot.child("Activity").child("activity" + pos).child("Student_id").getChildrenCount();
                                                myRef.child("Activity").child("activity" + getAdapterPosition()).child("Student").child("student" + studentNum).child("id").setValue(userUID);
                                                Intent intent = new Intent(course_activity, com.example.facecompare.FaceVerificationActivity.class);
                                                intent.putExtra("pos", pos);
                                                intent.putExtra("UID",userUID);
                                                course_activity.startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("No", null).create()
                                        .show();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 設置txtItem要顯示的內容
        holder.course_name_tv.setText(course_name.get(position));
    }

    @Override
    public int getItemCount() {
        return course_name.size();
    }

}
