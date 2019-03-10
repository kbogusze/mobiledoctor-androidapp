package com.example.admin.inzv2;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Admin on 2016-08-21.
 */
public class Poling {

    String doctorname;
    String doctoremail;
    Question.List questionList ;
    Diagnosis.List diagnosisList;
    DateTime created_at;

    public Poling(Question.List questionList, String doctorname, String doctoremail, Diagnosis.List diagnosisList, DateTime created_at) {
        this.questionList = questionList;
        this.doctoremail = doctoremail;
        this.doctorname = doctorname;
        this.diagnosisList = diagnosisList;
        this.created_at = created_at;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctoremail() {
        return doctoremail;
    }

    public void setDoctoremail(String doctoremail) {
        this.doctoremail = doctoremail;
    }

    public Question.List getQuestionList() {
        return questionList;
    }

    public void setQuestionList(Question.List questionList) {
        this.questionList = questionList;
    }

    public Diagnosis.List getDiagnosisList() {
        return diagnosisList;
    }

    public void setDiagnosisList(Diagnosis.List diagnosisList) {
        this.diagnosisList = diagnosisList;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public static class List extends ArrayList<Poling> {

    }
}
