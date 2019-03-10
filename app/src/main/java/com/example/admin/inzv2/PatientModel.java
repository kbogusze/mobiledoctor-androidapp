package com.example.admin.inzv2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 2016-08-21.
 */
public class PatientModel  {
    private String  id;
    private String  pacientname;
    private String  email;
    private String  password;
    private int age;
    private String  city;
    private String Number;
    private Poling.List polingList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PatientModel(String id,String pacientname, String email, String password, int age, String city, String number, Poling.List polingList) {
        this.id = id;
        this.pacientname = pacientname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.city = city;
        Number = number;
        this.polingList = polingList;
    }

    public PatientModel() {
        this.id = "";
        this.pacientname = "";
        this.email = "";
        this.password = "";
        this.age = 0;
        this.city = "";
        Number = "";
        this.polingList = new Poling.List();
    }

    public String getPacientname() {
        return pacientname;
    }

    public void setPacientname(String pacientname) {
        this.pacientname = pacientname;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Poling.List getPolingList() {
        return polingList;
    }

    public void setPolingList(Poling.List polingList) {
        this.polingList = polingList;
    }

    public static class List extends ArrayList<PatientModel> {

    }

    public JSONObject patienttoJson (PatientModel  patient) throws JSONException {


        JSONObject dataToSend = new JSONObject();
        dataToSend.put("pacientname", patient.getPacientname());
        dataToSend.put("email",patient.getEmail());
        dataToSend.put("password",patient.getPassword());
        dataToSend.put("age",patient.getAge());
        dataToSend.put("city",patient.getCity());
        dataToSend.put("phonenumber",patient.getNumber());

    return dataToSend;
    }

    public JSONObject patienttoJsonPolling (PatientModel  patient) throws JSONException {
        JSONArray pollingarray = new JSONArray();


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
    }
}




