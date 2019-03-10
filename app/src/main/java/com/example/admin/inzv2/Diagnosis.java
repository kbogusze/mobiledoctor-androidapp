package com.example.admin.inzv2;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Admin on 2016-08-21.
 */
public class Diagnosis {


    String diagnosis_line;
    DateTime created_at;

    public Diagnosis(String diagnosis_line, DateTime created_at) {
        this.diagnosis_line = diagnosis_line;
        this.created_at = created_at;
    }

    public String getDiagnosis_line() {
        return diagnosis_line;
    }

    public void setDiagnosis_line(String diagnosis_line) {
        this.diagnosis_line = diagnosis_line;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public static class List extends ArrayList<Diagnosis> {

    }
}
