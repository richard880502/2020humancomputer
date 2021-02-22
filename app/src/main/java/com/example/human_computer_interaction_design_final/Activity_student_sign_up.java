package com.example.human_computer_interaction_design_final;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facecompare.SelectImageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.helper.ImageHelper;

public class Activity_student_sign_up extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String username;
    private String schoolNum;
    private String email;
    private String password;

    private Bitmap bitmap;
    private Button upload_img_btn;

    private Button sign_up_btn;
    private EditText name_ev;
    private EditText schoolNum_ev;
    private EditText email_ev;
    private EditText password_ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_student_sign_up);

        name_ev = findViewById(R.id.editTextTextPersonName2);
        schoolNum_ev = findViewById(R.id.editTextNumberDecimal);
        email_ev = findViewById(R.id.editTextTextEmailAddress4);
        password_ev = findViewById(R.id.editTextTextPassword4);
        sign_up_btn = findViewById(R.id.button8);
        upload_img_btn = findViewById(R.id.button9);


        sign_up_btn.setOnClickListener(signUpUser);
        upload_img_btn.setOnClickListener(uploadImg);

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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // If image is selected successfully, set the image URI and bitmap.
            Bitmap img = ImageHelper.loadSizeLimitedBitmapFromUri(
                    data.getData(), getContentResolver());
            if (img != null) {
                bitmap = img;
                FirebaseStorage storage = FirebaseStorage.getInstance();
                String filename = schoolNum_ev.getText().toString();

                //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                StorageReference storageRef = storage.getReference().child(filename + ".jpg");
                Uri uri = data.getData();
                // Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                storageRef.putFile(uri);
            }
        }
        else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String filename = schoolNum_ev.getText().toString();

            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference storageRef = storage.getReference().child(filename + ".jpg");
            Uri uri = data.getData();
            // Uri tempUri = getImageUri(getApplicationContext(), bitmap);

            storageRef.putFile(uri);
        }
    }

    private Button.OnClickListener signUpUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!TextUtils.isEmpty(name_ev.getText()) && !TextUtils.isEmpty(schoolNum_ev.getText()) && !TextUtils.isEmpty(email_ev.getText()) && !TextUtils.isEmpty(password_ev.getText())) {
                username = name_ev.getText().toString();
                schoolNum = schoolNum_ev.getText().toString();
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
                                            int userNum = (int) dataSnapshot.child("User").child("Student").getChildrenCount();
                                            myRef.child("User").child("Student").child("user" + userNum).child("id").setValue(userUID);
                                            myRef.child("User").child("Student").child("user" + userNum).child("name").setValue(username);
                                            myRef.child("User").child("Student").child("user" + userNum).child("school_num").setValue(schoolNum);
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
            else if (TextUtils.isEmpty(schoolNum_ev.getText()))
                toastMessage("學號為空");
            else if (TextUtils.isEmpty(email_ev.getText()))
                toastMessage("信箱為空");
            else if (TextUtils.isEmpty(password_ev.getText()))
                toastMessage("密碼為空");
        }
    };

    private Button.OnClickListener uploadImg = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Activity_student_sign_up.this, SelectImageActivity.class);
            startActivityForResult(intent, 0);
        }
    };

    private void toSignIn(){
        Intent intent = new Intent(Activity_student_sign_up.this, Activity_student_sign_in.class);
        startActivity(intent);
        finish();
    }

    private void toastMessage(String str){
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,500);
        toast.show();
    }
}