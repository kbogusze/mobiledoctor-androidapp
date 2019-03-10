package com.example.admin.inzv2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Patient_Register_Activity extends AppCompatActivity {

    private PatientModel patient;
    private EditText mTelNrView;
    private EditText mnameView;
    private EditText memailView;
    private EditText mpasswordView;
    private EditText mpassword2View;
    private EditText mdirecionView;
    private EditText mAgeView;

    private Button button ;
    public String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__register_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mnameView = (EditText) findViewById(R.id.nameeditText);
        memailView = (EditText) findViewById(R.id.emaileditText);
        mpasswordView = (EditText) findViewById(R.id.passwordeditText);
        mpassword2View = (EditText) findViewById(R.id.password2editText);
        mdirecionView = (EditText) findViewById(R.id.direcioneditText);
        mTelNrView = (EditText) findViewById(R.id.nrTELeditText);
        mAgeView = (EditText) findViewById(R.id.ageeditText);
        button = (Button) findViewById(R.id.savebutton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
                // Perform action on click
            }
        });
    }

    private void register() {
// Reset errors.
        mTelNrView.setError(null);
        mnameView.setError(null);
        memailView.setError(null);
        mpasswordView.setError(null);
        mpassword2View.setError(null);
        mdirecionView.setError(null);
        mAgeView.setError(null);

        String id = null;
        String pacientname = null;
        String email = null;
        String phonenumber = null;
        String city = null;
        String agestring;
        int age = 0;
        String password = null;
        String password2 = null;

        pacientname = String.valueOf(mnameView.getText());
        email = String.valueOf(memailView.getText());
        password = String.valueOf(mpasswordView.getText());
        city = String.valueOf(mdirecionView.getText());
        phonenumber = String.valueOf(mTelNrView.getText());
        agestring=String.valueOf(mAgeView.getText());
        password2 = String.valueOf(mpassword2View.getText());

        if (agestring.equals(""))
            age=0;
        else
        age = Integer.parseInt(agestring);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mpasswordView.setError("Wpisz hasło");
            focusView = mpasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password2)) {
            mpassword2View.setError("Wpisz hasło");
            focusView = mpassword2View;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(pacientname)) {
            mnameView.setError("Wpisz Imię");
            focusView = mnameView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(city)) {
            mdirecionView.setError("Wpisz Miasto");
            focusView = mdirecionView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(phonenumber)) {
            mTelNrView.setError("Wpisz Telefon");
            focusView = mTelNrView;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            memailView.setError(getString(R.string.error_field_required));
            focusView = memailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            memailView.setError(getString(R.string.error_invalid_email));
            focusView = memailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


            if (mpasswordView.getText().toString().equals(mpassword2View.getText().toString())) {
                if (isNetworkAvailable(getApplicationContext())) {
                    Poling.List polingList = new Poling.List();
                    patient = new PatientModel(id, pacientname, email, password, age, city, phonenumber, polingList);
                    String url = "http://boguinzynierkarestapi.azurewebsites.net/api/pacient/";
                    new PostDataTask().execute(url);
                } else {
                    new AlertDialog.Builder(Patient_Register_Activity.this)
                            .setTitle("Błąd")
                            .setMessage("Brak połączenia z internetem. Proszę spróbować później.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            } else {
                mpasswordView.setError("Popraw hasło");
                mpassword2View.setError("Popraw hasło");
                focusView = mpasswordView;
                focusView.requestFocus();
                new AlertDialog.Builder(Patient_Register_Activity.this)
                        .setTitle("Błąd")
                        .setMessage("Podane hasła nie są jednakowe")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();

            }

        }

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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();
    }

    public class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String responsecode="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Patient_Register_Activity.this);
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            Toast.makeText(Patient_Register_Activity.this, responsecode, Toast.LENGTH_SHORT).show();
            if(responsecode.equals("Udało się zarejestrować")){
                Intent i = new Intent(Patient_Register_Activity.this, Login_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return postData(params[0]);
            } catch (IOException e) {
                return "Brak połączenia z internetem";
            } catch (JSONException e) {
                return "Złe dane";
            }

        }


        private String postData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader=null;
            StringBuilder stringBuilder = new StringBuilder();

            //create data to update
            JSONObject dataToSend = patient.patienttoJson(patient);

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
                if (urlConnection.getResponseCode() == 201) {
                    responsecode= "Udało się zarejestrować";
                    //
                } else {

                    responsecode= "Nie udało się zarejestrować";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }
                if (bufferedReader != null) {

                    bufferedReader.close();
                }

                return   stringBuilder.toString();
            }

        }
    }
}
