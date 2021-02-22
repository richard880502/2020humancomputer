package com.example.human_computer_interaction_design_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_professor_sign_up extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    private String username ;
    private String email;
    private String password;

    private Button sign_up_btn;
    private EditText name_ev;
    private EditText email_ev;
    private EditText password_ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_professor_sign_up);

        sign_up_btn = findViewById(R.id.button7);
        name_ev = findViewById(R.id.editTextTextPersonName);
        email_ev = findViewById(R.id.editTextTextEmailAddress2);
        password_ev = findViewById(R.id.editTextTextPassword2);

        sign_up_btn.setOnClickListener(signUpUser);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    userUID =  user.getUid();
                    Log.d("onAuthStateChanged", "user: "+ userUID);
                }
                else
                    Log.d("onAuthStateChanged", "user: null");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    private Button.OnClickListener signUpUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!TextUtils.isEmpty(name_ev.getText()) && !TextUtils.isEmpty(email_ev.getText()) && !TextUtils.isEmpty(password_ev.getText())) {
                username = name_ev.getText().toString();
                email = email_ev.getText().toString();
                password = password_ev.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            database = FirebaseDatabase.getInstance();
                                            myRef = database.getReference();
                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int userNum = (int) dataSnapshot.child("User").child("Professor").getChildrenCount();
                                                    myRef.child("User").child("Professor").child("user" + userNum).child("id").setValue(userUID);
                                                    myRef.child("User").child("Professor").child("user" + userNum).child("name").setValue(username);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            toastMessage("註冊成功");
                                            auth.signOut();
                                            toSignIn();
                                        }
                                        else {
                                            toastMessage("註冊失敗(信箱已存在)");
                                        }
                                    }
                                });
            }
            else if (TextUtils.isEmpty(name_ev.getText()))
                toastMessage("姓名為空");
            else if (TextUtils.isEmpty(email_ev.getText()))
                toastMessage("信箱為空");
            else if (TextUtils.isEmpty(password_ev.getText()))
                toastMessage("密碼為空");
        }
    };

    private void toSignIn(){
        Intent intent = new Intent(Activity_professor_sign_up.this, Activity_professor_sign_in.class);
        startActivity(intent);
        finish();
    }

    private void toastMessage(String str){
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,500);
        toast.show();
    }
}