package com.example.admin.inzv2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 2016-08-26.
 */
public class Doctor_PatientDiagnosis_Adapter extends RecyclerView.Adapter<Doctor_PatientDiagnosis_Adapter.ViewHolder> {

    private PatientModel patient;
    private DoctorModel doctor;
    private int value;
    private int pollingnr;
    public String anserwstring;
    public ProgressDialog progressDialog;

    public Doctor_PatientDiagnosis_Adapter(int value) {
        patient=  Doctor_PatientList_Activity.getPatientModelsList().get(value);
        doctor=Doctor_Menu_Activity.getDoctor();

        pollingnr=searchpolling();
        this.value= value;
    }

    private int searchpolling() {
        int flag =0;
        for (int i=0; i <patient.getPolingList().size();i++){
            if(patient.getPolingList().get(i).getDoctoremail().equals(doctor.getEmail())){
                if(patient.getPolingList().get(i).getCreated_at().equals(doctor.getPatients().get(value).created_at));
                    flag=i;
            }

        }

        return flag;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class DiagnosisViewHolder extends ViewHolder {
        public TextView info;
        public EditText anserwline;
        public Button btnButton1;

        public DiagnosisViewHolder(View v) {
            super(v);
            this.info = (TextView) v.findViewById(R.id.questionlinepolling);
            this.anserwline= (EditText) itemView.findViewById(R.id.anserweditText);
            this.btnButton1 = (Button) itemView.findViewById(R.id.patientsendpollingbutton);
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

        if (viewType == (getItemCount() -1)) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.patient_fillpolling_card, viewGroup, false);
            return new DiagnosisViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.patient_question_card, viewGroup, false);
            return new QuestionViewHolder(v) ;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType()  == (getItemCount() -1)) {
            final DiagnosisViewHolder holder = (DiagnosisViewHolder) viewHolder;
            holder.info.setText("Twoja Diagnoza:");
            holder.anserwline.setTag(false);

            holder.anserwline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    holder.anserwline.setTag(true);
                }
            });
            holder.anserwline.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("") && (Boolean) holder.anserwline.getTag()) {
                        // Do your Logic here
                        anserwstring = holder.anserwline.getText().toString();

                    }
                }
            });

            holder.btnButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    /// button click event
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Wysyłanie Ankiety")
                            .setMessage("Czy na pewno chcesz wysłać ankietę?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                        buttonsendaction();
                                        progressDialog = new ProgressDialog(v.getContext());
                                        String url = "http://boguinzynierkarestapi.azurewebsites.net/api/pacient/" + String.valueOf(patient.getId());
                                        new PutDataTask().execute(url);


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
        else {
            QuestionViewHolder holder = (QuestionViewHolder) viewHolder;
            holder.questionline.setText(patient.getPolingList().get(pollingnr).getQuestionList().get(position).getQuestion_line());
            holder.anserwline.setText(patient.getPolingList().get(pollingnr).getQuestionList().get(position).getAnserw_line());
        }

    }

    private void buttonsendaction() {
        patient.getPolingList().get(pollingnr).getDiagnosisList().get(0).setDiagnosis_line(anserwstring);
        Doctor_Menu_Activity.getDoctor().getPatients().remove(value);

    }

    @Override
    public int getItemCount() {
        int count=1+patient.getPolingList().get(pollingnr).getQuestionList().size();

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class PutDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //       Context context = null;
            //      progressDialog = new ProgressDialog(context.getApplicationContext());
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String url=  "http://boguinzynierkarestapi.azurewebsites.net/api/status/"+ String.valueOf(Doctor_Menu_Activity.getDoctor().getId());
            new PutDataTask2().execute(url);
            /*
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            */
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
            String result = null;
            //create data to update
            JSONObject dataToSend = patient.patienttoJsonPolling(patient);

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
                    return "Diagnoza została wysłana do pacienta";
                } else {

                    return "Wysłanie diagnozy nie powiodło się";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }


            }

        }
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
            Toast.makeText(progressDialog.getContext(), result.toString(), Toast.LENGTH_LONG).show();
            if(result.equals("Diagnoza została wysłana do pacienta")) {
                Intent startNew_Doctor_Menu_Activity = new Intent(progressDialog.getContext(), Doctor_Menu_Activity.class);
                progressDialog.getContext().startActivity(startNew_Doctor_Menu_Activity);
            }

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
            JSONObject dataToSend = doctor.DoctortoJsonPatientid(doctor);
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
                    return "Diagnoza została wysłana do pacienta";
                } else {
                    return "Wysłanie diagnozy nie powiodło się";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }


            }

        }
    }
}
