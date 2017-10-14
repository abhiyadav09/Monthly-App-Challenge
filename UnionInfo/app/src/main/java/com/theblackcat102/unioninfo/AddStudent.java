package com.theblackcat102.unioninfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theblackcat102.unioninfo.Models.Student;

import java.util.HashMap;
import java.util.Map;

public class AddStudent extends AppCompatActivity {
    DatabaseReference mDatabase;
    Button submitBtn;
    EditText studentName,studentID,studentInstitude;
    RadioGroup studentGender;
    AddStudent mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        submitBtn = (Button) findViewById(R.id.submit_btn);
        studentName = (EditText) findViewById(R.id.new_student_name);
        studentInstitude = (EditText) findViewById(R.id.new_student_institude);
        studentID = (EditText) findViewById(R.id.new_student_id);
        studentGender = (RadioGroup) findViewById(R.id.new_student_gender);
        mContext = this;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentNameStr = studentName.getText().toString();
                String studentIDStr = studentID.getText().toString();
                String studentInstitudeStr = studentInstitude.getText().toString();
                String gender = "none";
                int selected = studentGender.getCheckedRadioButtonId();
                switch (selected){
                    case R.id.male:
                        gender = "male";
                        break;
                    case R.id.female:
                        gender = "female";
                        break;
                    case R.id.other:
                        gender="other";
                        break;
                }
                if( studentIDStr != "" &&  studentInstitudeStr != "" && studentNameStr != "" && gender != "none"){
                    writeNewStudent(studentNameStr,studentIDStr,studentInstitudeStr,gender);
                    Intent intent = new Intent(mContext,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void writeNewStudent(String name,String studentID,String institute,String sex){
        String key = studentID;
        Student student = new Student(name,institute,studentID,sex);
        Map<String,Object> postValues = student.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/students/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
