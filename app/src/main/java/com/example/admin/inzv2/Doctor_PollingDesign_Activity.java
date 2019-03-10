package com.example.admin.inzv2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Doctor_PollingDesign_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DoctorModel doctor;
    private Button addNewQuestionButton;
    private Button savePollingButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__polling_design_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doctor= Doctor_Menu_Activity.getDoctor();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Doctor_PollingDesign_Adapter();
        mRecyclerView.setAdapter(mAdapter);

        addNewQuestionButton = (Button) findViewById(R.id.newquestionbutton);
        savePollingButton = (Button) findViewById(R.id.savebutton);

        savePollingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                savebuttonclick();
                }
            });

        addNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addquestionbuttonclick();
            }
        });
    }

    public void addquestionbuttonclick(){
        doctor.getQuestions().add(new Question2(""));
        mAdapter.notifyDataSetChanged();
    }
    public void savebuttonclick(){

        new AlertDialog.Builder(this)
                .setTitle("Zapisywanie Ankiety")
                .setMessage("Czy na pewno chcesz zapisać ankietę?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        progressDialog = new ProgressDialog(getApplicationContext());
                        String url = "http://boguinzynierkarestapi.azurewebsites.net/api/status/" + String.valueOf(doctor.getId());
                        new PutDataTask2().execute(url);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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

    public class PutDataTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return putData(params[0]);
            } catch (IOException e) {
                return "Brak dostępu do internetu";
            } catch (JSONException e) {
                return "Złe dane";
            }

        }


        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            //create data to update
            JSONObject dataToSend = doctor.DoctortoJsonPolling(doctor);
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
                    return "Zapisanie ankiety powiodło się";
                } else {

                    return "Zapisanie ankiety nie powiodło się";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }
            }

        }
    }
}
