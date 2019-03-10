package com.example.admin.inzv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class Login_Activity extends AppCompatActivity  {



    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private Button mRegisterButton;
    public boolean Doctorflag=false;

    private static DoctorModel doctor;
    private static PatientModel patient;

    public static DoctorModel getDoctor() {
        return doctor;
    }

    public static PatientModel getPatient() {
        return patient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mDoctorSignInButton = (Button) findViewById(R.id.doctorbutton);
        mDoctorSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorLogin();
            }
        });

        mRegisterButton = (Button) findViewById(R.id.registerbutton);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptRegister() {
        Intent startNewRegisterActivity = new Intent(this,Register_Choose_Activity.class);
        startActivity(startNewRegisterActivity);

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

    private void doctorLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError("Wpisz hasło");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (isNetworkAvailable(getApplicationContext()))
            {
                Doctorflag=true;
                new PostDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/logindoctor");
            }
            else{

                new AlertDialog.Builder(Login_Activity.this)
                        .setTitle("Błąd")
                        .setMessage("Brak połączenia z internetem. Proszę spróbować później.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }

        }



    }



    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError("Wpisz hasło");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (isNetworkAvailable(getApplicationContext()))
            {
                Doctorflag=false;
                new PostDataTask().execute("http://boguinzynierkarestapi.azurewebsites.net/api/loginpatient");
            }
            else{

                new AlertDialog.Builder(Login_Activity.this)
                        .setTitle("Error")
                        .setMessage("Brak połączenia z internetem. Proszę spróbować później.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }

        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String responsecode="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login_Activity.this);
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result.equals(""))
            Toast.makeText(Login_Activity.this, "Nie udało się zalogować, błędny login lub hasło", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return postData(params[0]);
            } catch (IOException e) {
                Toast.makeText(Login_Activity.this, "Brak połączenia z internetem", Toast.LENGTH_LONG).show();
                return "Brak połączenia z internetem";
            } catch (JSONException e) {
                Toast.makeText(Login_Activity.this, "Złe dane", Toast.LENGTH_LONG).show();
                return "Złe dane";
            }

        }


        private String postData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader=null;
            StringBuilder stringBuilder = new StringBuilder();
            responsecode="";

                    //create data to update
            JSONObject dataToSend = getdatafromlayout();

            //Initialize and config request, then connect to server.
            try {
                //initialize and config request then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();


                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }
                //check udate succesfull or not
                if (urlConnection.getResponseCode() == 200) {
                    responsecode= "udało sie zalogować";
                    if(Doctorflag==false) {
                        patient = parsejson(stringBuilder.toString());
                        Intent startNewActivity = new Intent(Login_Activity.this, Patient_Menu_Activity.class);
                        startActivity(startNewActivity);
                    }else {
                        doctor=parsejsontodoctor(stringBuilder.toString());
                        Intent startNewActivity = new Intent(Login_Activity.this, Doctor_Menu_Activity.class);
                        startActivity(startNewActivity);
                    }
                } else {
                    responsecode= "";

                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }
                if (bufferedReader != null) {

                    bufferedReader.close();
                }
                return  responsecode;
            }

        }
    }

    private JSONObject getdatafromlayout() throws JSONException {
        JSONObject dataToSend = new JSONObject();
        dataToSend.put("email",mEmailView.getText().toString());
        dataToSend.put("password", mPasswordView.getText().toString());
        return dataToSend;
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
                question2List=questionListreader(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DoctorModel( id, doctorname,  email,  password,  profesion,  phonenumber,  city,  question2List, patien2tList);

    }

    private static Question2.List questionListreader(JsonReader reader) throws IOException {
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


}

