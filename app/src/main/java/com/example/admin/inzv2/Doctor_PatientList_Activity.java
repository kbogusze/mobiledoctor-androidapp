package com.example.admin.inzv2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.MenuItem;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Doctor_PatientList_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context context;
    public static PatientModel.List patientModelsList;
    private DoctorModel doctor;

    public static Context getContext() {
        return context;
    }

    public static PatientModel.List getPatientModelsList() {
        return patientModelsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        patientModelsList = new PatientModel.List();

        updateDoctor();
    }

    public boolean isNetworkAvailable(Context context) {
        boolean value = false;

        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            value = true;
        }

        // Log.d ("1", Boolean.toString(value) );
        return value;
    }

    private void updateDoctor() {
        if (isNetworkAvailable(getApplicationContext()))
        {
            new GetDoctorDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/status/"+ String.valueOf(Doctor_Menu_Activity.getDoctor().getId()));
        }
        else{

            new AlertDialog.Builder(Doctor_PatientList_Activity.this)
                    .setTitle("Błąd")
                    .setMessage("Brak połączenia z internetem. Proszę spróbować później.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();
    }

    private void getPatients() {
        if (patientModelsList.size()==0){
            Toast.makeText(Doctor_PatientList_Activity.getContext(), "Brak pacjentów w kolejce", Toast.LENGTH_LONG).show();
            //onBackPressed();
        }
        else {
            for (int i = 0; i < doctor.getPatients().size(); i++) {
                new GetDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/pacient/" + String.valueOf(doctor.getPatients().get(i).getPatient()));
            }
        }
    }

    public class GetDataTask extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;

        public GetDataTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Activity handleacitivity
            progressDialog = new ProgressDialog(Doctor_PatientList_Activity.this);
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            //Initialize and config request, then connect to server.
            try{
                //initialize and config request then connect to server
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //cancel progress dialog
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            if(!result.equals("")) {
                try {
                    patientModelsList.add(parsejson(result));
                    mAdapter = new Doctor_PatientList_Adapter();
                    mRecyclerView.setAdapter(mAdapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                Toast.makeText(getApplicationContext(), "Nie udało się pobrać danych z serwera", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public PatientModel parsejson(String s) {

        PatientModel patientModelsListparse = null;
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                patientModelsListparse=(readPatient(reader));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientModelsListparse;
    }

    public PatientModel readPatient(JsonReader reader) throws IOException {
        String id = null;
        String pacientname=null;
        String email=null;
        String phonenumber=null;
        String city=null;
        int age=0;
        String password=null;

        Poling.List polingList= new Poling.List();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("_id")) {
                id = reader.nextString();
            } else if (name.equals("pacientname")) {
                pacientname = reader.nextString();
            } else if (name.equals("email"))  {
                email = reader.nextString();
            } else if (name.equals("password")) {
                password = reader.nextString();
            } else if (name.equals("age")) {
                age = Integer.parseInt(reader.nextString());
            } else if (name.equals("city")) {
                city = reader.nextString();
            } else if (name.equals("phonenumber")) {
                // phonenumber = Integer.parseInt(reader.nextString());
                phonenumber = reader.nextString();
            } else if (name.equals("ankieta")) {
                polingList=polingListreader(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PatientModel(id,pacientname, email, password,  age,  city, phonenumber,  polingList);
    }

    public  Poling.List polingListreader(JsonReader reader) throws IOException {

        String doctorname=null;
        String doctoremail=null;
        Question.List questionList=new Question.List() ;
        Diagnosis.List diagnosisList=new Diagnosis.List();
        DateTime created_at=null;
        Poling.List polingList = new Poling.List();

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("doctorname")) {
                    doctorname = reader.nextString();
                } else if (name.equals("doctoremail")) {
                    doctoremail = reader.nextString();
                } else if (name.equals("created_at1")) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

                    created_at = DateTime.parse(reader.nextString(), formatter);

                } else if (name.equals("diagnosis")) {
                    diagnosisList=(diagnosisreader(reader));
                } else if (name.equals("question")) {
                    questionList=(questionreader(reader));
                } else {
                    reader.skipValue();
                }
            }reader.endObject();
            polingList.add(new Poling(questionList,  doctorname,doctoremail, diagnosisList, created_at));
        }
        reader.endArray();

        return polingList;


    }

    public Question.List questionreader(JsonReader reader) throws IOException {
        String question_line=null;
        String anserw_line=null;
        Question.List questionList=new Question.List() ;


        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("question_line")) {
                    question_line = reader.nextString();
                } else if (name.equals("anserw_line")) {
                    anserw_line = reader.nextString();
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            questionList.add( new Question(question_line, anserw_line));
        }

        reader.endArray();
        return questionList ;


    }

    public Diagnosis.List diagnosisreader(JsonReader reader) throws IOException {
        String diagnosis_line=null;
        DateTime created_at=null;
        Diagnosis.List diagnosisList=new Diagnosis.List();

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("diagnosis_line")) {
                    diagnosis_line = reader.nextString();
                } else if (name.equals("created_at2")) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
                    created_at = DateTime.parse(reader.nextString(), formatter);
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            diagnosisList.add(new Diagnosis(diagnosis_line, created_at));
        }
        reader.endArray();
        return diagnosisList;

    }

    public DoctorModel parsejsontodoctor(String s) {
        DoctorModel doctorModelparse = null;
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            //       reader.beginArray();
            //       while (reader.hasNext()) {
            doctorModelparse=readDoctor(reader);
            //       }
            //        reader.endArray();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorModelparse;

    }

    private  DoctorModel readDoctor(JsonReader reader) throws IOException {

        String id = null;
        String doctorname=null;
        String email=null;
        String phonenumber=null;
        String city=null;
        String profesion=null;
        String password=null;

        Question2.List question2List= new Question2.List();
        Patient2.List patien2tList= new Patient2.List();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("_id")) {
                id = reader.nextString();
            } else if (name.equals("doctorname")) {
                doctorname = reader.nextString();
            } else if (name.equals("email"))  {
                email = reader.nextString();
            } else if (name.equals("password")) {
                password = reader.nextString();
            } else if (name.equals("profesion")) {
                profesion = reader.nextString();
            } else if (name.equals("city")) {
                city = reader.nextString();
            } else if (name.equals("phonenumber")) {
                // phonenumber = Integer.parseInt(reader.nextString());
                phonenumber = reader.nextString();
            } else if (name.equals("patient")) {
                patien2tList=patien2tListreader(reader);
            } else if (name.equals("question")) {
                question2List=question2Listreader(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DoctorModel( id, doctorname,  email,  password,  profesion,  phonenumber,  city,  question2List, patien2tList);

    }

    private static Question2.List question2Listreader(JsonReader reader) throws IOException {
        String question_line=null;
        Question2.List questionList=new Question2.List() ;


        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("question_line")) {
                    question_line = reader.nextString();
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            questionList.add( new Question2(question_line));
        }

        reader.endArray();
        return questionList ;
    }

    private static Patient2.List patien2tListreader(JsonReader reader) throws IOException {
        String patientid=null;
        Patient2.List patientList=new Patient2.List() ;
        DateTime created_at=null;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("patientid")) {
                    patientid = reader.nextString();
                }else if (name.equals("created_at2")) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
                    created_at = DateTime.parse(reader.nextString(), formatter);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            patientList.add(new Patient2(patientid, created_at));
        }

        reader.endArray();
        return patientList ;
    }

    public class GetDoctorDataTask extends AsyncTask<String,Void,String> {

        public String getresponse;
        ProgressDialog progressDialog;

        public GetDoctorDataTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Activity handleacitivity
            progressDialog = new ProgressDialog(Doctor_PatientList_Activity.this);
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();


            //Initialize and config request, then connect to server.
            try{
                //initialize and config request then connect to server
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");

                }
                int code=  urlConnection.getResponseCode();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //set data response to textView
            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if(!result.equals("")) {
                doctor = parsejsontodoctor(result.toString());
                getPatients();
            }
        }

    }
}
