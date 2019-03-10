package com.example.admin.inzv2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Admin on 2016-08-27.
 */
public class Doctor_PollingDesign_Adapter extends RecyclerView.Adapter<Doctor_PollingDesign_Adapter.ViewHolder> {

    private DoctorModel doctor;
    public String[] anserwsarray ;

    public Doctor_PollingDesign_Adapter() {
        doctor= Doctor_Menu_Activity.getDoctor();
      //  anserwsarray = new String[Patient_DoctorList_Activity.doctorModelsList.get(value).getQuestions().size()];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class QuestionViewHolder extends ViewHolder {
        public EditText questionline;
        public Button deleteButton;


        public QuestionViewHolder(View v) {
            super(v);
            this.deleteButton = (Button) itemView.findViewById(R.id.deletebutton);
            this.questionline= (EditText) itemView.findViewById(R.id.questioneditText);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.doctor_questiondesign_card, viewGroup, false);
            return new QuestionViewHolder(v) ;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            final QuestionViewHolder holder = (QuestionViewHolder) viewHolder;
            holder.questionline.setText(doctor.getQuestions().get(position).getQuestion());
            holder.questionline.setTag(false);

            holder.questionline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    holder.questionline.setTag(true);
                }
            });
            holder.questionline.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if ((Boolean) holder.questionline.getTag()&& viewHolder.getItemViewType()  ==position) {
                        if (!s.toString().equals("")) {
                            if (getItemCount() >= position)
                                doctor.getQuestions().get(position).setQuestion(holder.questionline.getText().toString());
                        }
                    }
                }

                    @Override
                    public void afterTextChanged (Editable s){

                        // Do your Logic here

                    }


    });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    /// button click event
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Tworzenie Ankiety")
                            .setMessage("Czy na pewno chcesz usunąć pytanie?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    doctor.getQuestions().remove(position);
                                    notifyDataSetChanged();


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
            });


    }

    @Override
    public int getItemCount() {
        int count=doctor.getQuestions().size();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
