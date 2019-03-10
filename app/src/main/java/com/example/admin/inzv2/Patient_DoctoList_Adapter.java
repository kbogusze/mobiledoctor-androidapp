package com.example.admin.inzv2;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by michael on 14.04.15.
 */
public class Patient_DoctoList_Adapter extends RecyclerView.Adapter<Patient_DoctoList_Adapter.DoctorModelViewHolder> {

    public  DoctorModel.List doctorModelsList;



    public Patient_DoctoList_Adapter() throws IOException {
        doctorModelsList = new DoctorModel.List();
        doctorModelsList= Patient_DoctorList_Activity.doctorModelsList;
/*
       doctorModelsList.add(new DoctorModel(0, "Jan Kowalski", "Kardiolog", 605449465, "Radom"));
        doctorModelsList.add(new DoctorModel(1, "Piotr Nowak", "Pediatra", 605449465, "Warszawa"));
        doctorModelsList.add(new DoctorModel(2, "Zenek ", "Ortopeda", 605449465, "Lublin"));
        doctorModelsList.add(new DoctorModel(3, "Piotr Nowak", "Pediatra", 605449465, "Radom"));
        doctorModelsList.add(new DoctorModel(4, "Jan Kowalski", "Pediatra", 605449465, "Radom"));
        doctorModelsList.add(new DoctorModel(5, "Jan Boguszewski", "Kardiolog", 605449465, "Warszawa"));
        */
    }

    @Override
    public DoctorModelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_doctormodel_card, viewGroup, false);

        return new DoctorModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorModelViewHolder doctorModelViewHolder, final int i) {

        DoctorModel doctorModel = doctorModelsList.get(i);

        doctorModelViewHolder.nameTextView.setText(doctorModel.doctorname);
        doctorModelViewHolder.professionTextView.setText(doctorModel.profesion);
        doctorModelViewHolder.telnumberTextView.setText((doctorModel.phonenumber));
        doctorModelViewHolder.cityTextView.setText(doctorModel.city);

        doctorModelViewHolder.btnButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// button click event
                Intent startNewActivity = new Intent(v.getContext(), Patient_FillPoling_Activity.class);
                startNewActivity.putExtra("doctor_value", i);
                v.getContext().startActivity(startNewActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorModelsList.size();
    }




    public class DoctorModelViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public TextView professionTextView;
        public TextView cityTextView;
        public TextView telnumberTextView;
        public Button btnButton1;

        public DoctorModelViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            professionTextView = (TextView) itemView.findViewById(R.id.professionTextView);
            cityTextView = (TextView) itemView.findViewById(R.id.cityTextView);
            telnumberTextView = (TextView) itemView.findViewById(R.id.telnumberTextView);
            btnButton1 = (Button) itemView.findViewById(R.id.getquestion);
        }
    }



}
