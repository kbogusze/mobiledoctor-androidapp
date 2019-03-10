package com.example.admin.inzv2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Admin on 2016-08-22.
 */
public class Patient_History_Adapter extends RecyclerView.Adapter<Patient_History_Adapter.ViewHolder>{

    private PatientModel patient;
    private String id;


    public Patient_History_Adapter(String id,PatientModel patient) {
            this.id=id;
            this.patient=patient;
        }


    public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

        public class DiagnosisViewHolder extends ViewHolder {
            TextView date;
            TextView diagnosis;
            TextView doctorname;
            TextView polingdate;
            Button btnButton1;

            public DiagnosisViewHolder(View v) {
                super(v);
                this.diagnosis = (TextView) v.findViewById(R.id.diagnosisline);
                this.date = (TextView) v.findViewById(R.id.datediagnosis);
                this.doctorname = (TextView) v.findViewById(R.id.doctornameline);
                this.polingdate = (TextView) v.findViewById(R.id.polingdate);
                this.btnButton1 = (Button) v.findViewById(R.id.read_more);
            }
        }

        public class QuestionViewHolder extends ViewHolder {
            TextView questionline;
            TextView anserwline;

            public QuestionViewHolder(View v) {
                super(v);
                this.questionline = (TextView) v.findViewById(R.id.questionline);
                this.anserwline= (TextView) v.findViewById(R.id.anserwline);
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.patient_diagnosis_card, viewGroup, false);
                return new DiagnosisViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


                final int tempint = getItemCount()-(i+1);
                DiagnosisViewHolder holder = (DiagnosisViewHolder) viewHolder;
                holder.diagnosis.setText(patient.getPolingList().get(tempint).diagnosisList.get(0).getDiagnosis_line());
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String dtStr = fmt.print(patient.getPolingList().get(tempint).diagnosisList.get(0).getCreated_at());
                holder.date.setText(dtStr);
                holder.doctorname.setText("Imię i nazwisko lekarza: " + patient.getPolingList().get(tempint).getDoctorname());
                dtStr = fmt.print(patient.getPolingList().get(tempint).getCreated_at());
                holder.polingdate.setText("Data wypełnienia ankiety:  " + dtStr);
                holder.btnButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /// button click event
                        Intent startNewActivity = new Intent(v.getContext(), Patient_HistoryFull_Activity.class);
                        startNewActivity.putExtra("int_value", tempint);
                        v.getContext().startActivity(startNewActivity);
                    }
                });
        }

            @Override
        public int getItemCount() {
            int count=patient.getPolingList().size();
            return count;
        }



}

