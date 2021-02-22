package com.example.human_computer_interaction_design_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button professor_btn;
    private Button student_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);

        professor_btn = findViewById(R.id.button);
        student_btn = findViewById(R.id.button2);

        professor_btn.setOnClickListener(goProfessorSign);
        student_btn.setOnClickListener(goStudentSign);
    }

    private Button.OnClickListener goProfessorSign = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, Activity_professor_sign_in.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener goStudentSign = new View.OnClickListener(){
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, Activity_student_sign_in.class);
            startActivity(intent);
        }
    };
}