package com.example.admin.inzv2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Patient_Profile_Activity extends AppCompatActivity {

    private PatientModel patient;
    private EditText mTelNrView;
    private EditText mnameView;
    private EditText memailView;
    private EditText mpasswordView;
    private EditText mnewpasswordView;
    private EditText mnewpassword2View;
    private EditText mdirecionView;
    private EditText mAgeView;

    private Button button ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        patient = Patient_Menu_Activity.getPatient();

   //     mnameView = (EditText) findViewById(R.id.nameeditText);
        memailView = (EditText) findViewById(R.id.emaileditText);
        mpasswordView = (EditText) findViewById(R.id.passwordeditText);
        mnewpasswordView = (EditText) findViewById(R.id.newpasswordeditText);
        mnewpassword2View = (EditText) findViewById(R.id.newpassword2editText);
        mdirecionView = (EditText) findViewById(R.id.direcioneditText);
        mTelNrView = (EditText) findViewById(R.id.nrTELeditText);
   //     mAgeView = (EditText) findViewById(R.id.ageeditText);
        button = (Button) findViewById(R.id.savebutton);

    //    mnameView.setText(patient.getPacientname());
        memailView.setText(patient.getEmail());
    //    mpasswordView.setText(patient.getPassword());
        mdirecionView.setText(patient.getCity());
        mTelNrView.setText(patient.getNumber());
    //    mAgeView.setText(Integer.toString(patient.getAge()));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateinfo();
                // Perform action on click
            }
        });


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

    private void updateinfo() {
        mTelNrView.setError(null);
        mnewpasswordView.setError(null);
        memailView.setError(null);
        mpasswordView.setError(null);
        mnewpassword2View.setError(null);
        mdirecionView.setError(null);

        String email = null;
        String phonenumber = null;
        String city = null;
        String password = null;
        String newpassword2 = null;
        String newpassword = null;


        email = String.valueOf(memailView.getText());
        password = String.valueOf(mpasswordView.getText());
        city = String.valueOf(mdirecionView.getText());
        phonenumber = String.valueOf(mTelNrView.getText());
        newpassword = String.valueOf(mnewpasswordView.getText());
        newpassword2 = String.valueOf(mnewpassword2View.getText());

        boolean cancel = false;
        View focusView = null;

        if(!password.equals("") || !newpassword.equals("") || !newpassword2.equals("")) {
            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mpasswordView.setError("Wpisz hasło");
                focusView = mpasswordView;
                cancel = true;
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(newpassword2)) {
                mnewpassword2View.setError("Wpisz hasło");
                focusView = mnewpassword2View;
                cancel = true;
            }
            if (TextUtils.isEmpty(newpassword)) {
                mnewpasswordView.setError("Wpisz hasło");
                focusView = mnewpasswordView;
                cancel = true;
            }

            if (!password.equals(patient.getPassword())) {
                mpasswordView.setError("Złe Hasło");
                focusView = mpasswordView;
                cancel = true;
            }
            else{
                if (mnewpasswordView.getText().toString().equals(mnewpassword2View.getText().toString())&&cancel==false)
                    patient.setPassword(String.valueOf(mnewpasswordView.getText()));
            }
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

            if (mnewpasswordView.getText().toString().equals(mnewpassword2View.getText().toString())) {
                if (isNetworkAvailable(getApplicationContext())) {


                    //   patient.setPacientname(String.valueOf(mnameView.getText()));
                    patient.setEmail(String.valueOf(memailView.getText()));

                    patient.setCity(String.valueOf(mdirecionView.getText()));
                    patient.setNumber(String.valueOf(mTelNrView.getText()));
                    //     patient.setAge(Integer.parseInt(String.valueOf(mAgeView.getText())));
                    String url = "http://boguinzynierkarestapi.azurewebsites.net/api/pacient/" + String.valueOf(patient.getId());
                    new PutDataTask().execute(url);
                } else {
                    new AlertDialog.Builder(Patient_Profile_Activity.this)
                            .setTitle("Błąd")
                            .setMessage("Brak połączenia z internetem. Proszę spróbować później.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            } else {
                mnewpasswordView.setError("Popraw hasło");
                mnewpassword2View.setError("Popraw hasło");
                focusView = mnewpasswordView;
                focusView.requestFocus();
                new AlertDialog.Builder(Patient_Profile_Activity.this)
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

   public class PutDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Patient_Profile_Activity.this);
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            Toast.makeText(Patient_Profile_Activity.this, result.toString(), Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return putData(params[0]);
            } catch (IOException e) {
                return "Brak połączenia z internetem. Proszę spróbować później.";
            } catch (JSONException e) {
                return "Nieporawne dane";
            }

        }


        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            String result = null;
            //create data to update
            JSONObject dataToSend = patient.patienttoJson(patient);

            //Initialize and config request, then connect to server.
            try {
                //initialize and config request then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //check udate succesfull or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Udało się zarejestrować zmiany";
                } else {
                    return "Nie udało się zarejestrować zmian";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }


            }

        }
    }


}
