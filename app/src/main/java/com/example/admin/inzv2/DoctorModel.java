package com.example.admin.inzv2;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 2016-06-13.
 */
public class DoctorModel {

    public String id;
    public String doctorname;
    private String  email;
    private String  password;
    public String profesion;
    public String phonenumber;
    public String city;
    private Question2.List questions;
    private Patient2.List patients;

    public DoctorModel(String id, String doctorname, String email, String password, String profesion, String phonenumber, String city, Question2.List questions, Patient2.List patients) {
        this.id = id;
        this.doctorname = doctorname;
        this.email = email;
        this.password = password;
        this.profesion = profesion;
        this.phonenumber = phonenumber;
        this.city = city;
        this.questions = questions;
        this.patients = patients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Question2.List getQuestions() {
        return questions;
    }

    public void setQuestions(Question2.List questions) {
        this.questions = questions;
    }

    public Patient2.List getPatients() {
        return patients;
    }

    public void setPatients(Patient2.List patients) {
        this.patients = patients;
    }

    public JSONObject doctortoJson(DoctorModel doctor) throws JSONException {

        JSONObject dataToSend = new JSONObject();
        dataToSend.put("doctorname", doctor.getDoctorname());
        dataToSend.put("email",doctor.getEmail());
        dataToSend.put("password",doctor.getPassword());
        dataToSend.put("profesion",doctor.getProfesion());
        dataToSend.put("phonenumber", doctor.getPhonenumber());
        dataToSend.put("city", doctor.getCity());


        return dataToSend;
    }


    public static class List extends ArrayList<DoctorModel> {

    }

    public JSONObject DoctortoJson (DoctorModel  doctor) throws JSONException {
        JSONArray pollingarray = new JSONArray();

/*
        for (int i = 0; i < patient.getPolingList().size(); i++) {

            JSONObject polling = new JSONObject();
            JSONArray questionarray = new JSONArray();
            JSONArray diagnosisnarray = new JSONArray();
            polling.put("doctorname", patient.getPolingList().get(i).getDoctorname());
            polling.put("doctoremail", patient.getPolingList().get(i).getDoctoremail());

            for (int j = 0; j < patient.getPolingList().get(i).getQuestionList().size(); j++) {
                JSONObject question = new JSONObject();
                question.put("question_line", patient.getPolingList().get(i).getQuestionList().get(j).getQuestion_line());
                question.put("anserw_line", patient.getPolingList().get(i).getQuestionList().get(j).getAnserw_line());

                questionarray.put(question);
            }

            polling.putOpt("question", questionarray);

            for (int j = 0; j < patient.getPolingList().get(i).getDiagnosisList().size(); j++) {
                JSONObject diagnos = new JSONObject();
                diagnos.put("diagnosis_line", patient.getPolingList().get(i).getDiagnosisList().get(j).getDiagnosis_line());
                diagnos.put("created_at2", patient.getPolingList().get(i).getDiagnosisList().get(j).getCreated_at());

                diagnosisnarray.put(diagnos);
            }

            polling.putOpt("diagnosis", diagnosisnarray);

            pollingarray.put(polling);
        }

        JSONObject json = new JSONObject();
        json.putOpt("ankieta", pollingarray);

        return json;
        */
        JSONObject json = new JSONObject();


        return json;
    }

    public JSONObject DoctortoJsonPatientid (DoctorModel  doctor) throws JSONException {

        JSONArray pateintnarray = new JSONArray();


            for (int j = 0; j < doctor.getPatients().size(); j++) {
                JSONObject patient = new JSONObject();
                patient.put("patientid", doctor.getPatients().get(j).getPatient());
                patient.put("created_at2", doctor.getPatients().get(j).getCreated_at());
                pateintnarray.put(patient);
            }


        JSONObject json = new JSONObject();
        json.putOpt("patient", pateintnarray);

        return json;
    }

    public JSONObject DoctortoJsonPolling (DoctorModel  doctor) throws JSONException {

        JSONArray questionarray = new JSONArray();
        for (int j = 0; j < doctor.getQuestions().size(); j++) {
            JSONObject patient = new JSONObject();
            patient.put("question_line", doctor.getQuestions().get(j).getQuestion());
            questionarray.put(patient);
        }
        JSONObject json = new JSONObject();
        json.putOpt("question", questionarray);

        return json;
    }

}

class Question2 {

    public String question;

    public Question2(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public static class List extends ArrayList<Question2> {

    }
}

class Patient2 {

    public String patient;
    DateTime created_at;

    public Patient2(String patient, DateTime created_at) {
        this.patient = patient;
        this.created_at = created_at;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public static class List extends ArrayList<Patient2> {

    }
}