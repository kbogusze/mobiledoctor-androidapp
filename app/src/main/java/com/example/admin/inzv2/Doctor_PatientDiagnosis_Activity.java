package com.example.admin.inzv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Doctor_PatientDiagnosis_Activity extends AppCompatActivity {


    private int value ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__patient_diagnosis_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        value = intent.getIntExtra("int_value",0); //if it's a string you stored.

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Doctor_PatientDiagnosis_Adapter(value);
        mRecyclerView.setAdapter(mAdapter);

        historyButton = (Button) findViewById(R.id.historybutton);

        historyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startNewPatientHistory = new Intent(Doctor_PatientDiagnosis_Activity.this,Patient_History_Activity.class);
                startNewPatientHistory.putExtra("id",Doctor_PatientList_Activity.getPatientModelsList().get(value).getId());
                startActivity(startNewPatientHistory);
                // Perform action on click
            }
        });

    }


}
