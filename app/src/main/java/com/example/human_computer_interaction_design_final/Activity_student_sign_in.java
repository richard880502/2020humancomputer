package com.example.human_computer_interaction_design_final;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_student_sign_in extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText ed_email;
    private EditText ed_password;
    private String userUID;

    private Button sign_up_btn;
    private Button activity_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_student_sign_in);

        sign_up_btn = findViewById(R.id.button5);
        activity_btn = findViewById(R.id.button6);

        sign_up_btn.setOnClickListener(goSignUp);
        activity_btn.setOnClickListener(signInUser);

        auth = FirebaseAuth.getInstance();
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user!=null) {
                userUID =  user.getUid();
                Log.d("onAuthStateChanged", "user: "+ userUID);
            }else{
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        auth.signOut();
    }

    private Button.OnClickListener signInUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ed_email = (EditText)findViewById(R.id.editTextTextEmailAddress3);
            ed_password = (EditText)findViewById(R.id.editTextTextPassword3);

            if(!TextUtils.isEmpty(ed_email.getText()) && !TextUtils.isEmpty(ed_password.getText())) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    toastMessage("登入成功");
                                    goActivity();
                                } else {
                                    toastMessage("登入失敗");
                                }
                            }
                        });
            }
            else if (TextUtils.isEmpty(ed_email.getText()))
                toastMessage("信箱為空");
            else if (TextUtils.isEmpty(ed_password.getText()))
                toastMessage("密碼為空");
        }
    };

    private Button.OnClickListener goSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Activity_student_sign_in.this, Activity_student_sign_up.class);
            startActivity(intent);
            //finish();
        }
    };

    private void goActivity(){
        ed_email.setText("");
        ed_password.setText("");
        Intent intent = new Intent(Activity_student_sign_in.this, Activity_student_activity_list.class);
        startActivity(intent);
    }

    private void toastMessage(String str){
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,500);
        toast.show();
    }
}
