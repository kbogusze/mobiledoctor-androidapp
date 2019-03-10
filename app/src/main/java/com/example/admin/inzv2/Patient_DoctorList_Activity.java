package com.example.admin.inzv2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
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

public class Patient_DoctorList_Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static DoctorModel.List doctorModelsList;


    public DoctorModel.List getDoctorModelsList() {
        return doctorModelsList;
    }

    public void setDoctorModelsList(DoctorModel.List doctorModelsList) {
        this.doctorModelsList = doctorModelsList;
    }

    private CharSequence mTitle;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_doctor_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        doctorModelsList = new DoctorModel.List();
        new GetDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/status");



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


    public void onSectionAttached(int number) {

    }

    public class GetDataTask extends AsyncTask<String,Void,String> {

        public String getresponse;
        ProgressDialog progressDialog;

        public GetDataTask() {

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //Activity handleacitivity
            progressDialog = new ProgressDialog(Patient_DoctorList_Activity.this);
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
            //set data response to textView
            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (!result.toString().equals("")) {
                try {
                    doctorModelsList = parsejson(result);
                    mAdapter = new Patient_DoctoList_Adapter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecyclerView.setAdapter(mAdapter);
            }else{
                Toast.makeText(progressDialog.getContext(), "Nie udało się pobrać danych z serwera", Toast.LENGTH_LONG).show();
                finish();
            }

        }


    }

    public DoctorModel.List parsejson(String s) {

        DoctorModel.List doctorModelsListparse = new DoctorModel.List();
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                doctorModelsListparse.add(readDoctor(reader));
            }
            reader.endArray();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorModelsListparse;
    }



    public DoctorModel readDoctor(JsonReader reader) throws IOException {
        String id = null;
        String doctorname="";
        String email=null;
        String profesion="";
        String phonenumber=null;
        String city="";
        String password=null;
        Patient2.List patients = new Patient2.List();
        Question2.List  questions = new Question2.List();
        String patientid=null;
        DateTime created_at=null;


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
            } else if (name.equals("profesion"))  {
                profesion = reader.nextString();
            } else if (name.equals("phonenumber")) {
                phonenumber = (reader.nextString());
            } else if (name.equals("city")) {
                city = reader.nextString();
            } else if (name.equals("patient")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name= reader.nextName();
                        if (name.equals("patientid")) {
                            patientid = reader.nextString();
                        }else if (name.equals("created_at2")) {
                            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
                            created_at = DateTime.parse(reader.nextString(), formatter);
                        }else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    patients.add(new Patient2(patientid,created_at));
                }

                reader.endArray();
            } else if (name.equals("question")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name= reader.nextName();
                        if (name.equals("question_line")) {
                            patientid = reader.nextString();
                        }else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    questions.add(new Question2(patientid));
                }

                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DoctorModel( id, doctorname,  email,  password,  profesion,  phonenumber,  city,questions,  patients);
    }

}
