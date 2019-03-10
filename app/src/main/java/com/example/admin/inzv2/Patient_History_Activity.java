package com.example.admin.inzv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Patient_History_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String id;
    private static PatientModel patient;

    public static PatientModel getPatient() {
        return patient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        Intent intent = getIntent();
        id=intent.getStringExtra("id"); //if it's a string you stored.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/pacient/"+String.valueOf(id));

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
      //  mAdapter = new Patient_History_Adapter(id);
      //  mRecyclerView.setAdapter(mAdapter);
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

    public class GetDataTask extends AsyncTask<String,Void,String> {

        public String getresponse;
        ProgressDialog progressDialog;

        public GetDataTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

                 progressDialog = new ProgressDialog(Patient_History_Activity.this);
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
            //mResult.setText(result);
            getresponse = result.toString();
            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (!getresponse.equals("")) {
                patient = parsejson(getresponse);

                mAdapter = new Patient_History_Adapter(id, patient);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Toast.makeText(progressDialog.getContext(), "Nie udało się pobrać danych z serwera", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    public PatientModel parsejson(String s) {

        PatientModel patientModelparse = null;
        InputStream stream = new ByteArrayInputStream(s.getBytes());
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            patientModelparse=readPatient(reader);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientModelparse;
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
            polingList.add(new Poling(questionList, doctorname, doctoremail, diagnosisList, created_at));
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
}
