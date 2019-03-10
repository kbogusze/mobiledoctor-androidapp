package com.example.admin.inzv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

public class Patient_Menu_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static PatientModel patient;
    private DrawerLayout drawer;

    public static PatientModel getPatient() {
        return patient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        patient= Login_Activity.getPatient();

        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
  //      getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_patientcalculator) {
            Intent startNewBMIActivity = new Intent(this,BMI_Activity.class);
            startActivity(startNewBMIActivity);
        } else if (id == R.id.nav_patientprofil) {
            Intent startNewProfileActivity = new Intent(this, Patient_Profile_Activity.class);
            startActivity(startNewProfileActivity);
        } else if (id == R.id.nav_doclist) {
            Intent startNewDoctorListActivity = new Intent(this,Patient_DoctorList_Activity.class);
            startActivity(startNewDoctorListActivity);
        }  else if (id == R.id.nav_pateintappinfo) {
            Intent startNewDescriptionActivity = new Intent(this,Description_Activity.class);
            startActivity(startNewDescriptionActivity);
        }
        else if (id == R.id.nav_doctorappinfo) {
            Intent startNewDescriptionActivity = new Intent(this,Description_Activity.class);
            startActivity(startNewDescriptionActivity);

        }
        else if (id == R.id.nav_doctorprofil) {
            Intent startNewDoctorProfileActivity = new Intent(this,Doctor_Profile_Activity.class);
            startActivity(startNewDoctorProfileActivity);

        }
        else if (id == R.id.nav_doctorcalculator) {
            Intent startNewBMIActivity = new Intent(this,BMI_Activity.class);
            startActivity(startNewBMIActivity);
        }
        else if (id == R.id.nav_patienthistory) {
            Intent startNewPatientHistory = new Intent(this,Patient_History_Activity.class);
            startNewPatientHistory.putExtra("id", patient.getId());
            startActivity(startNewPatientHistory);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
