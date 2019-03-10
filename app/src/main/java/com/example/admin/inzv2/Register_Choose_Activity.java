package com.example.admin.inzv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Register_Choose_Activity extends AppCompatActivity {

    private Button mDoctorButton;
    private Button mPatientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__choose_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDoctorButton = (Button) findViewById(R.id.doctorbutton);
        mDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorRegister();
            }
        });

        mPatientButton = (Button) findViewById(R.id.patientbutton);
        mPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientRegister();
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

    private void patientRegister() {
        Intent startNewRegisterActivity = new Intent(this,Patient_Register_Activity.class);
        startActivity(startNewRegisterActivity);

    }

    private void doctorRegister() {
        Intent startNewRegisterActivity = new Intent(this,Doctor_Register_Activity.class);
        startActivity(startNewRegisterActivity);

    }
}
