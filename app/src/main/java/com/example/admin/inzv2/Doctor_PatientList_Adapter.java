package com.example.admin.inzv2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Admin on 2016-08-26.
 */
public class Doctor_PatientList_Adapter extends RecyclerView.Adapter<Doctor_PatientList_Adapter.PatientModelViewHolder> {

    public PatientModel.List patientModelsList;

    public Doctor_PatientList_Adapter() throws IOException {
        patientModelsList = new PatientModel.List();
        patientModelsList = Doctor_PatientList_Activity.getPatientModelsList();




    }

    @Override
    public PatientModelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_pacient_card, viewGroup, false);

        return new PatientModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientModelViewHolder doctorModelViewHolder, final int i) {

        PatientModel patientModel = patientModelsList.get(i);
        doctorModelViewHolder.nameTextView.setText(patientModel.getPacientname());
        doctorModelViewHolder.ageTextView.setText("Wiek: " + Integer.toString(patientModel.getAge()));
        doctorModelViewHolder.telnumberTextView.setText((patientModel.getNumber()));
        doctorModelViewHolder.cityTextView.setText(patientModel.getCity());

        doctorModelViewHolder.btnButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// button click event
                        Intent startNewActivity = new Intent(v.getContext(), Doctor_PatientDiagnosis_Activity.class);
                        startNewActivity.putExtra("doctor_value", i);
                        v.getContext().startActivity(startNewActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientModelsList.size();
    }


    public class PatientModelViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView ageTextView;
        public TextView cityTextView;
        public TextView telnumberTextView;
        public Button btnButton1;

        public PatientModelViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            ageTextView = (TextView) itemView.findViewById(R.id.ageTextView);
            cityTextView = (TextView) itemView.findViewById(R.id.cityTextView);
            telnumberTextView = (TextView) itemView.findViewById(R.id.telnumberTextView);
            btnButton1 = (Button) itemView.findViewById(R.id.getquestion);
        }
    }
}
