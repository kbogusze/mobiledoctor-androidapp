package com.example.admin.inzv2;

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
public class Patient_HistoryFull_Adapter extends RecyclerView.Adapter<Patient_HistoryFull_Adapter.ViewHolder> {

    private PatientModel patient;
    private int value;

    public Patient_HistoryFull_Adapter(int value) {
        patient= Patient_History_Activity.getPatient();
        this.value= value;
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

            if (viewType == 0) {
            v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.patient_diagnosis_card, viewGroup, false);
            return new DiagnosisViewHolder(v);
            } else {
             v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.patient_question_card, viewGroup, false);
                return new QuestionViewHolder(v) ;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType()  ==0){
        DiagnosisViewHolder holder = (DiagnosisViewHolder) viewHolder;
        holder.diagnosis.setText(patient.getPolingList().get(value).diagnosisList.get(0).getDiagnosis_line());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String dtStr = fmt.print(patient.getPolingList().get(value).diagnosisList.get(0).getCreated_at());
        holder.date.setText(dtStr);
        holder.doctorname.setText("Imię i nazwisko lekarza: " + patient.getPolingList().get(value).getDoctorname());
        dtStr = fmt.print(patient.getPolingList().get(value).getCreated_at());
        holder.polingdate.setText("Data wypełnienia ankiety:  " + dtStr);
        holder.btnButton1.setVisibility(View.GONE);}

    else {
        QuestionViewHolder holder = (QuestionViewHolder) viewHolder;
        holder.questionline.setText(patient.getPolingList().get(value).getQuestionList().get(position-1).getQuestion_line());
        holder.anserwline.setText(patient.getPolingList().get(value).getQuestionList().get(position-1).getAnserw_line());
    }

}

    @Override
    public int getItemCount() {
        int count=1+patient.getPolingList().get(value).getQuestionList().size();

        return count;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
