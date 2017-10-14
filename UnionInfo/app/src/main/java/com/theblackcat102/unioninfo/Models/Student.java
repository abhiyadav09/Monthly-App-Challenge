package com.theblackcat102.unioninfo.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.orm.SugarRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by theblackcat on 13/10/17.
 */


@IgnoreExtraProperties
public class Student extends SugarRecord{
    private String studentName;
    private String studentID;
    private String institude;
    private String sex;


    public Student(){
        super();
    }

    public Student(String studentName, String institude,String studentID,String sex){
        this.studentID = studentID;
        this.studentName = studentName;
        this.institude = institude;
        this.sex = sex;
    }

    public String getStudentName(){
        return studentName;
    }

    public String getInstitude(){
        return institude;
    }

    public String getStudentID(){
        return studentID;
    }

    public String getSex(){
        return sex;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("studentID",studentID);
        result.put("studentName",studentName);
        result.put("institude",institude);
        return result;
    }
}
